//package com.eduplan.presentation.mapper
//
//import com.eduplan.application.port.input.CreateDemoUseCase
//import com.eduplan.application.port.input.UpdateDemoUseCase
//import com.eduplan.domain.model.Demo
//import com.eduplan.presentation.dto.CreateDemoRequest
//import com.eduplan.presentation.dto.DemoResponse
//import com.eduplan.presentation.dto.UpdateDemoRequest
//import org.springframework.stereotype.Component
//import java.util.UUID
//
///**
// * Mapper для конвертации между Presentation DTO и Application команд/моделей
// */
//@Component
//class DemoRequestMapper {
//
//    /**
//     * Конвертировать CreateDemoRequest в команду use case
//     */
//    fun toCreateCommand(request: CreateDemoRequest): CreateDemoUseCase.CreateDemoCommand {
//        return CreateDemoUseCase.CreateDemoCommand(
//            description = request.description,
//            longDescription = request.longDescription,
//            demoPath = request.demoPath,
//            isActive = request.isActive
//        )
//    }
//
//    /**
//     * Конвертировать UpdateDemoRequest в команду use case
//     */
//    fun toUpdateCommand(id: UUID, request: UpdateDemoRequest): UpdateDemoUseCase.UpdateDemoCommand {
//        return UpdateDemoUseCase.UpdateDemoCommand(
//            id = id,
//            description = request.description,
//            longDescription = request.longDescription,
//            demoPath = request.demoPath,
//            isActive = request.isActive
//        )
//    }
//
//    /**
//     * Конвертировать Domain модель в Response DTO
//     */
//    fun toResponse(demo: Demo): DemoResponse {
//        return DemoResponse(
//            id = demo.id,
//            description = demo.description,
//            longDescription = demo.longDescription,
//            demoPath = demo.demoPath,
//            isActive = demo.isActive,
//            createdAt = demo.createdAt,
//            updatedAt = demo.updatedAt
//        )
//    }
//
//    /**
//     * Конвертировать список Domain моделей в Response DTO
//     */
//    fun toResponseList(demos: List<Demo>): List<DemoResponse> {
//        return demos.map { toResponse(it) }
//    }
//}
