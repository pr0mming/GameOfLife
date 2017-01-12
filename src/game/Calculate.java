
package game;

import javafx.scene.control.Label;

/**
 *
 * @author pr0mming
 * 
 * How few simple rules can determine such complex things?
 * 
 * GitHub: https://github.com/pr0mming
 */


public class Calculate {
    
    private int[][] replic;
    private String colorDeath, colorLife;
    private long population;
    
    public Calculate(String ColorDeath, String ColorLife, int rows, int cols) {       
        this.colorLife = ColorLife;
        this.colorDeath = ColorDeath;
        //This variable is initialized by adding 2 rows and 2 columns, thus the edges are not ignored
        this.replic = new int[rows+2][cols+2];
    }
    /*        
        1 = cell alive
        0 = cell dead

        A two-dimensional matrix having reference pattern exists in the two-dimensional 
        array is recreated Labels. The first problem I had was to ignore the 
        calculation of the edges, this is a solution to this problem.
    */
    private void generateReplic(Label[][] l) {
        for (int i = 0; i < replic.length; i++) 
            for (int j = 0; j < replic[i].length; j++) 
                if (i > 0 && i < replic.length-1 && j > 0 && j < replic[i].length-1) 
                    replic[i][j] = ((getColorLabel(l[i-1][j-1].getStyle())).equals(colorLife)?1:0);
                            
    }
    //This method returns the new pattern.
    public Label[][] generatePattern(Label[][] label) {
        generateReplic(label);
        for (int i = 1; i < replic.length-1; i++) 
            for (int j = 1; j < replic[i].length-1; j++) 
                evaluateCell(i, j, replic, label);              
                   
        return label;
    }
    /*   
        This method receives a block (coordinates) and evaluates the block around them. 
        From the counter it is determined whether the block is still alive, 
        dies or not their status is altered ...
    */
    private void evaluateCell(int row, int col, int[][] replic, Label[][] label) {
        int count = 0;
        for (int i = row-1; i < (row+2); i++) 
            for (int j = col-1; j < (col+2); j++) 
                if (replic[i][j] == 1 && (i != row || j != col)) 
                    count+=1;             
        
        if ((count < 2 && replic[row][col] == 1) || (count > 3 && replic[row][col] == 1)) {
            population-=1;
            label[row-1][col-1].setStyle("-fx-background-color: "+this.colorDeath+";");
        } else 
            if (count == 3 && replic[row][col] == 0) {
                population+=1;
                label[row-1][col-1].setStyle("-fx-background-color: "+this.colorLife+";");                
            }
    }
    /*       
       This method returns the color of the Label. 
       This is done from CSS, so far not found a lighter way as it is in Swing
    */
    public String getColorLabel(String style) {
        String[] s = style.split(";");
        for (int i = 0; i < s.length; i++) 
            if (s[i].contains("-fx-background-color:")) 
                return s[i].substring(s[i].indexOf(":")+1, s[i].length()).replaceAll(" ", ""); 
            
        return "No Color";
    }
    /*        
        "r" is a variable that evaluates whether the block is dead the population is 
        subtracted or otherwise adds. This has more to do in the event that the game itself ...
    */
    public void changePopulation(boolean r) {
        population += (r) ? 1 : -1;
    }
    
    public void restorePopulation() {
        population = 0l;
    }
    //Clean the grid
    public Label[][] restoreGrid(Label[][] l){
        if (population > 0) {
            for (int i = 0, c = 0; i < l.length && c < population; i++) {
                for (int j = 0; j < l[i].length && c < population; j++) {
                    if (getColorLabel(l[i][j].getStyle()).equals(colorLife)) {
                        c++;
                        l[i][j].setStyle("-fx-background-color: "+this.colorDeath+";");
                    }
                }
            }
        }
        return l;
    }
    
    public void redefineReplic(int rows, int cols) {
        this.replic = new int[rows+2][cols+2];
    }
    
    public String getPopulation() {
        return String.valueOf(population);
    }
}
