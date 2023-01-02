package com.codely.course.domain

import arrow.core.continuations.Raise

interface CourseRepository {
    fun save(course: Course)

    context(Raise<CourseError>)
    fun find(id: CourseId): Course
}
