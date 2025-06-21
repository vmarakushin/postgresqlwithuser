package com.example.userservice.vo;

import javax.persistence.Embeddable;

/**
 * Объект данных для электронки с валидацией
 * immutable
 * @author vmarakushin
 * @varsion 1.0
 */
@Embeddable
public class Email extends StringVO{

    public Email(String value) {
        super(value);
    }
    public Email(){
        super();
    }

    protected String getLabel(){return "Email";}
    @Override
    protected void customValidate(String value) {
        if (!value.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"))
            throw new IllegalArgumentException("Email должен соответствовать формату , например example@example.com");
    }

}
