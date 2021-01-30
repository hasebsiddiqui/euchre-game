/* Classroom use only */
package euchre;

public enum Suit {
    JOKER(4), SPADES(3), HEARTS(2), DIAMONDS(1), CLUBS(0);

    private final int value;

    Suit(int value) {
        this.value = value;
    }

    public Suit opposite() {
        if (value == 0) {
            return SPADES;
        }
        if (value == 1) {
            return HEARTS;
        }
        if (value == 2) {
            return DIAMONDS;
        }
        if (value == 4) {
            return JOKER;
        } else {
            return CLUBS;
        }
    }

}
