package com.eduplan.presentation.exception

import java.util.UUID

class PlanTaskNotFoundException(taskId: UUID) : RuntimeException("Plan task not found: $taskId")

