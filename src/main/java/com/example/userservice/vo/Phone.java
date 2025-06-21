package com.example.userservice.vo;

import javax.persistence.Embeddable;


/**
 * Объект данных для телефона с валидацией
 * immutable
 * @author vmarakushin
 * @varsion 1.0
 */
@Embeddable
public class Phone extends StringVO {


    public Phone(String value) {
        super(value);
    }
    public Phone() {super();}

    protected String getLabel(){return "Телефон";}

    @Override
    protected void customValidate(String value) {
        if (!value.matches("^\\+\\d{11}$"))
            throw new IllegalArgumentException("Телефон должен соответствовать формату , например +79992225566");
    }
}
