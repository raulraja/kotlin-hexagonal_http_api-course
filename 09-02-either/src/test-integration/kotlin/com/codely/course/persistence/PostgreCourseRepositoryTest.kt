package com.codely.course.persistence

import arrow.core.Either
import arrow.core.continuations.either
import arrow.core.identity
import com.codely.common.course.CourseMother
import com.codely.course.domain.CourseId
import com.codely.course.domain.CourseNotFoundError
import com.codely.course.infrastructure.persistence.PostgreCourseRepository
import com.codely.shared.persistence.BaseIntegrationTest
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import java.util.*
import kotlin.test.assertEquals

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PostgreCourseRepositoryTest : BaseIntegrationTest() {

    @Autowired
    private lateinit var repository: PostgreCourseRepository

    @Test
    fun `should save a course`() {
        val courseId = "13590efb-c181-4c5f-9f95-b768abde13e2"
        val courseToSave = either { CourseMother.sample(id = courseId) }.fold({ throw RuntimeException("unexepcted") }, ::identity)
        repository.save(courseToSave)

        val courseFromDb = either { repository.find(CourseId.fromString(courseId)) }

        assertEquals(Either.Right(courseToSave), courseFromDb)
    }

    @Test
    fun `should fail when course is not found`() {
        val courseId = CourseId(UUID.fromString("13590efb-c181-4c5f-9f95-b768abde13e2"))

        val courseFromDb = either {
            repository.find(courseId)
        }

        assertEquals(Either.Left(CourseNotFoundError(courseId)), courseFromDb)
    }
}
