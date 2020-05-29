package pgdp.minijvm;

public class FJump extends Instruction {
    private final int i;

    public FJump(int _i) {
        i = _i;
    }

    @Override
    public void execute(Simulator simulator) {
        boolean truthiness = simulator.getStack().pop() == 0 ? true : false;
        if (truthiness)
            simulator.setProgramCounter(i);
    }
}
