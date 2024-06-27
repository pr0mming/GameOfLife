package com.game;

import com.core.GameRulesManager;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * This class represents the GUI in JavaFX
 */
public class Game extends Application {

	private GridPane gridPaneMatrix;
	private Label[][] matrix;
	private GameRulesManager calculator;
	private Button buttonStart, buttonPause, buttonStep, buttonRestore, buttonRedefine;
	private String colorLife, colorDeath;
	private Label labelGeneration, labelPopulation;
	private Timeline animation;
	private ComboBox<String> comboBox;
	private Rectangle2D primaryScreenBounds;

	@Override
	public void start(Stage primaryStage) throws Exception {

		primaryScreenBounds = Screen.getPrimary().getVisualBounds();

		colorLife = "#39E600";
		colorDeath = "#1A1A00";

		var cssInputStream = getClass().getResource("/global.css");

		if (cssInputStream == null) {
			throw new Exception("Stream is null");
		}

		BorderPane root = new BorderPane();
		root.getStylesheets().add(cssInputStream.toExternalForm());

		initializeMatrix(60, 30);
		initializeAnimations();

		calculator = new GameRulesManager(matrix, colorDeath, colorLife);

		// Buttons
		HBox boxTop = new HBox();
		boxTop.setPadding(new Insets(30, 10, 10, 10));
		boxTop.setSpacing(15);
		boxTop.setAlignment(Pos.CENTER);
		boxTop.setStyle("-fx-background-color: black;");

		buttonStart = new Button("Start Game");
		buttonStart.setPrefSize(145, 40);
		buttonStart.getStyleClass().add("button");
		buttonStart.setOnAction(e -> {
            if (calculator.getPopulation() > 0) {
                animation.play();
                ((Button) e.getSource()).setDisable(true);
                buttonPause.setDisable(false);
                buttonStep.setDisable(true);
                buttonRestore.setDisable(true);
                buttonRedefine.setDisable(true);
                comboBox.setDisable(true);
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("The grid is empty");
                alert.setHeaderText(null);
                alert.setContentText("The game starts at least one living cell");
                alert.showAndWait();
            }
        });

		buttonPause = new Button("Pause");
		buttonPause.setDisable(true);
		buttonPause.setPrefSize(145, 40);
		buttonPause.getStyleClass().add("button");
		buttonPause.setOnAction(event -> {
            animation.stop();
            ((Button) event.getSource()).setDisable(true);
            buttonStep.setDisable(false);
            buttonStart.setDisable(false);
            buttonRestore.setDisable(false);
        });

		buttonStep = new Button("Step by step");
		buttonStep.setDisable(false);
		buttonStep.setPrefSize(145, 40);
		buttonStep.getStyleClass().add("button");
		buttonStep.setOnAction(event -> {
            if (calculator.getPopulation() > 0)
                calculator.iterateGeneration();
            else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("The grid is empty");
                alert.setHeaderText(null);
                alert.setContentText("The game starts at least one living cell");
                alert.showAndWait();
            }
        });

		buttonRestore = new Button("Restore");
		buttonRestore.setDisable(false);
		buttonRestore.setPrefSize(145, 40);
		buttonRestore.getStyleClass().add("button");
		buttonRestore.setOnAction(event -> {
            calculator.restoreGame();
            calculator.setGeneration(0);
            calculator.setPopulation(0);
            updateGeneration();
            updatePopulation();
            buttonRedefine.setDisable(false);
            comboBox.setDisable(false);
            ((Button) event.getSource()).setDisable(true);
        });

		labelGeneration = new Label("Generation " + calculator.getGeneration());
		labelGeneration.setPrefSize(150, 40);
		labelGeneration.getStyleClass().add("label-information");

		labelPopulation = new Label("Population " + calculator.getPopulation());
		labelPopulation.setPrefSize(150, 40);
		labelPopulation.getStyleClass().add("label-information");

		boxTop.getChildren().addAll(buttonStart, buttonPause, buttonStep, buttonRestore, labelGeneration,
				labelPopulation);

		HBox boxBottom = new HBox();
		boxBottom.setPadding(new Insets(10, 10, 50, 10));
		boxBottom.setSpacing(10);
		boxBottom.setAlignment(Pos.BOTTOM_CENTER);
		boxBottom.setStyle("-fx-background-color: black;");

