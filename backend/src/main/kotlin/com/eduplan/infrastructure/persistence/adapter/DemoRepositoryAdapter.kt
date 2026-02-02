//package com.eduplan.infrastructure.persistence.adapter
//
//import com.eduplan.application.port.output.DemoRepositoryPort
//import com.eduplan.domain.model.Demo
//import com.eduplan.infrastructure.persistence.mapper.DemoMapper
//import com.eduplan.infrastructure.persistence.repository.DemoJpaRepository
//import org.springframework.stereotype.Repository
//import java.util.UUID
//
///**
// * Адаптер репозитория Demo
// *
// * Реализует output порт DemoRepositoryPort из application слоя,
// * используя Spring Data JPA репозиторий и маппер.
// *
// * Это мост между application и infrastructure слоями.
// */
//@Repository
//class DemoRepositoryAdapter(
//    private val jpaRepository: DemoJpaRepository,
//    private val mapper: DemoMapper
//) : DemoRepositoryPort {
//
//    override fun save(demo: Demo): Demo {
//        // Проверяем, существует ли уже entity
//        val entity = jpaRepository.findById(demo.id)
//            .map { mapper.updateJpa(it, demo) }
//            .orElseGet { mapper.toJpa(demo) }
//
//        val savedEntity = jpaRepository.save(entity)
//        return mapper.toDomain(savedEntity)
//    }
//
//    override fun findById(id: UUID): Demo? {
//        return jpaRepository.findById(id)
//            .map { mapper.toDomain(it) }
//            .orElse(null)
//    }
//
//    override fun findAll(): List<Demo> {
//        return jpaRepository.findAll()
//            .let { mapper.toDomainList(it) }
//    }
//
//    override fun findAllActive(): List<Demo> {
//        return jpaRepository.findByIsActiveTrue()
//            .let { mapper.toDomainList(it) }
//    }
//
//    override fun deleteById(id: UUID) {
//        jpaRepository.deleteById(id)
//    }
//
//    override fun existsById(id: UUID): Boolean {
//        return jpaRepository.existsById(id)
//    }
//}
