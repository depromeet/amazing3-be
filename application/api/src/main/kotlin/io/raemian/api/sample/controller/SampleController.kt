package io.raemian.api.sample.controller

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import io.raemian.api.hanlder.Failure
import io.raemian.api.hanlder.IllegalArgumentFailure
import io.raemian.api.hanlder.ValidationFailure
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class SampleController {

    @GetMapping("/sample")
    fun sample(): ResponseEntity<String> {
        val item = getRandomItem()

        return item.fold(
            { mapFailure(it) },
            { mapSuccess(it) },
        )
    }

    private fun mapSuccess(item: Int): ResponseEntity<String> {
        return ResponseEntity.ok().body("$item")
    }

    private fun mapFailure(failure: Failure): ResponseEntity<String> {
        return when (failure) {
            is IllegalArgumentFailure -> ResponseEntity.badRequest().body("illegal argument error")
            is ValidationFailure -> ResponseEntity.badRequest().body("validation error")
            else -> ResponseEntity.internalServerError().body("unknown error")
        }
    }

    private fun getRandomItem(): Either<Failure, Int> {
        val number = IntRange(0, 2).random()
        if (number == 2) {
            return ValidationFailure("i dont like 2").left()
        }
        return if (number % 2 == 0) number.right() else IllegalArgumentFailure.of("number is $number").left()
    }
}
