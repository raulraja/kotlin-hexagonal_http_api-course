package com.codely.course.application

import arrow.core.continuations.Raise
import com.codely.course.domain.*
import java.time.LocalDateTime

class CourseCreator(private val repository: CourseRepository) {

    context(Raise<InvalidCourse>)
    fun create(id: String, name: String) {
        val course = Course(CourseId.fromString(id), CourseName(name), LocalDateTime.now())
        repository.save(course)
    }
}