		comboBox = new ComboBox<>();
		comboBox.getItems().addAll("70x40", "70x35", "60x30", "50x25", "40x20");
		comboBox.setValue("60x30");

		Label LabelDefineGrid = new Label("Redefine Grid: ");
		LabelDefineGrid.getStyleClass().add("label-information");

		buttonRedefine = new Button("Resize");
		buttonRedefine.getStyleClass().add("button");
		buttonRedefine.setPrefSize(145, 40);
		buttonRedefine.setOnMouseClicked(event -> {
            gridPaneMatrix.getChildren().clear();
            String[] cords = comboBox.getValue().split("x");

			var x = Integer.parseInt(cords[0]) + 1 ;
			var y = Integer.parseInt(cords[1]) + 1;

            initializeMatrix(x, y);
            calculator.resizeMatrix(matrix);
            root.setCenter(gridPaneMatrix);
        });

		boxBottom.getChildren().addAll(LabelDefineGrid, comboBox, buttonRedefine);

		root.setTop(boxTop);
		root.setCenter(gridPaneMatrix);
		root.setBottom(boxBottom);
		root.getStyleClass().add("border-pane");

		Scene scene = new Scene(root, (primaryScreenBounds.getWidth() * 0.5625),
				(primaryScreenBounds.getHeight() * 0.867));

		primaryStage.setTitle("Game of Life");
		primaryStage.setScene(scene);
		primaryStage.centerOnScreen();

		var iconInputStream = getClass().getResourceAsStream("/icon.png");

		if (iconInputStream == null) {
			throw new Exception("Stream is null");
		}

		primaryStage.getIcons().addAll(new Image(iconInputStream));
		primaryStage.show();
	}

	private void initializeMatrix(int rows, int cols) {
		// Matrix
		gridPaneMatrix = new GridPane();
		gridPaneMatrix.setPrefSize(primaryScreenBounds.getWidth() * 0.60, primaryScreenBounds.getHeight() * 0.75);
		gridPaneMatrix.setPadding(new Insets(5, 20, 5, 20));
		gridPaneMatrix.setAlignment(Pos.CENTER);

		matrix = new Label[rows][cols];

		for (int i = 0; i < matrix.length; i++)
			for (int j = 0; j < matrix[i].length; j++) {

				matrix[i][j] = new Label();
				matrix[i][j].setAccessibleHelp(i + "," + j); // Save cell position, I didn't find a better way
				matrix[i][j].getStyleClass().add("classic-label");
				matrix[i][j].setStyle("-fx-background-color: " + colorDeath + ";");
				matrix[i][j].setMinWidth(primaryScreenBounds.getWidth() * 0.0070);
				matrix[i][j].setMinHeight(primaryScreenBounds.getHeight() * 0.01);
				matrix[i][j].setOnMouseClicked(event -> {
                    String[] cords = ((Label) event.getSource()).getAccessibleHelp().split(",");

					var x = Integer.parseInt(cords[0]) + 1 ;
					var y = Integer.parseInt(cords[1]) + 1;

                    var cellState = calculator.changeCellState(x, y);
                    ((Label) event.getSource())
                            .setStyle("-fx-background-color: " + ((cellState) ? colorLife : colorDeath) + ";");

					calculator.setPopulation(calculator.getPopulation() + (cellState ? 1 : -1));

                    updatePopulation();

					if (buttonRestore.isDisable()) {
						buttonRestore.setDisable(false);
					}
                });

				gridPaneMatrix.add(matrix[i][j], i, j);

			}
	}

	private void initializeAnimations() {
		// Animation
		animation = new Timeline(new KeyFrame(Duration.millis(100), event -> {
			calculator.iterateGeneration();
			calculator.setGeneration(calculator.getGeneration() + 1);

			updateGeneration();
			updatePopulation();
		}));

		animation.setCycleCount(Timeline.INDEFINITE);
	}

	private void updateGeneration() {
		labelGeneration.setText("Generation: " + calculator.getGeneration());
	}

	private void updatePopulation() {
		labelPopulation.setText("Population: " + calculator.getPopulation());
	}

	public static void main(String[] args) {
		launch();
	}
}