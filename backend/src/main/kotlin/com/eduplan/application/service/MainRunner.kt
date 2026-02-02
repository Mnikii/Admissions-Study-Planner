package com.eduplan.application.service

import UniversityService
import UserService
import org.springframework.boot.CommandLineRunner

class MainRunner (
    private val uniService: UniversityService,
    private val userService: UserService
): CommandLineRunner {

    fun register_user() {
        userService.register(1, "Vasya", "02-02-2026")
    }

    fun register_uni() {
        uniService.register(1, "Real University", "ul. Ulichnaya, d.17, str. 19", mutableMapOf("IT" to mutableListOf("Program Engineering", "Applied mathematics and computer science")))
    }

    override fun run(vararg args: String?) {
        println("Запущена тестовая процедура регистрации пользователя.")
        register_user()
        println("Запущена тестовая процедура регистрации университета.")
        register_uni()
    }

}