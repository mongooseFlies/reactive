package com.reactive.config

import com.reactive.handler.SportHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.router

@Configuration
class RouteConfig(val sportHandler: SportHandler) {

    @Bean
    fun apiRoute() = router {
        (accept(MediaType.APPLICATION_JSON) and "/api/v1").nest {
            "sport".nest {
                GET("", sportHandler::find)
                POST("/{name}", sportHandler::addSport)
                DELETE("/{id}", sportHandler::deleteSport)
            }
        }
    }
}