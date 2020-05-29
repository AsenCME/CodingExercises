package pgdp.minijvm;

public class Sub extends Instruction {
    @Override
    public void execute(Simulator simulator) {
        int a = simulator.getStack().pop();
        int b = simulator.getStack().pop();
        simulator.getStack().push(b - a);
    }
}