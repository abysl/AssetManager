package com.abysl.humble

import org.junit.jupiter.api.Test

internal class HumbleTest {

    val humble = Humble()

    @Test
    fun getProducts() {
        val test = humble.getProducts()
        test.forEach { println(it.name) }
    }
}