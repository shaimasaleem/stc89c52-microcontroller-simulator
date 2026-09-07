package simulator;

public class DataMemory {

    private int[] memory = new int[256];

    private void checkAddress(int address) {
        if (address < 0 || address > 0xFF) {
            throw new IllegalArgumentException("Invalid memory address");
        }
    }

    public void write(int address, int value) {
        checkAddress(address);
        memory[address] = value & 0xFF;
    }

    public int read(int address) {
        checkAddress(address);
        return memory[address];
    }
}
