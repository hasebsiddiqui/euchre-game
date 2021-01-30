/* Classroom use only */
package euchre;

import java.util.ArrayList;

public class CardGroup {

    private ArrayList<Card> cards;

    CardGroup() {
        cards = new ArrayList<Card>(0);
    }

    public Card getCard(int i) {
        return cards.get(i);
    }

    public int numCards() {
        return cards.size();
    }

    public void clear() {
        cards.clear();
    }

    public void addCard(Card c) {
        cards.add(c);
    }

    // Modifies: Cards
    // Effect: removes card c from group
    public void removeCard(Card c) {
        cards.remove(c);
    }

    // Effect: returns lowest card, considering trump
    public Card findLowest(Suit trump) {
        Card card = new Card();

        for (int i = 0; i < cards.size(); i++) {
            if (i == 0) {
                card = cards.get(i);
            } else if (cards.get(i).lessThan(card, trump)) {
                card = cards.get(i);
            }

        }

        return card;
    }

    // Effect: returns highest card, considering trump
    public Card findHighest(Suit trump) {
        Card card = new Card();

        for (int i = 0; i < cards.size(); i++) {
            if (i == 0) {
                card = cards.get(i);
            } else {
                if (!cards.get(i).lessThan(card, trump)) {
                    card = cards.get(i);
                }
            }
        }

        return card;
    }

    // Effect: true if hand has suit test, considering trump
    public boolean hasSuit(Suit trump, Suit test) {
        for (Card temp : cards) {
            if (temp.getSuit(trump) == test) {
                return true;
            }
        }
        return false;
    }

    // Requires: has a card in suit
    // Effect: finds lowest, considering trump (ie, left bower)
    public Card findLowestInSuit(Suit trump, Suit test) {
        Card card = null;

        for (int i = 0; i < cards.size(); i++) {
            if (card == null && cards.get(i).getSuit(trump) == test) {
                card = cards.get(i);
            } else if (cards.get(i).getSuit(trump) == test) {
                if (cards.get(i).lessThan(card, trump)) {
                    card = cards.get(i);
                }
            }
        }

        return card;
    }

    // Requires: has a card in suit
    // Effect: finds lowest, considering trump (ie, left bower)
    public Card findHighestInSuit(Suit trump, Suit test) {
        Card card = null;

        for (int i = 0; i < cards.size(); i++) {
            if (card == null && cards.get(i).getSuit(trump) == test) {
                card = cards.get(i);
            } else if (cards.get(i).getSuit(trump) == test) {
                if (!cards.get(i).lessThan(card, trump)) {
                    card = cards.get(i);
                }
            }
        }

        return card;
    }

    public void show() {
        for (int i = 0; i < cards.size(); i++) {
            Card c = cards.get(i);
            System.out.println("   " + c.name());
        }
    }
}
