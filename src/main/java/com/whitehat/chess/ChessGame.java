package main.java.com.whitehat.chess;

import com.whitehatgaming.UserInput;
import com.whitehatgaming.UserInputFile;

public class ChessGame {
    private final ChessBoard board;
    private final UserInput input;

    public ChessGame(String filePath) {
        System.out.println("[INFO] Initializing game with file: " + filePath);
        board = new ChessBoard();
        try {
            input = new UserInputFile(filePath);
        } catch (Exception e) {
            System.out.println("[ERROR] Failed to initialize input: " + e.getMessage());
            throw new RuntimeException(e); // Fallback to runtime exception for simplicity
        }
    }

    public void play() {
        System.out.println("[INFO] Starting game...");
        board.printBoard();

        int[] move;
        try {
            while ((move = input.nextMove()) != null) {
                int startCol = move[0];
                int startRow = move[1];
                int endCol = move[2];
                int endRow = move[3];
                String moveStr = "" + (char)('a' + startCol) + (8 - startRow) + " to " + (char)('a' + endCol) + (8 - endRow);

                System.out.println("[MOVE] " + moveStr);
                if (isValidMove(startRow, startCol, endRow, endCol)) {
                    ChessBoard.Piece captured = new ChessBoard.Piece(board.getPiece(endRow, endCol), board.isWhitePiece(endRow, endCol));
                    boolean wasWhiteTurn = board.isWhiteTurn();
                    board.movePiece(startRow, startCol, endRow, endCol);

                    if (isInCheck(wasWhiteTurn)) {
                        System.out.println("[ERROR] Move puts own king in check");
                        board.undoMove(startRow, startCol, endRow, endCol, captured);
                    } else {
                        board.printBoard();
                        boolean currentPlayer = board.isWhiteTurn();
                        if (isInCheck(currentPlayer)) {
                            System.out.println("[CHECK] " + (currentPlayer ? "White" : "Black") + " is in check");
                        }
                        boolean opponent = !currentPlayer;
                        if (isInCheck(opponent)) {
                            System.out.println("[CHECK] " + (opponent ? "White" : "Black") + " is in check");
                            if (isCheckmate(opponent)) {
                                System.out.println("[CHECKMATE] " + (!opponent ? "White" : "Black") + " wins!");
                                return;
                            }
                        }
                    }
                } else {
                    System.out.println("[ERROR] Invalid move: " + moveStr);
                }
            }
            System.out.println("[INFO] Game ended: No more moves");
        } catch (java.io.IOException e) {
            System.out.println("[ERROR] Error reading moves: " + e.getMessage());
        }
    }

    private boolean isValidMove(int startX, int startY, int endX, int endY) {
        if (startX < 0 || startX > 7 || startY < 0 || startY > 7 || endX < 0 || endX > 7 || endY < 0 || endY > 7) {
            return false;
        }

        ChessPiece piece = board.getPiece(startX, startY);
        ChessPiece target = board.getPiece(endX, endY);
        boolean isWhitePiece = board.isWhitePiece(startX, startY);

        if (piece == ChessPiece.EMPTY || isWhitePiece != board.isWhiteTurn() ||
                (target != ChessPiece.EMPTY && isWhitePiece == board.isWhitePiece(endX, endY))) {
            return false;
        }

        return piece.isValidMove(startX, startY, endX, endY, board, isWhitePiece);
    }

    private boolean isInCheck(boolean white) {
        int kingX = -1, kingY = -1;
        for (int i = 0; i < 8 && kingX == -1; i++) {
            for (int j = 0; j < 8; j++) {
                if (board.getPiece(i, j) == ChessPiece.KING && board.isWhitePiece(i, j) == white) {
                    kingX = i;
                    kingY = j;
                    break;
                }
            }
        }

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                ChessPiece piece = board.getPiece(i, j);
                boolean isOpponent = board.isWhitePiece(i, j) != white;
                if (isOpponent && piece != ChessPiece.EMPTY) {
                    if (piece.isValidMove(i, j, kingX, kingY, board, !white)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean isCheckmate(boolean white) {
        if (!isInCheck(white)) {
            return false;
        }

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board.getPiece(i, j) != ChessPiece.EMPTY && board.isWhitePiece(i, j) == white) {
                    for (int x = 0; x < 8; x++) {
                        for (int y = 0; y < 8; y++) {
                            if (isValidMove(i, j, x, y)) {
                                ChessBoard.Piece captured = new ChessBoard.Piece(board.getPiece(x, y), board.isWhitePiece(x, y));
                                board.movePiece(i, j, x, y);
                                boolean stillInCheck = isInCheck(white);
                                board.undoMove(i, j, x, y, captured);
                                if (!stillInCheck) {
                                    return false;
                                }
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("[ERROR] Usage: java ChessGame <moves_file>");
            return;
        }

        ChessGame game = new ChessGame(args[0]);
        game.play();
    }
}