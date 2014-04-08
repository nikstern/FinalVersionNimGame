package FinalVersion5.Game;
// Nick Dolan-Stern Comp 151 December 2013
import FinalVersion5.Graph.DirectedGraph;
import FinalVersion5.Graph.Vertex;
import FinalVersion5.Graph.VertexInterface;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

// My requirements for this class were that no function is dependent on the client other than the constructor being called.
public class NimGameMechanics {
    private boolean[][] adjacencyMatrix;
    private final DirectedGraph<NimPosition> graph;
    private ArrayList<VertexInterface<NimPosition>> vertexMapping;
    private VertexInterface<NimPosition> currentVertex;
    private final Input input;
    private boolean playerTurn;
    private boolean optimalMove;
    private String firstPlayerName;
    private String secondPlayerName;

    private enum GamePlayStatus {PVP, PVC, CVC}

    private GamePlayStatus gamePlayStatus;

    NimGameMechanics() {
        this.input = new Input(); // class for input given by the user assumes responsibility for validity unless giving different states
        setGamePlayStatus();
        this.graph = new DirectedGraph<>();
        this.vertexMapping = new ArrayList<>(0);
        setInitialPiles();   // sets the initial piles of chips for the game
        startAdjacencyMatrix(); // calculates the adjacency matrix from the vertices of the graph
        setGraph();   // sets up the vertices but does not fill in every game state
        setVertexMapping(); // gets the mapping from the graph
        setWinLosses();  // fills in the rest of the game states and sets the starting vertex
        setFirstVertex(); // sets currentVertex to the initial quantities of the piles
        setOptimalMove(); // sets whether the computer should find the move that minimizes the length of the game
        setPlayerTurn(); // sets who goes first
    }

    private void setGamePlayStatus() {  // method sets name of player's depending on gameplay type for output.
        final int PVP_NUM = 0; // index for a player versus player game
        final int PVC_NUM = 1; // index for a player versus computer game
        final int CVC_NUM = 2; // index for a computer versus computer

        switch (this.input.gamePlayStatusInput()) {
            case PVP_NUM:
                this.gamePlayStatus = GamePlayStatus.PVP;
                setFirstPlayerName("First Player");
                setSecondPlayerName("Second Player");
                break;
            case PVC_NUM:
                this.gamePlayStatus = GamePlayStatus.PVC;
                setFirstPlayerName("Player");
                setSecondPlayerName("Computer");
                break;
            case CVC_NUM:
                this.gamePlayStatus = GamePlayStatus.CVC;
                setFirstPlayerName("First Computer");
                setSecondPlayerName("Second Computer");
                break;
            default:
                setGamePlayStatus(); // this section should not be reached as input is responsible for error checking.
        }
    }


    private void setInitialPiles()  // sets the initial piles of chips for the game
    {
        this.input.pilesInput(); // the input class assumes responsibility for validity of input
    }

    private void startAdjacencyMatrix() // sets the adjacency matrix of the graph to the adjacency matrix of the game.
    {
        int total = adjMatrixPos(this.input.getFirstPile(), this.input.getSecondPile(), this.input.getThirdPile());
        this.adjacencyMatrix = new boolean[total + 1][]; // total + 1 would be the same as the number of vertices in the completed graph.
        for (int i = 0; i < this.adjacencyMatrix.length; i++) {
            this.adjacencyMatrix[i] = new boolean[i + 1]; // each vertex can only have an edge of up to its number, the plus one is really so 0,0,0 is displayed with a value
        }
    }

