package FinalVersion5.Game;
// Nick Dolan-Stern Comp 151 December 2013
public class NimPosition implements Comparable<NimPosition> {
    private final int firstStack;
    private final int secondStack;
    private final int thirdStack;
    private Status positionValue;

    private enum Status {WIN, LOSS, UNKNOWN}

    // Constructor for objects of class NimPosition
    public NimPosition(int first, int second, int third) {

        this.firstStack = first;
        this.secondStack = second;
        this.thirdStack = third;
        if ((first == 0) && (second == 0) && (third == 0))
            positionValue = Status.LOSS;
        else
            positionValue = Status.UNKNOWN;
    }

    public int getFirstStack() {
        return this.firstStack;
    }

    public int getSecondStack() {
        return this.secondStack;
    }

    public int getThirdStack() {
        return this.thirdStack;
    }

    public boolean isWin() {
        return this.positionValue == Status.WIN;
    }

    public boolean isLoss() {
        return this.positionValue == Status.LOSS;
    }

    // Only set value to win or loss provided it has not been set before.

    public void setToWin() {
        if (this.positionValue == Status.UNKNOWN)
            this.positionValue = Status.WIN;
    }

    public void setToLoss() {
        if (this.positionValue == Status.UNKNOWN)
            this.positionValue = Status.LOSS;
    }

    public String toString() {
        return "<" + this.firstStack + ',' + this.secondStack + ',' + this.thirdStack + ", " + this.positionValue + '>';
    }

    public String toStringJustNumbers() {
        return this.firstStack + " " + this.secondStack + ' ' + this.thirdStack;
    }

    public boolean equals(NimPosition other) {
        return this.firstStack == other.firstStack &&
                this.secondStack == other.secondStack &&
                this.thirdStack == other.thirdStack;
    }

    // Provide methods so that this class can be used as the key in either
    //   a hash-table-based map or a comparison-based map (like a binary search tree)

    public int hashCode() {
        return Math.abs(this.toString().hashCode());
    }

    public int compareTo(NimPosition other) {
        int result = 0;

        // do the comparison lexicographically
        if (this.firstStack < other.firstStack)
            result = -1;
        else if (this.firstStack > other.firstStack)
            result = 1;
        else if (this.secondStack < other.secondStack)
            result = -1;
        else if (this.secondStack > other.secondStack)
            result = 1;
        else if (this.thirdStack < other.thirdStack)
            result = -1;
        else if (this.thirdStack > other.thirdStack)
            result = 1;

        return result;
    }
    public boolean isEnd()
    {
       return this.firstStack == 0 && this.secondStack == 0 && this.thirdStack == 0;
    }
    public boolean isUnknown()
    {
        return this.positionValue == Status.UNKNOWN;
    }
}
