package com.example.hockeyfileserver.exceptions

import com.example.hockeyfileserver.message.ResponseMessage
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.multipart.MaxUploadSizeExceededException

@ControllerAdvice
class FileUploadExceptionAdvice {

    @ExceptionHandler(MaxUploadSizeExceededException::class)
    open fun handleMaxSizeException(exc: MaxUploadSizeExceededException?): ResponseEntity<ResponseMessage?>? {
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
            .body<ResponseMessage>(ResponseMessage("File too large"))
    }
}