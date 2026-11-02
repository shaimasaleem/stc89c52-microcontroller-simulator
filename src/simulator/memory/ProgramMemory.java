package simulator;

import java.util.ArrayList;
import java.util.List;

public class ProgramMemory {

    private List<Instruction> instructions = new ArrayList<>();

    public void load(Instruction instruction) {
        instructions.add(instruction);
    }

    public Instruction read(int address) {

        if (address < 0 || address >= instructions.size()) {
            throw new IllegalArgumentException("Invalid program address");
        }

        return instructions.get(address);
    }

    public int size() {
        return instructions.size();
    }

    public void clear() {
        instructions.clear();
    }
    public void loadProgram(Instruction[] program) {

    instructions.clear();

    for (Instruction instruction : program) {
        instructions.add(instruction);
    }
}
}
