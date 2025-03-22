package com.whitehat.chess;

public class ChessBoard {
    private final ChessPiece[][] board;
    private boolean whiteTurn;

    public ChessBoard() {
        board = new ChessPiece[8][8];
        whiteTurn = true;
        initializeBoard();
    }

    private void initializeBoard() {
        System.out.println("Initializing board...");
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = ChessPiece.EMPTY;
            }
        }
        // Black pieces (rows 0-1, ranks 8-7)
        board[0][0] = board[0][7] = ChessPiece.ROOK;    // a8, h8
        board[0][1] = board[0][6] = ChessPiece.KNIGHT;  // b8, g8
        board[0][2] = board[0][5] = ChessPiece.BISHOP;  // c8, f8
        board[0][3] = ChessPiece.QUEEN;                 // d8
        board[0][4] = ChessPiece.KING;                  // e8
        for (int j = 0; j < 8; j++) board[1][j] = ChessPiece.PAWN;  // rank 7

        // White pieces (rows 6-7, ranks 2-1)
        board[7][0] = board[7][7] = ChessPiece.ROOK;    // a1, h1
        board[7][1] = board[7][6] = ChessPiece.KNIGHT;  // b1, g1
        board[7][2] = board[7][5] = ChessPiece.BISHOP;  // c1, f1
        board[7][3] = ChessPiece.QUEEN;                 // d1
        board[7][4] = ChessPiece.KING;                  // e1
        for (int j = 0; j < 8; j++) board[6][j] = ChessPiece.PAWN;  // rank 2
        System.out.println("Board initialized.");
    }

    public ChessPiece getPiece(int x, int y) {
        return board[x][y];
    }

    public void movePiece(int startX, int startY, int endX, int endY) {
        System.out.println("Moving piece from [" + startX + "," + startY + "] to [" + endX + "," + endY + "]");
        board[endX][endY] = board[startX][startY];
        board[startX][startY] = ChessPiece.EMPTY;
        whiteTurn = !whiteTurn;
    }

    public void undoMove(int startX, int startY, int endX, int endY, ChessPiece captured) {
        System.out.println("Undoing move from [" + startX + "," + startY + "] to [" + endX + "," + endY + "]");
        board[startX][startY] = board[endX][endY];
        board[endX][endY] = captured;
        whiteTurn = !whiteTurn;
    }

    public boolean isWhiteTurn() {
        return whiteTurn;
    }

    public boolean isPathClear(int startX, int startY, int endX, int endY) {
        System.out.println("Checking path from [" + startX + "," + startY + "] to [" + endX + "," + endY + "]");
        int dx = Integer.compare(endX, startX);
        int dy = Integer.compare(endY, startY);
        int x = startX + dx;
        int y = startY + dy;
        while (x != endX || y != endY) {
            if (board[x][y] != ChessPiece.EMPTY) {
                System.out.println("Path blocked at [" + x + "," + y + "]");
                return false;
            }
            x += dx;
            y += dy;
        }
        System.out.println("Path clear.");
        return true;
    }

    public void printBoard() {
        System.out.println("  a b c d e f g h");
        for (int i = 0; i < 8; i++) {
            System.out.print(8 - i + " ");
            for (int j = 0; j < 8; j++) {
                char symbol = (i >= 6) ? board[i][j].getWhiteSymbol() : board[i][j].getBlackSymbol();
                System.out.print(symbol + " ");
            }
            System.out.println(8 - i);
        }
        System.out.println("  a b c d e f g h");
        System.out.println("Turn: " + (whiteTurn ? "White" : "Black"));
    }
}