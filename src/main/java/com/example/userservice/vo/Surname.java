package com.example.userservice.vo;

import javax.persistence.Embeddable;


/**
 * Объект данных для фамилии с валидацией
 * immutable
 * @author vmarakushin
 * @varsion 1.0
 */
@Embeddable
public class Surname extends Name{



    public  Surname(String value) {super(value);}
    public Surname() {super();}
    @Override
    protected String getLabel() {return "Фамилия";}
}
