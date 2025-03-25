package main.java.com.whitehat.chess;

public enum ChessPiece {
    EMPTY(' ', ' ') {
        @Override
        public boolean isValidMove(int startX, int startY, int endX, int endY, ChessBoard board, boolean isWhite) {
            return false;
        }
    },
    PAWN('P', 'p') {
        @Override
        public boolean isValidMove(int startX, int startY, int endX, int endY, ChessBoard board, boolean isWhite) {
            int direction = isWhite ? -1 : 1;  // White up (-), Black down (+)
            int startRow = isWhite ? 6 : 1;    // White starts row 6, Black row 1
            int dx = endX - startX;
            int dy = Math.abs(endY - startY);
            if (dy == 0 && board.getPiece(endX, endY) == ChessPiece.EMPTY) {
                if (dx == direction) return true;
                if (dx == 2 * direction && startX == startRow && board.getPiece(startX + direction, startY) == ChessPiece.EMPTY) {
                    return true;
                }
            }
            if (dx == direction && dy == 1 && board.getPiece(endX, endY) != ChessPiece.EMPTY) {
                return true;
            }
            return false;
        }
    },
    ROOK('R', 'r') {
        @Override
        public boolean isValidMove(int startX, int startY, int endX, int endY, ChessBoard board, boolean isWhite) {
            return (startX == endX || startY == endY) && board.isPathClear(startX, startY, endX, endY);
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
    BISHOP('B', 'b') {
        @Override
        public boolean isValidMove(int startX, int startY, int endX, int endY, ChessBoard board, boolean isWhite) {
            int dx = Math.abs(endX - startX);
            int dy = Math.abs(endY - startY);
            return (dx == dy) && board.isPathClear(startX, startY, endX, endY);
        }
    },
    QUEEN('Q', 'q') {
        @Override
        public boolean isValidMove(int startX, int startY, int endX, int endY, ChessBoard board, boolean isWhite) {
            int dx = Math.abs(endX - startX);
            int dy = Math.abs(endY - startY);
            return ((dx == dy) || (dx == 0 || dy == 0)) && board.isPathClear(startX, startY, endX, endY);
        }
    },
    KING('K', 'k') {
        @Override
        public boolean isValidMove(int startX, int startY, int endX, int endY, ChessBoard board, boolean isWhite) {
            int dx = Math.abs(endX - startX);
            int dy = Math.abs(endY - startY);
            return dx <= 1 && dy <= 1;
        }
    };

    private final char whiteSymbol;
    private final char blackSymbol;

    ChessPiece(char whiteSymbol, char blackSymbol) {
        this.whiteSymbol = whiteSymbol;
        this.blackSymbol = blackSymbol;
    }

    public char getWhiteSymbol() {
        return whiteSymbol;
    }

    public char getBlackSymbol() {
        return blackSymbol;
    }

    public abstract boolean isValidMove(int startX, int startY, int endX, int endY, ChessBoard board, boolean isWhite);
}