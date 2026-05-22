package com.eduplan.presentation.exception

import java.util.UUID

class StudyPlanNotFoundException(planId: UUID) : RuntimeException("Study plan not found: $planId")

