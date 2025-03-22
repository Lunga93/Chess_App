package com.whitehat.chess;

import com.whitehatgaming.UserInput;
import com.whitehatgaming.UserInputFile;

public class ChessGame {
    private final ChessBoard board;
    private final UserInput input;

    public ChessGame(String filePath) throws Exception {
        System.out.println("Initializing ChessGame with file: " + filePath);
        board = new ChessBoard();
        input = new UserInputFile(filePath);
        System.out.println("ChessGame initialized successfully.");
    }

    public void play() {
        System.out.println("Starting game play...");
        board.printBoard();
        checkIfInCheck();

        int[] move;
        try {
            System.out.println("Entering move processing loop...");
            while ((move = input.nextMove()) != null) {
                System.out.println("Read move: " + java.util.Arrays.toString(move));
                int startCol = move[0];
                int startRow = move[1];
                int endCol = move[2];
                int endRow = move[3];

                String moveStr = "" + (char)('a' + startCol) + (8 - startRow) +
                        " to " + (char)('a' + endCol) + (8 - endRow);
                System.out.println("\nAttempting move: " + moveStr + " (start: [" + startRow + "," + startCol + "], end: [" + endRow + "," + endCol + "])");

                if (isValidMove(startRow, startCol, endRow, endCol)) {
                    System.out.println("Move validated as legal.");
                    ChessPiece captured = board.getPiece(endRow, endCol);
                    System.out.println("Captured piece (if any): " + captured);
                    board.movePiece(startRow, startCol, endRow, endCol);
                    System.out.println("Move applied, turn switched to: " + (board.isWhiteTurn() ? "White" : "Black"));
                    if (isInCheck(board.isWhiteTurn())) {
                        System.out.println("Invalid move: puts own king in check");
                        board.undoMove(startRow, startCol, endRow, endCol, captured);
                        System.out.println("Move undone, turn reverted to: " + (board.isWhiteTurn() ? "White" : "Black"));
                    } else {
                        System.out.println("Move successful, displaying updated board...");
                        board.printBoard();
                        checkIfInCheck();
                    }
                } else {
                    System.out.println("Invalid move: " + moveStr);
                }
            }
            System.out.println("No more moves to process.");
        } catch (java.io.IOException e) {
            System.err.println("Error reading moves: " + e.getMessage());
        }
        System.out.println("Game play completed.");
    }

    private boolean isValidMove(int startX, int startY, int endX, int endY) {
        System.out.println("Validating move from [" + startX + "," + startY + "] to [" + endX + "," + endY + "]");
        if (startX < 0 || startX > 7 || startY < 0 || startY > 7 ||
                endX < 0 || endX > 7 || endY < 0 || endY > 7) {
            System.out.println("Move rejected: Out of bounds.");
            return false;
        }

        ChessPiece piece = board.getPiece(startX, startY);
        ChessPiece target = board.getPiece(endX, endY);
        boolean isWhitePiece = (startX >= 6);  // White on rows 6-7
        System.out.println("Piece: " + piece + ", Target: " + target + ", Is White: " + isWhitePiece + ", Current Turn: " + (board.isWhiteTurn() ? "White" : "Black"));

        if (piece == ChessPiece.EMPTY) {
            System.out.println("Move rejected: No piece at starting position.");
            return false;
        }
        if (isWhitePiece != board.isWhiteTurn()) {
            System.out.println("Move rejected: Wrong player's turn.");
            return false;
        }
        if (target != ChessPiece.EMPTY && (isWhitePiece == (endX >= 6))) {
            System.out.println("Move rejected: Cannot capture own piece.");
            return false;
        }

        System.out.println("Checking piece rule: dx=" + Math.abs(endX - startX) + ", dy=" + Math.abs(endY - startY));
        boolean pieceValid = piece.isValidMove(startX, startY, endX, endY, board, isWhitePiece);
        System.out.println("Piece-specific validation result: " + pieceValid);
        return pieceValid;
    }

    private boolean isInCheck(boolean white) {
        System.out.println("Checking if " + (white ? "White" : "Black") + " is in check...");
        int kingX = -1, kingY = -1;
        for (int i = 0; i < 8 && kingX == -1; i++) {
            for (int j = 0; j < 8; j++) {
                if (board.getPiece(i, j) == ChessPiece.KING && (i >= 6) == white) {
                    kingX = i;
                    kingY = j;
                    System.out.println("Found king at [" + kingX + "," + kingY + "]");
                    break;
                }
            }
        }

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                ChessPiece piece = board.getPiece(i, j);
                boolean isOpponent = (i >= 6) != white;
                if (isOpponent && piece != ChessPiece.EMPTY) {
                    if (piece.isValidMove(i, j, kingX, kingY, board, !white)) {
                        System.out.println("Check detected: " + piece + " can attack king.");
                        return true;
                    }
                }
            }
        }
        System.out.println("No check detected.");
        return false;
    }

    private void checkIfInCheck() {
        System.out.println("Checking if current player is in check...");
        if (isInCheck(board.isWhiteTurn())) {
            System.out.println((board.isWhiteTurn() ? "White" : "Black") + " in check");
        } else {
            System.out.println("Current player not in check.");
        }
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java ChessGame <moves_file>");
            return;
        }
        System.out.println("Starting ChessGame with argument: " + args[0]);
        try {
            ChessGame game = new ChessGame(args[0]);
            game.play();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
        System.out.println("ChessGame execution finished.");
    }
}