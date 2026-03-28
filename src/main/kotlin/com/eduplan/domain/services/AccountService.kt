package com.eduplan.domain.services

import com.eduplan.domain.model.Properties
import com.eduplan.domain.model.User
import com.eduplan.domain.model.StudentStatus
import com.eduplan.domain.model.University
import com.eduplan.domain.repositories.StudentRepository
import com.eduplan.domain.repositories.UniversitiesRepository
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import java.time.LocalDate
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


    fun uniRegister(universityId: UUID, name: String, address: String, country: String, website: String): Boolean {
        if (false) {
            logger.error("Unable to register an university: UUID = $universityId | name = $name", "ACCOUNT")
            return false
        }
        uniStorage.add(University(universityId, name, address, country, website, LocalDateTime.now(), null))
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

    fun studentRegister(username: String, email: String, firstName: String, lastName: String, phoneNumber: String, birthday: LocalDate): Boolean {
        if (username in restrictedNames) {
            logger.error("Unable to register a student: Username = $username | name = $firstName + $lastName", "ACCOUNT")
            return false
        }
        logger.info("Registered a student: Username = $username", "ACCOUNT")
        studentStorage.add(User(username, email, firstName, lastName, phoneNumber, birthday, StudentStatus.PENDING_VERIFICATION, UUID.randomUUID()))
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