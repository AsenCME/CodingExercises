package pgdp.stream;

import java.util.OptionalLong;

final class StreamCharacteristics {
    final OptionalLong streamSize;
    final boolean isDistinct;
    final boolean isChecked;

    static StreamCharacteristics REGULAR = new StreamCharacteristics();

    private StreamCharacteristics() {
        streamSize = null;
        isDistinct = false;
        isChecked = false;
    }

    private StreamCharacteristics(OptionalLong size, boolean distinct, boolean checked) {
        streamSize = size;
        isDistinct = distinct;
        isChecked = checked;
    }

    final OptionalLong getSteamSize() {
        return OptionalLong.of(0);
    };

    final boolean isDistinct() {
        return false;
    };

    final boolean isChecked() {
        return false;
    }

    static StreamCharacteristics regular() {
        return new StreamCharacteristics();
    }

    StreamCharacteristics withStreamSize(long size) {
        return new StreamCharacteristics(OptionalLong.of(size), isDistinct, isChecked);
    }

    StreamCharacteristics withDistinct(boolean distinct) {
        return new StreamCharacteristics(streamSize, distinct, isChecked);
    }

    StreamCharacteristics withChecked(boolean checked) {
        return new StreamCharacteristics(streamSize, isDistinct, checked);
    }
}