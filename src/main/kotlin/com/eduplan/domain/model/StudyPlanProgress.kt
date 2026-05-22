package com.eduplan.domain.model

data class StudyPlanProgress(
    val totalTasks: Int,
    val completedTasks: Int,
    val percent: Int,
)

