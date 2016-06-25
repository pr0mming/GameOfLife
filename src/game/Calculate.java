
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
    
    private int[][] Replic;
    private String ColorDeath, ColorLife;
    private long Population;
    
    public Calculate(String ColorDeath, String ColorLife, int rows, int cols) {       
        this.ColorLife = ColorLife;
        this.ColorDeath = ColorDeath;
        //This variable is initialized by adding 2 rows and 2 columns, thus the edges are not ignored
        this.Replic = new int[rows+2][cols+2];
    }
    /*        
        1 = cell alive
        0 = cell dead

        A two-dimensional matrix having reference pattern exists in the two-dimensional 
        array is recreated Labels. The first problem I had was to ignore the 
        calculation of the edges, this is a solution to this problem.
    */
    private void GenerateReplic(Label[][] l) {
        for (int i = 0; i < Replic.length; i++) 
            for (int j = 0; j < Replic[i].length; j++) 
                if (i > 0 && i < Replic.length-1 && j > 0 && j < Replic[i].length-1) 
                    Replic[i][j] = ((getColorLabel(l[i-1][j-1].getStyle())).equals(ColorLife)?1:0);
                            
    }
    //This method returns the new pattern.
    public Label[][] GeneratePattern(Label[][] label) {
        GenerateReplic(label);
        for (int i = 1; i < Replic.length-1; i++) 
            for (int j = 1; j < Replic[i].length-1; j++) 
                EvaluateCell(i, j, Replic, label);              
                   
        return label;
    }
    /*   
        This method receives a block (coordinates) and evaluates the block around them. 
        From the counter it is determined whether the block is still alive, 
        dies or not their status is altered ...
    */
    private void EvaluateCell(int row, int col, int[][] replic, Label[][] label) {
        int count = 0;
        for (int i = row-1; i < (row+2); i++) 
            for (int j = col-1; j < (col+2); j++) 
                if (replic[i][j] == 1 && (i != row || j != col)) 
                    count+=1;             
        
        if ((count < 2 && replic[row][col] == 1) || (count > 3 && replic[row][col] == 1)) {
            Population-=1;
            label[row-1][col-1].setStyle("-fx-background-color: "+this.ColorDeath+";");
        } else 
            if (count == 3 && replic[row][col] == 0) {
                Population+=1;
                label[row-1][col-1].setStyle("-fx-background-color: "+this.ColorLife+";");                
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
    public void setPopulation(boolean r) {
        Population += (r)?1:-1;
    }
    
    public String getPopulation() {
        return String.valueOf(Population);
    }
}
