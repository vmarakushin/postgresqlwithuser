package com.example.userservice.vo;

import javax.persistence.Embeddable;

/**
 * Объект данных для имени с валидацией
 * immutable
 * @author vmarakushin
 * @varsion 1.0
 */
@Embeddable
public class Name extends StringVO {


    public Name(String value) {super(value);}
    public Name() {super();}

    @Override
    protected String getLabel() {return "Имя";}

    @Override
    protected void customValidate(String value) {
        if (!value.matches("[a-zA-Zа-яА-ЯёЁ\\-\\s]+"))
            throw new IllegalArgumentException(getLabel() + " может содержать только буквы, дефис и пробел");
    }
}
