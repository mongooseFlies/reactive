package com.reactive

import com.reactive.model.Sport
import com.reactive.repository.SportReactiveRepository
import io.mockk.every
import io.mockk.mockk
import org.springframework.test.web.reactive.server.WebTestClient
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import reactor.kotlin.core.publisher.toFlux

@SpringBootTest
@AutoConfigureWebTestClient
class RouterWebFluxTest(
    @Autowired val client: WebTestClient
) {

    private val baseUri = "/api/v1/sport"

    private var repository: SportReactiveRepository = mockk()

    private val sports = listOf(
            Sport(0, "Tennis"),
            Sport(1, "BasketBall"),
            Sport(2, "FootBall")).toFlux()


    @Test
    fun shouldReturnSportsWhenGetAll() {
        every { repository.findAll() } returns sports

        client.get()
            .uri(baseUri)
            .exchange()
            .expectStatus().isOk
    }

    @Test
    fun shouldFilterByNameAndReturnSport() {
        every { repository.findAll() } returns sports

        client.get()
            .uri(baseUri)
            .exchange()
            .expectStatus().isOk
    }

    @Test
    fun shouldThrowAnErrorIfFilterByNameEmpty() {
        every { repository.findAll() } returns sports

        client.get()
            .uri("$baseUri?name=")
            .exchange()
            .expectStatus().isBadRequest
    }

}