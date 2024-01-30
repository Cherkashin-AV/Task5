package ru.vtb.javaCourse.Task5;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.FieldError;
import ru.vtb.javaCourse.Task5.Exceptions.DataNotFoundException;
import ru.vtb.javaCourse.Task5.Exceptions.HTTPNotFoundException;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ExceptionHandler //extends ResponseEntityExceptionHandler
{
    @Autowired
    HttpServletRequest httpRequest;

    @org.springframework.web.bind.annotation.ExceptionHandler( MethodArgumentNotValidException.class)
    protected ResponseEntity<String> handleConflict(MethodArgumentNotValidException ex) {
        StringBuilder builder = new StringBuilder(512);
        List<ObjectError> errors = ex.getBindingResult().getAllErrors();
        if (errors.size() > 1)
            builder.append("Имена обязательных параметров ");
        else
            builder.append("Имя обязательного параметра ");
        for (int i = 0; i < errors.size(); i++) {
            if (i>0)
                builder.append(", ");
            builder.append(((FieldError)errors.get(i)).getField());
        }
        if (errors.size() > 1)
            builder.append(" не заполнены");
        else
            builder.append(" не заполнено");
        ResponseEntity responseEntity = ResponseEntity.badRequest().body(builder.toString());
        return responseEntity;
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(ValidationException.class)
    protected ResponseEntity<String> handleConflict(ValidationException ex) {
        String message = ex.getMessage()
            +"\r\nСтек: " + Arrays.stream(ex.getStackTrace()).map((s)->s.toString()).collect(Collectors.joining("\r\n"));
        ResponseEntity responseEntity = ResponseEntity.badRequest().body(message);
        return responseEntity;
    }

    @org.springframework.web.bind.annotation.ExceptionHandler({DataNotFoundException.class})
    protected ResponseEntity<String> handleConflict(DataNotFoundException ex) {
        String message = ex.getMessage()
                +"\r\nСтек: " + Arrays.stream(ex.getStackTrace()).map((s)->s.toString()).collect(Collectors.joining("\r\n"));
         return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler({HTTPNotFoundException.class})
    protected ResponseEntity<String> handleConflict(HTTPNotFoundException ex) {
        String message = ex.getMessage()
                +"\r\nСтек: " + Arrays.stream(ex.getStackTrace()).map((s)->s.toString()).collect(Collectors.joining("\r\n"));
        return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(RuntimeException.class)
    protected ResponseEntity<String> handleConflict(RuntimeException ex) {
        String message = String.format("Ошибка %s. %s",ex.getClass().toString(), ex.getMessage())
                +"\r\nСтек: " + Arrays.stream(ex.getStackTrace()).map((s)->s.toString()).collect(Collectors.joining("\r\n"));
        ResponseEntity responseEntity = ResponseEntity.internalServerError().body(message);
        return responseEntity;
    }

}