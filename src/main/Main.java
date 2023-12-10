package main;

import main.http.HttpTaskServer;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        new HttpTaskServer().start();
    }
}
