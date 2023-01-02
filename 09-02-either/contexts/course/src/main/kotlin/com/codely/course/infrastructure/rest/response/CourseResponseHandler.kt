package com.codely.course.infrastructure.rest.response

import arrow.core.continuations.Raise
import arrow.core.continuations.eagerEffect
import arrow.core.continuations.fold
import arrow.core.identity
import com.codely.course.domain.*
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.ResponseEntity

/**
 * Responds to all endpoints handling throwables and raised values
 */
internal fun <T> respond(f: Raise<CourseApplicationError>.() -> ResponseEntity<T>): ResponseEntity<T> =
    eagerEffect { f() }.fold(::handleThrowable, ::handleCourseError, ::identity)

/**
 * handle typed domain errors
 */
private fun <T> handleCourseError(error: CourseApplicationError): ResponseEntity<T> =
    when (error) {
        is CourseNotFoundError -> ResponseEntity.status(NOT_FOUND).build()
        is CourseCannotBeFoundError -> ResponseEntity.status(NOT_FOUND).build()
        is InvalidCourseId -> ResponseEntity.status(BAD_REQUEST).build()
        is InvalidCourseName -> ResponseEntity.status(BAD_REQUEST).build()
    }

/**
 * handled uncaught exceptions
 */
private fun <T> handleThrowable(throwable: Throwable): ResponseEntity<T> =
    ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()