package com.eduplan.domain.service

import com.eduplan.config.AppProperties
import jakarta.annotation.PostConstruct
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service

data class User(
    val id: Int,
    val name: String,
    val today: String
)

@Repository
class UserRepository {
    private val users = mutableMapOf<Int, User>()

    fun add(user: User) {
        users[user.id] = user
    }

    fun delete(userId: Int) {
        users.remove(userId)
    }

    fun update(new_user: User) {
        users[new_user.id] = new_user
    }

    fun get(userId: Int): User? {
        return users[userId]
    }
}


@Service
class UserService(private val appProperties: AppProperties) {
    private val userStorage = UserRepository()

    @PostConstruct
    fun init() {
        println("Загружен список запрещенных имен: ${appProperties.restrictedNames}")
    }

    fun register(userId: Int, name: String, today: String): Boolean {
        if (name in appProperties.restrictedNames) {
            println("Ошибка регистрации. Недопустимое имя пользователя: $name")
            return false
        }
        userStorage.add(User(userId, name, today))
        println("Зарегестрирован пользователь: \nName: $name\nId: $userId\nRegistration date: $today")
        return true
    }

    fun unregister(userId: Int): Boolean {
        if (false) {
            println("Ошибка разрегистрации.")
            return false
        }
        println("Разрегистрирован пользователь: \nId: $userId")
        userStorage.delete(userId)
        return true
    }
}