package com.eduplan.domain.services

import com.eduplan.domain.model.Properties
import com.eduplan.domain.model.Student
import com.eduplan.domain.model.StudentStatus
import com.eduplan.domain.model.University
import com.eduplan.domain.repositories.StudentRepository
import com.eduplan.domain.repositories.UniversitiesRepository
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.UUID


@Service
class AccountService(
    private val properties: Properties
) {


    private val restrictedNames = properties.restrictedNames
    private val studentStorage = StudentRepository()
    private val uniStorage = UniversitiesRepository()
    private val logger = LoggingService()

    @PostConstruct
    fun init() {
        logger.debug("Loaded restricted names list: length = ${restrictedNames.size}", "ACCOUNT")
    }


    fun uniRegister(universityId: UUID, name: String, address: String, structure: MutableMap<String, MutableList<String>>): Boolean {
        if (false) {
            logger.error("Unable to register an university: UUID = $universityId | name = $name", "ACCOUNT")
            return false
        }
        uniStorage.add(University(universityId, name, address, structure))
        logger.info("Registered an university: UUID = $universityId | name = $name", "ACCOUNT")
        return true
    }

    fun uniUnregister(universityId: UUID): Boolean {
        if (false) {
            logger.error("Unable to unregister an university: UUID = {${universityId}}", "ACCOUNT")
            return false
        }
        logger.info("Unregistered an university: UUID = $universityId", "ACCOUNT")
        uniStorage.delete(universityId)
        return true
    }

    fun studentRegister(studentId: UUID, email: String, fullName: String, phoneNumber: String, today: LocalDateTime): Boolean {
        if (fullName in restrictedNames) {
            logger.error("Unable to register a student: UUID = $studentId | name = $fullName", "ACCOUNT")
            return false
        }
        logger.info("Registered a student: UUID = $studentId", "ACCOUNT")
        studentStorage.add(Student(studentId, email, fullName, phoneNumber, StudentStatus.PENDING_VERIFICATION, today, today))
        return true
    }

    fun studentUnregister(studentId: UUID): Boolean {
        if (false) {
            logger.error("Unable to unregister a student: UUID = $studentId", "ACCOUNT")
            return false
        }
        logger.info("Registered a student: UUID = $studentId", "ACCOUNT")
        studentStorage.delete(studentId)
        return true
    }

}