package com.core;

import javafx.scene.control.Label;

/**
 * This class manage all the rules of Conway's game
 */
public class GameRulesManager {

	private boolean[][] replica;

	private final String colorDeath;
	private final String colorLife;

	private long population;
	private long generation;

	private Label[][] matrix;

	public GameRulesManager(Label[][] matrix, String ColorDeath, String ColorLife) {
		this.matrix = matrix;
		this.colorLife = ColorLife;
		this.colorDeath = ColorDeath;
		this.population = 0;
		this.generation = 0;

		// This variable is initialized by adding 2 rows and 2 columns, thus the edges are not ignored
		this.replica = new boolean[matrix.length + 2][matrix[0].length + 2];
	}

	/**
	 * This method creates first a copy of the values (as boolean) of the matrix
	 * Then pass it as read-only to calculate the new matrix, the changes are made only on the replica
	 */
	public void iterateGeneration() {
		boolean[][] copy = new boolean[replica.length][];

		for (int i = 0; i < copy.length; i++)
			copy[i] = replica[i].clone();

		computeNewPattern(copy);
	}

	/**
	 * This method iterate each cell to calculate the new state (alive or dead)
	 * Note: I know it's too expensive but at the moment I haven't thought in a better approach ...
	 * @param copy copy of the replica, used as read-only because to write is used the replica ...
	 */
	private void computeNewPattern(boolean[][] copy) {
		for (int i = 1; i < copy.length - 1; i++)
			for (int j = 1; j < copy[i].length - 1; j++)
				evaluateCell(i, j, copy);
	}

	/**
	 * This method performs the GUI action to change the state of the cell
	 * @param row position in X axis
	 * @param col position in Y axis
	 * @param copy copy of the replica, it's used only to read
	 */
	private void evaluateCell(int row, int col, boolean[][] copy) {
		var cellState = getCellState(row, col, copy);

		if (cellState != copy[row][col]) {
			if (cellState) {
				population++;
				replica[row][col] = true;
				matrix[row - 1][col - 1].setStyle("-fx-background-color: " + this.colorLife + ";");
			} else {
				population--;
				replica[row][col] = false;
				matrix[row - 1][col - 1].setStyle("-fx-background-color: " + this.colorDeath + ";");
			}
		}
	}

	/**
	 * This method applies the rules of the game, please check those on Wikipedia to know more
	 * @param row position in X axis
	 * @param col position in Y axis
	 * @param copy copy of the replica, it's used only to read
	 * @return new state of the cell alive or keep alive (true) or dead (false)
	 */
	private boolean getCellState(int row, int col, boolean[][] copy) {
		var aliveCellsCount = 0;

		for (int i = row - 1; i < (row + 2); i++)
			for (int j = col - 1; j < (col + 2); j++)
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
	 * @param x position in X axis
	 * @param y position in Y axis
	 * @return new state of the cell to change in the GUI
	 */
	public boolean changeCellState(int x, int y) {
		replica[x][y] = !replica[x][y];

		return replica[x][y];
	}

	/**
	 * This method cleans the board and assign false to the matrix
	 */
	public void restoreGame() {
		if (population > 0) {
			for (int i = 0, c = 0; i < matrix.length && c < population; i++)
				for (int j = 0; j < matrix[i].length && c < population; j++)
					if (replica[i + 1][j + 1]) {
						c++;
						replica[i + 1][j + 1] = false;
						matrix[i][j].setStyle("-fx-background-color: " + this.colorDeath + ";");
					}
		}
	}

	/**
	 * This method reassign the JavaFX matrix and the replica with a new X, Y matrix dimension
	 * @param matrix JavaFX matrix value
	 */
	public void resizeMatrix(Label[][] matrix) {
		this.matrix = matrix;
		this.replica = new boolean[matrix.length + 2][matrix[0].length + 2];
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
