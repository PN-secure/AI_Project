package put.ai.games.piotrrishi;

import java.util. ArrayList;
import java.util. List;
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


        List<Move> bestMoves = new ArrayList<>();
        int maxCaptured = -1;


        for (Move move : moves) {
            int capturedCount = countCapturedPieces(b, move);

            if (capturedCount > maxCaptured) {
                maxCaptured = capturedCount;
                bestMoves.clear();
                bestMoves.add(move);
            } else if (capturedCount == maxCaptured) {
                bestMoves. add(move);
            }
        }

        if (bestMoves. size() == 1) {
            return bestMoves.get(0);
        }

        Move bestMove = null;
        int maxExpansion = -1;

        for (Move move : bestMoves) {
            int expansion = calculateExpansionAfterMove(b, move);

            if (expansion > maxExpansion) {
                maxExpansion = expansion;
                bestMove = move;
            }
        }

        return bestMove != null ? bestMove : bestMoves.get(0);
    }


    private int countCapturedPieces(Board board, Move move) {
        Color opponent = Player.getOpponent(getColor());

        int opponentPiecesBefore = countPieces(board, opponent);

        Board testBoard = board.clone();
        testBoard.doMove(move);

        int opponentPiecesAfter = countPieces(testBoard, opponent);

        return opponentPiecesBefore - opponentPiecesAfter;
    }


    private int calculateExpansionAfterMove(Board board, Move move) {

        Board testBoard = board.clone();
        testBoard.doMove(move);

        int size = testBoard.getSize();
        boolean[][] expansionMap = new boolean[size][size];


        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                if (testBoard.getState(x, y) == getColor()) {

                    markExpansionArea(expansionMap, x, y, size);
                }
            }
        }


        return countExpansionFields(expansionMap);
    }


    private void markExpansionArea(boolean[][] expansionMap, int centerX, int centerY, int boardSize) {

        int radius = 2;

        for (int dx = -radius; dx <= radius; dx++) {
            for (int dy = -radius; dy <= radius; dy++) {
                int x = centerX + dx;
                int y = centerY + dy;


                if (x >= 0 && x < boardSize && y >= 0 && y < boardSize) {
                    expansionMap[x][y] = true;
                }
            }
        }
    }


    private int countExpansionFields(boolean[][] expansionMap) {
        int count = 0;
        for (int x = 0; x < expansionMap.length; x++) {
            for (int y = 0; y < expansionMap[x].length; y++) {
                if (expansionMap[x][y]) {
                    count++;
                }
            }
        }
        return count;
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