//package com.eduplan.application.port.output
//
//import com.eduplan.domain.model.Demo
//import java.util.UUID
//
///**
// * Output порт для работы с Demo репозиторием
// *
// * Это интерфейс, определенный в application слое,
// * но реализуемый в infrastructure слое (адаптером).
// */
//interface DemoRepositoryPort {
//
//    /**
//     * Сохранить Demo (создать или обновить)
//     */
//    fun save(demo: Demo): Demo
//
//    /**
//     * Найти Demo по ID
//     * @return Demo или null, если не найден
//     */
//    fun findById(id: UUID): Demo?
//
//    /**
//     * Найти все Demo
//     */
//    fun findAll(): List<Demo>
//
//    /**
//     * Найти все активные Demo
//     */
//    fun findAllActive(): List<Demo>
//
//    /**
//     * Удалить Demo по ID
//     */
//    fun deleteById(id: UUID)
//
//    /**
//     * Проверить существование Demo по ID
//     */
//    fun existsById(id: UUID): Boolean
//}
