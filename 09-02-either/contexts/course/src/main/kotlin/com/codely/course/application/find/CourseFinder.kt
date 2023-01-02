package com.codely.course.application.find

import arrow.core.continuations.Raise
import com.codely.course.domain.*
import java.time.LocalDateTime

class CourseFinder(private val courseRepository: CourseRepository) {
    context(Raise<CourseApplicationError>)
    fun execute(courseId: String): CourseResponse {
        val cid = CourseId.fromString(courseId)
        val course = courseRepository.find(cid)
        return CourseResponse(course)
    }
}

data class CourseResponse(val id: String, val name: String, val createdAt: LocalDateTime) {
    companion object {
        operator fun invoke(course: Course): CourseResponse = with(course) {
            CourseResponse(
                id = id.value.toString(),
                name = name.value,
                createdAt = createdAt
            )
        }
    }
}
