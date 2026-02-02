//package com.eduplan.domain.service
//
//import com.eduplan.domain.model.Demo
//import java.time.LocalDateTime
//import java.util.UUID
//
///**
// * Фабрика для создания доменных объектов Demo
// *
// * Инкапсулирует логику создания и валидации инвариантов.
// */
//class DemoFactory {
//
//    /**
//     * Создать новый Demo с автоматической генерацией ID и timestamp
//     */
//    fun create(
//        description: String,
//        longDescription: String,
//        demoPath: String? = null,
//        isActive: Boolean = true
//    ): Demo {
//        validateDescription(description)
//        validateLongDescription(longDescription)
//        demoPath?.let { validateDemoPath(it) }
//
//        val now = LocalDateTime.now()
//
//        return Demo(
//            id = UUID.randomUUID(),
//            description = description.trim(),
//            longDescription = longDescription.trim(),
//            demoPath = demoPath?.trim(),
//            isActive = isActive,
//            createdAt = now,
//            updatedAt = now
//        )
//    }
//
//    /**
//     * Восстановить Demo из существующих данных (например, из БД)
//     */
//    fun restore(
//        id: UUID,
//        description: String,
//        longDescription: String,
//        demoPath: String?,
//        isActive: Boolean,
//        createdAt: LocalDateTime,
//        updatedAt: LocalDateTime
//    ): Demo {
//        return Demo(
//            id = id,
//            description = description,
//            longDescription = longDescription,
//            demoPath = demoPath,
//            isActive = isActive,
//            createdAt = createdAt,
//            updatedAt = updatedAt
//        )
//    }
//
//    private fun validateDescription(description: String) {
//        require(description.isNotBlank()) { "Description cannot be blank" }
//        require(description.length <= 255) { "Description cannot exceed 255 characters" }
//    }
//
//    private fun validateLongDescription(longDescription: String) {
//        require(longDescription.isNotBlank()) { "Long description cannot be blank" }
//        require(longDescription.length <= 5000) { "Long description cannot exceed 5000 characters" }
//    }
//
//    private fun validateDemoPath(demoPath: String) {
//        require(demoPath.isNotBlank()) { "Demo path cannot be blank if provided" }
//        require(demoPath.length <= 500) { "Demo path cannot exceed 500 characters" }
//    }
//}
