package com.reactive.repository

object Queries {
    const val findByName = "SELECT * FROM sport WHERE name like \$1"
    const val findOneByName = "SELECT * FROM sport WHERE name = \$1"
}