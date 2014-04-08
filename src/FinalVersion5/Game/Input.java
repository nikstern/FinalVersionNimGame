package FinalVersion5.Game;
// Nick Dolan-Stern Comp 151 December 2013
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

class Input {
    private final UserValidation UV;
    private int firstPile;
    private int secondPile;
    private int thirdPile;
    private int amountInPile;
    private int pileNumber;
    private final Scanner scan;

    static final private int ERROR_CODE = 3;

    public Input() {
        this.UV = new UserValidation();
        this.scan = new Scanner(System.in);
    }

    private static void displayQueryAndPrompt(String query, String prompt) {
        System.out.println(query);
        System.out.print(prompt);
    }

    public int gamePlayStatusInput() {
        final String QUERY = "Would you like the game to be player versus player, player versus computer or computer versus computer?";
        final String PROMPT = "Enter pvp, pvc, or cvc: ";
        int gameStatusInput;
        displayQueryAndPrompt(QUERY, PROMPT);
        try {
            String gameInput = scan.next();
            gameInput = gameInput.toLowerCase();
            int gamePlayStatusIndex = this.UV.checkGamePlay(gameInput);
            if (gamePlayStatusIndex == ERROR_CODE)
                return gamePlayStatusIndex;
            return gamePlayStatusInput();
        }
        catch (InputMismatchException e) {
            System.out.println(this.UV.getDEFAULT_ERROR_MESSAGE());
            return gamePlayStatusInput();
        }
        catch (NoSuchElementException e) {
            System.out.println("Goodbye.");
            System.exit(0);
            return 0;
        }


    }

    public void pilesInput() {
        final String QUERY = "What would you like the initial pile sizes to be?";
        final String PROMPT = "Enter three numbers each separated by a space: ";

        displayQueryAndPrompt(QUERY, PROMPT);
        try {
            int firstPileInput = this.scan.nextInt();
            int secondPileInput = this.scan.nextInt();
            int thirdPileInput = this.scan.nextInt();
            if (UV.checkStartingPiles(firstPileInput, secondPileInput, thirdPileInput)) {
                setFirstPile(firstPileInput);
                setSecondPile(secondPileInput);
                setThirdPile(thirdPileInput);
            } else
                pilesInput();
        }
        catch (Exception e) {
            System.out.println("Goodbye.");
            System.exit(0);
        }

    }

    public int firstPlayerInput(String firstPlayerName, String secondPlayerName) {
        final String QUERY = "Who would you like to go first?";
        final String PROMPT = "Enter p1 for the " + firstPlayerName + ", p2 for the " + secondPlayerName + ", or r for random: ";
        displayQueryAndPrompt(QUERY, PROMPT);
        String playerInput = this.scan.next();
        playerInput = playerInput.toLowerCase();
        int result = this.UV.checkUserTurn(playerInput);
        if (result == ERROR_CODE)
            return firstPlayerInput(firstPlayerName, secondPlayerName);
        else
            return result;
    }

    public int removalPileInput(NimPosition currentNimPosition) {
        final String QUERY = "Which pile would you like to remove from?";
        final String PROMPT = "Enter 1, 2, or 3: ";

        displayQueryAndPrompt(QUERY, PROMPT);
        try {
            int pileNumber = this.scan.nextInt();
            setPileNumber(pileNumber);
            setAmountInPile(this.UV.checkPileNumber(currentNimPosition, this.pileNumber));
            return this.amountInPile < 1 ? removalPileInput(currentNimPosition) : this.amountInPile;
        } catch (InputMismatchException e) {
            System.out.println(this.UV.getDEFAULT_ERROR_MESSAGE());
            return removalPileInput(currentNimPosition);
        } catch (NoSuchElementException e) {
            System.out.println("Goodbye.");
            System.exit(0);
            return ERROR_CODE;
        }
    }

    public int removalChipsInput() {
        final String QUERY = "How many chips would you like to remove?";
        final String PROMPT = "Enter the number of chips you would like to remove: ";
        final int DEFAULT_CHIP_ERROR_VALUE = -1;

        int amountToRemove = DEFAULT_CHIP_ERROR_VALUE;
        boolean checkInput = false;
        while (!checkInput) {
            displayQueryAndPrompt(QUERY, PROMPT);
            try {
                amountToRemove = this.scan.nextInt();
            } catch (InputMismatchException e) {
                amountToRemove = DEFAULT_CHIP_ERROR_VALUE;
            } catch (NoSuchElementException e) {
                System.out.println("Goodbye.");
                System.exit(0);
            }
            checkInput = this.UV.checkRemoveFromPile(this.amountInPile, amountToRemove);
        }
        return amountToRemove;
    }

    public boolean getOptimalMoveInput() {
        final String QUERY = "Would you like the computer's moves to be optimal?";
        final String PROMPT = "Enter yes or no: ";

        displayQueryAndPrompt(QUERY, PROMPT);
        try {
            String optimalInput = this.scan.next();
            optimalInput = optimalInput.toLowerCase();
            final String OPTIMAL_FALSE_INPUT = "no";
            final String OPTIMAL_TRUE_INPUT = "yes";
            if (optimalInput.equals(OPTIMAL_TRUE_INPUT))
                return true;
            else if (optimalInput.equals(OPTIMAL_FALSE_INPUT))
                return false;
            System.out.println(this.UV.getDEFAULT_ERROR_MESSAGE());
            return getOptimalMoveInput();
        } catch (NoSuchElementException e) {
            System.out.println("Goodbye");
            System.exit(0);
        }
        return false;
    }

    public int getFirstPile() {
        return this.firstPile;
    }

    public int getSecondPile() {
        return this.secondPile;
    }

    public int getThirdPile() {
        return this.thirdPile;
    }

    public int getPileNumber() {
        return this.pileNumber;
    }

    private void setFirstPile(int first) {
        this.firstPile = first;
    }

    private void setSecondPile(int second) {
        this.secondPile = second;
    }

    private void setThirdPile(int third) {
        this.thirdPile = third;
    }

    private void setAmountInPile(int amountInPile) {
        this.amountInPile = amountInPile;
    }

    private void setPileNumber(int pile) {
        this.pileNumber = pile;
    }
}

