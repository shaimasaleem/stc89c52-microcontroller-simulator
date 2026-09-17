package simulator;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class SimulatorServer {

    private static CPU cpu = new CPU();
    private static FIFOQueue fifoQueue = cpu.getFIFOQueue();
    private static void addCorsHeaders(HttpExchange exchange) {

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

private static boolean handleOptions(HttpExchange exchange)
        throws IOException {

    addCorsHeaders(exchange);

    if (exchange.getRequestMethod()
            .equalsIgnoreCase("OPTIONS")) {

        exchange.sendResponseHeaders(204, -1);
        exchange.close();

        return true;
    }

    return false;
}
    public static void main(String[] args) throws Exception {

        System.out.println("Starting STC89C52 Simulator Server...");

        loadDemoProgram();

        HttpServer server = HttpServer.create(
                new InetSocketAddress(8080), 0
        );

        // API endpoints
        server.createContext("/api/state",
                SimulatorServer::handleState);

        server.createContext("/api/load",
                SimulatorServer::handleLoad);

        server.createContext("/api/reset",
                SimulatorServer::handleReset);

        server.createContext("/api/step",
                SimulatorServer::handleStep);

        server.createContext("/api/run",
                SimulatorServer::handleRun);
        server.createContext("/api/enqueue",
                SimulatorServer::handleEnqueue);
        server.createContext("/api/dequeue",
                SimulatorServer::handleDequeue);
        server.createContext("/api/push",
                SimulatorServer::handlePush);

        server.createContext("/api/pop",
                SimulatorServer::handlePop);
        // Home page
        server.createContext("/",
                SimulatorServer::handleHome);

        server.start();

        System.out.println("----------------------------------");
        System.out.println("STC89C52 Simulator Server Started");
        System.out.println("Open: http://localhost:8080");
        System.out.println("----------------------------------");

        Thread.currentThread().join();
    }


    // =========================================================
    // LOAD DEMO PROGRAM
    // =========================================================

    private static void loadDemoProgram() {

        Instruction[] program = {

                new Instruction(
                        "MOV",
                        new String[]{"A", "#10"}
                ),

                new Instruction(
                        "MOVC",
                        new String[]{"A", "10"}
                ),

                new Instruction(
                        "ADD",
                        new String[]{"A", "#5"}
                ),

                new Instruction(
                        "SUB",
                        new String[]{"A", "#2"}
                ),

                new Instruction(
                        "ANL",
                        new String[]{"A", "#3"}
                ),

                new Instruction(
                        "INC",
                        new String[]{"A"}
                ),

                new Instruction(
                        "SJMP",
                        new String[]{"7"}
                ),

                new Instruction(
                        "HALT",
                        new String[]{}
                )
        };

        // Code memory value
        cpu.setCodeMemory(10, 3);

        // Load program
        cpu.loadProgram(program);
    }


    // =========================================================
    // STATE
    // =========================================================

    private static void handleState(HttpExchange exchange)
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

        String response = createStateJSON();

        sendJSON(exchange, response);
    }


    // =========================================================
// LOAD
// =========================================================

private static void handleLoad(HttpExchange exchange)
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

    String body = new String(
        exchange.getRequestBody().readAllBytes(),
        StandardCharsets.UTF_8
    );

    Instruction[] program = parseProgram(body);

    cpu.reset();

    // Required for MOVC A,10
    cpu.setCodeMemory(10, 3);

    cpu.loadProgram(program);

    sendJSON(
        exchange,
        createStateJSON()
    );
}
// =========================================================
// PARSE PROGRAM FROM HTML
// =========================================================

private static Instruction[] parseProgram(String body) {

    int start = body.indexOf("[");
    int end = body.lastIndexOf("]");

    if (start == -1 || end == -1) {
        return new Instruction[0];
    }

    String programText = body.substring(start + 1, end);

    String[] instructionStrings =
            programText.split("\",\"");

    Instruction[] program =
            new Instruction[instructionStrings.length];

    for (int i = 0; i < instructionStrings.length; i++) {

        String instructionText =
                instructionStrings[i];

        instructionText =
                instructionText
                        .replace("\"", "")
                        .trim();

        String[] parts =
                instructionText.split("\\s+");

        String operation = parts[0];

        String[] operands;

        if (parts.length > 1) {
            operands = parts[1].split(",");
        } else {
            operands = new String[0];
        }

        program[i] =
                new Instruction(
                        operation,
                        operands
                );
    }

    return program;
}


    // =========================================================
    // RESET
    // =========================================================

    private static void handleReset(HttpExchange exchange)
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

        cpu.reset();

        sendJSON(
                exchange,
                createStateJSON()
        );
    }


    // =========================================================
    // STEP
    // =========================================================

    private static void handleStep(HttpExchange exchange)
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

        if (!cpu.isHalted()) {

            // FETCH → DECODE → EXECUTE
            cpu.step();

        }

        sendJSON(
                exchange,
                createStateJSON()
        );
    }


    // =========================================================
    // RUN
    // =========================================================

    private static void handleRun(HttpExchange exchange)
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

        if (!cpu.isHalted()) {

            cpu.run();

        }

        sendJSON(
                exchange,
                createStateJSON()
        );
    }

