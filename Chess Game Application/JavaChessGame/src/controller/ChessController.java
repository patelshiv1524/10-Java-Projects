package controller;

import model.ChessGame;
import model.PieceColor;
import model.Position;
import view.ChessGameGUI;
import view.ChessSquareComponent;
import javafx.scene.control.Alert;

public class ChessController {
    private final ChessGame game;
    private final ChessGameGUI gui;

    public ChessController(ChessGameGUI gui) {
        this.gui = gui;
        this.game = new ChessGame();
    }

    /**
     * Handles a square click from the GUI, selecting or moving a piece.
     * 
     * @param row Row of the clicked square.
     * @param col Column of the clicked square.
     */
    public boolean handleSquareClick(int row, int col) {
        boolean moveResult = game.handleSquareSelection(row, col); // Try selecting or moving a piece
        gui.updateBoard(); // Refresh the board view after any action

        if (moveResult) { // If a move was made, check the game state
            checkGameState();
            checkGameOver();
            return true;
        } else if (game.isPieceSelected()) { // If a piece is selected, highlight possible moves
            highlightLegalMoves(row, col);
        } else {
            clearHighlights();
        }
        return false;
    }

    /**
     * Checks the current game state for check or checkmate and displays relevant alerts.
     */
    private void checkGameState() {
        PieceColor currentPlayer = game.getCurrentPlayerColor();
        if (game.isInCheck(currentPlayer)) {
            showAlert(currentPlayer + " is in check!");
        }
    }

    /**
     * Highlights legal moves for the selected piece.
     * 
     * @param row Row of the selected piece.
     * @param col Column of the selected piece.
     */
    private void highlightLegalMoves(int row, int col) {
        clearHighlights(); // Clear previous highlights
        Position selectedPosition = new Position(row, col);
        var legalMoves = game.getLegalMovesForPieceAt(selectedPosition);

        for (Position move : legalMoves) {
            ChessSquareComponent square = gui.getSquare(move.getRow(), move.getColumn());
            square.setStyle("-fx-background-color: green;"); // Highlight legal move squares
        }
    }

    /**
     * Clears highlights from the board.
     */
    private void clearHighlights() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                ChessSquareComponent square = gui.getSquare(row, col);
                square.resetBackground(row, col); // Reset to original background color
            }
        }
    }

    /**
     * Checks if the game is over due to checkmate and prompts to reset if so.
     */
    private void checkGameOver() {
        PieceColor currentPlayer = game.getCurrentPlayerColor();
        if (game.isCheckmate(currentPlayer)) {
            showAlert("Checkmate! " + currentPlayer + " loses. Game over.");
            resetGame(); // Automatically reset the game after checkmate
        }
    }

    /**
     * Resets the game to its initial state.
     */
    public void resetGame() {
        game.resetGame();
        gui.updateBoard(); // Refresh the GUI to show the reset board
        clearHighlights(); // Clear any remaining highlights
    }

    /**
     * Displays an alert dialog with a given message.
     * 
     * @param message The message to display.
     */
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Alert");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Returns the current ChessGame instance.
     */
    public ChessGame getGame() {
        return game;
    }
}
