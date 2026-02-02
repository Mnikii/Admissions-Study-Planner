//package com.eduplan.presentation.controller
//
//import com.eduplan.application.port.input.CreateDemoUseCase
//import com.eduplan.application.port.input.DeleteDemoUseCase
//import com.eduplan.application.port.input.GetDemoUseCase
//import com.eduplan.application.port.input.UpdateDemoUseCase
//import com.eduplan.presentation.dto.*
//import com.eduplan.presentation.mapper.DemoRequestMapper
//import io.swagger.v3.oas.annotations.Operation
//import io.swagger.v3.oas.annotations.Parameter
//import io.swagger.v3.oas.annotations.media.Content
//import io.swagger.v3.oas.annotations.media.Schema
//import io.swagger.v3.oas.annotations.responses.ApiResponse
//import io.swagger.v3.oas.annotations.responses.ApiResponses
//import io.swagger.v3.oas.annotations.tags.Tag
//import jakarta.validation.Valid
//import org.springframework.http.HttpStatus
//import org.springframework.http.ResponseEntity
//import org.springframework.web.bind.annotation.*
//import java.util.UUID
//
///**
// * REST контроллер для управления Demo
// *
// * Обрабатывает HTTP запросы и делегирует выполнение use cases.
// */
//@RestController
//@RequestMapping("/api/demos")
//@Tag(name = "Demo", description = "API для управления Demo объектами")
//class DemoController(
//    private val createDemoUseCase: CreateDemoUseCase,
//    private val getDemoUseCase: GetDemoUseCase,
//    private val updateDemoUseCase: UpdateDemoUseCase,
//    private val deleteDemoUseCase: DeleteDemoUseCase,
//    private val mapper: DemoRequestMapper
//) {
//
//    @Operation(
//        summary = "Создать новый Demo",
//        description = "Создает новый Demo объект с указанными параметрами"
//    )
//    @ApiResponses(
//        value = [
//            ApiResponse(
//                responseCode = "201",
//                description = "Demo успешно создан",
//                content = [Content(schema = Schema(implementation = DemoResponse::class))]
//            ),
//            ApiResponse(
//                responseCode = "400",
//                description = "Невалидные данные",
//                content = [Content(schema = Schema(implementation = com.eduplan.presentation.exception.ErrorResponse::class))]
//            )
//        ]
//    )
//    @PostMapping
//    fun createDemo(
//        @Valid @RequestBody request: CreateDemoRequest
//    ): ResponseEntity<DemoResponse> {
//        val command = mapper.toCreateCommand(request)
//        val demo = createDemoUseCase.create(command)
//        val response = mapper.toResponse(demo)
//        return ResponseEntity.status(HttpStatus.CREATED).body(response)
//    }
//
//    @Operation(
//        summary = "Получить Demo по ID",
//        description = "Возвращает информацию о конкретном Demo по его идентификатору"
//    )
//    @ApiResponses(
//        value = [
//            ApiResponse(
//                responseCode = "200",
//                description = "Demo найден",
//                content = [Content(schema = Schema(implementation = DemoResponse::class))]
//            ),
//            ApiResponse(
//                responseCode = "404",
//                description = "Demo не найден",
//                content = [Content(schema = Schema(implementation = com.eduplan.presentation.exception.ErrorResponse::class))]
//            )
//        ]
//    )
//    @GetMapping("/{id}")
//    fun getDemoById(
//        @Parameter(description = "UUID идентификатор Demo", example = "550e8400-e29b-41d4-a716-446655440000")
//        @PathVariable id: UUID
//    ): ResponseEntity<DemoResponse> {
//        val demo = getDemoUseCase.getById(id)
//        val response = mapper.toResponse(demo)
//        return ResponseEntity.ok(response)
//    }
//
//    @Operation(
//        summary = "Получить все Demo",
//        description = "Возвращает список всех Demo объектов"
//    )
//    @ApiResponses(
//        value = [
//            ApiResponse(
//                responseCode = "200",
//                description = "Список Demo получен",
//                content = [Content(schema = Schema(implementation = DemoListResponse::class))]
//            )
//        ]
//    )
//    @GetMapping
//    fun getAllDemos(
//        @Parameter(description = "Фильтр: только активные")
//        @RequestParam(required = false, defaultValue = "false") activeOnly: Boolean
//    ): ResponseEntity<DemoListResponse> {
//        val demos = if (activeOnly) {
//            getDemoUseCase.getAllActive()
//        } else {
//            getDemoUseCase.getAll()
//        }
//        val responses = mapper.toResponseList(demos)
//        val listResponse = DemoListResponse.of(responses)
//        return ResponseEntity.ok(listResponse)
//    }
//
//    @Operation(
//        summary = "Обновить Demo",
//        description = "Обновляет существующий Demo объект"
//    )
//    @ApiResponses(
//        value = [
//            ApiResponse(
//                responseCode = "200",
//                description = "Demo успешно обновлен",
//                content = [Content(schema = Schema(implementation = DemoResponse::class))]
//            ),
//            ApiResponse(
//                responseCode = "404",
//                description = "Demo не найден",
//                content = [Content(schema = Schema(implementation = com.eduplan.presentation.exception.ErrorResponse::class))]
//            ),
//            ApiResponse(
//                responseCode = "400",
//                description = "Невалидные данные",
//                content = [Content(schema = Schema(implementation = com.eduplan.presentation.exception.ErrorResponse::class))]
//            )
//        ]
//    )
//    @PutMapping("/{id}")
//    fun updateDemo(
//        @Parameter(description = "UUID идентификатор Demo", example = "550e8400-e29b-41d4-a716-446655440000")
//        @PathVariable id: UUID,
//        @Valid @RequestBody request: UpdateDemoRequest
//    ): ResponseEntity<DemoResponse> {
//        val command = mapper.toUpdateCommand(id, request)
//        val demo = updateDemoUseCase.update(command)
//        val response = mapper.toResponse(demo)
//        return ResponseEntity.ok(response)
//    }
//
//    @Operation(
//        summary = "Удалить Demo",
//        description = "Удаляет Demo объект по его идентификатору"
//    )
//    @ApiResponses(
//        value = [
//            ApiResponse(
//                responseCode = "204",
//                description = "Demo успешно удален"
//            ),
//            ApiResponse(
//                responseCode = "404",
//                description = "Demo не найден",
//                content = [Content(schema = Schema(implementation = com.eduplan.presentation.exception.ErrorResponse::class))]
//            )
//        ]
//    )
//    @DeleteMapping("/{id}")
//    fun deleteDemo(
//        @Parameter(description = "UUID идентификатор Demo", example = "550e8400-e29b-41d4-a716-446655440000")
//        @PathVariable id: UUID
//    ): ResponseEntity<Void> {
//        deleteDemoUseCase.delete(id)
//        return ResponseEntity.noContent().build()
//    }
//}
