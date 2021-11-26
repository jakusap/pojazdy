package com.example.pojazdy.exceptions
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value = HttpStatus.NOT_FOUND)
class NotFoundException {

    NotFoundException(String var1) {
        super(var1)
    }
}
