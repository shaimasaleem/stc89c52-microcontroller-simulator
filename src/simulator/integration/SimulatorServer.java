package simulator;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class SimulatorServer {


    // =====================================================
    // MAIN
    // =====================================================

    public static void main(String[] args) throws Exception {

    SimulatorServer simulatorServer =
            new SimulatorServer();

    simulatorServer.startServer();
}
    // =====================================================
    // START SERVER
    // =====================================================

    public void startServer() throws Exception {

        System.out.println(
                "Starting STC89C52 Simulator UI Server..."
        );


        HttpServer server = HttpServer.create(
                new InetSocketAddress(8080),
                0
        );


        // =================================================
        // API ENDPOINTS
        // =================================================

        server.createContext(
                "/api/state",
                this::handleState
        );

        server.createContext(
                "/api/load",
                this::handleLoad
        );

        server.createContext(
                "/api/reset",
                this::handleReset
        );

        server.createContext(
                "/api/step",
                this::handleStep
        );

        server.createContext(
                "/api/run",
                this::handleRun
        );

        server.createContext(
                "/api/enqueue",
                this::handleEnqueue
        );

        server.createContext(
                "/api/dequeue",
                this::handleDequeue
        );

        server.createContext(
                "/api/push",
                this::handlePush
        );

        server.createContext(
                "/api/pop",
                this::handlePop
        );


        // Home page
        server.createContext(
                "/",
                this::handleHome
        );


        server.start();

        System.out.println("[UI] Started - Port 8080"); 


        Thread.currentThread().join();
    }


    // =====================================================
    // SEND COMMAND TO CORE
    // =====================================================
    // UI sends commands to the Core process using TCP sockets.
    //
    // STEP
    // RUN
    // RESET
    // STATE
    // LOAD
    // ENQUEUE
    // DEQUEUE
    // PUSH
    // POP
// =====================================================
// SEND UI LOG TO LOGGER PROCESS
// =====================================================

private void sendUILog(String message) {

    try (
        Socket socket = new Socket("127.0.0.1", 5002);

        PrintWriter output = new PrintWriter(
                socket.getOutputStream(), true)
    ) {

        output.println("[UI] " + message);

    } catch (IOException e) {

        System.out.println(
                "UI Logger unavailable: " + e.getMessage()
        );
    }
}
 
private synchronized String sendCommand(String command)
        throws IOException {

    // Decide whether this command should be stored in UI history
    // STATE is ignored because the UI requests it many times automatically
    boolean shouldLog =
            !command.trim().equalsIgnoreCase("STATE");

    // Send UI history to Logger
    if (shouldLog) {

        if (command.startsWith("LOAD|")) {
            sendUILog("Sending program to Core");
        } else {
            sendUILog("Sending to Core: " + command);
        }
    }

    try (
        Socket socket = new Socket("127.0.0.1", 5001);

        PrintWriter output = new PrintWriter(
                socket.getOutputStream(), true);

        BufferedReader input = new BufferedReader(
                new InputStreamReader(socket.getInputStream()))
    ) {

        // Send command to Core
        output.println(command);

        // Receive response from Core
        String response = input.readLine();

        if (response == null) {
            throw new IOException("Core disconnected");
        }

        // Store UI response history
        if (shouldLog) {
            sendUILog("Response received from Core");
        }

        return response;
    }
}


    // =====================================================
    // CORS
    // =====================================================

    private void addCorsHeaders(
            HttpExchange exchange) {

        exchange.getResponseHeaders().set(
                "Access-Control-Allow-Origin",
                "*"
        );

        exchange.getResponseHeaders().set(
                "Access-Control-Allow-Methods",
                "GET, POST, OPTIONS"
        );

        exchange.getResponseHeaders().set(
                "Access-Control-Allow-Headers",
                "Content-Type"
        );
    }


    // =====================================================
    // OPTIONS
    // =====================================================

    private boolean handleOptions(
            HttpExchange exchange)
            throws IOException {

        addCorsHeaders(exchange);

        if (exchange.getRequestMethod()
                .equalsIgnoreCase("OPTIONS")) {

            exchange.sendResponseHeaders(
                    204,
                    -1
            );

            exchange.close();

            return true;
        }

        return false;
    }


    // =====================================================
    // STATE
    // =====================================================

    private void handleState(
            HttpExchange exchange)
            throws IOException {

        if (handleOptions(exchange)) {
            return;
        }


        if (!exchange.getRequestMethod()
                .equalsIgnoreCase("GET")) {

            sendResponse(
                    exchange,
                    405,
                    "Method Not Allowed"
            );

            return;
        }


        try {

            /*
             * Ask Core for current CPU state.
             */

            String response =
                    sendCommand("STATE");


            sendJSON(
                    exchange,
                    response
            );

        } catch (IOException e) {

            Thread.currentThread().interrupt();

            sendResponse(
                    exchange,
                    500,
                    "Core response interrupted"
            );
        }
    }


    // =====================================================
    // LOAD PROGRAM
    // =====================================================

   private void handleLoad(HttpExchange exchange)
        throws IOException {

    if (handleOptions(exchange)) {
        return;
    }

    if (!exchange.getRequestMethod()
            .equalsIgnoreCase("POST")) {

        sendResponse(exchange, 405, "Method Not Allowed");
        return;
    }

    String body = new String(
            exchange.getRequestBody().readAllBytes(),
            StandardCharsets.UTF_8
    );

    String command = "LOAD|" + body;

    System.out.println("[UI RECEIVED PROGRAM] " + body);
    System.out.println("[UI SENDING TO CORE] " + command);

    try {
        String response = sendCommand(command);
        sendJSON(exchange, response);

    } catch (IOException e) {
        Thread.currentThread().interrupt();

        sendResponse(
                exchange,
                500,
                "Core response interrupted"
        );
    }
}
    // =====================================================
    // RESET
    // =====================================================

    private void handleReset(
            HttpExchange exchange)
            throws IOException {

        if (handleOptions(exchange)) {
            return;
        }


        if (!exchange.getRequestMethod()
                .equalsIgnoreCase("POST")) {

            sendResponse(
                    exchange,
                    405,
                    "Method Not Allowed"
            );

            return;
        }


        try {

            String response =
                    sendCommand("RESET");


            sendJSON(
                    exchange,
                    response
            );

        } catch (IOException e) {

            Thread.currentThread().interrupt();

            sendResponse(
                    exchange,
                    500,
                    "Core response interrupted"
            );
        }
    }


    // =====================================================
    // STEP
    // =====================================================

    private void handleStep(
            HttpExchange exchange)
            throws IOException {

        if (handleOptions(exchange)) {
            return;
        }


        if (!exchange.getRequestMethod()
                .equalsIgnoreCase("POST")) {

            sendResponse(
                    exchange,
                    405,
                    "Method Not Allowed"
            );

            return;
        }


        try {

            /*
             * Send STEP command to Core.
             */

            String response =
                    sendCommand("STEP");


            sendJSON(
                    exchange,
                    response
            );

        } catch (IOException e) {

            Thread.currentThread().interrupt();

            sendResponse(
                    exchange,
                    500,
                    "Core response interrupted"
            );
        }
    }


    // =====================================================
    // RUN
    // =====================================================

    private void handleRun(
            HttpExchange exchange)
            throws IOException {

        if (handleOptions(exchange)) {
            return;
        }


        if (!exchange.getRequestMethod()
                .equalsIgnoreCase("POST")) {

            sendResponse(
                    exchange,
                    405,
                    "Method Not Allowed"
            );

            return;
        }


        try {

            /*
             * Send RUN command to Core.
             */

            String response =
                    sendCommand("RUN");


            sendJSON(
                    exchange,
                    response
            );

        } catch (IOException e) {

            Thread.currentThread().interrupt();

            sendResponse(
                    exchange,
                    500,
                    "Core response interrupted"
            );
        }
    }


    // =====================================================
    // ENQUEUE
    // =====================================================

    private void handleEnqueue(
            HttpExchange exchange)
            throws IOException {

        if (handleOptions(exchange)) {
            return;
        }


        if (!exchange.getRequestMethod()
                .equalsIgnoreCase("POST")) {

            sendResponse(
                    exchange,
                    405,
                    "Method Not Allowed"
            );

            return;
        }


        String query =
                exchange.getRequestURI().getQuery();


        if (query == null ||
                !query.startsWith("value=")) {

            sendResponse(
                    exchange,
                    400,
                    "Missing value"
            );

            return;
        }


        String value =
                query.substring(6);


        try {

            String response =
                    sendCommand(
                            "ENQUEUE|" + value
                    );


            sendResponse(
                    exchange,
                    200,
                    response
            );

        } catch (IOException e) {

            Thread.currentThread().interrupt();

            sendResponse(
                    exchange,
                    500,
                    "Core response interrupted"
            );
        }
    }


    // =====================================================
    // DEQUEUE
    // =====================================================

    private void handleDequeue(
            HttpExchange exchange)
            throws IOException {

        if (handleOptions(exchange)) {
            return;
        }


        if (!exchange.getRequestMethod()
                .equalsIgnoreCase("POST")) {

            sendResponse(
                    exchange,
                    405,
                    "Method Not Allowed"
            );

            return;
        }


        try {

            String response =
                    sendCommand("DEQUEUE");


            sendResponse(
                    exchange,
                    200,
                    response
            );

        } catch (IOException e) {

            Thread.currentThread().interrupt();

            sendResponse(
                    exchange,
                    500,
                    "Core response interrupted"
            );
        }
    }


    // =====================================================
    // PUSH
    // =====================================================

    private void handlePush(
            HttpExchange exchange)
            throws IOException {

        if (handleOptions(exchange)) {
            return;
        }


        if (!exchange.getRequestMethod()
                .equalsIgnoreCase("POST")) {

            sendResponse(
                    exchange,
                    405,
                    "Method Not Allowed"
            );

            return;
        }


        String query =
                exchange.getRequestURI().getQuery();


        if (query == null ||
                !query.startsWith("value=")) {

            sendResponse(
                    exchange,
                    400,
                    "Missing value"
            );

            return;
        }


        String value =
                query.substring(6);


        try {

            String response =
                    sendCommand(
                            "PUSH|" + value
                    );


            sendResponse(
                    exchange,
                    200,
                    response
            );

        } catch (IOException e) {

            Thread.currentThread().interrupt();

            sendResponse(
                    exchange,
                    500,
                    "Core response interrupted"
            );
        }
    }


    // =====================================================
    // POP
    // =====================================================

    private void handlePop(
            HttpExchange exchange)
            throws IOException {

        if (handleOptions(exchange)) {
            return;
        }


        if (!exchange.getRequestMethod()
                .equalsIgnoreCase("POST")) {

            sendResponse(
                    exchange,
                    405,
                    "Method Not Allowed"
            );

            return;
        }


        try {

            String response =
                    sendCommand("POP");


            sendResponse(
                    exchange,
                    200,
                    response
            );

        } catch (IOException e) {

            Thread.currentThread().interrupt();

            sendResponse(
                    exchange,
                    500,
                    "Core response interrupted"
            );
        }
    }


    // =====================================================
    // HOME PAGE
    // =====================================================

   private void handleHome(HttpExchange exchange)
        throws IOException {

    if (!exchange.getRequestMethod()
            .equalsIgnoreCase("GET")) {

        sendResponse(exchange, 405, "Method Not Allowed");
        return;
    }

    String htmlPath = "ui-process/index.html";

    java.nio.file.Path path =
            java.nio.file.Paths.get(htmlPath)
                    .toAbsolutePath()
                    .normalize();

    if (!java.nio.file.Files.exists(path)) {
        sendResponse(
                exchange,
                404,
                "index.html not found: " + path
        );
        return;
    }

    byte[] data = java.nio.file.Files.readAllBytes(path);

    exchange.getResponseHeaders().set(
            "Content-Type",
            "text/html; charset=UTF-8"
    );

    exchange.sendResponseHeaders(200, data.length);

    try (OutputStream output = exchange.getResponseBody()) {
        output.write(data);
    }
}
// SEND JSON RESPONSE
private void sendJSON(
        HttpExchange exchange,
        String response)
        throws IOException {

    addCorsHeaders(exchange);

    byte[] data = response.getBytes(
            StandardCharsets.UTF_8
    );

    exchange.getResponseHeaders().set(
            "Content-Type",
            "application/json; charset=UTF-8"
    );

    exchange.sendResponseHeaders(
            200,
            data.length
    );

    try (OutputStream output =
                 exchange.getResponseBody()) {

        output.write(data);
    }
}

    // =====================================================
    // SEND NORMAL RESPONSE
    // =====================================================

    private void sendResponse(
            HttpExchange exchange,
            int statusCode,
            String response)
            throws IOException {

        addCorsHeaders(exchange);


        byte[] data =
                response.getBytes(
                        StandardCharsets.UTF_8
                );


        exchange.getResponseHeaders().set(
                "Content-Type",
                "text/plain"
        );


        exchange.sendResponseHeaders(
                statusCode,
                data.length
        );


        OutputStream output =
                exchange.getResponseBody();


        output.write(data);

        output.close();
    }
    

public static void startSocketServer() throws Exception {

    SimulatorServer server = new SimulatorServer();

    server.startServer();
}


}
