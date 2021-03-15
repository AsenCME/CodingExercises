package pgdp.robot;

import java.util.function.Consumer;

public abstract class Sensor<T> {
    protected Robot owner;
    protected Consumer<T> processor;

    void setOwner(Robot robot) {
        this.owner = robot;
    }

    public Sensor<T> setProcessor(Consumer<T> processor) {
        this.processor = processor;
        return this;
    }

    public abstract T getData();
}
