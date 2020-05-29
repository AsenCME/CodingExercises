package pgdp.color;

public class RgbColor {
    final int red, green, blue, depth;

    public RgbColor(int _depth, int _red, int _green, int _blue) {
        depth = _depth;
        red = _red;
        green = _green;
        blue = _blue;

        if (_depth <= 0 || _depth > 31)
            ExceptionUtil.unsupportedOperation("Color depth must be between 0 and 31");

        long maxValue = IntMath.powerOfTwo(depth) - 1;
        if (red < 0 || red > maxValue)
            ExceptionUtil.unsupportedOperation("Red channel out of range!");
        if (green < 0 || green > maxValue)
            ExceptionUtil.unsupportedOperation("Green channel out of range!");
        if (blue < 0 || blue > maxValue)
            ExceptionUtil.unsupportedOperation("Blue channel out of range!");
    }

    public int getRed() {
        return red;
    }

    public int getGreen() {
        return green;
    }

    public int getBlue() {
        return blue;
    }

    public int getBitDepth() {
        return depth;
    }

    public RgbColor8Bit toRgbColor8Bit() {
        int r = red, g = green, b = blue;
        if (depth > 8) {
            r = convertHigherColor(red);
            g = convertHigherColor(green);
            b = convertHigherColor(blue);
        } else if (depth < 8) {
            r = convertLowerColor(red);
            g = convertLowerColor(green);
            b = convertLowerColor(blue);
        }

        return new RgbColor8Bit(r, g, b);

    }

    private int convertHigherColor(int color) {
        int maxValue = 255;
        int power = IntMath.powerOfTwo(depth - 9);

        int newColor = color / power;
        int lastBit = newColor % 2;
        newColor /= 2;
        newColor += lastBit;

        if (newColor > maxValue)
            return maxValue;
        else
            return newColor;
    }

    private int convertLowerColor(int color) {
        int[] bin = toBinary(color);
        int[] newBin = new int[8];
        int binIndex = 0;
        for (int i = 0; i < 8; i++) {
            newBin[i] = bin[binIndex++];
            if (binIndex >= bin.length)
                binIndex = 0;
        }
        return toDecimal(newBin);
    }

    private int[] toBinary(int number) {
        int[] arr = new int[depth];
        int pos = arr.length;
        while (number > 0) {
            arr[--pos] = number % 2;
            number /= 2;
        }
        return arr;
    }

    private int toDecimal(int[] binary) {
        int num = 0;
        int power = 0;
        for (int i = binary.length - 1; i >= 0; i--)
            num += binary[i] * IntMath.powerOfTwo(power++);
        return num;
    }
}