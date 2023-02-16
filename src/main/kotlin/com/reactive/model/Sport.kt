package com.reactive.model

import com.fasterxml.jackson.databind.JsonNode
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table(name = "SPORT")
data class Sport(
    @Id
    val id: Int? = null,
    val name: String
) {
    companion object {
        fun fromJsonNode(node: JsonNode) =
            Sport(
//                id = node.at("/id").asInt(),
                name = node.at("/attributes/name").asText()
            )
    }
}
