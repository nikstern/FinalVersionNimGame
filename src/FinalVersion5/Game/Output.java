package FinalVersion5.Game;
// Nick Dolan-Stern Comp 151 December 2013
import java.util.Scanner;

class Output {
    private String playerOne;
    private String playerTwo;

    public Output(String playerOne, String playerTwo) {
        setPlayerOneName(playerOne);
        setPlayerTwoName(playerTwo);
    }

    private void setPlayerOneName(String playerOne) {
        this.playerOne = playerOne;
    }

    private void setPlayerTwoName(String playerTwo) {
        this.playerTwo = playerTwo;
    }

    public void displayTurn(boolean turn) // displays whose turn it is.
    {
        System.out.println("It is now the " + (turn ? playerTwo : playerOne) + "'s turn.");
    }

    public void displayWinner(boolean turn) // displays the message for the games completion.
    {
        System.out.println((turn ? playerOne : playerTwo) + " wins!");
    }

    public void displayPileNumbers(String currentPiles) // displays a text graphic of the status of the piles
    {
        System.out.println("The piles are now: ");
        Scanner scan = new Scanner(currentPiles);
        int firstPile = scan.nextInt();
        int secondPile = scan.nextInt();
        int thirdPile = scan.nextInt();
        System.out.println();
        System.out.println("Quantities:  " + firstPile + "  " + secondPile + "  " + thirdPile + '\n');
        for (int i = getTallestPile(firstPile, secondPile, thirdPile); i > 0; i--) {
            System.out.print("             ");
            firstPile = displayPileHelper(i, firstPile);
            secondPile = displayPileHelper(i, secondPile);
            thirdPile = displayPileHelper(i, thirdPile);
            System.out.println();
        }
        System.out.println("\nPile number: 1  2  3");

    }

    private static int displayPileHelper(int i, int pileSize) {
        if (pileSize == i) {
            System.out.print("#  ");
            pileSize--;
        } else
            System.out.print("   ");
        return pileSize;
    }

    private static int getTallestPile(int a, int b, int c) {
        return Math.max(Math.max(a, b), c);
    }

}
