package pgdp.minijvm;

public class Store extends Instruction {
    private final int i;

    public Store(int _i) {
        i = _i;
    }

    @Override
    public void execute(Simulator simulator) {
        int value = simulator.getStack().pop();
        simulator.getStack().setValueAtIndex(i, value);
    }
}
