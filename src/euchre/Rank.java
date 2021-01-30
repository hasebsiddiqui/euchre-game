/* Classroom use only */
package euchre;

public enum Rank {
    TWO(2), THREE(3), FOUR(4),
    FIVE(5), SIX(6), SEVEN(7),
    EIGHT(8), NINE(9), TEN(10),
    JACK(11), QUEEN(12), KING(13),
    ACE(14), JOKER(15);

    private final int value;

    Rank(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public boolean equals(Rank r) {
        return (this.value == r.getValue());
    }

    public int getMax() {
        return JOKER.getValue();
    }
}
