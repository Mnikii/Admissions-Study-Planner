package com.eduplan.presentation.dto

import com.eduplan.domain.model.DocumentType
import java.time.LocalDate

data class DocumentUpdateRequest(
    val documentType: DocumentType?,
    val expiryDate: LocalDate?,
)
