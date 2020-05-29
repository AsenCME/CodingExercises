package pgdp.minijvm;

public class Add extends Instruction {
    @Override
    public void execute(Simulator simulator) {
        int a = simulator.getStack().pop();
        int b = simulator.getStack().pop();
        simulator.getStack().push(a + b);
    }
}