    private void setGraph() // sets up the vertices but does not fill in every game state
    {

        int firstPile = this.input.getFirstPile();
        int secondPile = this.input.getSecondPile();
        int thirdPile = this.input.getThirdPile(); // values of the initial stacks
        int countNumVertices = 0;
        for (int pile1 = 0; pile1 <= firstPile; pile1++)   // from first piles with 0 to first piles with the starting value
            for (int pile2 = 0; pile2 <= secondPile; pile2++)  // from second piles with zero to second piles with the starting value
                for (int pile3 = 0; pile3 <= thirdPile; pile3++)  // from third piles with zero to third piles with the starting value
                {

                    NimPosition position = new NimPosition(pile1, pile2, pile3);
                    this.graph.addVertex(position);
                    addEdges(position, countNumVertices, pile1, pile2, pile3); // sets all edges to that vertex as well as sets the values in adjacency matrix to all edges to true
                    this.vertexMapping.add(new Vertex<>(position)); // here I am using vertexMapping to store all values so far for use in check edges.
                    countNumVertices++;
                }
    }

    private void addEdges(NimPosition position, int count, int pile1, int pile2, int pile3)  // helps routines setGraph and setFirstVertex by finding the vertex in the graph with that Nim Position
    {
        for (int edgePile1 = 0; edgePile1 < pile1; edgePile1++) {
            this.graph.addEdge(position, new NimPosition(edgePile1, pile2, pile3));
            setAdjMatrixPosition(count, adjMatrixPos(edgePile1, pile2, pile3)); // sets the matrix position at the index of the vertex in vertex mapping, without actually accessing the vertex mapping
        }
        for (int edgePile2 = 0; edgePile2 < pile2; edgePile2++) {
            this.graph.addEdge(position, new NimPosition(pile1, edgePile2, pile3));
            setAdjMatrixPosition(count, adjMatrixPos(pile1, edgePile2, pile3));  // see above comment
        }
        for (int edgePile3 = 0; edgePile3 < pile3; edgePile3++) {
            this.graph.addEdge(position, new NimPosition(pile1, pile2, edgePile3));
            setAdjMatrixPosition(count, adjMatrixPos(pile1, pile2, edgePile3));  // see above comment's above comment.
        }
    }

    private int adjMatrixPos(int pile1, int pile2, int pile3) {  // gives where the index a vertex with those piles would appear in vertex mapping
        int initialPile2 = this.input.getSecondPile() + 1;
        int initialPile3 = this.input.getThirdPile() + 1;
        return (pile1 * initialPile2 * initialPile3 + pile2 * initialPile3 + pile3); // basically works the same way as one would convert from one base to another
    }

    private void setAdjMatrixPosition(int indexOfFirstVertex, int indexOfSecondVertex) {    // fairly clear what is going on here.
        this.adjacencyMatrix[indexOfFirstVertex][indexOfSecondVertex] = true;
    }



    private void setWinLosses() {
        for (int i = 0; i < this.vertexMapping.size(); i++) {
            VertexInterface<NimPosition> vertex = this.vertexMapping.get(i);
            if (vertex.getLabel().isUnknown()) // if the NimPosition is still labelled as UNKNOWN
            {
                boolean checkIfLoss = true; // checks whether all vertices from vertex are wins, if so it is a loss
                for (int j = 0; j < i && checkIfLoss; j++) {
                    if (this.adjacencyMatrix[i][j]) // if it shares an edge with another the vertex at j
                    {
                        VertexInterface<NimPosition> otherVertex = this.vertexMapping.get(j);
                        if (otherVertex.getLabel().isLoss()) // if it shares an edge with a loss it cannot be a loss itself and so is a win
                        {
                            checkIfLoss = false;
                            vertex.getLabel().setToWin();
                        }
                    }
                }
                if (checkIfLoss)    // if the loop has travelled through all edges without finding a win, the vertex is a loss
                    vertex.getLabel().setToLoss();
            }

        }
    }

    private void setVertexMapping() {
        this.vertexMapping = this.graph.getMapping();
    }

    private void setFirstVertex() // sets currentVertex to the initial quantities of the piles
    {
        this.currentVertex = getVertex(new NimPosition(this.input.getFirstPile(), this.input.getSecondPile(), this.input.getThirdPile()));
    }

