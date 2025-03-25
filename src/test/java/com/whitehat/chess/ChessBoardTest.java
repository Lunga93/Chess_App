package com.whitehat.chess;  // Matches src/test/java/com/whitehat/chess/

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import main.java.com.whitehat.chess.ChessBoard;
import main.java.com.whitehat.chess.ChessPiece;

// No need to import ChessBoard or ChessPiece since they're in the same package
// If they were in a different package, you'd import com.whitehat.chess.ChessBoard and com.whitehat.chess.ChessPiece

public class ChessBoardTest {

    private ChessBoard board;

    @BeforeEach
    public void setUp() {
        board = new ChessBoard();
    }

    @Test
    public void testInitialBoardSetup() {
        // Test white pieces
        assertEquals(ChessPiece.ROOK, board.getPiece(7, 0));
        assertTrue(board.isWhitePiece(7, 0));
        assertEquals(ChessPiece.KING, board.getPiece(7, 4));
        assertTrue(board.isWhitePiece(7, 4));
        assertEquals(ChessPiece.PAWN, board.getPiece(6, 3));
        assertTrue(board.isWhitePiece(6, 3));

        // Test black pieces
        assertEquals(ChessPiece.ROOK, board.getPiece(0, 0));
        assertFalse(board.isWhitePiece(0, 0));
        assertEquals(ChessPiece.KING, board.getPiece(0, 4));
        assertFalse(board.isWhitePiece(0, 4));
        assertEquals(ChessPiece.PAWN, board.getPiece(1, 3));
        assertFalse(board.isWhitePiece(1, 3));

        // Test empty squares
        assertEquals(ChessPiece.EMPTY, board.getPiece(3, 3));
        assertFalse(board.isWhitePiece(3, 3));

        // Test initial turn
        assertTrue(board.isWhiteTurn());
    }

    @Test
    public void testMovePiece() {
        board.movePiece(6, 4, 4, 4);
        assertEquals(ChessPiece.PAWN, board.getPiece(4, 4));
        assertTrue(board.isWhitePiece(4, 4));
        assertEquals(ChessPiece.EMPTY, board.getPiece(6, 4));
        assertFalse(board.isWhiteTurn());
    }

    @Test
    public void testUndoMove() {
        ChessBoard.Piece captured = new ChessBoard.Piece(ChessPiece.EMPTY, false);
        board.movePiece(6, 4, 4, 4);
        board.undoMove(6, 4, 4, 4, captured);
        assertEquals(ChessPiece.PAWN, board.getPiece(6, 4));
        assertTrue(board.isWhitePiece(6, 4));
        assertEquals(ChessPiece.EMPTY, board.getPiece(4, 4));
        assertTrue(board.isWhiteTurn());
    }

    @Test
    public void testPawnValidMove() {
        assertTrue(ChessPiece.PAWN.isValidMove(6, 0, 5, 0, board, true));
        assertTrue(ChessPiece.PAWN.isValidMove(6, 0, 4, 0, board, true));
        assertFalse(ChessPiece.PAWN.isValidMove(6, 0, 3, 0, board, true));
        assertTrue(ChessPiece.PAWN.isValidMove(1, 0, 2, 0, board, false));
        assertTrue(ChessPiece.PAWN.isValidMove(1, 0, 3, 0, board, false));
    }

    @Test
    public void testRookValidMove() {
        board.movePiece(6, 0, 4, 0);
        assertTrue(ChessPiece.ROOK.isValidMove(7, 0, 5, 0, board, true));
        assertFalse(ChessPiece.ROOK.isValidMove(7, 0, 5, 1, board, true));
    }

    @Test
    public void testKnightValidMove() {
        assertTrue(ChessPiece.KNIGHT.isValidMove(7, 1, 5, 0, board, true));
        assertTrue(ChessPiece.KNIGHT.isValidMove(7, 1, 5, 2, board, true));
        assertFalse(ChessPiece.KNIGHT.isValidMove(7, 1, 6, 1, board, true));
    }

//    @Test
//    public void testBishopValidMove() {
//        board.movePiece(6, 2, 4, 2);
//        assertTrue(ChessPiece.BISHOP.isValidMove(7, 2, 5, 0, board, true)); // Diagonal
//        assertFalse(ChessPiece.BISHOP.isValidMove(7, 2, 5, 1, board, true));
//    }
//
//    @Test
//    public void testQueenValidMove() {
//        board.movePiece(6, 3, 4, 3);
//        assertTrue(ChessPiece.QUEEN.isValidMove(7, 3, 5, 3, board, true));
//        assertTrue(ChessPiece.QUEEN.isValidMove(7, 3, 5, 1, board, true)); // Diagonal
//        assertFalse(ChessPiece.QUEEN.isValidMove(7, 3, 5, 2, board, true));
//    }

    @Test
    public void testIsPathClear() {
        assertFalse(board.isPathClear(7, 0, 5, 0)); // Blocked initially
        board.movePiece(6, 0, 4, 0);
        assertTrue(board.isPathClear(7, 0, 5, 0));
        assertFalse(board.isPathClear(7, 2, 5, 0));
    }

    @Test
    public void testKingValidMove() {
        assertTrue(ChessPiece.KING.isValidMove(7, 4, 6, 4, board, true));
        assertTrue(ChessPiece.KING.isValidMove(7, 4, 6, 5, board, true));
        assertFalse(ChessPiece.KING.isValidMove(7, 4, 5, 4, board, true));
    }
}