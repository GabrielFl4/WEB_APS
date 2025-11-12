// sync.js — dispara o broadcast SSE para um tópico

const SYNC_TOPIC = "apresentacao";
const SYNC_BASE = "http://localhost:8080";

function configurarBotaoSync() {
  const btn = document.getElementById("btn-sync-apresentacao");
  if (!btn) return;
  btn.addEventListener("click", async () => {
    try {
      await fetch(`${SYNC_BASE}/sync/trigger/${SYNC_TOPIC}`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ msg: "SHOW_OK" })
      });
      // feedback visual no painel:
      btn.disabled = true;
      setTimeout(() => (btn.disabled = false), 1000);
    } catch (e) {
      console.error("Falha ao disparar sicronização! -> ", e);
    }
  });
}

document.addEventListener("DOMContentLoaded", configurarBotaoSync);
