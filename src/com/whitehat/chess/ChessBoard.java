package com.whitehat.chess;

public class ChessBoard {
    public static class Piece {
        ChessPiece type;
        boolean isWhite;

        Piece(ChessPiece type, boolean isWhite) {
            this.type = type;
            this.isWhite = isWhite;
        }

        Piece(Piece other) {
            this.type = other.type;
            this.isWhite = other.isWhite;
        }
    }

    private final Piece[][] board;
    private boolean whiteTurn;

    public ChessBoard() {
        board = new Piece[8][8];
        whiteTurn = true;
        initializeBoard();
    }

    private void initializeBoard() {
        System.out.println("[INFO] Initializing board...");
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = new Piece(ChessPiece.EMPTY, false);
            }
        }
        board[0][0] = board[0][7] = new Piece(ChessPiece.ROOK, false);
        board[0][1] = board[0][6] = new Piece(ChessPiece.KNIGHT, false);
        board[0][2] = board[0][5] = new Piece(ChessPiece.BISHOP, false);
        board[0][3] = new Piece(ChessPiece.QUEEN, false);
        board[0][4] = new Piece(ChessPiece.KING, false);
        for (int j = 0; j < 8; j++) board[1][j] = new Piece(ChessPiece.PAWN, false);

        board[7][0] = board[7][7] = new Piece(ChessPiece.ROOK, true);
        board[7][1] = board[7][6] = new Piece(ChessPiece.KNIGHT, true);
        board[7][2] = board[7][5] = new Piece(ChessPiece.BISHOP, true);
        board[7][3] = new Piece(ChessPiece.QUEEN, true);
        board[7][4] = new Piece(ChessPiece.KING, true);
        for (int j = 0; j < 8; j++) board[6][j] = new Piece(ChessPiece.PAWN, true);
    }

    public ChessPiece getPiece(int x, int y) {
        return board[x][y].type;
    }

    public boolean isWhitePiece(int x, int y) {
        return board[x][y].isWhite;
    }

    public void movePiece(int startX, int startY, int endX, int endY) {
        board[endX][endY] = new Piece(board[startX][startY]);
        board[startX][startY] = new Piece(ChessPiece.EMPTY, false);
        whiteTurn = !whiteTurn;
    }

    public void undoMove(int startX, int startY, int endX, int endY, Piece captured) {
        board[startX][startY] = new Piece(board[endX][endY]);
        board[endX][endY] = captured != null ? new Piece(captured) : new Piece(ChessPiece.EMPTY, false);
        whiteTurn = !whiteTurn;
    }

    public boolean isWhiteTurn() {
        return whiteTurn;
    }

    public boolean isPathClear(int startX, int startY, int endX, int endY) {
        int dx = Integer.compare(endX, startX);
        int dy = Integer.compare(endY, startY);
        int x = startX + dx;
        int y = startY + dy;
        while (x != endX || y != endY) {
            if (board[x][y].type != ChessPiece.EMPTY) {
                return false;
            }
            x += dx;
            y += dy;
        }
        return true;
    }

    public void printBoard() {
        System.out.println("  a b c d e f g h");
        for (int i = 0; i < 8; i++) {
            System.out.print(8 - i + " ");
            for (int j = 0; j < 8; j++) {
                char symbol = board[i][j].isWhite ? board[i][j].type.getWhiteSymbol() : board[i][j].type.getBlackSymbol();
                System.out.print(symbol + " ");
            }
            System.out.println(8 - i);
        }
        System.out.println("  a b c d e f g h");
        System.out.println("Turn: " + (whiteTurn ? "White" : "Black"));
    }
}