    private VertexInterface<NimPosition> getOptimalMove() // gets (hopefully) the "best" move the computer can make
    {
        VertexInterface<NimPosition> bestMoveVertex = this.currentVertex;
        NimPosition currentNim = this.currentVertex.getLabel();
        if (currentNim.isWin()) {
            Iterator<VertexInterface<NimPosition>> sides = this.currentVertex.getNeighborIterator();
            int minMoves = Integer.MIN_VALUE; // compares to count
            while (sides.hasNext()) {
                VertexInterface<NimPosition> side = sides.next();
                NimPosition sideNim = side.getLabel();
                if (sideNim.isLoss()) {
                    int amountRemoved = getChangeInPiles(sideNim); // will count the number of possible moves from a vertex
                    if (amountRemoved > minMoves) {
                        minMoves = amountRemoved;
                        bestMoveVertex = side;
                    }
                }

            }
            return bestMoveVertex;
        } else                // if the vertex is a loss and the computer is calculating it, there is no hope, so it just chooses the first available edge.
            return this.currentVertex.getUnvisitedNeighbor();
    }

    private int getChangeInPiles(NimPosition nimPosition)    // helper method for getOptimumMoves, returns the number of possible moves from a vertex
    {    // returns the difference between the values of whatever line was changed
        final int UNCHANGED_PILE = 0;
        int aInitial = this.currentVertex.getLabel().getFirstStack();
        int bInitial = this.currentVertex.getLabel().getSecondStack();
        int cInitial = this.currentVertex.getLabel().getThirdStack();
        int differenceA = aInitial - nimPosition.getFirstStack();
        int differenceB = bInitial - nimPosition.getSecondStack();
        int differenceC = cInitial - nimPosition.getThirdStack();
        if (differenceA != UNCHANGED_PILE)
            return differenceA;
        else if (differenceB != UNCHANGED_PILE)
            return differenceB;
        return differenceC;
    }
    private VertexInterface<NimPosition> getMove() {  // same basic algorithm as getOptimalMove but will randomly select a value if there are multiple choices
        Random random = new Random();
        NimPosition currentNim = this.currentVertex.getLabel();
        if (currentNim.isWin()) {
            Iterator<VertexInterface<NimPosition>> sides = this.currentVertex.getNeighborIterator();
            VertexInterface<NimPosition> returnVertexValue = null;
            while (sides.hasNext()) {
                VertexInterface<NimPosition> side = sides.next();
                NimPosition sideNim = side.getLabel();
                if (sideNim.isLoss())
                {
                    boolean rand = random.nextBoolean();
                    if (returnVertexValue == null || rand)
                    {
                        returnVertexValue = side;
                        if (random.nextBoolean())
                            return returnVertexValue;
                    }

                }
            }
            return returnVertexValue;
        } else
            return this.currentVertex.getUnvisitedNeighbor();
    }

    private VertexInterface<NimPosition> playerTurn() {
        final int FIRST_PILE_INDEX = 1;
        final int SECOND_PILE_INDEX = 2;
        final int THIRD_PILE_INDEX = 3;
        final int DEFAULT_ERROR_INDEX = 0;
        NimPosition currentNim = this.currentVertex.getLabel();
        int amountToRemove = this.input.removalPileInput(currentNim);
        int amountInPile = this.input.removalChipsInput();
        NimPosition nimPosition;
        switch (this.input.getPileNumber()) {  // getPileNumber returns the value that the user is trying to remove from
            case FIRST_PILE_INDEX:
                nimPosition = new NimPosition(amountToRemove - amountInPile, currentNim.getSecondStack(), currentNim.getThirdStack());
                break;
            case SECOND_PILE_INDEX:
                nimPosition = new NimPosition(currentNim.getFirstStack(), amountToRemove - amountInPile, currentNim.getThirdStack());
                break;
            case THIRD_PILE_INDEX:
                nimPosition = new NimPosition(currentNim.getFirstStack(), currentNim.getSecondStack(), amountToRemove - amountInPile);
                break;
            default:   // again, this is just in case something is ever implemented incorrectly in the input class, should never occur.
                nimPosition = new NimPosition(DEFAULT_ERROR_INDEX, DEFAULT_ERROR_INDEX, DEFAULT_ERROR_INDEX);
                break;
        }
        return getVertex(nimPosition);
    }

