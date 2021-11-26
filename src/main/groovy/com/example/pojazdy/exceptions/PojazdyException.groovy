package com.example.pojazdy.exceptions

/**
 *
 * @author Jakub Sapiński
 * */

class PojazdyException extends RuntimeException{

    PojazdyException(Throwable e) {
        super(e)
    }

    PojazdyException(String message) {
        super(message)
    }

    PojazdyException(String message, Throwable ex) {
        super(message, ex)
    }
}
