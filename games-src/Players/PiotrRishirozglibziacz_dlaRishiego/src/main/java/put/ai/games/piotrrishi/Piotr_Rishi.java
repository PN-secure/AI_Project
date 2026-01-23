package put.ai.games.piotrrishi;

import java.util.*;
import put.ai.games.game.Board;
import put.ai.games.game.Move;
import put.ai.games.game.Player;

public class Piotr_Rishi extends Player {

    private long startTime;
    private long maxTime;

    @Override
    public String getName() {
        return "Piotr Nowak 160071 Rishi Dixit 160334";
    }

    private int calculateOptimalDepth(Board board) {
        int size = board.getSize();
        if (size <= 4) return 8;
        else if (size <= 6) return 6;
        else if (size <= 10) return 5;
        else if (size <= 16) return 5;
        else return 4;
    }

    @Override
    public Move nextMove(Board b) {
        startTime = System.currentTimeMillis();
        maxTime = (long) (getTime() * 0.90);

        List<Move> moves = b.getMovesFor(getColor());
        if (moves.isEmpty()) return null;

        int depth = calculateOptimalDepth(b);


        List<Move> bestMoves = new ArrayList<>();
        double bestValue = Double.NEGATIVE_INFINITY;

        for (Move move : moves) {
            if (System.currentTimeMillis() - startTime > maxTime) break;

            Board testBoard = b.clone();
            testBoard.doMove(move);

            double value = minimax(testBoard, depth - 1, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, false);

            if (value > bestValue) {
                bestValue = value;
                bestMoves.clear();
                bestMoves.add(move);
            } else if (value == bestValue) {
                bestMoves.add(move);
            }
        }


        Move selected = (bestMoves.size() == 1)
                ? bestMoves.get(0)
                : Collections.max(bestMoves, Comparator.comparingInt(move -> {
            Board testBoard = b.clone();
            testBoard.doMove(move);
            return calculateControl(testBoard, getColor());
        }));

        return selected;
    }

    private double minimax(Board board, int depth, double alpha, double beta, boolean isMaximizing) {
        if (System.currentTimeMillis() - startTime > maxTime)
            return evaluate(board);

        Color winner = board.getWinner(isMaximizing ? getColor() : Player.getOpponent(getColor()));
        if (depth == 0 || winner != null)
            return evaluate(board);

        Color currentPlayer = isMaximizing ? getColor() : Player.getOpponent(getColor());
        List<Move> moves = board.getMovesFor(currentPlayer);
        if (moves.isEmpty()) return evaluate(board);

        if (isMaximizing) {
            double maxEval = Double.NEGATIVE_INFINITY;
            for (Move move : moves) {
                Board testBoard = board.clone();
                testBoard.doMove(move);
                double eval = minimax(testBoard, depth - 1, alpha, beta, false);
                maxEval = Math.max(maxEval, eval);
                alpha = Math.max(alpha, eval);
                if (beta <= alpha) break;
            }
            return maxEval;
        } else {
            double minEval = Double.POSITIVE_INFINITY;
            for (Move move : moves) {
                Board testBoard = board.clone();
                testBoard.doMove(move);
                double eval = minimax(testBoard, depth - 1, alpha, beta, true);
                minEval = Math.min(minEval, eval);
                beta = Math.min(beta, eval);
                if (beta <= alpha) break;
            }
            return minEval;
        }
    }


    private double evaluate(Board board) {
        Color winner = board.getWinner(getColor());
        if (winner == getColor()) return 100000.0;
        if (winner == Player.getOpponent(getColor())) return -100000.0;
        if (winner == Color.EMPTY) return 0.0;

        int myPieces = countPieces(board, getColor());
        int oppPieces = countPieces(board, Player.getOpponent(getColor()));
        return (myPieces - oppPieces) * 1000.0;
    }


    private int calculateControl(Board board, Color color) {
        int size = board.getSize();
        boolean[][] control = new boolean[size][size];
        int coverage = 0;

        for (int px = 0; px < size; px++) {
            for (int py = 0; py < size; py++) {
                if (board.getState(px, py) == color) {
                    for (int dx = -2; dx <= 2; dx++) {
                        for (int dy = -2; dy <= 2; dy++) {
                            int nx = px + dx, ny = py + dy;
                            if (nx >= 0 && nx < size && ny >= 0 && ny < size && !control[nx][ny]) {
                                control[nx][ny] = true;
                                coverage++;
                            }
                        }
                    }
                }
            }
        }
        return coverage;
    }

    private int countPieces(Board board, Color color) {
        int count = 0, size = board.getSize();
        for (int x = 0; x < size; x++)
            for (int y = 0; y < size; y++)
                if (board.getState(x, y) == color)
                    count++;
        return count;
    }
}