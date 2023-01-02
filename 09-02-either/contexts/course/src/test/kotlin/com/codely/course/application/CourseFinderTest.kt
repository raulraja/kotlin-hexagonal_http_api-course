package com.codely.course.application

import arrow.core.Either
import arrow.core.continuations.Raise
import arrow.core.continuations.either
import com.codely.common.course.CourseMother
import com.codely.course.BaseTest
import com.codely.course.application.find.CourseFinder
import com.codely.course.application.find.CourseResponse
import com.codely.course.domain.*
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import kotlin.test.assertEquals

class CourseFinderTest : BaseTest() {

    private lateinit var courseRepository: CourseRepository
    private lateinit var courseFinder: CourseFinder

    @BeforeEach
    internal fun setUp() {
        courseRepository = mockk()
        courseFinder = CourseFinder(courseRepository)
    }

    @Test
    fun `should find an existing course`() {
        `given an saved course`()

        val actualCourse = `when the finder is executed`()

        `then the found course is equals to expected`(actualCourse)
    }

    @Test
    fun `should throw an exception when course is not found`() {
        `given no course is saved`()

        val actualResult = `when the finder is executed`()

        `then the result is a failure with no found exception`(actualResult)
    }

    private fun `then the result is a failure with no found exception`(actualResult: Either<CourseApplicationError, CourseResponse>) {
        val expected = either {
            raise(CourseNotFoundError(courseId()))
        }
        assertEquals(expected, actualResult)
    }

    private fun `given no course is saved`() {
        every { either { courseRepository.find(courseId()) } } returns either {
            raise(CourseNotFoundError(courseId()))
        }
    }

    private fun `then the found course is equals to expected`(actualCourse: Either<CourseApplicationError, CourseResponse>) {
        val expected = Either.Right(
            CourseResponse(
                id = id,
                name = courseName,
                createdAt = courseCreatedAt
            )
        )

        assertEquals(expected, actualCourse)
    }

    private fun `when the finder is executed`(): Either<CourseApplicationError, CourseResponse> {
        return either { courseFinder.execute(id) }
    }

    private fun `given an saved course`() {

        val course = either {
            CourseMother.sample(
                id = id,
                name = courseName,
                createdAt = courseCreatedAt
            )
        }

        every { either { courseRepository.find(course.bind().id) } } returns course
    }

    companion object {
        private const val id = "7ab75530-5da7-4b4a-b083-a779dd6c759e"

        context(Raise<InvalidCourseId>)
        private fun courseId() = CourseId.fromString(id)
        private const val courseName = "Course Finder Test Name"
        private val courseCreatedAt = LocalDateTime.parse("2022-08-31T09:00:00")
    }
}
