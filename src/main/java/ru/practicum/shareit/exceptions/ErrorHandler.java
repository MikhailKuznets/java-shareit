package ru.practicum.shareit.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
//@Validated - Нужна ли тут данная аннотация? На вебинаре сказали что да
public class ErrorHandler {

    @ExceptionHandler(EmailAlreadyExistException.class)
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public ErrorResponse handleEmailAlreadyExistException(final EmailAlreadyExistException e) {
        log.error("КОД 409: {}", e.getMessage());
        return new ErrorResponse(e.getMessage());
    }


    @ExceptionHandler(InvalidIdException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ErrorResponse handleInvalidIdException(final InvalidIdException e) {
        log.error("КОД 404 - неверный id: {}", e.getMessage());
        return new ErrorResponse(e.getMessage());
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidateParameterException(final MethodArgumentNotValidException e) {
        log.error("КОД 404 - Ошибка валидации данных: {}", e.getMessage());
        return new ErrorResponse(e.getMessage());
    }


    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleThrowable(final Exception e) {
        log.error("КОД 500 - Непредвиденная ошибка: {}", e.getMessage());
        return new ErrorResponse(e.getMessage());
    }
}