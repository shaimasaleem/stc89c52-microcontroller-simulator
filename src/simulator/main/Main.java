package simulator;

import java.io.File;
import java.io.IOException;


public class Main {

    public static void main(String[] args) throws Exception {

        // If a process role is provided, start only that process
        if (args.length > 0) {
            startProcess(args[0]);
            return;
        }

        printBanner();

        // Launch the three independent simulator processes
        launch("logger");
        Thread.sleep(500);

        launch("core");
        Thread.sleep(500);

        launch("ui");

        System.out.println("\nAll simulator processes launched successfully.");
        System.out.println("Open: http://localhost:8080");
    }

    // Starts the process based on the role passed by Main
    private static void startProcess(String role) throws Exception {

        switch (role.toLowerCase()) {

            case "logger":
                Logger.startSocketServer();
                break;

            case "core":
                Core.startSocketServer();
                break;

            case "ui":
                SimulatorServer.startSocketServer();
                break;

            default:
                System.out.println("Unknown process: " + role);
        }
    }

    // Creates a new independent JVM process
    private static void launch(String role) throws IOException {

        String javaPath =
                System.getProperty("java.home")
                + File.separator
                + "bin"
                + File.separator
                + "java";

        String classPath = System.getProperty("java.class.path");

        ProcessBuilder builder = new ProcessBuilder(
                javaPath,
                "-cp",
                classPath,
                "simulator.Main",
                role
        );

        builder.inheritIO();
        builder.start();

        System.out.println("[MAIN] "
                + role.toUpperCase()
                + " process launched.");
    }

    // Displays simulator information
    private static void printBanner() {

        System.out.println("==========================================");
        System.out.println("       STC89C52 MICROCONTROLLER");
        System.out.println("         MULTIPROCESS SIMULATOR");
        System.out.println("==========================================");
    }
}
