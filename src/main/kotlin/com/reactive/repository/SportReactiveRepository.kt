package com.reactive.repository

import com.reactive.model.Sport
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
interface SportReactiveRepository : ReactiveCrudRepository<Sport, Int> {
    @Query(Queries.findByName)
    fun findByName(name: String): Flux<Sport>

    @Query(Queries.findOneByName)
    fun findOneByName(name: String): Mono<Sport>

    override fun deleteById(id: Int): Mono<Void>

    override fun findAll(): Flux<Sport>

    override fun <S : Sport> save(entity: S): Mono<S>

    override fun <S : Sport?> saveAll(entities: MutableIterable<S>): Flux<S>
}