/* Classroom use only */
package euchre;

import java.util.ArrayList;
import java.util.Scanner;

abstract public class Player {

    private String name;
    public CardGroup hand;

    Player() {
        hand = new CardGroup();
    }

    Player(String name) {
        this.name = name;
        hand = new CardGroup();
    }

    public String getName() {
        return name;
    }

    public void showHand() {
        System.out.println("--- " + name);
        hand.show();
    }

    /* Notes on classes and interfaces
    
    A class may have abtract methods that are not defined
    Likewise, an abstract class may have defined methods
    Abstract methods in java are similar to virtual methods in C++
    They get defined in extensions to the class
    Java can only inherit (extend) from one class

    Java also has interfaces
    Interfaces are like classes with all abstract methods
    All methods are public (as are any fields, by default)
    Interfaces are implemented rather than extended
    A java class can implement multiple interfaces,
        which permits multiple inheretance
    Since one of the methods here is implemented,
        we will not be using an interface
     */
    // Requires: valid upcard
    // Effects: first round, true if call up
    //   must have 2 trump facecards plus
    //   either a third trump, or an off ace
    // dealer should consider upcard in trump counts
    abstract public boolean callUp(Card up, boolean isDealer);

    // Requires: valid upcard
    // Effects: second round, choose highest qualifying suit
    //   must have 2 trump facecards plus
    //   either a third trump, or an off ace
    // return suit of upcard to pass
    abstract public Suit chooseTrump(Card up);

    // Requires: Hand is not empty (at least 1 card)
    // Modifies: hand
    // Effects: replaces lowest card in hand with upcard
    //  unless upcard is even lower
    abstract public void addAndDiscard(Card up);

    // Requires: trump is valid, hand is not empty
    // Modifies: hand
    // Effects: lowest card in hand
    abstract public Card leadCard(Suit trump);

    // Requires: trump is valid, hand is not empty
    //      winning is true or false as team is winning the hand
    //      best is the card currently winning the trick
    // Modifies: hand
    // Effects: card is played
    abstract public Card playCard(Suit trump, Suit lead, boolean winning, Card best);

    // This is called a "factory method"
    public static Player makePlayer(String name, String strat) {
        if (strat.equals("B") || strat.equals("b")) {
            return new Basic(name);
        }

        if (strat.equals("H")) {
            return new Human(name);
        }

        return new Human(name); // avoids java warnings
    }
}

class Basic extends Player {

    Basic(String name) {
        super(name);
    }

    // Requires: 
    // Effects: Call up if have 2 trump facecards plus third trump or off ace
    //  If dealer, account for card that would be picked up
    @Override
    public boolean callUp(Card up, boolean isDealer) {

        int trumpFaceCards = 0;
        int totalTrumpCount = 0;
        boolean hasOffAce = false;

        if (isDealer) {
            totalTrumpCount++;
            if (up.isFace()) {
                trumpFaceCards++;
            }
        }
        for (int i = 0; i < hand.numCards(); i++) {
            if (hand.getCard(i).isTrump(up.getSuit())) {
                totalTrumpCount++;
                if (hand.getCard(i).isFace()) {
                    trumpFaceCards++;
                }
            } else if (hand.getCard(i).getRank() == Rank.ACE) {
                hasOffAce = true;
            }
        }

        if ((totalTrumpCount >= 3 && trumpFaceCards >= 2) || (totalTrumpCount >= 2 && hasOffAce)) {
            System.out.println(getName() + " calls " + up.getSuit());
            return true;
        }

        System.out.println(getName() + " passes ");
        return false;

    }

    // Requires: 
    // Effects: Adds up card to hand, them removes lowest card (considering trump)
    @Override
    public void addAndDiscard(Card up) {
        hand.addCard(up);
        hand.removeCard(hand.findLowest(up.getSuit()));
    }

    // Requires;
    // Effects: call up trump that doesn't match up suit
    //    if have 2 trump facecards plus third trump or off ace
    //    Prefer in order Spades > Hearts > Diamonds > Clubs
    //    Returning the upcard suit indicates a PASS
    @Override
    public Suit chooseTrump(Card up) {
        Suit[] trumps = {Suit.SPADES, Suit.HEARTS, Suit.DIAMONDS, Suit.CLUBS};

        for (int j = 0; j < trumps.length; j++) {
            if (trumps[j] == up.getSuit()) {
                continue;
            }

            int trumpFaceCount = 0;
            int trumpCount = 0;
            boolean hasOffAce = false;

            for (int i = 0; i < hand.numCards(); i++) {
                if (hand.getCard(i).isTrump(trumps[j])) {
                    trumpCount++;
                    if (hand.getCard(i).isFace()) {
                        trumpFaceCount++;
                    }
                } else if (hand.getCard(i).getRank() == Rank.ACE) {
                    hasOffAce = true;
                }
            }

            if ((trumpFaceCount >= 2 && hasOffAce) || (trumpCount >= 3 && trumpFaceCount >= 2)) {
                System.out.println(getName() + " calls " + trumps[j].name());
                return trumps[j];
            }

        }

        System.out.println(getName() + " passes ");
        return up.getSuit();
    }

    // Requires: hand not empty
    // Modifies: hand
    // Effects: lead lowest card from hand (considering trump)
    @Override
    public Card leadCard(Suit trump) {

        Card c = hand.findLowest(trump);
        System.out.println(getName() + " leads " + c.name());
        hand.removeCard(c);
        return c;
    }

