package pgdp.color;

public class RgbaColor extends RgbColor {
    final int alpha;

    public RgbaColor(int _depth, int _red, int _green, int _blue, int _alpha) {
        super(_depth, _red, _green, _blue);
        alpha = _alpha;

        long maxValue = IntMath.powerOfTwo(_depth) - 1;
        if (alpha < 0 || alpha > maxValue)
            ExceptionUtil.unsupportedOperation("Alpha channel not in range!");
    }

    public int getAlpha() {
        return alpha;
    }

    @Override
    public RgbColor8Bit toRgbColor8Bit() {
        long maxValue = IntMath.powerOfTwo(depth) - 1;
        if (alpha != maxValue)
            ExceptionUtil.unsupportedOperation("Cannot convert color, alpha channel not supported!");
        return super.toRgbColor8Bit();
    }
}