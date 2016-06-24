
package game;

import javafx.scene.control.Label;

/**
 *
 * @author pr0mming
 */

public class Calculate {
    
    private int[][] Replic;
    private String ColorDeath, ColorLife;
    private long poblation;
    
    public Calculate(String ColorDeath, String ColorLife, int rows, int cols) {       
        this.ColorLife = ColorLife;
        this.ColorDeath = ColorDeath;
        this.Replic = new int[rows+2][cols+2];
    }
    
    private void GenerateReplic(Label[][] l) {
        for (int i = 0; i < Replic.length; i++) 
            for (int j = 0; j < Replic[i].length; j++) 
                if (i > 0 && i < Replic.length-1 && j > 0 && j < Replic[i].length-1) 
                    Replic[i][j] = ((getColorLabel(l[i-1][j-1].getStyle())).equals(ColorLife)?1:0);
                            
    }
    
    public Label[][] GeneratePattern(Label[][] label) {
        GenerateReplic(label);
        for (int i = 1; i < Replic.length-1; i++) 
            for (int j = 1; j < Replic[i].length-1; j++) 
                EvaluateCell(i, j, Replic, label);              
                   
        return label;
    }
    
    private void EvaluateCell(int row, int col, int[][] replic, Label[][] label) {
        int count = 0;
        for (int i = row-1; i < (row+2); i++) 
            for (int j = col-1; j < (col+2); j++) 
                if (replic[i][j] == 1 && (i != row || j != col)) 
                    count+=1;             
        
        if ((count < 2 && replic[row][col] == 1) || (count > 3 && replic[row][col] == 1)) {
            poblation-=1;
            label[row-1][col-1].setStyle("-fx-background-color: "+this.ColorDeath+";");
        } else 
            if (count == 3 && replic[row][col] == 0) {
                poblation+=1;
                label[row-1][col-1].setStyle("-fx-background-color: "+this.ColorLife+";");                
            }
    }
    
    public String getColorLabel(String style) {
        String[] s = style.split(";");
        for (int i = 0; i < s.length; i++) 
            if (s[i].contains("-fx-background-color:")) 
                return s[i].substring(s[i].indexOf(":")+1, s[i].length()).replaceAll(" ", ""); 
            
        return "Not Found";
    }
    
    public void setPoblation(boolean r) {
        poblation += (r)?1:-1;
    }
    
    public String getPoblation() {
        return String.valueOf(poblation);
    }
}
