package com.example.userservice.service;

import java.util.Scanner;

/**
 * Класс-обертка для сканнера, исключительно для того,
 * чтобы можно было мокнуть и подставить в validInt()
 *
 * @author vmarakushin
 * @varsion 1.0
 */


public class Reader {
    private final Scanner scanner;

    public Reader(Scanner scanner) {
        this.scanner = scanner;
    }

    public String readLine() {
        return scanner.nextLine();
    }
}