package com.example.userservice.vo;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;


/**
 * Абстрактный класс для строковых данных
 * @author vmarakushin
 * @version 1.0
 */
@MappedSuperclass
public abstract class StringVO {
    @Column(name = "value")
    protected String value;

    public StringVO(String value){
        isNullOrEmpty(value);
        value = value.trim();
        customValidate(value);
        this.value = value;
    }
    public StringVO(){}

    public String getValue() {return this.value;}

    protected abstract String getLabel();

    protected void  isNullOrEmpty(String value){
        if (value == null || value.isBlank())
            throw new IllegalArgumentException(getLabel() + " не может быть пустым");
    }

    protected abstract void  customValidate(String value);

    public String toString(){
        return this.value;
    }
}
