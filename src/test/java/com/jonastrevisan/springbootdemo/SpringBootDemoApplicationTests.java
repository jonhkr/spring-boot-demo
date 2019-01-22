package com.jonastrevisan.springbootdemo;

import com.jonastrevisan.springbootdemo.command.CreateTodo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.util.UriComponentsBuilder;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureWebTestClient
public class SpringBootDemoApplicationTests {

	@Autowired
	private WebTestClient webTestClient;

	@Test
	public void contextLoads() {
	}

	@Test
	public void shouldCreateTodo() {
		CreateTodo createTodo = new CreateTodo();
		createTodo.setDescription("Test todo");

		webTestClient.post().uri("/api/v1/todos")
				.contentType(MediaType.APPLICATION_JSON)
				.syncBody(createTodo)
				.exchange()
				.expectStatus().isCreated()
				.expectHeader().valueMatches("location", "/api/v1/todos/[0-9]+");
	}

	@Test
	public void shouldReturnNotFoundForNotFoundTodo() {
		webTestClient.get().uri("/api/v1/todos/{id}", 1L)
				.exchange()
				.expectStatus().isNotFound();
	}

	@Test
	public void shouldReturnExistingTodo() {
		CreateTodo createTodo = new CreateTodo();
		createTodo.setDescription("Test todo");

		EntityExchangeResult<Void> result = webTestClient.post().uri("/api/v1/todos")
				.contentType(MediaType.APPLICATION_JSON)
				.syncBody(createTodo)
				.exchange()
				.expectStatus().isCreated()
				.expectHeader().valueMatches("location", "/api/v1/todos/[0-9]+")
				.expectBody().isEmpty();

		webTestClient.get().uri(result.getResponseHeaders().getLocation())
				.exchange()
				.expectStatus().isOk()
				.expectBody().jsonPath("$.description").isEqualTo(createTodo.getDescription());
	}
}

