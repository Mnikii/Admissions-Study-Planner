package com.eduplan.domain.service

import com.eduplan.config.AppProperties
import jakarta.annotation.PostConstruct
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service

data class User(
    val userId: Int,
    var name: String,
    val registration_date: String
) {
}

@Repository
class UserRepository {
    private val users = mutableMapOf<Int, User>()

    fun add(user: User) {
        users[user.userId] = user
    }

    fun delete(userId: Int) {
        users.remove(userId)
    }

    fun update(new_user: User) {
        users[new_user.userId] = new_user
    }

    fun get(userId: Int): User? {
        return users[userId]
    }
}


@Service
class UserService {
    private val userStorage = UserRepository()

    private var restrNames = AppProperties().restrictedNames

    @PostConstruct
    fun init() {
        println("Загружен список запрещенных имен: $restrNames")
    }

    fun register(userId: Int, name: String, today: String): Boolean {
        if (name in restrNames) {
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