/* Classroom use only */
package euchre;

import java.util.ArrayList;

public class Deck {

    private ArrayList<Card> cards;
    private int index;

    Deck() {
        index = 0;
        cards = new ArrayList(0);
    }

    private void add1(Rank r, Suit s) {
        Card c = new Card(r, s);
        cards.add(c);
    }

    private void add6(Suit s) {
        add1(Rank.NINE, s);
        add1(Rank.TEN, s);
        add1(Rank.JACK, s);
        add1(Rank.QUEEN, s);
        add1(Rank.KING, s);
        add1(Rank.ACE, s);
    }

    public void setEuchre() {
        index = 0;
        cards.clear();
        add6(Suit.CLUBS);
        add6(Suit.DIAMONDS);
        add6(Suit.HEARTS);
        add6(Suit.SPADES);
    }

    // Modifies: cards
    // Effect: shuffles cards once
    public void inShuffle() {
        ArrayList<Card> shuffledDeck = new ArrayList<>();
        int halfIndex = cards.size() / 2;

        for (int i = 0; i < halfIndex; i++) {
            shuffledDeck.add(cards.get(i + halfIndex));
            shuffledDeck.add(cards.get(i));
        }
        cards = shuffledDeck;
    }

    public void shuffle(boolean b) {
        index = 0;
        if (b) {
            inShuffle();
            inShuffle();
            inShuffle();
            inShuffle();
            inShuffle();
            inShuffle();
            inShuffle();
        }
    }

    public Card deal1() {
        Card c = cards.get(index);
        index = (index + 1) % cards.size();
        return c;
    }

    public void show() {
        System.out.println("====================");
        for (int i = 0; i < cards.size(); i++) {
            System.out.println("   " + cards.get(i).name());
        }
    }

    public void checkCard() {
        for (int i = 0; i < cards.size(); i++) {
            for (int j = 0; j < cards.size() - 1; j++) {
                if (cards.get(j).lessThan(cards.get(j + 1), Suit.CLUBS)) {
                    Card temp = cards.remove(j);
                    cards.add(j + 1, temp);
                }
            }

        }

    }
}