    // Requires: hand not empty
    // Modifies: hand
    // Effects: If has suit lead, follow with highest card in suit
    //  If missing suit lead and partner isn't winning
    //      and has trump and lowest trump would win, play it,
    //  Otherwise, discard lowest card in hand
    @Override
    public Card playCard(Suit trump, Suit lead, boolean winning, Card best) {

        Card c = new Card();
        if (hand.hasSuit(trump, lead)) {
            c = hand.findHighestInSuit(trump, lead);
            hand.removeCard(c);
            System.out.println(getName() + " plays " + c.name()); // follow
        } else if (!winning && hand.hasSuit(trump, trump) && !hand.findLowestInSuit(trump, trump).lessThan(best, trump)) {
            c = hand.findLowestInSuit(trump, trump);
            hand.removeCard(c);
            System.out.println(getName() + " plays trump " + c.name()); // trump
        } else {
            c = hand.findLowest(trump);
            hand.removeCard(c);
            System.out.println(getName() + " discards " + c.name()); // otherwise
        }

        return c;
    }
}

class Human extends Player {

    Scanner scan = new Scanner(System.in);

    Human(String name) {
        super(name);
    }

    @Override
    public boolean callUp(Card up, boolean isDealer) {
        System.out.print(getName() + " do you want to call up? (1 - Yes else No): ");
        int choice = scan.nextInt();
        if (choice == 1) {

            System.out.println(getName() + " calls " + up.getSuit());
            return true;
        } else {
            System.out.println(getName() + " passes ");
        }
        return false;
    }

    @Override
    public void addAndDiscard(Card up) {
        hand.addCard(up);
        hand.removeCard(hand.findLowest(up.getSuit()));
    }

    @Override
    public Suit chooseTrump(Card up) {
        Suit s = up.getSuit();

        System.out.print(getName() + " What do you want to choose trump? 0-CLUBS 1-DIAMONDS 2-HEARTS Else-SPADES: ");
        int choice = scan.nextInt();
        if (choice == 0) {
            s = Suit.CLUBS;
        } else if (choice == 1) {
            s = Suit.DIAMONDS;
        } else if (choice == 1) {
            s = Suit.HEARTS;
        } else {
            s = Suit.SPADES;
        }
        return s;
    }

    @Override
    public Card leadCard(Suit trump) {
        Card c = new Card();
        System.out.print(getName() + " Which card do you want to lead? ");

        for (int i = 0; i < hand.numCards(); i++) {
            if (i != hand.numCards() - 1) {
                System.out.print(i + "-" + hand.getCard(i).name() + " ");
            } else {
                System.out.print("else" + "-" + hand.getCard(i).name());
            }
        }
        System.out.println("");
        int choice = scan.nextInt();

        if (choice >= hand.numCards()) {
            choice = hand.numCards() - 1;
        }
        c = hand.getCard(choice);
        System.out.println(getName() + " leads " + c.name());
        hand.removeCard(c);
        return c;

    }

    @Override
    public Card playCard(Suit trump, Suit lead, boolean winning, Card best) {
        Card c = new Card();

        if (hand.hasSuit(trump, lead)) {

            ArrayList<Card> leadSuitCards = new ArrayList<>();

            for (int i = 0; i < hand.numCards(); i++) {
                if (hand.getCard(i).getSuit(trump) == lead) {

                    leadSuitCards.add(hand.getCard(i));
                }

            }

            System.out.print(getName() + " choose cards from lead suit: ");
            for (int i = 0; i < leadSuitCards.size(); i++) {
                if (i != leadSuitCards.size() - 1) {
                    System.out.print(i + "-" + leadSuitCards.get(i).name() + " ");
                } else {
                    System.out.print("else" + "-" + leadSuitCards.get(i).name());
                }
            }
            System.out.println("");
            int choice = scan.nextInt();

            if (choice >= leadSuitCards.size()) {
                choice = leadSuitCards.size() - 1;
            }

            c = leadSuitCards.get(choice);
            hand.removeCard(c);
            System.out.println(getName() + " plays " + c.name()); // follow
        } else {
            int choice1 = 2;
            if (hand.hasSuit(trump, trump)) {
                System.out.print(getName() + " do you want to 1-trump else - discard?: ");
                choice1 = scan.nextInt();
                if (choice1 == 1) {
                    ArrayList<Card> trumpSuitCards = new ArrayList<>();

                    for (int i = 0; i < hand.numCards(); i++) {
                        if (hand.getCard(i).isTrump(trump)) {

                            trumpSuitCards.add(hand.getCard(i));
                        }

                    }
                    System.out.print(getName() + " choose cards from trump suit: ");
                    for (int i = 0; i < trumpSuitCards.size(); i++) {
                        if (i != trumpSuitCards.size() - 1) {
                            System.out.print(i + "-" + trumpSuitCards.get(i).name() + " ");
                        } else {
                            System.out.print("else" + "-" + trumpSuitCards.get(i).name());
                        }
                    }
                    System.out.println("");

                    int choice = scan.nextInt();

                    if (choice >= trumpSuitCards.size()) {
                        choice = trumpSuitCards.size() - 1;
                    }

                    c = trumpSuitCards.get(choice);
                    hand.removeCard(c);
                    System.out.println(getName() + " plays trump " + c.name());
                }

            }
            if (choice1 != 1) {
                System.out.print(getName() + " choose card to discard. ");
                for (int i = 0; i < hand.numCards(); i++) {
                    if (i != hand.numCards() - 1) {
                        System.out.print(i + "-" + hand.getCard(i).name() + " ");
                    } else {
                        System.out.print("else" + "-" + hand.getCard(i).name());
                    }
                }

                System.out.println("");
                int choice = scan.nextInt();

                if (choice >= hand.numCards()) {
                    choice = hand.numCards() - 1;
                }
                c = hand.getCard(choice);
                hand.removeCard(c);
                System.out.println(getName() + " discards " + c.name());
            }

        }

        return c;
    }
}
