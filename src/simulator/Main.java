
package simulator;

import simulator.integration.BlockingQueueIPC;

public class Main {

    public static void main(String[] args) {

        System.out.println("STC89C52 Week 4 Simulator Started");

        BlockingQueueIPC ipc = new BlockingQueueIPC();

        // Create Core
        Core core = new Core(
                ipc.getCommandQueue(),
                ipc.getResponseQueue(),
                ipc.getLogQueue()
        );

        // Create Logger
        Logger logger = new Logger(
                ipc.getLogQueue()
        );

        // Create UI Server
        SimulatorServer server = new SimulatorServer(
                ipc.getCommandQueue(),
                ipc.getResponseQueue()
        );

        // Start Core
        Thread coreThread = new Thread(core, "Core-Thread");
        coreThread.start();

        // Start Logger
        Thread loggerThread = new Thread(logger, "Logger-Thread");
        loggerThread.start();

        // Start UI Server
       // Start UI Server
try {
    server.startServer();

    // Automatically open simulator in browser
    new ProcessBuilder(
        "powershell.exe",
        "-NoProfile",
        "-Command",
        "Start-Process 'http://localhost:8080'"
    ).start();

    System.out.println("Opening simulator in browser...");

    } catch (Exception e) {
        e.printStackTrace();
    }
    }
}
