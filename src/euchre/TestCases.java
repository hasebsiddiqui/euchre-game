
package euchre;

public class TestCases {

    int score = 0;

    private int test(boolean passes, String msg) {
        if (passes) {
            System.out.println(msg + " passed");
            return 1;
        } else {
            System.out.println(msg + " FAILED");
        }
        return 0;
    }

    private void testCard() {
        Card c = new Card(Rank.JACK, Suit.SPADES);
        Card c2 = new Card(Rank.ACE, Suit.SPADES);
        score += test(c.getRank().equals(Rank.JACK), "Rank");
        score += test(c.getSuit().equals(Suit.SPADES), "Suit");
        score += test(c.getSuit(Suit.CLUBS).equals(Suit.CLUBS), "Suit trump");
        score += test(c.getSuit(Suit.HEARTS).equals(Suit.SPADES), "Suit not trump");
        score += test(c.isFace(), "Face");
        score += test(!c.isTrump(Suit.HEARTS), "Not trump");
        score += test(c.isTrump(Suit.SPADES), "Trump");
        score += test(c.isTrump(Suit.CLUBS), "Trump left");
        score += test(c.isLeft(Suit.CLUBS), "Left");
        score += test(c.isRight(Suit.SPADES), "Right");
        score += test(!c2.lessThan(c, Suit.HEARTS), "Ace > Jack");
        score += test(c2.lessThan(c, Suit.CLUBS), "Ace < Left");
        score += test(c2.lessThan(c, Suit.SPADES), "Ace < Right");

    }

    private void testCardGroup() {

        CardGroup group = new CardGroup();

        Card c = new Card(Rank.TEN, Suit.DIAMONDS);
        Card c2 = new Card(Rank.QUEEN, Suit.DIAMONDS);
        Card c3 = new Card(Rank.JACK, Suit.DIAMONDS);
        Card c4 = new Card(Rank.TEN, Suit.SPADES);
        Card c5 = new Card(Rank.QUEEN, Suit.SPADES);
        Card c6 = new Card(Rank.JACK, Suit.HEARTS);

        group.addCard(c);
        group.addCard(c2);
        group.addCard(c3);
        group.addCard(c4);
        group.addCard(c5);
        group.addCard(c6);

        score += test(group.numCards() == 6, "Num card");
        group.removeCard(c);
        score += test(group.numCards() == 5, "Remove Card");
        group.addCard(c);
        score += test(group.findLowest(Suit.DIAMONDS).equals(c4), "Find Lowest");
        score += test(group.findHighest(Suit.DIAMONDS).equals(c3), "Find Highest");
        score += test(group.findHighestInSuit(Suit.DIAMONDS, Suit.SPADES).equals(c5), "Find Highest in Suit");
        score += test(!group.hasSuit(Suit.DIAMONDS, Suit.CLUBS), "Has Suit");
        score += test(group.findLowestInSuit(Suit.DIAMONDS, Suit.SPADES).equals(c4), "Find Lowest in Suit");

    }

    public static void main(String[] args) {
        TestCases test = new TestCases();
        test.testCard();
        test.testCardGroup();
        System.out.println("Score = " + test.score);

    }

}
