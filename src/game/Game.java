
package game;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Screen;
import javafx.util.Duration;

/**
 *
 * @author pr0mming
 * 
 * How few simple rules can determine such complex things?
 * 
 * GitHub: https://github.com/pr0mming
 */
public class Game extends Application{

    private GridPane gridPaneMatrix;
    private Label[][] matrix;
    private Calculate calculator;
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
        
        BorderPane root = new BorderPane();
        root.getStylesheets().addAll(getClass().getResource("StyleGame.css").toExternalForm());
        
        //Matrix
        gridPaneMatrix = new GridPane();
        gridPaneMatrix.setPrefSize(primaryScreenBounds.getWidth() * 0.60, primaryScreenBounds.getHeight() * 0.75);
        gridPaneMatrix.setPadding(new Insets(5, 20, 5, 20));
        gridPaneMatrix.setAlignment(Pos.CENTER);
        
        defineMatrix(60, 30);
        
        //Animation
        animation = new Timeline(new KeyFrame(Duration.millis(70), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                calculator.createPattern();
                calculator.increaseGeneration(1);
                updateGeneration();
                updatePopulation();
            }
        }));
        
        animation.setCycleCount(Timeline.INDEFINITE);
        
        calculator = new Calculate(matrix, colorDeath, colorLife);      
        
        //Buttons
        HBox boxTop = new HBox();
        boxTop.setPadding(new Insets(30, 10, 10, 10));
        boxTop.setSpacing(15);
        boxTop.setAlignment(Pos.CENTER);
        boxTop.setStyle("-fx-background-color: black;");
        
        buttonStart = new Button("Start Game");
        buttonStart.setPrefSize((primaryScreenBounds.getWidth() * 0.077), (primaryScreenBounds.getWidth() * 0.021));
        buttonStart.getStyleClass().add("button");
        buttonStart.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                if (Integer.valueOf(calculator.getPopulation()) > 0) {
                    animation.play();
                    ((Button)e.getSource()).setDisable(true);
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
            }
        });
        
        buttonPause = new Button("Pause");
        buttonPause.setDisable(true);
        buttonPause.setPrefSize((primaryScreenBounds.getWidth() * 0.077), (primaryScreenBounds.getWidth() * 0.021));
        buttonPause.getStyleClass().add("button");
        buttonPause.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                animation.stop();
                ((Button) event.getSource()).setDisable(true);
                buttonStep.setDisable(false);
                buttonStart.setDisable(false);
                buttonRestore.setDisable(false);
            }
        });
        
        buttonStep = new Button("Step by step");
        buttonStep.setDisable(false);
        buttonStep.setPrefSize((primaryScreenBounds.getWidth() * 0.077), (primaryScreenBounds.getWidth() * 0.021));
        buttonStep.getStyleClass().add("button");
        buttonStep.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (Integer.valueOf(calculator.getPopulation()) > 0) calculator.createPattern();
                else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("The grid is empty");
                    alert.setHeaderText(null);
                    alert.setContentText("The game starts at least one living cell");
                    alert.showAndWait();
                }
            }
        });
        
        buttonRestore = new Button("Restore");
        buttonRestore.setDisable(false);
        buttonRestore.setPrefSize((primaryScreenBounds.getWidth() * 0.077), (primaryScreenBounds.getWidth() * 0.021));
        buttonRestore.getStyleClass().add("button");
        buttonRestore.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {               
                calculator.restoreMatrix();
                calculator.setGeneration(0l);
                calculator.setPopulation(0l);
                updateGeneration();
                updatePopulation();
                buttonRedefine.setDisable(false);
                comboBox.setDisable(false);
                ((Button)event.getSource()).setDisable(true);
            }
        });
        
        labelGeneration = new Label("Generation "+calculator.getGeneration());
        labelGeneration.getStyleClass().add("label-information");
        
        labelPopulation = new Label("Population "+calculator.getPopulation());
        labelPopulation.getStyleClass().add("label-information");       
        
        boxTop.getChildren().addAll(buttonStart, buttonPause, buttonStep, buttonRestore, labelGeneration, labelPopulation);
        
        HBox boxBottom = new HBox();
        boxBottom.setPadding(new Insets(10, 10, 50, 10));
        boxBottom.setSpacing(10);
        boxBottom.setAlignment(Pos.BOTTOM_CENTER);
        boxBottom.setStyle("-fx-background-color: black;");
        
        comboBox = new ComboBox<>();
        comboBox.getItems().addAll("80x40", "70x35", "60x30", "50x25", "40x20");      
        comboBox.setValue("60x30");
        
        Label LabelDefineGrid = new Label("Redefine Grid: ");
        LabelDefineGrid.getStyleClass().add("label-information");
        
        buttonRedefine = new Button("Redefine");
        buttonRedefine.getStyleClass().add("button");
        buttonRedefine.setPrefSize((primaryScreenBounds.getWidth() * 0.077), (primaryScreenBounds.getWidth() * 0.021));
        buttonRedefine.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {   
                gridPaneMatrix.getChildren().clear();
                String[] coordinates = comboBox.getValue().split("x");
                
                defineMatrix(Integer.valueOf(coordinates[0]), Integer.valueOf(coordinates[1]));
                calculator.redefineReplic(matrix);
                root.setCenter(gridPaneMatrix);
            }
        });
        
        boxBottom.getChildren().addAll(LabelDefineGrid, comboBox, buttonRedefine);
        
        root.setTop(boxTop);
        root.setCenter(gridPaneMatrix);
        root.setBottom(boxBottom);
        root.getStyleClass().add("border-pane");
               
        Scene scene = new Scene(root, (primaryScreenBounds.getWidth() * 0.5625), (primaryScreenBounds.getHeight() * 0.867));

        primaryStage.setTitle("Game of Life");
        primaryStage.setScene(scene);      
        primaryStage.centerOnScreen();
        primaryStage.getIcons().addAll(new Image(getClass().getResourceAsStream("/resources/icon.png")));
        primaryStage.show();         
    }
    
    private void defineMatrix(int rows, int cols) {  
        
        matrix = new Label[rows][cols];
        
        for (int i = 0; i < matrix.length; i++) 
            for (int j = 0; j < matrix[i].length; j++) {
                
                matrix[i][j] = new Label();
                matrix[i][j].setAccessibleHelp(i+","+j);
                matrix[i][j].getStyleClass().add("classic-label");
                matrix[i][j].setStyle("-fx-background-color: "+colorDeath+";");
                matrix[i][j].setMinWidth(primaryScreenBounds.getWidth() * 0.0070);
                matrix[i][j].setMinHeight(primaryScreenBounds.getWidth() * 0.01);
                matrix[i][j].setMaxWidth(primaryScreenBounds.getWidth() * 0.0090);
                matrix[i][j].setMaxHeight(primaryScreenBounds.getWidth() * 0.001);
                matrix[i][j].setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        String[] coords = ((Label)event.getSource()).getAccessibleHelp().split(",");
                        
                        int x = calculator.modifyReplic(Integer.valueOf(coords[0]) + 1, Integer.valueOf(coords[1]) + 1);
                        ((Label)event.getSource()).setStyle("-fx-background-color: "+((x == 1) ? colorLife : colorDeath)+";");
                        calculator.modifyPopulation(x == 1);
                        updatePopulation();
                    }
                });
                
                gridPaneMatrix.add(matrix[i][j], i, j);
                
            }
    }
    
    private void updateGeneration() {
        labelGeneration.setText("Generation: "+calculator.getGeneration());
    }
    
    private void updatePopulation() {
        labelPopulation.setText("Population: "+calculator.getPopulation());
    }
    
    public static void main(String[] args) {
        Application.launch(args);
    }
}
