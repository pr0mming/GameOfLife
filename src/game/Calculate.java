
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
    private long population, generation;
    private Label[][] matrix;
    
    public Calculate(Label[][] matrix, String ColorDeath, String ColorLife) {       
        this.matrix = matrix;
        this.colorLife = ColorLife;
        this.colorDeath = ColorDeath;
        this.population = 0l;
        this.generation = 0l;
        //This variable is initialized by adding 2 rows and 2 columns, thus the edges are not ignored
        this.replic = new int[matrix.length + 2][matrix[0].length + 2];
    }
    
    /*        
        1 = cell alive
        0 = cell dead

        This method clones the array, however clone () does not clone itself 
        into all elements of the array so I have had to do it manually. 
    */
    private int[][] copyReplic() {
        int[][] copy = new int[replic.length][];
        
        for (int i = 0; i < copy.length; i++) 
            copy[i] = replic[i].clone();       
        
        return copy;
    }
    
    public int modifyReplic(int x, int y) {
        replic[x][y] = (replic[x][y] == 0) ? 1 : 0;
        
        return replic[x][y];
    }
    
    //This method returns the new pattern.
    public void createPattern() {
        evaluateReplic(copyReplic());
    }
    
    private void evaluateReplic(int[][] copy) {
        for (int i = 1; i < copy.length-1; i++) 
            for (int j = 1; j < copy[i].length-1; j++) 
                evaluateCell(i, j, copy);  
    }
    /*   
        This method receives a block (coordinates) and evaluates the block around them. 
        From the counter it is determined whether the block is still alive, 
        dies or not their status is altered ...
    */
    private void evaluateCell(int row, int col, int[][] copy) {
        int count = 0;
        
        for (int i = row - 1; i < (row + 2); i++) 
            for (int j = col - 1; j < (col + 2); j++) 
                if (copy[i][j] == 1 && (i != row || j != col)) 
                    count+=1;             
        
        if ((count < 2 && copy[row][col] == 1) || (count > 3 && copy[row][col] == 1)) {
            population -= 1;
            replic[row][col] = 0;
            matrix[row - 1][col - 1].setStyle("-fx-background-color: "+this.colorDeath+";");
        } else 
            if (count == 3 && copy[row][col] == 0) {
                population += 1;
                replic[row][col] = 1;
                matrix[row - 1][col - 1].setStyle("-fx-background-color: "+this.colorLife+";");                
            }
    }
    
    //Clean the grid
    public void restoreMatrix(){
        if (population > 0) {
            for (int i = 0, c = 0; i < matrix.length && c < population; i++) 
                for (int j = 0; j < matrix[i].length && c < population; j++) 
                    if (replic[i + 1][j + 1] == 1) {
                        c++;
                        replic[i + 1][j + 1] = 0;
                        matrix[i][j].setStyle("-fx-background-color: "+this.colorDeath+";");
                    }                        
        }
    }
    
    public void redefineReplic(int rows, int cols) {
        this.replic = new int[rows+2][cols+2];
    }
    
    /*        
        "r" is a variable that evaluates whether the block is dead the population is 
        subtracted or otherwise adds. This has more to do in the event that the game itself ...
    */
    public void modifyPopulation(boolean r) {
        population += (r) ? 1 : -1;
    }
    
    public void increaseGeneration(int x) {
        generation += x;
    }
    
    public String getPopulation() {
        return String.valueOf(population);
    }
    
    public String getGeneration() {
        return String.valueOf(generation);
    }
    
    public void setGeneration(long x) {
        generation = x;
    }
    
    public void setPopulation(long x) {
        population = x;
    }
}
