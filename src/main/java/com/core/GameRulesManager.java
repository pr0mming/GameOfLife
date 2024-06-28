package com.core;

import com.core.types.Point;
import javafx.scene.control.Label;

import java.util.HashSet;

/**
 * This class manage all the rules of Conway's game
 */
public class GameRulesManager {

    private final String deadCellColor;
    private final String aliveCellColor;

    private long population;
    private long generation;

    private Label[][] matrix;
    private HashSet<Point> aliveCells;
    private boolean[][] replica;

    public GameRulesManager(Label[][] matrix, String deadCellColor, String aliveCellColor) {
        this.deadCellColor = deadCellColor;
        this.aliveCellColor = aliveCellColor;

        this.population = 0;
        this.generation = 0;

        this.matrix = matrix;
        this.aliveCells = new HashSet<>();

        // This matrix is useful to access easily to the state of each cell
        this.replica = new boolean[matrix.length][matrix[0].length];
    }

    /**
     * This method creates first a copy of the values (as boolean) of the matrix
     * Then pass it as read-only to calculate the new matrix, the changes are made only on the replica
     * Finally, iterate each alive cell (and the nearest ones) to calculate the new state (alive or dead)
     */
    public void iterateGeneration() {
        boolean[][] copy = getCopyFromReplica();
        HashSet<Point> aliveCellsTmp = new HashSet<>();

        for (Point aliveCell : aliveCells) {
            var row = aliveCell.x;
            var col = aliveCell.y;

            // Extra validation to avoid out of range exception in array
            if (row - 1 < 0) row++;
            if (col - 1 < 0) col++;

            for (int i = row - 1; i < (row + 2) && i < copy.length; i++) {
                for (int j = col - 1; j < (col + 2) && j < copy[i].length; j++) {
                    var cellState = evaluateCell(i, j, copy);

                    // Keep only alive cells, the HashSet avoid duplicate values ...
                    if (cellState) {
                        aliveCellsTmp.add(new Point(i, j));
                    }
                }
            }
        }

        // Save new pattern with alive cells
        aliveCells = aliveCellsTmp;

        population = aliveCells.size();
    }

    private boolean[][] getCopyFromReplica() {
        boolean[][] copy = new boolean[replica.length][];

        for (int i = 0; i < copy.length; i++)
            copy[i] = replica[i].clone();

        return copy;
    }

    /**
     * This method performs the GUI action to change the state of the cell
     *
     * @param row  position in X axis
     * @param col  position in Y axis
     * @param copy copy of the replica, it's used only to read
     */
    private boolean evaluateCell(int row, int col, boolean[][] copy) {
        var cellState = getCellState(row, col, copy);

        if (cellState != copy[row][col]) {
            if (cellState) {
                replica[row][col] = true;
                matrix[row][col].setStyle("-fx-background-color: " + this.aliveCellColor + ";");
            } else {
                replica[row][col] = false;
                matrix[row][col].setStyle("-fx-background-color: " + this.deadCellColor + ";");
            }
        }

        return cellState;
    }

    /**
     * This method applies the rules of the game, please check those on Wikipedia to know more
     *
     * @param row  position in X axis
     * @param col  position in Y axis
     * @param copy copy of the replica, it's used only to read
     * @return new state of the cell alive or keep alive (true) or dead (false)
     */
    private boolean getCellState(int row, int col, boolean[][] copy) {
        var aliveCellsCount = 0;

        if (row - 1 < 0) row++;
        if (col - 1 < 0) col++;

        for (int i = row - 1; i < (row + 2) && i < copy.length; i++)
            for (int j = col - 1; j < (col + 2) && j < copy[i].length; j++)
                if (copy[i][j]) {
                    aliveCellsCount++;
                }

        var isCellAlive = copy[row][col];

        if (isCellAlive) {
            // Exclude the cell of the center because isn't necessary for the game's rules
            aliveCellsCount--;

            // Keep cell alive (true) or dead (false)?
            return aliveCellsCount == 2 || aliveCellsCount == 3;
        }

        // A new alive cell (true) or not (false)?
        return aliveCellsCount == 3;
    }

    /**
     * This method convert an alive cell (true) to a dead cell (false) and vice versa
     *
     * @param x position in X axis
     * @param y position in Y axis
     * @return new state of the cell to change in the GUI
     */
    public boolean changeCellState(int x, int y) {
        replica[x][y] = !replica[x][y];

        if (replica[x][y]) {
            aliveCells.add(new Point(x, y));
        } else {
            aliveCells.remove(new Point(x, y));
        }

        return replica[x][y];
    }

    /**
     * This method cleans the board and assign false to the matrix
     */
    public void restoreGame() {
        for (Point aliveCell : aliveCells) {
            var x = aliveCell.x;
            var y = aliveCell.y;

            replica[x][y] = false;
            matrix[x][y].setStyle("-fx-background-color: " + this.deadCellColor + ";");
        }
    }

    /**
     * This method reassign the JavaFX matrix and the replica with a new X, Y matrix dimension
     *
     * @param matrix JavaFX matrix value
     */
    public void resizeMatrix(Label[][] matrix) {
        this.matrix = matrix;
        this.replica = new boolean[matrix.length][matrix[0].length];
    }

    public long getPopulation() {
        return population;
    }

    public void setPopulation(long population) {
        this.population = population;
    }

    public long getGeneration() {
        return generation;
    }

    public void setGeneration(long generation) {
        this.generation = generation;
    }
}
