
package euchre;

public class Card {

    private Rank rank;
    private Suit suit;

    // Effect: initializes to Two of Clubs
    Card() {
        rank = Rank.TWO;
        suit = Suit.CLUBS;
    }

    // Requires r and s valid rank and suit
    // Effect: initializes Card to r & s
    Card(Rank r, Suit s) {
        rank = r;
        suit = s;
    }

    // Effect: returns rank
    public Rank getRank() {
        return rank;
    }

    // Effect: returns suit, ignoring trump
    public Suit getSuit() {
        return suit;
    }

    // Requires: trump is valid trump
    // Effect: returns suit, considering trump
    public Suit getSuit(Suit trump) {
        if (isLeft(trump)) {
            return trump;
        } else {
            return suit;
        }
    }

    // Requires: trump is valid trump
    // Effect: true if is trump, including bowers
    public boolean isTrump(Suit trump) {
        if (isLeft(trump)) {
            return true;
        } else if (trump == suit) {
            return true;
        }
        return false;
    }

    // Requires: trump is valid trump
    // Effect: returns true if left bower
    public boolean isLeft(Suit trump) {
        if (rank == Rank.JACK && suit == trump.opposite()) {
            return true;
        }

        return false;
    }

    // Requires: trump is valid trump
    // Effect: returns true if righjt bower
    public boolean isRight(Suit trump) {
        if (rank == Rank.JACK && trump == suit) {
            return true;
        }
        return false;
    }

    // Effect: returns true if a face card (JQKA)
    public boolean isFace() {
        if (rank == Rank.ACE || rank == Rank.JACK || rank == Rank.KING || rank == Rank.QUEEN) {
            return true;
        }
        return false;
    }

    public String name() {
        return (rank + " of " + suit);
    }

    // Requires: c is a valid card, c and Card not equal
    // Effect: returns true if thiscard < c, considering trump
    public boolean lessThan(Card c, Suit trump) {
        if (c.isTrump(trump) && !isTrump(trump)) {
            return true;
        } else if (!c.isTrump(trump) && isTrump(trump)) {
            return false;
        } else if (c.isTrump(trump) && isTrump(trump)) {
            if (c.isRight(trump)) {
                return true;
            } else if (isRight(trump)) {
                return false;
            } else if (c.isLeft(trump)) {
                return true;
            } else if (isLeft(trump)) {
                return false;
            } else if (getRank().getValue() < c.getRank().getValue()) {
                return true;
            } else {
                return false;
            }
        } else {
            if (getRank().getValue() < c.getRank().getValue()) {
                return true;
            } else {
                return false;
            }
        }
    }
}
