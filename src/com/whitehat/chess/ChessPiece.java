package com.whitehat.chess;

public enum ChessPiece {
    KING('K', 'k'), QUEEN('Q', 'q'), ROOK('R', 'r'),
    BISHOP('B', 'b'), KNIGHT('N', 'n'), PAWN('P', 'p'), EMPTY(' ', ' ');

    private final char whiteSymbol;
    private final char blackSymbol;

    ChessPiece(char whiteSymbol, char blackSymbol) {
        this.whiteSymbol = whiteSymbol;
        this.blackSymbol = blackSymbol;
    }

    public char getSymbol(boolean isWhite) {
        return isWhite ? whiteSymbol : blackSymbol;
    }
}