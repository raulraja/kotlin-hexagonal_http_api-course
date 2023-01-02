package com.codely.course.domain

sealed class CourseApplicationError

sealed class CourseError : CourseApplicationError()
data class CourseNotFoundError(val id: CourseId) : CourseError()
data class CourseCannotBeFoundError(val id: CourseId) : CourseError()

sealed class InvalidCourse : CourseApplicationError()
data class InvalidCourseId(val id: String, val cause: Throwable?) : InvalidCourse()
data class InvalidCourseName(val name: String, val cause: Throwable?) : InvalidCourse()

