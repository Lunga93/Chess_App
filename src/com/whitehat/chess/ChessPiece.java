package com.whitehat.chess;

public enum ChessPiece {
    KING('K', 'k') {
        @Override
        public boolean isValidMove(int startX, int startY, int endX, int endY, ChessBoard board, boolean isWhite) {
            int dx = Math.abs(endX - startX);
            int dy = Math.abs(endY - startY);
            return dx <= 1 && dy <= 1;
        }
    },
    QUEEN('Q', 'q') {
        @Override
        public boolean isValidMove(int startX, int startY, int endX, int endY, ChessBoard board, boolean isWhite) {
            int dx = Math.abs(endX - startX);
            int dy = Math.abs(endY - startY);
            return (dx == dy || dx == 0 || dy == 0) && board.isPathClear(startX, startY, endX, endY);
        }
    },
    ROOK('R', 'r') {
        @Override
        public boolean isValidMove(int startX, int startY, int endX, int endY, ChessBoard board, boolean isWhite) {
            int dx = Math.abs(endX - startX);
            int dy = Math.abs(endY - startY);
            return (dx == 0 || dy == 0) && board.isPathClear(startX, startY, endX, endY);
        }
    },
    BISHOP('B', 'b') {
        @Override
        public boolean isValidMove(int startX, int startY, int endX, int endY, ChessBoard board, boolean isWhite) {
            int dx = Math.abs(endX - startX);
            int dy = Math.abs(endY - startY);
            return (dx == dy) && board.isPathClear(startX, startY, endX, endY);
        }
    },
    KNIGHT('N', 'n') {
        @Override
        public boolean isValidMove(int startX, int startY, int endX, int endY, ChessBoard board, boolean isWhite) {
            int dx = Math.abs(endX - startX);
            int dy = Math.abs(endY - startY);
            return (dx == 2 && dy == 1) || (dx == 1 && dy == 2);
        }
    },
    PAWN('P', 'p') {
        @Override
        public boolean isValidMove(int startX, int startY, int endX, int endY, ChessBoard board, boolean isWhite) {
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
    },
    EMPTY(' ', ' ') {
        @Override
        public boolean isValidMove(int startX, int startY, int endX, int endY, ChessBoard board, boolean isWhite) {
            return false; // Empty squares can't move
        }
    };

    private final char whiteSymbol;
    private final char blackSymbol;

    ChessPiece(char whiteSymbol, char blackSymbol) {
        this.whiteSymbol = whiteSymbol;
        this.blackSymbol = blackSymbol;
    }

    public char getSymbol(boolean isWhite) {
        return isWhite ? whiteSymbol : blackSymbol;
    }

    public abstract boolean isValidMove(int startX, int startY, int endX, int endY, ChessBoard board, boolean isWhite);
}