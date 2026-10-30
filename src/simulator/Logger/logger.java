package simulator;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Logger implements Runnable {

    private final BlockingQueue<String> logQueue;
    private final String logFile = "simulator.log";

    // Constructor
    public Logger(BlockingQueue<String> logQueue) {
        this.logQueue = logQueue;
    }

    // Logger worker thread
    @Override
    public void run() {

        while (!Thread.currentThread().isInterrupted()) {

            try {
                String message = logQueue.take();

                if ("EXIT".equals(message)) {
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

    // Write log message to simulator.log
    private void writeToFile(String message) {

        try (FileWriter writer = new FileWriter(logFile, true)) {

            writer.write(message);
            writer.write(System.lineSeparator());

        } catch (IOException e) {

            System.err.println(
                    "[LOGGER ERROR] " + e.getMessage()
            );
        }
    }

   // Start socket server for the independent Logger process
    public static void startSocketServer() throws Exception {

        BlockingQueue<String> logs =
                new LinkedBlockingQueue<>();

        Logger logger = new Logger(logs);

        Thread loggerWorker =
                new Thread(logger, "Logger-Worker");

        loggerWorker.start();

        try (ServerSocket server = new ServerSocket(5002)) {

            System.out.println(
                    "[LOGGER] Started - Port 5002"
            );

            while (true) {

                try (
                    Socket socket = server.accept();

                    BufferedReader input =
                            new BufferedReader(
                                    new InputStreamReader(
                                            socket.getInputStream()
                                    )
                            )
                ) {

                    String message;

                    while ((message = input.readLine()) != null) {
                        logs.put(message);
                    }
                }
            }
        }
    }
}
