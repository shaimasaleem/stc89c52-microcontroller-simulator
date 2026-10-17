package simulator;

import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.LinkedBlockingQueue;
public class Logger implements Runnable {

    private final BlockingQueue<String> logQueue;
    private final String logFile = "simulator.log";

    public Logger(BlockingQueue<String> logQueue) {
        this.logQueue = logQueue;
    }

    @Override
    public void run() {

        System.out.println("Logger started.");

        while (true) {

            try {
                String message = logQueue.take();

                if (message.equals("EXIT")) {
                    System.out.println("Logger stopped.");
                    break;
                }

                System.out.println("[LOGGER] " + message);

                writeToFile(message);

            } catch (InterruptedException e) {

                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    private void writeToFile(String message) {

        try (FileWriter writer = new FileWriter(logFile, true)) {

            writer.write(message);
            writer.write(System.lineSeparator());

        } catch (IOException e) {

            System.out.println(
                "Logger file error: " + e.getMessage()
            );
        }
    }
    
public static void startSocketServer() throws Exception {

    BlockingQueue<String> logs = new LinkedBlockingQueue<>();

    Logger logger = new Logger(logs);

    Thread loggerWorker = new Thread(logger, "Logger-Worker");
    loggerWorker.start();

    try (ServerSocket server = new ServerSocket(5002)) {

        System.out.println("LOGGER PROCESS STARTED");
        System.out.println("Listening on port 5002");

        while (true) {

            try (Socket socket = server.accept();
                 BufferedReader input = new BufferedReader(
                     new InputStreamReader(socket.getInputStream()))) {

                String message;

                while ((message = input.readLine()) != null) {
                    logs.put(message);
                }
            }
        }
    }
}

}
