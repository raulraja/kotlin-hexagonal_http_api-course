package com.codely.course.infrastructure

import arrow.core.Either
import arrow.core.continuations.either
import arrow.core.identity
import com.codely.course.application.find.CourseFinder
import com.codely.course.application.find.CourseResponse
import com.codely.course.domain.CourseId
import com.codely.course.domain.CourseNotFoundError
import com.codely.course.infrastructure.rest.find.GetFindCourseByIdController
import io.mockk.every
import io.mockk.mockk
import java.time.LocalDateTime
import kotlin.test.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

class GetFindCourseByIdControllerTest {

    private lateinit var courseFinder: CourseFinder
    private lateinit var controller: GetFindCourseByIdController

    @BeforeEach
    internal fun setUp() {
        courseFinder = mockk()
        controller = GetFindCourseByIdController(courseFinder)
    }

    @Test
    fun `should return the course response`() {
        `given a course response`()

        val response = `when a course is requested by id`()

        `then a successful response is returned`(response)
    }

    private fun `then a successful response is returned`(actualResponse: ResponseEntity<CourseResponse>) {
        assertEquals(ResponseEntity<CourseResponse>(course, HttpStatus.OK), actualResponse)
    }

    private fun `when a course is requested by id`() = controller.execute(courseId)

    private fun `given a course response`() {
        every { either { courseFinder.execute(any()) } } returns Either.Right(course)
    }

    @Test
    fun `should fail when course is not found`() {
        `given there is no course found`()

        val response = `when a course is requested by id`()

        `then a not found response is returned`(response)
    }

    private fun `then a not found response is returned`(actualResponse: ResponseEntity<CourseResponse>) {
        assertEquals(
            ResponseEntity.status(HttpStatus.NOT_FOUND).build(),
            actualResponse
        )
    }

    private fun `given there is no course found`() {
        every { either { courseFinder.execute(any()) } } returns Either.Left(
            CourseNotFoundError(
                either { CourseId.fromString(courseId) }.fold({ throw RuntimeException("unexpected") }, ::identity)
            )
        )
    }

    companion object {
        private const val courseId = "e90cadc2-fbf6-49ee-bca4-3fc652ea0134"
        private val course = CourseResponse(
            id = courseId,
            name = "Kotlin - Your first API",
            createdAt = LocalDateTime.parse("2022-08-31T09:07:36")
        )
    }
}
