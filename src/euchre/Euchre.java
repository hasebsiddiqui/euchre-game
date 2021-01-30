/* Classroom use only */
package euchre;

import java.util.ArrayList;
import java.util.Scanner;

public class Euchre {

    // Effects: player number to the left
    private int left(int p) {
        return (p + 1) % 4;
    }

    // Rrequires: 4 players, Euchre deck
    // Effects: Deals starting to left: 2-3-2-3 then 3-2-3-2
    public void dealCards(ArrayList<Player> players,
            Deck deck, int dealer) {
        for (int i = 0; i < players.size(); i++) {
            players.get(i).hand.clear();
        }
        for (int j = 0; j < 2; j++) {

            for (int i = 0; i < 4; i++) {
                Player player = players.get(left(i));
                if (i % 2 == 0) {

                    player.hand.addCard(deck.deal1());
                    player.hand.addCard(deck.deal1());

                    if (j % 2 != 0) {
                        player.hand.addCard(deck.deal1());
                    }
                } else {
                    player.hand.addCard(deck.deal1());
                    player.hand.addCard(deck.deal1());

                    if (j % 2 == 0) {
                        player.hand.addCard(deck.deal1());
                    }
                }
            }

        }
    }
    // Requires: 4 players, valid trump suit
    //   players make plays that are legal and meet project specs
    // Effects: plays hand and returns count of tricks for each team
    //   this has only the trick playing, bidding is done in play()
    //   count[0] is team of player 0 and player 2
    //   count[1] is team of player 1 and player 3

    public int[] playHand(ArrayList<Player> players,
            int dealer, Suit trump) {
        int[] count = new int[2];
        count[0] = 0;
        count[1] = 0;
        int turn = left(dealer);

        int bestPlayer = -1;
        Suit lead;
        Card best = null;
        Card temp;
        for (int i = 0; i < 5; i++) {
            best = players.get(turn).leadCard(trump);
            lead = best.getSuit();
            bestPlayer = turn;

            turn = left(turn);

            temp = players.get(turn).playCard(trump, lead, false, best);
            if (best.lessThan(temp, trump)) {
                best = temp;
                bestPlayer = turn;
            }

            turn = left(turn);

            temp = players.get(turn).playCard(trump, lead, bestPlayer == (turn + 2) % 4, best);
            if (best.lessThan(temp, trump)) {
                best = temp;
                bestPlayer = turn;
            }

            turn = left(turn);

            temp = players.get(turn).playCard(trump, lead, bestPlayer == (turn + 2) % 4, best);
            if (best.lessThan(temp, trump)) {
                best = temp;
                bestPlayer = turn;
            }

            if (bestPlayer == 0 || bestPlayer == 2) {
                count[0]++;
            } else {
                count[1]++;
            }

            turn = bestPlayer;
            System.out.println("--- " + players.get(bestPlayer).getName() + " takes it");

        }
        if (count[0] > count[1]) {
            System.out.print(players.get(0).getName() + " and " + players.get(2).getName() + " win ");
        } else {
            System.out.print(players.get(1).getName() + " and " + players.get(3).getName() + " win ");
        }
        System.out.println(count[0] + "-" + count[1]);
        return count;
    }

    // Requires: 4 players
    // Effects: play full Euchre game
    public void play(ArrayList<Player> players, int winpoints, boolean shuffle) {
        int[] count = new int[2];
        count[0] = 0;
        count[1] = 0;

        for (int i = 0; count[0] < winpoints && count[1] < winpoints; i++) {
            int dealer = left(i - 1);
            System.out.println("====== deal: " + (i + 1));
            System.out.println("====== " + players.get(dealer).getName() + " dealing");

            Deck deck = new Deck();
            deck.setEuchre();

            deck.shuffle(shuffle);
            deck.show();

            dealCards(players, deck, dealer);
            for (Player p : players) {
                p.showHand();
            }

            Card upcard = deck.deal1();
            System.out.println("Up Card: " + upcard.name());

            System.out.println("--- bidding round 1");
            boolean isTrumpSelected = false;
            Suit trump = null;
            int whoSelectedTrump = -1;
            for (int j = 0; j < 4; j++) {
                int turn = left(dealer + j);
                if (players.get(turn).callUp(upcard, dealer == turn)) {
                    players.get(dealer).addAndDiscard(upcard);
                    trump = upcard.getSuit();
                    isTrumpSelected = true;
                    whoSelectedTrump = turn;
                    break;
                }
            }
            if (!isTrumpSelected) {
                System.out.println("--- bidding round 2");
                for (int j = 0; j < 4; j++) {
                    int turn = left(dealer + j);
                    trump = players.get(turn).chooseTrump(upcard);
                    if (trump != upcard.getSuit()) {
                        whoSelectedTrump = turn;
                        break;

                    }
                }
            }

            int[] temp;
            if (trump != null) {
                temp = playHand(players, dealer, trump);

                if (whoSelectedTrump == 0 || whoSelectedTrump == 2) {
                    if (temp[0] >= 3) {
                        count[0]++;
                    } else {
                        count[1] += 2;
                    }
                } else if (whoSelectedTrump == 1 || whoSelectedTrump == 3) {
                    if (temp[1] >= 3) {
                        count[1]++;
                    } else {
                        count[0] += 2;
                    }
                }
            }

            System.out.println("Total " + count[0] + "-" + count[1]);
        }

        System.out.println("=== Game over");
    }

    public static void main(String[] args) {

        ArrayList<Player> players = new ArrayList(0);
        Scanner scan = new Scanner(System.in);
        for (int i = 0; i < 4; i++) {
            System.out.println("---------------------------");
            System.out.println("Player; " + (i + 1));
            System.out.print("Enter name of player: ");
            String name = scan.nextLine();
            System.out.print("Press 'B'or 'b' for bot Else Human player: ");
            String isHuman = scan.nextLine();
            players.add(Player.makePlayer(name, isHuman));
            System.out.println("---------------------------");
        }

        System.out.println("Enter win points: ");
        int winpoints = scan.nextInt();

        Euchre game = new Euchre();

        game.play(players, winpoints, true);
    }
}
