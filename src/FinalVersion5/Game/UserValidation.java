package FinalVersion5.Game;
// Nick Dolan-Stern Comp 151 December 2013
import java.util.Random;

class UserValidation {
    private static final String DEFAULT_ERROR_MESSAGE = "Invalid input. Please try again.";
    private static final int DEFAULT_ERROR_CODE = 3;

    public String getDEFAULT_ERROR_MESSAGE() {
        return this.DEFAULT_ERROR_MESSAGE;
    }

    public static boolean checkStartingPiles(int firstPileAmount, int secondPileAmount, int thirdPileAmount) {
        if ((firstPileAmount + secondPileAmount + thirdPileAmount != 0) && firstPileAmount >= 0 && secondPileAmount >= 0 && thirdPileAmount >= 0)
            return true;
        final String SPECIAL_ERROR_MESSAGE = "All piles cannot be zero and no pile can be negative. Please try again.";
        System.out.println(SPECIAL_ERROR_MESSAGE);
        return false;
    }

    public int checkGamePlay(String input) {
        final String PLAYER_VERSUS_PLAYER_INPUT = "pvp";
        final String PLAYER_VERSUS_COMPUTER_INPUT = "pvc";
        final String COMPUTER_VERSUS_COMPUTER_INPUT = "cvc";

        switch (input) {
            case PLAYER_VERSUS_PLAYER_INPUT:
                final int PLAYER_VERSUS_PLAYER_NUM = 0;
                return PLAYER_VERSUS_PLAYER_NUM;
            case PLAYER_VERSUS_COMPUTER_INPUT:
                final int PLAYER_VERSUS_COMPUTER_NUM = 1;
                return PLAYER_VERSUS_COMPUTER_NUM;
            case COMPUTER_VERSUS_COMPUTER_INPUT:
                final int COMPUTER_VERSUS_COMPUTER_NUM = 2;
                return COMPUTER_VERSUS_COMPUTER_NUM;
            default:
                System.out.println(this.DEFAULT_ERROR_MESSAGE);
                return DEFAULT_ERROR_CODE;
        }

    }

    public int checkUserTurn(String input) {
        final int PLAYER_ONE_FIRST = 1;
        final int PLAYER_TWO_FIRST = 2;
        final String PLAYER_ONE_INPUT = "p1";
        final String PLAYER_TWO_INPUT = "p2";
        final String RANDOM_INPUT = "r";
        final String DEBUG_MODE_INPUT = "debug";

        switch (input) {
            case PLAYER_ONE_INPUT:
                return PLAYER_ONE_FIRST;
            case PLAYER_TWO_INPUT:
                return PLAYER_TWO_FIRST;
            case RANDOM_INPUT:
                Random rand = new Random();
                return rand.nextBoolean() ? PLAYER_ONE_FIRST : PLAYER_TWO_FIRST;
            case DEBUG_MODE_INPUT:
                final int DEBUG_MODE = 4;
                return DEBUG_MODE;
            default:
                System.out.println(this.DEFAULT_ERROR_MESSAGE);
                return DEFAULT_ERROR_CODE;
        }
    }

    public int checkPileNumber(NimPosition currentNimPosition, int pileNumber) {
        final int PILE_ONE_INPUT = 1;
        final int PILE_TWO_INPUT = 2;
        final int PILE_THREE_INPUT = 3;
        final String SPECIAL_ERROR_MESSAGE = "Pile " + pileNumber + " has zero chips, please enter a pile that contains chips.";
        int amountInPile;
        switch (pileNumber) {
            case PILE_ONE_INPUT:
                amountInPile = currentNimPosition.getFirstStack();
                break;
            case PILE_TWO_INPUT:
                amountInPile = currentNimPosition.getSecondStack();
                break;
            case PILE_THREE_INPUT:
                amountInPile = currentNimPosition.getThirdStack();
                break;
            default:
                System.out.println(this.DEFAULT_ERROR_MESSAGE);
                final int INVALID_INPUT_FLAG = -1;
                amountInPile = INVALID_INPUT_FLAG;
                break;
        }
        final int EMPTY_PILE_NUM = 0;
        if (amountInPile == EMPTY_PILE_NUM)
            System.out.println(SPECIAL_ERROR_MESSAGE);
        return amountInPile;
    }

    public boolean checkRemoveFromPile(int amountInPile, int amountToRemove) {
        final int MINIMUM_REMOVAL_AMT = 1;
        if (amountToRemove > amountInPile || amountToRemove < MINIMUM_REMOVAL_AMT) {
            System.out.println(this.DEFAULT_ERROR_MESSAGE);
            return false;
        }
        return true;
    }
}
