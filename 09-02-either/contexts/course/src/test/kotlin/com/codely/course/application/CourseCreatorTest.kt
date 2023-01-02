package com.codely.course.application

import arrow.core.Either
import arrow.core.continuations.either
import arrow.core.identity
import com.codely.course.BaseTest
import com.codely.course.domain.*
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.*
import kotlin.test.assertEquals

class CourseCreatorTest : BaseTest() {
    private lateinit var courseRepository: CourseRepository
    private lateinit var courseCreator: CourseCreator

    @BeforeEach
    fun setUp() {
        courseRepository = mockk(relaxUnitFun = true)
        courseCreator = CourseCreator(courseRepository)
    }

    @Test
    fun `should create a course successfully`() {
        givenFixedDate(fixedDate)

        either { courseCreator.create(id, name) }

        thenTheCourseShouldBeSaved()
    }

    @Test
    fun `should fail with invalid id`() {
        givenFixedDate(fixedDate)

        assertEquals(
            either<InvalidCourse, Unit> { courseCreator.create("Invalid", name) },
            Either.Left(InvalidCourseId("Invalid", null))
        )
    }

    @Test
    fun `should fail with invalid name`() {
        givenFixedDate(fixedDate)
        assertEquals(
            either<InvalidCourse, Unit> { courseCreator.create(id, "    ") },
            Either.Left(InvalidCourseName("    ", null))
        )
    }

    private fun thenTheCourseShouldBeSaved() {
        verify {
            courseRepository.save(
                Course(
                    id = CourseId(UUID.fromString(id)),
                    name = either { CourseName(name) }.fold({ throw RuntimeException("unexpected error on test name") }, ::identity),
                    createdAt = fixedDate
                )
            )
        }
    }

    companion object {
        private const val id = "caebae03-3ee9-4aef-b041-21a400fa1bb7"
        private const val name = "Kotlin Hexagonal Architecture Api Course"
        private val fixedDate = LocalDateTime.parse("2022-08-09T14:50:42")
    }
}
