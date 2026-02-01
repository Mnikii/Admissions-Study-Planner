package com.eduplan.domain.exception

/**
 * Базовое исключение для доменного слоя
 */
sealed class DomainException(message: String, cause: Throwable? = null) : RuntimeException(message, cause)

/**
 * Исключение - сущность не найдена
 */
class EntityNotFoundException(entityName: String, id: Any) :
    DomainException("$entityName with id=$id not found")

/**
 * Исключение - нарушение бизнес-правил
 */
class BusinessRuleViolationException(message: String) : DomainException(message)

/**
 * Исключение - недопустимое состояние
 */
class InvalidStateException(message: String) : DomainException(message)
