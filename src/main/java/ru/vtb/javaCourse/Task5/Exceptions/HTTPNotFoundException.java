package ru.vtb.javaCourse.Task5.Exceptions;

public class HTTPNotFoundException extends RuntimeException{
    public HTTPNotFoundException(String s) {
        super(s);
    }
}
