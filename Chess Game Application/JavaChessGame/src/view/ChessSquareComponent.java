package view;

import java.io.InputStream;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.Piece;
import model.PieceColor;

public class ChessSquareComponent extends Button {
    private int row;
    private int col;
    private static final String IMAGE_PATH = "/images/";

    public ChessSquareComponent(int row, int col) {
        this.row = row;
        this.col = col;
        initButton();
    }

    private void initButton() {
        setPrefSize(80, 80); // Adjust size as needed
        resetBackground(row, col); // Set initial background color
    }

    public void setPieceImage(Piece piece) {
        String imagePath = getImagePath(piece);
        System.out.println("Loading image from: " + imagePath); // Debug output

        try (InputStream stream = getClass().getResourceAsStream(imagePath)) {
            if (stream == null) {
                System.err.println("Image not found at: " + imagePath);
                System.err.println("Absolute path (for debugging): " + getClass().getResource(imagePath));
                return; // Skip loading for this piece if the image is missing
            }

            Image pieceImage = new Image(stream);
            ImageView imageView = new ImageView(pieceImage);
            imageView.setFitWidth(60); // Adjust image size as necessary
            imageView.setFitHeight(60);
            this.setGraphic(imageView);
        } catch (Exception e) {
            System.err.println("Error loading image from path: " + imagePath);
            e.printStackTrace();
        }
    }


    public void clearPieceImage() {
        this.setGraphic(null); // Remove image from the button
    }

    private String getImagePath(Piece piece) {
        String colorPrefix = (piece.getColor() == PieceColor.WHITE) ? "white" : "black";
        String pieceType = piece.getClass().getSimpleName().toLowerCase();
        return IMAGE_PATH + colorPrefix + "_" + pieceType + ".png";
    }

    public void resetBackground(int row, int col) {
        if ((row + col) % 2 == 0) {
            setStyle("-fx-background-color: #D2B48C;"); // Light brown color
        } else {
            setStyle("-fx-background-color: #8B4513;"); // Dark brown color
        }
    }
}
