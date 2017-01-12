
package game;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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

    private BorderPane root;
    private GridPane grid;
    private Label[][] labelGrid;
    private Calculate calc;
    private Button buttonStart, buttonPause, buttonStep, buttonRestore;
    private String colorLife, colorDeath;
    private Label labelGeneration, labelPopulation;
    private long generation;
    private Timeline animation;
    private HBox boxTop, boxBottom;
    private ComboBox<String> comboBox;
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        
        root = new BorderPane();
        root.getStylesheets().addAll(getClass().getResource("StyleGame.css").toExternalForm());
        colorLife = "#39E600";
        colorDeath = "#1A1A00";
        
        defineLabels(60, 30);              
        defineAnimation();      
        calc = new Calculate(colorDeath, colorLife, labelGrid.length, labelGrid[0].length);      
        defineButtons(); 
        defineComboBox();
        
        root.setTop(boxTop);
        root.setCenter(grid);
        root.setBottom(boxBottom);
        root.getStyleClass().add("border-pane");
               
        Scene scene = new Scene(root, 1050, 890);
        
        primaryStage.setTitle("Game of Life");
        primaryStage.setScene(scene);      
        primaryStage.centerOnScreen();
        primaryStage.getIcons().addAll(new Image(getClass().getResourceAsStream("/resources/icon.png")));
        primaryStage.show();         
    }
    
    private void defineButtons() {
        boxTop = new HBox();
        boxTop.setPadding(new Insets(30, 10, 10, 10));
        boxTop.setSpacing(15);
        boxTop.setAlignment(Pos.CENTER);
        boxTop.setStyle("-fx-background-color: black;");
        
        buttonStart = new Button("Start Game");
        buttonStart.setPrefSize(145, 40);
        buttonStart.getStyleClass().add("button");
        buttonStart.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                if (Integer.valueOf(calc.getPopulation()) > 0) {
                    animation.play();
                    ((Button)e.getSource()).setDisable(true);
                    buttonPause.setDisable(false);
                    buttonStep.setDisable(true);
                    buttonRestore.setDisable(true);
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
        buttonPause.setPrefSize(145, 40);
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
        buttonStep.setDisable(true);
        buttonStep.setPrefSize(145, 40);
        buttonStep.getStyleClass().add("button");
        buttonStep.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                labelGrid = calc.generatePattern(labelGrid);
            }
        });
        
        buttonRestore = new Button("Restore");
        buttonRestore.setDisable(false);
        buttonRestore.setPrefSize(145, 40);
        buttonRestore.getStyleClass().add("button");
        buttonRestore.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {               
                labelGrid = calc.restoreGrid(labelGrid);
                generation = 0l;
                calc.restorePopulation();
                labelGeneration.setText("Generation "+generation);
                labelPopulation.setText("Population "+calc.getPopulation());
                buttonStep.setDisable(true);
                ((Button)event.getSource()).setDisable(true);
            }
        });
        
        generation = 0l;
        labelGeneration = new Label("Generation "+generation);
        labelGeneration.setPrefSize(150, 40);
        labelGeneration.getStyleClass().add("label-information");
        labelPopulation = new Label("Population "+calc.getPopulation());
        labelPopulation.setPrefSize(160, 40);
        labelPopulation.getStyleClass().add("label-information");       
        
        boxTop.getChildren().addAll(buttonStart, buttonPause, buttonStep, buttonRestore, labelGeneration, labelPopulation);
    }
    
    private void defineAnimation() {
        animation = new Timeline(new KeyFrame(Duration.millis(70), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                labelGrid = calc.generatePattern(labelGrid);
                generation+=1;
                labelGeneration.setText("Generation "+generation);
                labelPopulation.setText("Population "+calc.getPopulation());
            }
        }));
        
        animation.setCycleCount(animation.INDEFINITE);
    }
    
    private void defineComboBox() {
        boxBottom = new HBox();
        boxBottom.setPadding(new Insets(10, 10, 20, 10));
        boxBottom.setSpacing(10);
        boxBottom.setAlignment(Pos.BOTTOM_CENTER);
        boxBottom.setStyle("-fx-background-color: black;");
        
        comboBox = new ComboBox<String>();
        comboBox.getItems().addAll("60x30", "55x30", "50x25", "45x25", "40x20", "35x20");      
        comboBox.setValue("60x30");
        
        Label LabelDefineGrid = new Label("Redefine Grid: ");
        LabelDefineGrid.getStyleClass().add("label-information");
        
        Button AceptChanges = new Button("Redefine");
        AceptChanges.getStyleClass().add("button");
        AceptChanges.setPrefSize(140, 40);
        AceptChanges.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {               
                root.getChildren().remove(grid);
                String[] coordinates = comboBox.getValue().split("x");
                defineLabels(Integer.valueOf(coordinates[0]), Integer.valueOf(coordinates[1]));
                calc.redefineReplic(labelGrid.length, labelGrid[0].length);
                root.setCenter(grid);
            }
        });
        
        boxBottom.getChildren().addAll(LabelDefineGrid, comboBox, AceptChanges);
    }
    
    private void defineLabels(int rows, int cols) {             
        
        grid = new GridPane();
        grid.setPadding(new Insets(5, 20, 5, 20));
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(0);
        grid.setVgap(0); 
        
        labelGrid = new Label[rows][cols];
        for (int i = 0; i < labelGrid.length; i++) {
            for (int j = 0; j < labelGrid[i].length; j++) {
                labelGrid[i][j] = new Label();
                labelGrid[i][j].getStyleClass().add("label");
                labelGrid[i][j].setStyle("-fx-background-color: "+colorDeath+";");
                labelGrid[i][j].setPrefSize(15, 15);
                labelGrid[i][j].setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        boolean status = calc.getColorLabel(((Label)event.getSource()).getStyle()).equals(colorDeath);
                        ((Label)event.getSource()).setStyle("-fx-background-color: "+((status)?colorLife:colorDeath)+";");
                        calc.changePopulation(status);
                        labelPopulation.setText("Population "+calc.getPopulation());
                    }
                });
                grid.add(labelGrid[i][j], i, j);
            }
        }     
    }
    
    public static void main(String[] args) {
        Application.launch(args);
    }
}
