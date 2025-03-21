package com.whitehat.chess;

import com.whitehatgaming.UserInput;
import com.whitehatgaming.UserInputFile;

public class ChessGame {
    private ChessBoard board;
    private UserInput input;

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
        if (startX < 0 || startX > 7 || startY < 0 || startY > 7 ||
            endX < 0 || endX > 7 || endY < 0 || endY > 7) {
            return false;
        }

        ChessPiece piece = board.getPiece(startX, startY);
        ChessPiece target = board.getPiece(endX, endY);
        // Correctly determine piece color based on initial position
        boolean isWhitePiece = (startX <= 1); // White pieces start on rows 0-1

        if (piece == ChessPiece.EMPTY || (isWhitePiece != board.isWhiteTurn()) ||
            (target != ChessPiece.EMPTY && (isWhitePiece == (endX <= 1)))) {
            return false;
        }

        int dx = Math.abs(endX - startX);
        int dy = Math.abs(endY - startY);

        switch (piece) {
            case KING:
                return dx <= 1 && dy <= 1;
            case QUEEN:
                return isPathClear(startX, startY, endX, endY) && (dx == dy || dx == 0 || dy == 0);
            case ROOK:
                return isPathClear(startX, startY, endX, endY) && (dx == 0 || dy == 0);
            case BISHOP:
                return isPathClear(startX, startY, endX, endY) && dx == dy;
            case KNIGHT:
                return (dx == 2 && dy == 1) || (dx == 1 && dy == 2);
            case PAWN:
                return isValidPawnMove(startX, startY, endX, endY, isWhitePiece);
            default:
                return false;
        }
    }

    private boolean isPathClear(int startX, int startY, int endX, int endY) {
        int dx = Integer.compare(endX, startX);
        int dy = Integer.compare(endY, startY);
        int x = startX + dx;
        int y = startY + dy;

        while (x != endX || y != endY) {
            if (board.getPiece(x, y) != ChessPiece.EMPTY) return false;
            x += dx;
            y += dy;
        }
        return true;
    }

    private boolean isValidPawnMove(int startX, int startY, int endX, int endY, boolean isWhite) {
        int direction = isWhite ? 1 : -1;
        int startRow = isWhite ? 1 : 6;
        int dx = endX - startX;
        int dy = Math.abs(endY - startY);

        if (dy == 0 && board.getPiece(endX, endY) == ChessPiece.EMPTY) {
            if (dx == direction) return true;
            if (dx == 2 * direction && startX == startRow &&
                board.getPiece(startX + direction, startY) == ChessPiece.EMPTY) {
                return true;
            }
        }
        if (dx == direction && dy == 1 && board.getPiece(endX, endY) != ChessPiece.EMPTY) {
            return true;
        }
        return false;
    }

    private boolean isInCheck(boolean white) {
        int kingX = -1, kingY = -1;
        for (int i = 0; i < 8 && kingX == -1; i++) {
            for (int j = 0; j < 8; j++) {
                if (board.getPiece(i, j) == ChessPiece.KING &&
                    ((i <= 1) == white)) {
                    kingX = i;
                    kingY = j;
                    break;
                }
            }
        }

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                boolean isOpponent = (i <= 1) != white;
                if (isOpponent && board.getPiece(i, j) != ChessPiece.EMPTY) {
                    if (isValidMove(i, j, kingX, kingY)) return true;
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