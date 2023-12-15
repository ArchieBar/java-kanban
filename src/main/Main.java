package main;

import main.http.HttpTaskManager;
import main.http.HttpTaskServer;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        KVServer kvServer = new KVServer();
        kvServer.start();

        HttpTaskServer httpTaskServer = new HttpTaskServer();
        httpTaskServer.start();

//        HttpTaskManager taskManager = new HttpTaskManager(KVServer.PORT);
//        taskManager = taskManager.load();
    }
}
