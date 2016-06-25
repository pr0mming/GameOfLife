
package game;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
    private Button ButtonStart, ButtonStop;
    private String ColorLife, ColorDeath;
    private Label Generation, Poblation;
    private long Generation_Number;
    private Timeline Animation;
    private HBox Box;
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        
        Root = new BorderPane();
        Root.getStylesheets().addAll(getClass().getResource("StyleGame.css").toExternalForm());
        Group group = new Group();           
        
        GenerateLabels();              
        DefineAnimation();      
        Calc = new Calculate(ColorDeath, ColorLife, Flat.length, Flat[0].length);      
        DefineButton();                       
        
        group.getChildren().addAll(Grid);        
        Root.setTop(Box);
        Root.setCenter(group);       
        Root.getStyleClass().add("stack-pane");
               
        Scene scene = new Scene(Root, 1000, 750);
        
        primaryStage.setTitle("Game of Life");
        primaryStage.setScene(scene);      
        primaryStage.centerOnScreen();
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/resources/icon.png")));
        primaryStage.show();         
    }
    
    private void DefineButton() {
        Box = new HBox();
        Box.setPadding(new Insets(30, 10, 10, 10));
        Box.setSpacing(20);
        Box.setAlignment(Pos.CENTER);
        Box.setStyle("-fx-background-color: black;");
        
        ButtonStart = new Button("Start Game");
        ButtonStart.setPrefSize(150, 40);
        ButtonStart.getStyleClass().add("button");
        ButtonStart.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                Animation.play();
                ((Button)e.getSource()).setDisable(true);
                ButtonStop.setDisable(false);
            }
        });
        
        ButtonStop = new Button("Pause");
        ButtonStop.setDisable(true);
        ButtonStop.setPrefSize(150, 40);
        ButtonStop.getStyleClass().add("button");
        ButtonStop.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Animation.stop();
                ((Button) event.getSource()).setDisable(true);
                ButtonStart.setDisable(false);
            }
        });
        
        Generation_Number = 0l;
        Generation = new Label("Generation "+Generation_Number);
        Generation.setPrefSize(150, 40);
        Generation.getStyleClass().add("label-information");
        Poblation = new Label("Population "+Calc.getPopulation());
        Poblation.setPrefSize(160, 40);
        Poblation.getStyleClass().add("label-information");       
        
        Box.getChildren().addAll(ButtonStart, ButtonStop, Generation, Poblation);
    }
    
    private void DefineAnimation() {
        Animation = new Timeline(new KeyFrame(Duration.millis(80), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Flat = Calc.GeneratePattern(Flat);
                Generation_Number+=1;
                Generation.setText("Generation "+Generation_Number);
                Poblation.setText("Population "+Calc.getPopulation());
            }
        }));
        
        Animation.setCycleCount(Animation.INDEFINITE);
    }
    
    private void GenerateLabels() {
        
        ColorLife = "#39E600";
        ColorDeath = "#1A1A00";
        
        Grid = new GridPane();
        Grid.setPadding(new Insets(5, 20, 20, 10));
        Grid.setHgap(0);
        Grid.setVgap(0); 
        
        Flat = new Label[50][35];
        for (int i = 0; i < Flat.length; i++) {
            for (int j = 0; j < Flat[i].length; j++) {
                Flat[i][j] = new Label();
                Flat[i][j].getStyleClass().add("label");
                Flat[i][j].setStyle("-fx-background-color: "+ColorDeath+";");
                Flat[i][j].setPrefSize(18, 18);
                Flat[i][j].setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        boolean status = Calc.getColorLabel(((Label)event.getSource()).getStyle()).equals(ColorDeath);
                        ((Label)event.getSource()).setStyle("-fx-background-color: "+((status)?ColorLife:ColorDeath)+";");
                        Calc.setPopulation(status);
                        Poblation.setText("Population "+Calc.getPopulation());
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
