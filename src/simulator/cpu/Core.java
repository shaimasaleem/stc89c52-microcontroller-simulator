package simulator;

import java.io.*;
import java.net.*;
import java.util.concurrent.LinkedBlockingQueue;

import java.util.*;
import java.util.concurrent.BlockingQueue;

public class Core implements Runnable {

    // Maximum number of instructions allowed during RUN
    // This prevents an infinite SJMP loop.
    private static final int MAX_RUN_STEPS = 100_000;

    // CPU belongs only to the Core
    private final CPU cpu;

    // Queue for receiving commands from UI
    private final BlockingQueue<String> commandQueue;

    // Queue for sending responses back to UI
    private final BlockingQueue<String> responseQueue;

    // Queue for sending logs to Logger
    private final BlockingQueue<String> logQueue;

    // Controls Core thread
    private volatile boolean running = true;

    // CPU should be accessed by one command at a time
    private final Object cpuLock = new Object();


    // Constructor
    public Core(
            BlockingQueue<String> commandQueue,
            BlockingQueue<String> responseQueue,
            BlockingQueue<String> logQueue) {

        this.commandQueue = commandQueue;
        this.responseQueue = responseQueue;
        this.logQueue = logQueue;

        this.cpu = new CPU();
    }


    // Core thread starts here
    @Override
    public void run() {

        System.out.println("Core started.");

        while (running) {

            try {

                // Wait for a command from UI
                String command = commandQueue.take();

                System.out.println("[CORE RECEIVED] " + command);

                if (command == null || command.isBlank()) {
                    continue;
                }

                command = command.trim();

                // Process command
                String response = handle(command);

                // Send response back to UI
                if (response != null) {
                    responseQueue.put(response);
                }

            } catch (InterruptedException e) {

                Thread.currentThread().interrupt();
                break;

            } catch (Exception e) {

                try {
                    logQueue.put("CORE ERROR: " + e.getMessage());

                    responseQueue.put(
                        "{\"ok\":false,\"error\":\""
                        + quote(String.valueOf(e.getMessage()))
                        + "\"}"
                    );

                } catch (InterruptedException ex) {

                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }

        System.out.println("Core stopped.");
    }


    // Handle commands received through BlockingQueue
    private String handle(String line) {

        line = line.replaceFirst("\\|", " ");
        String[] parts = line.split("\\s+", 2); 

        String command = parts[0].toUpperCase();

        String argument =
                parts.length > 1 ? parts[1].trim() : "";


        // EXIT
        if (command.equals("EXIT")) {

            running = false;

            log("Core stopped.");

            return "{\"ok\":true,\"message\":\"Core stopped\"}";
        }


        synchronized (cpuLock) {

            long startTime = System.nanoTime();

            try {

                String extra = dispatch(command, argument);

                if (extra == null) {

                    log("Unknown command: " + line);

                    return "{\"ok\":false,\"error\":\"Unknown command: "
                            + quote(command) + "\"}";
                }


                long elapsed =
                        System.nanoTime() - startTime;


                StringBuilder result =
                        new StringBuilder("{\"ok\":true,");


                if (!extra.isEmpty()) {
                    result.append(extra).append(",");
                }


                result.append("\"elapsedNanos\":")
                      .append(elapsed)
                      .append(",");


                result.append("\"state\":")
                      .append(stateJson());


                result.append("}");


                return result.toString();


            } catch (Exception e) {

                log("CORE ERROR: " + command + " failed: "
                        + e.getMessage());

                return "{\"ok\":false,\"error\":\""
                        + quote(String.valueOf(e.getMessage()))
                        + "\"}";
            }
        }
    }


    // Execute individual Core commands
    private String dispatch(String command, String argument) {

        switch (command) {


            // Test communication
            case "PING":

                return "\"pong\":true";


            // Return current CPU state
            case "STATE":

                return "";


            // Reset CPU
            case "RESET":

                cpu.reset();

                log("RESET");

                return "";


            // Load program
            case "LOAD": {

                Instruction[] program =
                        parseProgram(argument);

                cpu.reset();

                cpu.loadProgram(program);

                log("LOAD " + program.length
                        + " instructions");

                return "\"loaded\":" + program.length;
            }


            // Execute one instruction
            case "STEP": {

                int pcBefore =
                        cpu.getProgramCounter();

                cpu.step();

                log("STEP pc=" + pcBefore
                        + "->" + cpu.getProgramCounter()
                        + " A=" + cpu.getAccumulator()
                        + " B=" + cpu.getBRegister()
                        + (cpu.isHalted()
                            ? " HALTED"
                            : ""));

                return "";
            }


            // Execute program
            case "RUN": {

                int steps = 0;


                while (!cpu.isHalted()
                        && steps < MAX_RUN_STEPS) {

                    cpu.step();

                    steps++;
                }


                boolean limitReached =
                        !cpu.isHalted();


                log("RUN steps=" + steps
                        + " A=" + cpu.getAccumulator()
                        + (limitReached
                            ? " STOPPED(step limit)"
                            : " HALTED"));


                if (limitReached) {

                    log("WARNING: RUN reached "
                            + MAX_RUN_STEPS
                            + " step limit");
                }


                return "\"steps\":" + steps
                        + ",\"stepLimitHit\":"
                        + limitReached;
            }


            // PUSH
            case "PUSH": {

                int value =
                        Integer.parseInt(argument);

                cpu.push(value);

                log("PUSH " + value
                        + " sp=" + cpu.getStackPointer());

                return "";
            }


            // POP
            case "POP": {

                int value = cpu.pop();

                log("POP -> " + value
                        + " sp=" + cpu.getStackPointer());

                return "\"value\":" + value;
            }


            // ENQUEUE
            case "ENQUEUE": {

                int value =
                        Integer.parseInt(argument);

                cpu.enqueue(value);

                log("ENQUEUE " + value);

                return "";
            }


            // DEQUEUE
            case "DEQUEUE": {

                int value = cpu.dequeue();

                log("DEQUEUE -> " + value);

                return "\"value\":" + value;
            }


            default:

                return null;
        }
    }


    // Send message to Logger through BlockingQueue
    private void log(String message) {

        try {

            logQueue.put(message);

        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();
        }
    }


    // Convert program text into Instruction objects
    static Instruction[] parseProgram(String text) {

        List<Instruction> list =
                new ArrayList<>();


        for (String raw : text.split(";")) {

            String instructionText =
                    raw.trim();


            if (instructionText.isEmpty()) {
                continue;
            }


            String[] parts =
                    instructionText.split("\\s+", 2);


            String operation =
                    parts[0].toUpperCase();


            String[] operands =
                    (parts.length > 1
                    && !parts[1].isBlank())

                    ? parts[1]
                        .trim()
                        .split("\\s*,\\s*")

                    : new String[0];


            list.add(
                new Instruction(
                    operation,
                    operands
                )
            );
        }


        return list.toArray(
                new Instruction[0]
        );
    }


    // Create JSON representation of CPU state
    private String stateJson() {

        StringBuilder sb =
                new StringBuilder("{");


        sb.append("\"accumulator\":")
          .append(cpu.getAccumulator());


        sb.append(",\"bRegister\":")
          .append(cpu.getBRegister());


        sb.append(",\"programCounter\":")
          .append(cpu.getProgramCounter());


        sb.append(",\"stackPointer\":")
          .append(cpu.getStackPointer());


        sb.append(",\"registers\":[");


        for (int i = 0; i < 8; i++) {

            if (i > 0) {
                sb.append(',');
            }

            sb.append(
                cpu.getRegister(i)
            );
        }


        sb.append("]");


        sb.append(",\"CY\":")
          .append(cpu.getCY());


        sb.append(",\"AC\":")
          .append(cpu.getAC());


        sb.append(",\"OV\":")
          .append(cpu.getOV());


        sb.append(",\"P\":")
          .append(cpu.getP());


        sb.append(",\"halted\":")
          .append(cpu.isHalted());


        FIFOQueue queue =
                cpu.getFIFOQueue();


        sb.append(",\"queueSize\":")
          .append(queue.getCount());


        sb.append(",\"queue\":")
          .append(
              Arrays.toString(
                  queue.getQueueValues()
              ).replace(" ", "")
          );


        sb.append(",\"memory\":")
          .append(
              Arrays.toString(
                  cpu.getDataMemory()
              ).replace(" ", "")
          );


        sb.append("}");


        return sb.toString();
    }


    // Escape characters for JSON
    private static String quote(String text) {

        StringBuilder sb =
                new StringBuilder("\"");


        for (char c : text.toCharArray()) {

            switch (c) {

                case '"':
                    sb.append("\\\"");
                    break;

                case '\\':
                    sb.append("\\\\");
                    break;

                case '\n':
                    sb.append("\\n");
                    break;

                case '\r':
                    sb.append("\\r");
                    break;

                case '\t':
                    sb.append("\\t");
                    break;

                default:
                    sb.append(c);
            }
        }


        return sb.append('"').toString();
    }
    
public static void startSocketServer() throws Exception {

    BlockingQueue<String> commands = new LinkedBlockingQueue<>();
    BlockingQueue<String> responses = new LinkedBlockingQueue<>();
    BlockingQueue<String> logs = new LinkedBlockingQueue<>();


    
Thread logSender = new Thread(() -> {
    while (!Thread.currentThread().isInterrupted()) {
        try {
            String message = logs.take();

            try (Socket socket = new Socket("127.0.0.1", 5002);
                 PrintWriter output = new PrintWriter(
                         socket.getOutputStream(), true)) {

                output.println(message);
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (IOException e) {
            System.err.println(
                    "Logger unavailable: " + e.getMessage()
            );
        }
    }
}, "Core-Log-Sender");

logSender.setDaemon(true);
logSender.start();

    Core core = new Core(commands, responses, logs);

    Thread cpuWorker = new Thread(core, "CPU-Worker");
    cpuWorker.start();

    try (ServerSocket server = new ServerSocket(5001)) {

        System.out.println("CORE PROCESS STARTED");
        System.out.println("Listening on port 5001");

        while (true) {

            // Wait for a connection from UI
            try (Socket socket = server.accept();

                 BufferedReader input = new BufferedReader(
                     new InputStreamReader(socket.getInputStream()));

                 PrintWriter output = new PrintWriter(
                     socket.getOutputStream(), true)) {

                String command = input.readLine();

                if (command == null) {
                    continue;
                }

                // Socket -> BlockingQueue
                commands.put(command);

                // Wait for CPU response
                String response = responses.take();

                // BlockingQueue -> Socket
                output.println(response);
            }
        }
    }
}

}
