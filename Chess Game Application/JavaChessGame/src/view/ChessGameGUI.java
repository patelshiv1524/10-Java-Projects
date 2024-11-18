package view;

import controller.ChessController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.ChessBoard;
import model.ChessGame;
import model.Piece;
import model.Position;
import model.PieceColor;

import java.util.HashMap;
import java.util.Map;

public class ChessGameGUI extends Application {
    private ChessController controller;
    private final ChessSquareComponent[][] squares = new ChessSquareComponent[8][8];
    private ChessGame game;
    private GridPane boardGrid = new GridPane();

    // Map to store Unicode symbols for each piece type
    private final Map<Class<? extends Piece>, String> pieceUnicodeMap = new HashMap<>() {{
        put(model.Pawn.class, "\u265F");    // Black pawn
        put(model.Rook.class, "\u265C");    // Black rook
        put(model.Knight.class, "\u265E");  // Black knight
        put(model.Bishop.class, "\u265D");  // Black bishop
        put(model.Queen.class, "\u265B");   // Black queen
        put(model.King.class, "\u265A");    // Black king
    }};

    // Constructor with no arguments
    public ChessGameGUI() {
        // Initialize the controller and game here if necessary
    }

    // Initialize board and controller in the start method
    @Override
    public void start(Stage primaryStage) {
        controller = new ChessController(this); // Initialize controller
        game = controller.getGame(); // Initialize game from the controller

        initializeBoard();

        primaryStage.setTitle("Chess Game");

        // Menu bar with Reset option
        MenuBar menuBar = new MenuBar();
        Menu gameMenu = new Menu("Game");
        MenuItem resetItem = new MenuItem("Reset");
        resetItem.setOnAction(e -> resetGame());
        gameMenu.getItems().add(resetItem);
        menuBar.getMenus().add(gameMenu);

        // Setting up the root layout with menu bar and board grid
        VBox root = new VBox(menuBar, boardGrid);
        Scene scene = new Scene(root, 600, 600);
        primaryStage.setScene(scene);
        primaryStage.show();

        updateBoard(); // Initial display of pieces
    }

    private void initializeBoard() {
        for (int row = 0; row < squares.length; row++) {
            for (int col = 0; col < squares[row].length; col++) {
                ChessSquareComponent square = new ChessSquareComponent(row, col);
                final int finalRow = row;
                final int finalCol = col;
                square.setOnMouseClicked(e -> handleSquareClick(finalRow, finalCol)); // Handle click
                boardGrid.add(square, col, row);
                squares[row][col] = square;
            }
        }
    }

    private void handleSquareClick(int row, int col) {
        boolean moveResult = controller.handleSquareClick(row, col); // Delegate to controller
        clearHighlights();
        if (moveResult) {
            updateBoard(); // Refresh board
            checkGameState();
            checkGameOver();
        } else if (controller.getGame().isPieceSelected()) {
            highlightLegalMoves(new Position(row, col));
        }
    }

    public void updateBoard() {
        ChessBoard board = game.getBoard();
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = board.getPiece(row, col);
                if (piece != null) {
                    squares[row][col].setPieceImage(piece); // Use setPieceImage
                } else {
                    squares[row][col].clearPieceImage(); // Use clearPieceImage
                }
            }
        }
    }


    private void checkGameState() {
        PieceColor currentPlayer = game.getCurrentPlayerColor();
        if (game.isInCheck(currentPlayer)) {
            showAlert(currentPlayer + " is in check!");
        }
    }

    private void highlightLegalMoves(Position position) {
        var legalMoves = game.getLegalMovesForPieceAt(position);
        for (Position move : legalMoves) {
            squares[move.getRow()][move.getColumn()].setStyle("-fx-background-color: green;");
        }
    }

    private void clearHighlights() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                squares[row][col].resetBackground(row, col);
            }
        }
    }

    private void resetGame() {
        game.resetGame();
        updateBoard();
    }

    private void checkGameOver() {
        if (game.isCheckmate(game.getCurrentPlayerColor())) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Checkmate! Would you like to play again?");
            alert.showAndWait().ifPresent(response -> {
                if (response == javafx.scene.control.ButtonType.OK) {
                    resetGame();
                } else {
                    System.exit(0);
                }
            });
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message);
        alert.showAndWait();
    }

    public ChessSquareComponent getSquare(int row, int col) {
        return squares[row][col];
    }

    public static void main(String[] args) {
        launch(args);
    }
}



