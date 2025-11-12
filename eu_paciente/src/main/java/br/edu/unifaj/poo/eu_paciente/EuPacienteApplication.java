package br.edu.unifaj.poo.eu_paciente;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EuPacienteApplication {

	public static void main(String[] args) {
		SpringApplication.run(EuPacienteApplication.class, args);
		System.out.println();
		System.out.println("Servidor Iniciado!");
		System.out.println("Versão Experimental 12.11.25");
	}
}
