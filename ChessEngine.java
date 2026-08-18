public class ChessEngine {
    public static void main(String[] args) {
        Board board = new Board("r2qkbnr/pppp1p1p/n2bp3/6p1/1P2P3/N2B1P1P/P1PP2P1/R1BQK1NR b KQkq b3 0 1");
        board.printBoard();
        System.out.println(board.toFEN());
    }
}