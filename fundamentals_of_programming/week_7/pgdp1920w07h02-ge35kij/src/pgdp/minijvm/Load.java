package pgdp.minijvm;

public class Load extends Instruction {
    private final int i;

    public Load(int _i) {
        i = _i;
    }

    @Override
    public void execute(Simulator simulator) {
        int value = simulator.getStack().getValueAtIndex(i);
        simulator.getStack().push(value);
    }
}
