package pgdp.adventuin;

import pgdp.adventuin.HatType;
import pgdp.adventuin.Language;
import pgdp.color.ExceptionUtil;
import pgdp.color.RgbColor;

public final class Adventuin {
    private final String name;
    private final int height;
    private final RgbColor color;
    private final HatType hatType;
    private final Language language;

    public Adventuin(String _name, int _height, RgbColor _color, HatType _hat, Language _lang) {
        if (_name == null || _name.isEmpty())
            ExceptionUtil.unsupportedOperation("Name cannot be empty!");
        name = _name;
        if (_height <= 0)
            ExceptionUtil.unsupportedOperation("Height must be bigger than 0!");
        height = _height;

        if (_color == null)
            ExceptionUtil.unsupportedOperation("Color cannot be null!");
        color = _color;

        if (_hat == null)
            ExceptionUtil.unsupportedOperation("HatType cannot be null!");
        hatType = _hat;

        if (_lang == null)
            ExceptionUtil.unsupportedOperation("Language cannot be null!");
        language = _lang;
    }

    @Override
    public String toString() {
        return String.format("%s: %d cm tall, color - %s, wears %s, speaks %s", name, height, color.toString(), hatType,
                language);
    }

    public int compareByHeight(Adventuin other) {
        if (height > other.height)
            return 1;
        if (height < other.height)
            return -1;
        return 0;
    }

    public String getName() {
        return name;
    }

    public int getHeight() {
        return height;
    }

    public RgbColor getColor() {
        return color;
    }

    public HatType getHatType() {
        return hatType;
    }

    public Language getLanguage() {
        return language;
    }
}