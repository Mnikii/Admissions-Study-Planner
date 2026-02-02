//package com.eduplan.presentation.dto
//
//import io.swagger.v3.oas.annotations.media.Schema
//import jakarta.validation.constraints.NotBlank
//import jakarta.validation.constraints.Size
//import java.time.LocalDateTime
//import java.util.UUID
//
///**
// * DTO для создания Demo
// */
//@Schema(description = "Запрос на создание Demo")
//data class CreateDemoRequest(
//
//    @field:NotBlank(message = "Description is required")
//    @field:Size(max = 255, message = "Description cannot exceed 255 characters")
//    @Schema(description = "Краткое описание", example = "Demo example")
//    val description: String,
//
//    @field:NotBlank(message = "Long description is required")
//    @field:Size(max = 5000, message = "Long description cannot exceed 5000 characters")
//    @Schema(description = "Полное описание", example = "This is a detailed description of the demo")
//    val longDescription: String,
//
//    @field:Size(max = 500, message = "Demo path cannot exceed 500 characters")
//    @Schema(description = "Путь к demo файлу", example = "/demos/example.pdf", nullable = true)
//    val demoPath: String? = null,
//
//    @Schema(description = "Активность demo", example = "true", defaultValue = "true")
//    val isActive: Boolean = true
//)
//
///**
// * DTO для обновления Demo
// */
//@Schema(description = "Запрос на обновление Demo")
//data class UpdateDemoRequest(
//
//    @field:NotBlank(message = "Description is required")
//    @field:Size(max = 255, message = "Description cannot exceed 255 characters")
//    @Schema(description = "Краткое описание", example = "Updated demo")
//    val description: String,
//
//    @field:NotBlank(message = "Long description is required")
//    @field:Size(max = 5000, message = "Long description cannot exceed 5000 characters")
//    @Schema(description = "Полное описание", example = "Updated detailed description")
//    val longDescription: String,
//
//    @field:Size(max = 500, message = "Demo path cannot exceed 500 characters")
//    @Schema(description = "Путь к demo файлу", example = "/demos/updated.pdf", nullable = true)
//    val demoPath: String? = null,
//
//    @Schema(description = "Активность demo", example = "true")
//    val isActive: Boolean = true
//)
//
///**
// * DTO для ответа с одним Demo
// */
//@Schema(description = "Ответ с информацией о Demo")
//data class DemoResponse(
//
//    @Schema(description = "Уникальный идентификатор", example = "550e8400-e29b-41d4-a716-446655440000")
//    val id: UUID,
//
//    @Schema(description = "Краткое описание", example = "Demo example")
//    val description: String,
//
//    @Schema(description = "Полное описание", example = "This is a detailed description")
//    val longDescription: String,
//
//    @Schema(description = "Путь к demo файлу", example = "/demos/example.pdf", nullable = true)
//    val demoPath: String?,
//
//    @Schema(description = "Статус активности", example = "true")
//    val isActive: Boolean,
//
//    @Schema(description = "Дата создания", example = "2026-02-01T10:00:00")
//    val createdAt: LocalDateTime,
//
//    @Schema(description = "Дата обновления", example = "2026-02-01T15:30:00")
//    val updatedAt: LocalDateTime
//)
//
///**
// * DTO для списка Demo
// */
//@Schema(description = "Список Demo с метаинформацией")
//data class DemoListResponse(
//
//    @Schema(description = "Список Demo")
//    val items: List<DemoResponse>,
//
//    @Schema(description = "Общее количество элементов", example = "10")
//    val total: Int
//) {
//    companion object {
//        fun of(items: List<DemoResponse>): DemoListResponse {
//            return DemoListResponse(
//                items = items,
//                total = items.size
//            )
//        }
//    }
//}
