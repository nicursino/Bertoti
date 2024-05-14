package com.meuprimeirorest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SpringBootApplication
public class MeuPrimeiroRest {

	public static void main(String[] args) {
		SpringApplication.run(MeuPrimeiroRest.class, args);
	}

}

@RestController
@RequestMapping("/alunos")
class RestApiDemoController {
	private List<Aluno> alunos = new ArrayList<>();

	public RestApiDemoController() {
		alunos.addAll(List.of(
				new Aluno("Nicolas"),
				new Aluno("Pedro"),
				new Aluno("Augusto"),
				new Aluno("Lucas")
		));
	}

	@GetMapping
	Iterable<Aluno> getAlunos() {
		return alunos;
	}

	@GetMapping("/{id}")
	Optional<Aluno> getAlunoById(@PathVariable String id) {
		for (Aluno c: alunos) {
			if (c.getId().equals(id)) {
				return Optional.of(c);
			}
		}

		return Optional.empty();
	}

	@PostMapping
	Aluno postAluno(@RequestBody Aluno aluno) {
		alunos.add(aluno);
		return aluno;
	}

	@PutMapping("/{id}")
	ResponseEntity<Aluno> putAluno(@PathVariable String id,
									 @RequestBody Aluno aluno) {
		int alunoIndex = -1;

		for (Aluno c: alunos) {
			if (c.getId().equals(id)) {
				alunoIndex = alunos.indexOf(c);
				alunos.set(alunoIndex, aluno);
			}
		}

		return (alunoIndex == -1) ?
				new ResponseEntity<>(postAluno(aluno), HttpStatus.CREATED) :
				new ResponseEntity<>(aluno, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	void deleteAluno(@PathVariable String id) {
		alunos.removeIf(c -> c.getId().equals(id));
	}
}

class Aluno {
	private final String id;
	private String name;

	public Aluno(String id, String name) {
		this.id = id;
		this.name = name;
	}

	public Aluno(String name) {
		this(UUID.randomUUID().toString(), name);
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}