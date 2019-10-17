package maze;


public enum Square {
    WALL('#'), OPEN_SPACE('.'), START('o'), FINISH('*');

    /** The one character long string representation of this Square */
    private final char ch;

    private Square (char ch) {
        this.ch = ch;
    }
    
    /** Returns a String representation of this Square */
    public String toString() {
        return "" + ch;
    }

    /**
     * Takes one of four legal characters and returns the corresponding Square value.
     * @param ch One of four legal characters ('#', '.', 'o', '*')
     * @return The Square value corresponding to the character passed in
     */
    public static Square fromChar(char ch) {
        //TODO
        switch (ch) {
            case '#': return WALL;
            case '.': return OPEN_SPACE;
            case 'o': return START;
            case '*': return FINISH;
            default: throw new IllegalArgumentException("Illegal character");
        }
    }
}