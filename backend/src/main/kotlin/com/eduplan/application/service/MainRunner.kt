package com.eduplan.application.service


import com.eduplan.domain.service.UniversityService
import com.eduplan.domain.service.UserService
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component

@Component
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
    fun unregister_user() {
        userService.unregister(1)
    }

    fun unregister_uni() {
        uniService.unregister(1)
    }

    override fun run(vararg args: String?) {
        println("Запущена тестовая процедура регистрации пользователя.")
        register_user()
        println("Запущена тестовая процедура регистрации университета.")
        register_uni()
        println("Запущена тестовая процедура разрегистрации пользователя.")
        unregister_user()
        println("Запущена тестовая процедура разрегистрации университета.")
        unregister_uni()
    }

}