package br.edu.unifaj.poo.eu_paciente.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/sync")
@CrossOrigin("*")
public class SyncController {

    private static final Logger log = LoggerFactory.getLogger(SyncController.class);

    // tópico -> conjunto de conexões SSE
    private final Map<String, ConcurrentHashMap<String, SseEmitter>> rooms = new ConcurrentHashMap<>();


    // Esse cara deve manter a contexão aberta, assim quem tiver no mesmo "topic" recebe o trigger
    @GetMapping("/subscribe/{topic}")
    public SseEmitter subscribe(@PathVariable String topic,
                                @RequestParam("clientId") String clientId) {
        var room = rooms.computeIfAbsent(topic, t -> new ConcurrentHashMap<>());

        SseEmitter emitter = new SseEmitter(0L);
        SseEmitter old = room.put(clientId, emitter);
        if (old != null) {
            try {
                old.complete();
            } catch (Exception ignored) {
            }
            // log opcional: substituição
            // log.info("[SSE] {}: replaced client {}", topic, clientId);
        }

        Runnable remove = () -> {
            var r = rooms.get(topic);
            if (r != null) r.remove(clientId, emitter); // remove apenas se for este emitter
            // log.info("[SSE] {}: client {} disconnected", topic, clientId);
        };
        emitter.onCompletion(remove);
        emitter.onTimeout(remove);
        emitter.onError(e -> remove.run());

        try {
            emitter.send(SseEmitter.event().name("connected").data("ok"));
        } catch (Exception ignored) {
        }
        return emitter;
    }

    // O post vai enviar uma sincronia para todos os dispositivos conectados no tópico
    @PostMapping("/trigger/{topic}")
    public ResponseEntity<String> trigger(@PathVariable String topic,
                                          @RequestBody(required = false) Map<String, Object> payload) {
        var room = rooms.getOrDefault(topic, new ConcurrentHashMap<>());
        if (room.isEmpty()) return ResponseEntity.status(404).body("no subscribers");

        var data = (payload == null ? Map.of("msg", "SYNC") : payload);

        int ok = 0;
        List<Map.Entry<String, SseEmitter>> dead = new ArrayList<>();

        for (var entry : room.entrySet()) {
            var clientId = entry.getKey();
            var em = entry.getValue();
            try {
                em.send(SseEmitter.event().name("sync").data(data));
                ok++;
            } catch (Exception ex) {
                dead.add(entry);
            }
        }
        // remove mortos
        for (var e : dead) {
            var r = rooms.get(topic);
            if (r != null) r.remove(e.getKey(), e.getValue());
        }

        if (ok == 0) return ResponseEntity.status(404).body("no live subscribers");
        return ResponseEntity.ok("broadcast " + ok + " delivered");
    }
}
