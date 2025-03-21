package com.whitehat.chess;

public class ChessBoard {
    private ChessPiece[][] board;
    private boolean whiteTurn;

    public ChessBoard() {
        board = new ChessPiece[8][8];
        whiteTurn = true;
        initializeBoard();
    }

    private void initializeBoard() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = ChessPiece.EMPTY;
            }
        }
        board[0][0] = board[0][7] = ChessPiece.ROOK;
        board[0][1] = board[0][6] = ChessPiece.KNIGHT;
        board[0][2] = board[0][5] = ChessPiece.BISHOP;
        board[0][3] = ChessPiece.QUEEN;
        board[0][4] = ChessPiece.KING;
        for (int j = 0; j < 8; j++) board[1][j] = ChessPiece.PAWN;
        board[7][0] = board[7][7] = ChessPiece.ROOK;
        board[7][1] = board[7][6] = ChessPiece.KNIGHT;
        board[7][2] = board[7][5] = ChessPiece.BISHOP;
        board[7][3] = ChessPiece.QUEEN;
        board[7][4] = ChessPiece.KING;
        for (int j = 0; j < 8; j++) board[6][j] = ChessPiece.PAWN;
    }

    public void printBoard() {
        System.out.println("  a b c d e f g h");
        for (int i = 7; i >= 0; i--) {
            System.out.print((i + 1) + " ");
            for (int j = 0; j < 8; j++) {
                boolean isWhite = (i <= 1 || (i > 1 && board[i][j] == ChessPiece.EMPTY));
                System.out.print(board[i][j].getSymbol(isWhite) + " ");
            }
            System.out.println((i + 1));
        }
        System.out.println("  a b c d e f g h");
        System.out.println("Turn: " + (whiteTurn ? "White" : "Black"));
    }

    public ChessPiece getPiece(int x, int y) {
        return board[x][y];
    }

    public boolean isWhiteTurn() {
        return whiteTurn;
    }

    public void movePiece(int startX, int startY, int endX, int endY) {
        board[endX][endY] = board[startX][startY];
        board[startX][startY] = ChessPiece.EMPTY;
        whiteTurn = !whiteTurn;
    }

    public void undoMove(int startX, int startY, int endX, int endY, ChessPiece captured) {
        board[startX][startY] = board[endX][endY];
        board[endX][endY] = captured;
        whiteTurn = !whiteTurn;
    }
}