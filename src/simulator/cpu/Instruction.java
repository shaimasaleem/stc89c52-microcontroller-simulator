package simulator;

public class Instruction {
    private String operation;
    private String[] operands;
    public Instruction(String operation, String[] operands){
        this.operation=operation;
        this.operands=operands;

    }
    public String getOperation() {
        return operation;
    }
    public String[] getOperands() {
        return operands;
    }

}
