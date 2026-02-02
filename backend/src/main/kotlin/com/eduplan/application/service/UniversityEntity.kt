import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component

data class University(
    val universityId: Int,
    var name: String,
    val adress: String,
    val structure: MutableMap<String, MutableList<String>> // Key - faculty; Value - list of programmes on a faculty
) {
}


class UniversitiesRepository {
    private val unis = mutableMapOf<Int, University>()

    fun add(uni: University) {
        unis[uni.universityId] = uni
    }

    fun delete(universityId: Int) {
        unis.remove(universityId)
    }

    fun update(new_uni: University) {
        unis[new_uni.universityId] = new_uni
    }

    fun get(universityId: Int): University? {
        return unis[universityId]
    }
}




@Component
class UniversityService {
    private val uniStorage = UniversitiesRepository()

    @Bean
    fun register(universityId: Int, name: String, adress: String, structure: MutableMap<String, MutableList<String>>): Boolean {
        if (false) {
            println("Ошибка регистрации.")
            return false
        }
        uniStorage.add(University(universityId, name, adress, structure))
        println("Зарегистрирован университет: \nName: $name\nId: $universityId\nAdress: $adress\nStructure: $structure")
        return true
    }

    fun unregister(universityId: Int): Boolean {
        if (false) {
            println("Ошибка разрегистрации.")
            return false
        }
        println("Разрегистрирован университет: \nId: $universityId")
        uniStorage.delete(universityId)
        return true
    }
}