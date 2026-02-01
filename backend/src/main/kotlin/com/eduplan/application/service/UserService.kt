class User(
    val userId: Int,
    var name: String,
    val registration_date: String
) {
}


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

    fun get(userId): User {
        return users[userId]
    }
}




@Component
class UserService {
    private val userStorage = UserRepository()

    @Qualifer("restrictedNames")
    private val restrNames: List
    fun register(userId: Int, name: String, today: String): Boolean {
        if (name in restrNames) {
            println("Ошибка регистрации. Недопустимое имя пользователя: $name")
            return false
        }
        userStorage.add(User(userId, name, today))
        println("Зарегестрирован пользователь: \nName: $name\nId: $userId\nRegistration date: $today")
        return true
    }
}