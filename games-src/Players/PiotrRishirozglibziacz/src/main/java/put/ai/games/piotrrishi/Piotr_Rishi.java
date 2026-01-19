package put.ai.games.piotrrishi;

import java.util.List;
import put.ai.games.game.Board;
import put.ai.games.game.Move;
import put.ai.games.game.Player;

public class Piotr_Rishi extends Player {

    @Override
    public String getName() {
        return "Piotr Nowak 160071 Rishi Dixit 160334";
    }

    @Override
    public Move nextMove(Board b) {
        List<Move> moves = b.getMovesFor(getColor());
        if (moves.isEmpty()) {
            return null;
        }
        Move bestMove = null;
        int maxCaptured = -1;
        for (Move move : moves) {
            int capturedCount = countCapturedPieces(b, move);
            if (capturedCount > maxCaptured) {
                maxCaptured = capturedCount;
                bestMove = move;
            }
        }

        return bestMove != null ? bestMove : moves.get(0);
    }
    private int countCapturedPieces(Board board, Move move) {
        Color opponent = Player.getOpponent(getColor());
        int opponentPiecesBefore = countPieces(board, opponent);
        Board testBoard = board.clone();
        testBoard.doMove(move);
        int opponentPiecesAfter = countPieces(testBoard, opponent);
        return opponentPiecesBefore - opponentPiecesAfter;
    }
    private int countPieces(Board board, Color color) {
        int count = 0;
        int size = board.getSize();

        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                if (board.getState(x, y) == color) {
                    count++;
                }
            }
        }

        return count;
    }
}