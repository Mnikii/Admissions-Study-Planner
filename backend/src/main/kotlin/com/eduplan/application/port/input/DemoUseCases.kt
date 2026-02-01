package com.eduplan.application.port.input

import com.eduplan.domain.model.Demo
import java.util.UUID

/**
 * Input порт (Use Case) для создания Demo
 */
interface CreateDemoUseCase {

    /**
     * Создать новый Demo
     */
    fun create(command: CreateDemoCommand): Demo

    /**
     * Команда для создания Demo
     */
    data class CreateDemoCommand(
        val description: String,
        val longDescription: String,
        val demoPath: String? = null,
        val isActive: Boolean = true
    )
}

/**
 * Input порт (Use Case) для получения Demo
 */
interface GetDemoUseCase {

    /**
     * Получить Demo по ID
     */
    fun getById(id: UUID): Demo

    /**
     * Получить все Demo
     */
    fun getAll(): List<Demo>

    /**
     * Получить все активные Demo
     */
    fun getAllActive(): List<Demo>
}

/**
 * Input порт (Use Case) для обновления Demo
 */
interface UpdateDemoUseCase {

    /**
     * Обновить Demo
     */
    fun update(command: UpdateDemoCommand): Demo

    /**
     * Команда для обновления Demo
     */
    data class UpdateDemoCommand(
        val id: UUID,
        val description: String,
        val longDescription: String,
        val demoPath: String?,
        val isActive: Boolean
    )
}

/**
 * Input порт (Use Case) для удаления Demo
 */
interface DeleteDemoUseCase {

    /**
     * Удалить Demo по ID
     */
    fun delete(id: UUID)
}
