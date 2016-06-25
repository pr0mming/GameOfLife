
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

    private BorderPane Root;
    private GridPane Grid;
    private Label[][] Flat;
    private Calculate Calc;
    private Button ButtonStart, ButtonPause, ButtonStep, ButtonRestore;
    private String ColorLife, ColorDeath;
    private Label Generation, Population;
    private long Generation_Number;
    private Timeline Animation;
    private HBox BoxTop, BoxBottom;
    private ComboBox<String> Combo;
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        
        Root = new BorderPane();
        Root.getStylesheets().addAll(getClass().getResource("StyleGame.css").toExternalForm());
        ColorLife = "#39E600";
        ColorDeath = "#1A1A00";
        
        DefineLabels(60, 40);              
        DefineAnimation();      
        Calc = new Calculate(ColorDeath, ColorLife, Flat.length, Flat[0].length);      
        DefineButton(); 
        DefineComboBox();
        
        Root.setTop(BoxTop);
        Root.setCenter(Grid);
        Root.setBottom(BoxBottom);
        Root.getStyleClass().add("border-pane");
               
        Scene scene = new Scene(Root, 1050, 890);
        
        primaryStage.setTitle("Game of Life");
        primaryStage.setScene(scene);      
        primaryStage.centerOnScreen();
        primaryStage.getIcons().addAll(new Image(getClass().getResourceAsStream("/resources/icon.png")));
        primaryStage.show();         
    }
    
    private void DefineButton() {
        BoxTop = new HBox();
        BoxTop.setPadding(new Insets(30, 10, 10, 10));
        BoxTop.setSpacing(15);
        BoxTop.setAlignment(Pos.CENTER);
        BoxTop.setStyle("-fx-background-color: black;");
        
        ButtonStart = new Button("Start Game");
        ButtonStart.setPrefSize(145, 40);
        ButtonStart.getStyleClass().add("button");
        ButtonStart.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                if (Integer.valueOf(Calc.getPopulation()) > 0) {
                    Animation.play();
                    ((Button)e.getSource()).setDisable(true);
                    ButtonPause.setDisable(false);
                    ButtonStep.setDisable(true);
                    ButtonRestore.setDisable(true);
                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("The grid is empty");
                    alert.setHeaderText(null);
                    alert.setContentText("The game starts at least one living cell");
                    alert.showAndWait();
                }
            }
        });
        
        ButtonPause = new Button("Pause");
        ButtonPause.setDisable(true);
        ButtonPause.setPrefSize(145, 40);
        ButtonPause.getStyleClass().add("button");
        ButtonPause.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Animation.stop();
                ((Button) event.getSource()).setDisable(true);
                ButtonStep.setDisable(false);
                ButtonStart.setDisable(false);
                ButtonRestore.setDisable(false);
            }
        });
        
        ButtonStep = new Button("Step by step");
        ButtonStep.setDisable(true);
        ButtonStep.setPrefSize(145, 40);
        ButtonStep.getStyleClass().add("button");
        ButtonStep.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Flat = Calc.GeneratePattern(Flat);
            }
        });
        
        ButtonRestore = new Button("Restore");
        ButtonRestore.setDisable(false);
        ButtonRestore.setPrefSize(145, 40);
        ButtonRestore.getStyleClass().add("button");
        ButtonRestore.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {               
                Flat = Calc.RestoreGrid(Flat);
                Generation_Number = 0l;
                Calc.PopulationRestore();
                Generation.setText("Generation "+Generation_Number);
                Population.setText("Population "+Calc.getPopulation());
                ButtonStep.setDisable(true);
                ((Button)event.getSource()).setDisable(true);
            }
        });
        
        Generation_Number = 0l;
        Generation = new Label("Generation "+Generation_Number);
        Generation.setPrefSize(150, 40);
        Generation.getStyleClass().add("label-information");
        Population = new Label("Population "+Calc.getPopulation());
        Population.setPrefSize(160, 40);
        Population.getStyleClass().add("label-information");       
        
        BoxTop.getChildren().addAll(ButtonStart, ButtonPause, ButtonStep, ButtonRestore, Generation, Population);
    }
    
    private void DefineAnimation() {
        Animation = new Timeline(new KeyFrame(Duration.millis(70), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Flat = Calc.GeneratePattern(Flat);
                Generation_Number+=1;
                Generation.setText("Generation "+Generation_Number);
                Population.setText("Population "+Calc.getPopulation());
            }
        }));
        
        Animation.setCycleCount(Animation.INDEFINITE);
    }
    
    private void DefineComboBox() {
        BoxBottom = new HBox();
        BoxBottom.setPadding(new Insets(10, 10, 20, 10));
        BoxBottom.setSpacing(10);
        BoxBottom.setAlignment(Pos.BOTTOM_CENTER);
        BoxBottom.setStyle("-fx-background-color: black;");
        
        Combo = new ComboBox<String>();
        Combo.getItems().addAll("60x40", "55x35", "50x30", "45x25", "40x20", "35x20");      
        Combo.setValue("60x40");
        
        Label LabelDefineGrid = new Label("Redefine Grid: ");
        LabelDefineGrid.getStyleClass().add("label-information");
        
        Button AceptChanges = new Button("Redefine");
        AceptChanges.getStyleClass().add("button");
        AceptChanges.setPrefSize(140, 40);
        AceptChanges.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {               
                Root.getChildren().remove(Grid);
                String[] coordinates = Combo.getValue().split("x");
                DefineLabels(Integer.valueOf(coordinates[0]), Integer.valueOf(coordinates[1]));
                Calc.RedefineReplic(Flat.length, Flat[0].length);
                Root.setCenter(Grid);
            }
        });
        
        BoxBottom.getChildren().addAll(LabelDefineGrid, Combo, AceptChanges);
    }
    
    private void DefineLabels(int rows, int cols) {             
        
        Grid = new GridPane();
        Grid.setPadding(new Insets(5, 20, 5, 20));
        Grid.setAlignment(Pos.CENTER);
        Grid.setHgap(0);
        Grid.setVgap(0); 
        
        Flat = new Label[rows][cols];
        for (int i = 0; i < Flat.length; i++) {
            for (int j = 0; j < Flat[i].length; j++) {
                Flat[i][j] = new Label();
                Flat[i][j].getStyleClass().add("label");
                Flat[i][j].setStyle("-fx-background-color: "+ColorDeath+";");
                Flat[i][j].setPrefSize(16, 16);
                Flat[i][j].setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        boolean status = Calc.getColorLabel(((Label)event.getSource()).getStyle()).equals(ColorDeath);
                        ((Label)event.getSource()).setStyle("-fx-background-color: "+((status)?ColorLife:ColorDeath)+";");
                        Calc.PopulationIncrease(status);
                        Population.setText("Population "+Calc.getPopulation());
                    }
                });
                Grid.add(Flat[i][j], i, j);
            }
        }     
    }
    
    public static void main(String[] args) {
        Application.launch(args);
    }
}
