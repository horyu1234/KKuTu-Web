package me.horyu.kkutuweb.help

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class HelpServiceTest {
    private lateinit var helpService: HelpService

    @BeforeEach
    fun init() {
        helpService = HelpService()
    }

    @Test
    fun testRequiredExp() {
        val exp = helpService.getRequiredExp(315)
    }
}