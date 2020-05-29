package pgdp.minijvm;

public class Jump extends Instruction {
    private final int i;

    public Jump(int _i) {
        i = _i;
    }

    @Override
    public void execute(Simulator simulator) {
        simulator.setProgramCounter(i);
    }
}
