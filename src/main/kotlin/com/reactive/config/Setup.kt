package com.reactive.config

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.reactive.model.Sport
import com.reactive.repository.SportReactiveRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.core.io.buffer.DataBufferUtils
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.client.WebClient
import reactor.kotlin.core.publisher.toFlux
import java.nio.charset.StandardCharsets
import java.time.Duration

@Configuration
class Setup(
    val repository: SportReactiveRepository,
    val webClientBuilder: WebClient.Builder,
    val mapper: ObjectMapper,
    @Value("\${downstream.decathlon.url}") val baseUrl: String,
) : ApplicationRunner {

    val logger: Logger = LoggerFactory.getLogger(Setup::class.java)

    fun webClient() = webClientBuilder
        .baseUrl(baseUrl)
        .build()

    override fun run(args: ApplicationArguments?) {
        webClient()
            .get()
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .bodyToFlux(DataBuffer::class.java)
            .delayElements(Duration.ofMillis(500))
            .map(::bufferToString)
            .doOnSubscribe { it.request(20) }
            .doOnNext { logger.info("Incoming Data => $it") }
            .reduce { l, r -> "$l$r" }
            .flatMapMany { it.toJsonArray().asSequence().toFlux() }
            .map(Sport::fromJsonNode)
            .subscribe { repository.save(it).subscribe() }

    }

    private fun bufferToString(buffer: DataBuffer): String {
        val bufferSize = buffer.readableByteCount()
        val bytes = ByteArray(bufferSize)
        buffer.read(bytes)
        DataBufferUtils.release(buffer)
        return String(bytes, StandardCharsets.UTF_8)
    }

    private fun String.toJsonArray(): JsonNode =
        mapper.readTree(this).at("/data")

}