// =========================================================
// ENQUEUE
// =========================================================

private static void handleEnqueue(HttpExchange exchange)
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

    int value =
            Integer.parseInt(query.substring(6));

    cpu.enqueue(value);

    sendResponse(
            exchange,
            200,
            "Enqueued: " + value
    );
}
// =========================================================
// DEQUEUE
// =========================================================

private static void handleDequeue(HttpExchange exchange)
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

    int value = cpu.dequeue();

    sendResponse(
            exchange,
            200,
            "Dequeued: " + value
    );
}
private static void handlePush(HttpExchange exchange)
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

    try {

        int value =
                Integer.parseInt(query.substring(6));

        cpu.push(value);

        sendResponse(
                exchange,
                200,
                "Pushed: " + value
        );

    } catch (NumberFormatException e) {

        sendResponse(
                exchange,
                400,
                "Invalid value"
        );

    } catch (IllegalStateException e) {

        sendResponse(
                exchange,
                400,
                e.getMessage()
        );
    }
}
private static void handlePop(HttpExchange exchange)
        throws IOException {

    if (handleOptions(exchange)) {
        return;
    }

    try {

        int value = cpu.pop();

        sendResponse(exchange, 200, "Popped: " + value);

    } catch (IllegalStateException e) {

        sendResponse(exchange, 400, e.getMessage());
    }
}
    // =========================================================
    // CREATE CPU STATE JSON
    // =========================================================

    private static String createStateJSON() {

        StringBuilder json = new StringBuilder();

        json.append("{");

        // Accumulator
        json.append("\"accumulator\":");
        json.append(cpu.getAccumulator());
        json.append(",");

        // B Register
        json.append("\"bRegister\":");
        json.append(cpu.getBRegister());
        json.append(",");

        // Program Counter
        json.append("\"programCounter\":");
        json.append(cpu.getProgramCounter());
        json.append(",");

        // Stack Pointer
        json.append("\"stackPointer\":");
        json.append(cpu.getStackPointer());
        json.append(",");

        // Registers
        json.append("\"registers\":[");

        for (int i = 0; i < 8; i++) {

            json.append(cpu.getRegister(i));

            if (i < 7) {
                json.append(",");
            }
        }

        json.append("],");

        // Flags
        json.append("\"CY\":");
        json.append(cpu.getCY());
        json.append(",");

        json.append("\"AC\":");
        json.append(cpu.getAC());
        json.append(",");

        json.append("\"OV\":");
        json.append(cpu.getOV());
        json.append(",");

        json.append("\"P\":");
        json.append(cpu.getP());
        json.append(",");

        // Halted
        // Halted
        json.append("\"halted\":");
        json.append(cpu.isHalted());

        // FIFO Queue
        json.append(",\"queueSize\":");
        json.append(fifoQueue.getCount());

        json.append(",\"queue\":[");

        int[] values = fifoQueue.getQueueValues();

        for (int i = 0; i < values.length; i++) {
         if (i > 0) {
        json.append(",");
        }
         json.append(values[i]);
}

json.append("]");

// Data Memory
json.append(",\"memory\":[");

int[] memory = cpu.getDataMemory();

for (int i = 0; i < memory.length; i++) {
    json.append(memory[i]);

    if (i < memory.length - 1) {
        json.append(",");
    }
}

json.append("]");

json.append("}");
return json.toString();
    }


    // =========================================================
    // HOME
    // =========================================================

    private static void handleHome(HttpExchange exchange)
            throws IOException {

        String response =
                "STC89C52 Educational Microcontroller Simulator\n\n"
                + "Java CPU Backend is running.\n\n"
                + "API:\n"
                + "GET  /api/state\n"
                + "POST /api/load\n"
                + "POST /api/reset\n"
                + "POST /api/step\n"
                + "POST /api/run\n";

        sendResponse(
                exchange,
                200,
                response
        );
    }


    // =========================================================
    // SEND JSON
    // =========================================================

    private static void sendJSON(
            HttpExchange exchange,
            String response
    ) throws IOException {
        addCorsHeaders(exchange);

        byte[] data =
                response.getBytes(StandardCharsets.UTF_8);

        exchange.getResponseHeaders()
                .set(
                        "Content-Type",
                        "application/json"
                );

        exchange.getResponseHeaders()
                .set(
                        "Access-Control-Allow-Origin",
                        "*"
                );

        exchange.sendResponseHeaders(
                200,
                data.length
        );

        OutputStream output =
                exchange.getResponseBody();

        output.write(data);

        output.close();
    }


    // =========================================================
    // SEND NORMAL RESPONSE
    // =========================================================

    private static void sendResponse(
            HttpExchange exchange,
            int statusCode,
            String response
    ) throws IOException {
        addCorsHeaders(exchange);

        byte[] data =
                response.getBytes(StandardCharsets.UTF_8);

        exchange.getResponseHeaders()
                .set(
                        "Content-Type",
                        "text/plain"
                );

        exchange.getResponseHeaders()
                .set(
                        "Access-Control-Allow-Origin",
                        "*"
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
}
