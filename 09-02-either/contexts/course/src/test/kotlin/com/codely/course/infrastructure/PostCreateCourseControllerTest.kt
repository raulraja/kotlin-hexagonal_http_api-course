package com.codely.course.infrastructure

import arrow.core.continuations.Raise
import com.codely.course.application.CourseCreator
import com.codely.course.domain.InvalidCourse
import com.codely.course.domain.InvalidCourseId
import com.codely.course.infrastructure.rest.create.CreateCourseRequest
import com.codely.course.infrastructure.rest.create.PostCreateCourseController
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import java.net.URI
import kotlin.test.assertEquals

class PostCreateCourseControllerTest {

    private lateinit var courseCreator: CourseCreator
    private lateinit var controller: PostCreateCourseController

    @BeforeEach
    fun setUp() {
        courseCreator = mockk()
        controller = PostCreateCourseController(courseCreator)
    }

    @Test
    fun `should return a successfull response`() {
        every  { courseCreator::create.invoke(any(), any(), any()) } returns Unit
        val courseId = "03ef970b-719d-49c5-8d80-7dc762fe4be6"
        val response = controller.execute(CreateCourseRequest(courseId, "Test"))

        assertEquals(ResponseEntity.created(URI.create("/course/$courseId")).build(), response)
    }

    @Test
    fun `should fail when id is not valid`() {
        every { courseCreator::create.invoke(any(), any(), any()) } answers {
            firstArg<Raise<InvalidCourse>>().raise(InvalidCourseId("1", null))
        }

        val response = controller.execute(CreateCourseRequest("1", "Test"))

        assertEquals(ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body("The course id is not valid"), response)
    }

    @Test
    fun `should fail when name is not valid`() {
        every { courseCreator::create.invoke(any(), any(), any()) }  answers {
            firstArg<Raise<InvalidCourse>>().raise(InvalidCourseId("Invalid", null))
        }

        val response = controller.execute(CreateCourseRequest("03ef970b-719d-49c5-8d80-7dc762fe4be6", "Invalid"))

        assertEquals(ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body("The course name is not valid"), response)
    }

    @Test
    fun `should fail when there is an uncontrolled exception`() {
        every { courseCreator::create.invoke(any(), any(), any()) } throws Throwable()

        val response = controller.execute(CreateCourseRequest("03ef970b-719d-49c5-8d80-7dc762fe4be6", "Test"))

        assertEquals(ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .build(), response)
    }
}
