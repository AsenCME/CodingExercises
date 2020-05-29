package pgdp.color;

public class RgbColor8Bit extends RgbColor {

    public RgbColor8Bit(int _red, int _green, int _blue) {
        super(8, _red, _green, _blue);
    }

    @Override
    public RgbColor8Bit toRgbColor8Bit() {
        return this;
    }
}