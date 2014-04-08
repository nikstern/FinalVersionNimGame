package FinalVersion5.Game;
// Nick Dolan-Stern Comp 151 December 2013
// My goal here was to create the client class in a way
// that the client has no knowledge of the game mechanics, other than a purely top level view.
//
// I also wanted to separate the output from the client code in case the output code ever needs replacement.
// I believe each line is formatted in a way that the tasks it is doing is understandable.
class NimGraphGame {
    private final Output output;
    private final NimGameMechanics game;

    private NimGraphGame() {
        this.game = new NimGameMechanics();
        this.output = new Output(this.game.getFirstPlayerName(), this.game.getSecondPlayerName()); // parameters for output are player names
        play();
    }

    void play() {
        this.output.displayPileNumbers(this.game.currentDisplay());
        boolean done = true;
        while (done) {
            this.output.displayTurn(this.game.getPlayerTurn());
            this.game.updateGame(); // has the player or computer make their move then updates turn value
            this.output.displayPileNumbers(this.game.currentDisplay());
            done = !this.game.checkWin();
        }
        this.output.displayWinner(this.game.getPlayerTurn());
    }

    public static void main(String[] args) {
        new NimGraphGame();
    }
}
