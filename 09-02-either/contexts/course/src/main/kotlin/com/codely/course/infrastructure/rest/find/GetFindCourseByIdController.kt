package com.codely.course.infrastructure.rest.find

import arrow.core.continuations.*
import com.codely.course.application.find.CourseFinder
import com.codely.course.application.find.CourseResponse
import com.codely.course.infrastructure.rest.response.respond
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class GetFindCourseByIdController(private val courseFinder: CourseFinder) {

    @GetMapping("/course/{id}")
    fun execute(
        @PathVariable id: String
    ): ResponseEntity<CourseResponse> =
        respond {
            val course = courseFinder.execute(id)
            ResponseEntity.ok().body(course)
        }

}
