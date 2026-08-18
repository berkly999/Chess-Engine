import java.util.HashMap;

public class Board {
    //attributes
    private int[] state;
    private String castling;
    private String enPassant;
    private String nextPlayer;
    private int halfMoves;
    private int fullMoves;
    private static HashMap<String, Integer> FEN_NUMERICS;
    private static HashMap<Integer, String> NUMERICS_FEN;

    //constructor with FEN string input
    public Board(String fen) {
        try {
            //setting up the board itself
            this.state = new int[120];
            String[] splitFEN = fen.split(" ");
            String[] fenBoard = splitFEN[0].split("/");

            FEN_NUMERICS = this.getFenNumerics();
            NUMERICS_FEN = this.getNumericsFen();
            
            if (splitFEN.length != 6) {
                throw new FENParseException("FEN Input Is Malformed.");
            }

            this.state = this.setBoard(fenBoard);

            this.nextPlayer = splitFEN[1];
            this.castling = splitFEN[2];
            this.enPassant = splitFEN[3];
            this.halfMoves = Integer.parseInt(splitFEN[4]);
            this.fullMoves = Integer.parseInt(splitFEN[5]);
        }

        catch (Exception e) {
            System.out.println("Error Parsing FEN");
            e.printStackTrace();
        }
    }

    //constructor with no parameters for new game
    public Board() {
        this("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
    }

    //uppercase and positive numbers represent white pieces
    private HashMap<String, Integer> getFenNumerics() {
        HashMap<String, Integer> fenNumerics = new HashMap<String, Integer>();
        fenNumerics.put("p", -1);
        fenNumerics.put("P", 1);
        fenNumerics.put("n", -2);
        fenNumerics.put("N", 2);
        fenNumerics.put("b", -3);
        fenNumerics.put("B", 3);
        fenNumerics.put("r", -4);
        fenNumerics.put("R", 4);
        fenNumerics.put("q", -5);
        fenNumerics.put("Q", 5);
        fenNumerics.put("k", -6);
        fenNumerics.put("K", 6);
        return fenNumerics;
    }

    private HashMap<Integer, String> getNumericsFen() {
        HashMap<Integer, String> numericsFen = new HashMap<Integer, String>();
        numericsFen.put(-1, "p");
        numericsFen.put(1, "P");
        numericsFen.put(-2, "n");
        numericsFen.put(2, "N");
        numericsFen.put(-3, "b");
        numericsFen.put(3, "B");
        numericsFen.put(-4, "r");
        numericsFen.put(4, "R");
        numericsFen.put(-5, "q");
        numericsFen.put(5, "Q");
        numericsFen.put(-6, "k");
        numericsFen.put(6, "K");
        numericsFen.put(0, ".");
        return numericsFen;
    }

    private int[] setBoard(String[] fenBoard) throws FENParseException {
        int[] newState = new int[120];
        //setting the first two rows of padding
        for (int x = 0; x < 20; x++) {
            newState[x] = 99;
        }

        //Setting the pieces
        for (int y = 0; y < 8; y++) {
            newState[(10*y) + 20] = 99;
            int index = 0;
            for (int x = 0; x < fenBoard[y].length(); x++) {
                if (Character.isDigit(fenBoard[y].charAt(x))) {
                    for (int z = 0; z < Integer.parseInt(String.valueOf(fenBoard[y].charAt(x))); z++) {
                        newState[(10*y) + 21 + index] = 0;
                    }
                    index = index + (Integer.parseInt(String.valueOf(fenBoard[y].charAt(x)))) - 1;
                }

                else {
                    if (FEN_NUMERICS.keySet().contains(String.valueOf(fenBoard[y].charAt(x)))) {
                        newState[(10*y) + 21 + index] = FEN_NUMERICS.get(String.valueOf(fenBoard[y].charAt(x)));
                    }

                    else {
                        throw new FENParseException("Unexpected Piece Value In FEN Input.");
                    }
                }
                index++;
            }

            newState[(10*y) + 29] = 99;
        }

        //setting the final two rows of padding
        for (int x = 100; x < 120; x++) {
            newState[x] = 99;
        }

        return newState;
    }

    public void printBoard() {
        for (int y = 2; y < 10; y++) {
            for (int x = 1; x < 9; x++) {
                System.out.print(NUMERICS_FEN.get(this.state[10*y + x]));
            }
            System.out.print("\n");
        }

    }

    public String toFEN() {
        String fen = "";

        //writing the pieces of the board
        fen = fen + this.getPiecesFEN(this.state) + " " + this.nextPlayer + " ";
        fen = (this.castling.equals("")) ? (fen + "- ") : (fen + this.castling + " ");
        fen = (this.enPassant.equals("")) ? (fen + "- ") : (fen + this.enPassant + " ");
        fen = fen + String.valueOf(this.halfMoves) + " " + String.valueOf(this.fullMoves);

        return fen;
    }

    private String getPiecesFEN(int[] state) {
        String fen = "";
        int spaceCount = 0;
        for (int y = 2; y < 10; y++) {
            for (int x = 1; x < 9; x++) {
                if (this.state[10*y + x] == 0) {
                    spaceCount++;
                }

                else {
                    if (spaceCount != 0) {
                        fen = fen + String.valueOf(spaceCount);
                        spaceCount = 0;
                    }

                    fen = fen + NUMERICS_FEN.get(this.state[10*y +x]);
                }
            }

            if (spaceCount != 0) {
                fen = fen + String.valueOf(spaceCount);
                spaceCount = 0;
            }
            fen = fen + "/";
        }
        return fen.substring(0, fen.length() - 1);
    }
}