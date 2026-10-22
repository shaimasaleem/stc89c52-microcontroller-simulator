package simulator;

import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws Exception {

        // Start the requested process
        if (args.length > 0) {

            switch (args[0].toLowerCase()) {

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
                    System.out.println("Unknown process: " + args[0]);
            }

            return;
        }

        System.out.println("================================");
        System.out.println(" STC89C52 MICROCONTROLLER");
        System.out.println(" MULTIPROCESS SIMULATOR");
        System.out.println("================================");

        // Start Logger process
        launch("logger");
        Thread.sleep(500);

        // Start Core process
        launch("core");
        Thread.sleep(500);

        // Start UI process
        launch("ui");

        System.out.println("Three independent processes launched.");
        System.out.println("Open http://localhost:8080");
    }

    private static void launch(String role) throws IOException {

        String javaPath =
                System.getProperty("java.home")
                + File.separator
                + "bin"
                + File.separator
                + "java";

        String classPath =
                System.getProperty("java.class.path");

        ProcessBuilder builder = new ProcessBuilder(
                javaPath,
                "-cp",
                classPath,
                "simulator.Main",
                role
        );

        builder.inheritIO();

        builder.start();

        System.out.println(role.toUpperCase()
                + " process launched.");
    }
}
