package com.eduplan.presentation.exception

import com.eduplan.domain.exception.BusinessRuleViolationException
import com.eduplan.domain.exception.EntityNotFoundException
import com.eduplan.domain.exception.InvalidStateException
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import java.time.LocalDateTime

/**
 * Глобальный обработчик исключений для REST контроллеров
 */
@RestControllerAdvice
class GlobalExceptionHandler {

    /**
     * Обработка исключения - сущность не найдена
     */
    @ExceptionHandler(EntityNotFoundException::class)
    fun handleEntityNotFound(
        ex: EntityNotFoundException,
        request: WebRequest
    ): ResponseEntity<ErrorResponse> {
        val error = ErrorResponse(
            status = HttpStatus.NOT_FOUND.value(),
            error = HttpStatus.NOT_FOUND.reasonPhrase,
            message = ex.message ?: "Entity not found",
            path = request.getDescription(false).removePrefix("uri=")
        )
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error)
    }

    /**
     * Обработка нарушений бизнес-правил
     */
    @ExceptionHandler(BusinessRuleViolationException::class, InvalidStateException::class)
    fun handleBusinessRuleViolation(
        ex: RuntimeException,
        request: WebRequest
    ): ResponseEntity<ErrorResponse> {
        val error = ErrorResponse(
            status = HttpStatus.BAD_REQUEST.value(),
            error = HttpStatus.BAD_REQUEST.reasonPhrase,
            message = ex.message ?: "Business rule violation",
            path = request.getDescription(false).removePrefix("uri=")
        )
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error)
    }

    /**
     * Обработка ошибок валидации
     */
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationErrors(
        ex: MethodArgumentNotValidException,
        request: WebRequest
    ): ResponseEntity<ValidationErrorResponse> {
        val errors = ex.bindingResult.allErrors.associate { error ->
            val fieldName = (error as? FieldError)?.field ?: "unknown"
            val errorMessage = error.defaultMessage ?: "Validation error"
            fieldName to errorMessage
        }

        val error = ValidationErrorResponse(
            status = HttpStatus.BAD_REQUEST.value(),
            error = HttpStatus.BAD_REQUEST.reasonPhrase,
            message = "Validation failed",
            path = request.getDescription(false).removePrefix("uri="),
            validationErrors = errors
        )
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error)
    }

    /**
     * Обработка IllegalArgumentException (ошибки валидации в domain)
     */
    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgument(
        ex: IllegalArgumentException,
        request: WebRequest
    ): ResponseEntity<ErrorResponse> {
        val error = ErrorResponse(
            status = HttpStatus.BAD_REQUEST.value(),
            error = HttpStatus.BAD_REQUEST.reasonPhrase,
            message = ex.message ?: "Invalid argument",
            path = request.getDescription(false).removePrefix("uri=")
        )
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error)
    }

    /**
     * Обработка всех остальных исключений
     */
    @ExceptionHandler(Exception::class)
    fun handleGeneralException(
        ex: Exception,
        request: WebRequest
    ): ResponseEntity<ErrorResponse> {
        val error = ErrorResponse(
            status = HttpStatus.INTERNAL_SERVER_ERROR.value(),
            error = HttpStatus.INTERNAL_SERVER_ERROR.reasonPhrase,
            message = "Internal server error",
            path = request.getDescription(false).removePrefix("uri=")
        )
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error)
    }
}

/**
 * Стандартный ответ с ошибкой
 */
@Schema(description = "Ответ с ошибкой")
data class ErrorResponse(
    @Schema(description = "HTTP статус код", example = "404")
    val status: Int,

    @Schema(description = "Название ошибки", example = "Not Found")
    val error: String,

    @Schema(description = "Сообщение об ошибке", example = "Demo with id=123 not found")
    val message: String,

    @Schema(description = "Путь запроса", example = "/api/demos/123")
    val path: String,

    @Schema(description = "Время возникновения ошибки")
    val timestamp: LocalDateTime = LocalDateTime.now()
)

/**
 * Ответ с ошибками валидации
 */
@Schema(description = "Ответ с ошибками валидации")
data class ValidationErrorResponse(
    @Schema(description = "HTTP статус код", example = "400")
    val status: Int,

    @Schema(description = "Название ошибки", example = "Bad Request")
    val error: String,

    @Schema(description = "Общее сообщение", example = "Validation failed")
    val message: String,

    @Schema(description = "Путь запроса", example = "/api/demos")
    val path: String,

    @Schema(description = "Ошибки валидации по полям")
    val validationErrors: Map<String, String>,

    @Schema(description = "Время возникновения ошибки")
    val timestamp: LocalDateTime = LocalDateTime.now()
)
