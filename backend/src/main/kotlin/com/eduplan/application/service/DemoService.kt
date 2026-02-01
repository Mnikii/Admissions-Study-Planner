package com.eduplan.application.service

import com.eduplan.application.port.input.CreateDemoUseCase
import com.eduplan.application.port.input.DeleteDemoUseCase
import com.eduplan.application.port.input.GetDemoUseCase
import com.eduplan.application.port.input.UpdateDemoUseCase
import com.eduplan.application.port.output.DemoRepositoryPort
import com.eduplan.domain.exception.EntityNotFoundException
import com.eduplan.domain.model.Demo
import com.eduplan.domain.service.DemoFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

/**
 * Сервис приложения, реализующий все use cases для Demo
 *
 * Оркестрирует работу между domain и infrastructure слоями.
 * Все публичные методы выполняются в транзакции.
 */
@Service
@Transactional
class DemoService(
    private val demoRepository: DemoRepositoryPort,
    private val demoFactory: DemoFactory = DemoFactory()
) : CreateDemoUseCase,
    GetDemoUseCase,
    UpdateDemoUseCase,
    DeleteDemoUseCase {

    /**
     * Создание нового Demo
     */
    override fun create(command: CreateDemoUseCase.CreateDemoCommand): Demo {
        val demo = demoFactory.create(
            description = command.description,
            longDescription = command.longDescription,
            demoPath = command.demoPath,
            isActive = command.isActive
        )

        return demoRepository.save(demo)
    }

    /**
     * Получение Demo по ID
     */
    @Transactional(readOnly = true)
    override fun getById(id: UUID): Demo {
        return demoRepository.findById(id)
            ?: throw EntityNotFoundException("Demo", id)
    }

    /**
     * Получение всех Demo
     */
    @Transactional(readOnly = true)
    override fun getAll(): List<Demo> {
        return demoRepository.findAll()
    }

    /**
     * Получение всех активных Demo
     */
    @Transactional(readOnly = true)
    override fun getAllActive(): List<Demo> {
        return demoRepository.findAllActive()
    }

    /**
     * Обновление Demo
     */
    override fun update(command: UpdateDemoUseCase.UpdateDemoCommand): Demo {
        val existingDemo = demoRepository.findById(command.id)
            ?: throw EntityNotFoundException("Demo", command.id)

        val updatedDemo = existingDemo.updateDescription(
            newDescription = command.description,
            newLongDescription = command.longDescription
        ).copy(
            demoPath = command.demoPath,
            isActive = command.isActive
        )

        return demoRepository.save(updatedDemo)
    }

    /**
     * Удаление Demo
     */
    override fun delete(id: UUID) {
        if (!demoRepository.existsById(id)) {
            throw EntityNotFoundException("Demo", id)
        }
        demoRepository.deleteById(id)
    }
}
