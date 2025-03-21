package com.whitehat.chess;

import com.whitehatgaming.UserInput;
import com.whitehatgaming.UserInputFile;

public class ChessGame {
    private final ChessBoard board;
    private final UserInput input;

    public ChessGame(String filePath) throws Exception {
        board = new ChessBoard();
        input = new UserInputFile(filePath);
    }

    public void play() {
        board.printBoard();
        checkIfInCheck();

        int[] move;
        try {
            while ((move = input.nextMove()) != null) {
                int startCol = move[0];
                int startRow = move[1];
                int endCol = move[2];
                int endRow = move[3];

                String moveStr = "" + (char)('a' + startCol) + (8 - startRow) +
                                " to " + (char)('a' + endCol) + (8 - endRow);
                System.out.println("\nAttempting move: " + moveStr);

                if (isValidMove(startRow, startCol, endRow, endCol)) {
                    ChessPiece captured = board.getPiece(endRow, endCol);
                    board.movePiece(startRow, startCol, endRow, endCol);
                    if (isInCheck(board.isWhiteTurn())) {
                        System.out.println("Invalid move: puts own king in check");
                        board.undoMove(startRow, startCol, endRow, endCol, captured);
                    } else {
                        board.printBoard();
                        checkIfInCheck();
                    }
                } else {
                    System.out.println("Invalid move: " + moveStr);
                }
            }
        } catch (java.io.IOException e) {
            System.err.println("Error reading moves: " + e.getMessage());
        }
    }

    private boolean isValidMove(int startX, int startY, int endX, int endY) {
        // Bounds check
        if (startX < 0 || startX > 7 || startY < 0 || startY > 7 ||
            endX < 0 || endX > 7 || endY < 0 || endY > 7) {
            return false;
        }

        ChessPiece piece = board.getPiece(startX, startY);
        ChessPiece target = board.getPiece(endX, endY);
        boolean isWhitePiece = (startX <= 1); // White pieces on rows 0-1

        // Basic move rules
        if (piece == ChessPiece.EMPTY || (isWhitePiece != board.isWhiteTurn()) ||
            (target != ChessPiece.EMPTY && (isWhitePiece == (endX <= 1)))) {
            return false;
        }

        // Delegate to piece-specific validation
        return piece.isValidMove(startX, startY, endX, endY, board, isWhitePiece);
    }

    private boolean isInCheck(boolean white) {
        int kingX = -1, kingY = -1;
        for (int i = 0; i < 8 && kingX == -1; i++) {
            for (int j = 0; j < 8; j++) {
                if (board.getPiece(i, j) == ChessPiece.KING && (i <= 1) == white) {
                    kingX = i;
                    kingY = j;
                    break;
                }
            }
        }

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                ChessPiece piece = board.getPiece(i, j);
                boolean isOpponent = (i <= 1) != white;
                if (isOpponent && piece != ChessPiece.EMPTY) {
                    if (piece.isValidMove(i, j, kingX, kingY, board, !white)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void checkIfInCheck() {
        if (isInCheck(board.isWhiteTurn())) {
            System.out.println((board.isWhiteTurn() ? "White" : "Black") + " in check");
        }
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java ChessGame <moves_file>");
            return;
        }
        try {
            ChessGame game = new ChessGame(args[0]);
            game.play();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}