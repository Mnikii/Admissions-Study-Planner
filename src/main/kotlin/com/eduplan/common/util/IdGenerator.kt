package com.eduplan.common.util

import java.util.*

object IdGenerator {
    fun generateId(): String = UUID.randomUUID().toString()

    fun generateUUID(): UUID = UUID.randomUUID()
}
