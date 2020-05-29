package pgdp.minijvm;

public class Alloc extends Instruction {
    private final int i;

    public Alloc(int _i) {
        i = _i;
    }

    @Override
    public void execute(Simulator simulator) {
        simulator.getStack().alloc(i);
    }
}