    private VertexInterface<NimPosition> getVertex(NimPosition nimPosition) {  // A helper method for finding a position in the vertex mapping
        int i = adjMatrixPos(nimPosition.getFirstStack(),nimPosition.getSecondStack(),nimPosition.getThirdStack());                                 // Predates the getAdjacencyPosition method, should be recoded to use that instead.
        return this.vertexMapping.get(i);
    }
    // warning, using this with high pile quantities will probably crash the program due to all of the output.
    void debug() { // a bit of an easter egg, if you input "debug" when the program asks who would you like to go first, this function will be used
        this.graph.display(); // displays all the vertexes and there position values
        this.displayMatrix(); // displays adjacency matrix
    }
    // called only by the debug method, again be wary of using this as its high output content may crash the program
    private void displayMatrix() {
        for (boolean[] anAdjacencyMatrix : this.adjacencyMatrix) {
            for (boolean anAdjacencyMatrixFlag : anAdjacencyMatrix)
                System.out.print((anAdjacencyMatrixFlag ? "1" : "0") + ' ');
            System.out.println();
        }
    }

    boolean getFirstPlayer() {
        final int DEBUG_MODE = 4;
        final int PLAYER_ONE_FIRST = 1;
        final int PLAYER_TWO_FIRST = 2;
        switch (this.input.firstPlayerInput(this.firstPlayerName, this.secondPlayerName)) // result of validation of user input for who they wanted to go first
        {
            case PLAYER_ONE_FIRST:
                return false; // computer does not go first
            case PLAYER_TWO_FIRST:
                return true;  // computer goes first
            case DEBUG_MODE:  // program outputs graph and adjacency matrix for debugging or informational purposes
                debug();
            default:
                return getFirstPlayer();
        }
    }

    public boolean checkWin() {  // checks whether the game is over.
        NimPosition current = this.currentVertex.getLabel();
        return current.isEnd();
    }

    public String currentDisplay() { // gives the pile numbers to the client for output purposes.
        return this.currentVertex.getLabel().toStringJustNumbers();
    }

    public void updateGame() {  // basically combines the make move methods into one simplified method
        updateCurrentVertex();
        updateTurn();
    }

    private void updateCurrentVertex() { // methods to get the move depending on the status and the player types of those statuses.
        switch (this.gamePlayStatus) {
            case PVP:
                updateVertexPvP();
                break;
            case PVC:
                updateVertexPvC();
                break;
            case CVC:
                updateVertexCvC();
                break;
        }
    }

    private void updateTurn() {
        this.playerTurn = !this.playerTurn;
    }

    private void updateVertexPvC() {
        if (this.optimalMove)
        {

            this.currentVertex = this.playerTurn ? getOptimalMove() : playerTurn();
        }
        else
            this.currentVertex = this.playerTurn ? getMove() : playerTurn();
    }

    private void updateVertexCvC() {
        this.currentVertex = this.optimalMove ? getOptimalMove() : getMove();
    }

    private void updateVertexPvP() {
        this.currentVertex = playerTurn();
    }
    private void setFirstPlayerName(String firstPlayerName) {
        this.firstPlayerName = firstPlayerName;
    }

    private void setSecondPlayerName(String secondPlayerName) {
        this.secondPlayerName = secondPlayerName;
    }

    private void setPlayerTurn() {
        this.playerTurn = this.gamePlayStatus != GamePlayStatus.CVC && getFirstPlayer();
    }

    private void setOptimalMove() {
        this.optimalMove = this.gamePlayStatus != GamePlayStatus.PVP && this.input.getOptimalMoveInput();
    }

    public boolean getPlayerTurn() {
        return this.playerTurn;
    }

    public String getFirstPlayerName() {
        return this.firstPlayerName;
    }

    public String getSecondPlayerName() {
        return this.secondPlayerName;
    }
}
