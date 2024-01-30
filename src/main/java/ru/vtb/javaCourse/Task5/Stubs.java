package ru.vtb.javaCourse.Task5;

//Класс для заглушек
public class Stubs {
    public static Long getClientByMDMCode(String mdmCode){
        return (long) mdmCode.chars().sum();
    }
}
