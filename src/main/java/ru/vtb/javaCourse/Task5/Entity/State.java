package ru.vtb.javaCourse.Task5.Entity;

public enum State {
    OPEN("Открыт"),
    REGISTRED("Зарегистрирован"),
    CLOSE("Закрыт"),
    DELETED("Удален");


    private String description;
    State(String description){
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
