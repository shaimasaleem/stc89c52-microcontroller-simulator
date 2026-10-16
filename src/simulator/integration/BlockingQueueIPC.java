package simulator.integration;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class BlockingQueueIPC {

    // UI → Core
    private final BlockingQueue<String> commandQueue;

    // Core → UI
    private final BlockingQueue<String> responseQueue;

    // Core → Logger
    private final BlockingQueue<String> logQueue;


    public BlockingQueueIPC() {

        commandQueue = new LinkedBlockingQueue<>();
        responseQueue = new LinkedBlockingQueue<>();
        logQueue = new LinkedBlockingQueue<>();
    }


    public BlockingQueue<String> getCommandQueue() {
        return commandQueue;
    }


    public BlockingQueue<String> getResponseQueue() {
        return responseQueue;
    }


    public BlockingQueue<String> getLogQueue() {
        return logQueue;
    }


    // Send command from UI to Core
    public void sendCommand(String command)
            throws InterruptedException {

        commandQueue.put(command);
    }


    // Receive command by Core
    public String receiveCommand()
            throws InterruptedException {

        return commandQueue.take();
    }


    // Send response from Core to UI
    public void sendResponse(String response)
            throws InterruptedException {

        responseQueue.put(response);
    }


    // Receive response by UI
    public String receiveResponse()
            throws InterruptedException {

        return responseQueue.take();
    }


    // Send log from Core to Logger
    public void sendLog(String log)
            throws InterruptedException {

        logQueue.put(log);
    }


    // Receive log by Logger
    public String receiveLog()
            throws InterruptedException {

        return logQueue.take();
    }
}
