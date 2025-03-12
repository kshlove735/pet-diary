package com.myproject.petcare.pet_diary.common.exception.exhandler;

import com.myproject.petcare.pet_diary.common.dto.ResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class ExControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseDto> validException(MethodArgumentNotValidException e) {
        log.error("[exceptionHandler] ex", e);

        Map<String, String> errors = new HashMap<>();

        e.getBindingResult().getAllErrors()
                .forEach((error) -> {
                    String field = ((FieldError) error).getField();
                    String defaultMessage = error.getDefaultMessage();
                    errors.put(field, defaultMessage);
                });

        ResponseDto responseDto = new ResponseDto(false, "Validation Error", errors);


        return new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);
    }
}
