package com.example.userservice.vo;

import javax.persistence.Column;
import javax.persistence.Embeddable;


/**
 * Объект данных для возраста с валидацией
 * immutable
 * @author vmarakushin
 * @varsion 1.0
 */
@Embeddable
public class Age {
    @Column(name = "age")
    private int value;

    public Age() {}
    public Age(int value) {

        if ( value < 1 || value > 150)
            throw new IllegalArgumentException("Младенцам и врунам вход воспрещён");

        this.value = value;
    }

    public int getValue() {return value;}

    public String toString() {
        return String.valueOf(this.value);
    }
}
