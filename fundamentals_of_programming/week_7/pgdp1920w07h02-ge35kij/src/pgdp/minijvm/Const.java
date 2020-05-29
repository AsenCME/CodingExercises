package pgdp.minijvm;

public class Const extends Instruction {
    private final int i;

    public Const(int _i) {
        i = _i;
    }

    @Override
    public void execute(Simulator simulator) {
        simulator.getStack().push(i);
    }
}