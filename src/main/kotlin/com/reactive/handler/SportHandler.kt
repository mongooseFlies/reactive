package com.reactive.handler

import com.reactive.model.Sport
import com.reactive.repository.SportReactiveRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.*
import java.util.*

@Component
class SportHandler(private val repository: SportReactiveRepository) {
    fun find(req: ServerRequest) =
        when(val name = req.queryParam("name").toNullable()) {
            null -> repository.findAll().let {
                ServerResponse.ok().json().body(it)
            }
            "" -> ServerResponse.badRequest().json().bodyValue(ApiError("[name] query parameter should not be empty"))
            else -> ServerResponse.ok().json().body(repository.findByName(name))
        }

    fun addSport(req: ServerRequest) =
        req.pathVariable("name").let { name ->
            repository.findOneByName(name)
                .flatMap {
                    ServerResponse.status(HttpStatus.CONFLICT)
                        .bodyValue(ApiError("Sport with name=[${it.name}] already exists"))
                }
                .switchIfEmpty(ServerResponse.status(HttpStatus.CREATED).json().body(repository.save(Sport(name = name))))
        }

    fun deleteSport(req: ServerRequest) =
        req.pathVariable("id").toIntOrNull()?.let {
            repository.findById(it)
                .flatMap { found -> repository.delete(found).then(ServerResponse.ok().build()) }
                .switchIfEmpty(ServerResponse.notFound().build())
        } ?: ServerResponse.badRequest().json().bodyValue(ApiError("path variable must be integer"))

    private fun <T : Any> Optional<T>.toNullable(): T? = this.orElse(null)

}