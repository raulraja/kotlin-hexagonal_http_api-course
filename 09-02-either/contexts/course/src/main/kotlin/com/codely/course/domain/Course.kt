package com.codely.course.domain

import arrow.core.continuations.Raise
import arrow.core.continuations.ensure
import java.time.LocalDateTime
import java.util.UUID

data class CourseId(val value: UUID) {
    companion object {
        context(Raise<InvalidCourseId>)
        fun fromString(id: String): CourseId = try {
            CourseId(UUID.fromString(id))
        } catch (exception: IllegalArgumentException) {
            raise(InvalidCourseId(id, exception))
        }
    }
}

@JvmInline
value class CourseName private constructor(val value: String) {
    companion object {
        context(Raise<InvalidCourseName>)
        operator fun invoke(value: String): CourseName {
            ensure(value.isNotEmpty() && value.isNotBlank()) {
                InvalidCourseName(value, null)
            }
            return CourseName(value)
        }
    }
}

data class Course(
    val id: CourseId,
    val name: CourseName,
    val createdAt: LocalDateTime
)
