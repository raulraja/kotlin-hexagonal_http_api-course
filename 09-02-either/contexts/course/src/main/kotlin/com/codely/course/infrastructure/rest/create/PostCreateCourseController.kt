package com.codely.course.infrastructure.rest.create

import com.codely.course.application.CourseCreator
import com.codely.course.domain.*
import com.codely.course.infrastructure.rest.response.respond
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
class PostCreateCourseController(private val courseCreator: CourseCreator) {

    @PostMapping("/course")
    fun execute(
        @RequestBody request: CreateCourseRequest
    ): ResponseEntity<String> =
        respond {
            courseCreator.create(request.id, request.name)
            ResponseEntity.created(URI.create("/course/${request.id}")).build<String>()
        }

}

data class CreateCourseRequest(
    val id: String,
    val name: String
)
