package def;

import java.util.Random;
import java.util.Scanner;
import java.util.ArrayList;

public class Minesweeper {
	private enum CellType {
		EMPTY(0),
		BOMB(1),
		HIT(2);
		
		private final int value;
		CellType(int value) {
			this.value = value;
		}
		
		public int getValue() { return value; }
	}
	
	private static final int row = 6;
	private static final int col = 12;
	
	private static int bombsAmount = 9;
	private static ArrayList<String> bombPos = new ArrayList<String>();
	
	private static Scanner scan = new Scanner(System.in);
	private static String[][] userGrid = new String[col][row];
	private static CellType[][] grid = generateGrid();
	
	private static boolean isGameOver = false;
	private static int cells = col * row - bombsAmount;
	
	public static void main(String[] args) {
		tryInput();
	}
	
	/**
	 * Check if the input is a valid number.
	 */
	private static void tryInput() {
		while (cells > 0) {
			displayGrid();
			System.out.printf("Cells left : %s | Bombs : %s", cells, bombsAmount);
			System.out.println();
			
			if (scan.hasNextInt()) {
				String input = scan.nextLine();
				
				guess(input);
			}
			else {
				System.out.println("Please type valid numbers.");
			}
		}
	}
	
	private static void guess(String input) {
		String[] newPos = input.split(" ");
		int xPos = Integer.parseInt(newPos[1])-1;
		int yPos = Integer.parseInt(newPos[0])-1;
		
		if ((yPos >= 0 && yPos < col) && (xPos >= 0 && xPos < row)) {
			switch (grid[yPos][xPos]) {
			case EMPTY:
				getCount(input);
				cells--;
				break;
			case BOMB:
				isGameOver = true;
				cells = 0;
				break;
			case HIT:
				break;
			default:
				break;
			}
		}
		else
			tryInput();
	}
	
	/**
	 * Get the number of bombs around the user's input.
	 * @param pos The user's coordinates.
	 * @return Return the amount of bombs around.
	 */
	private static int getCount(String pos) {
		int count = 0;
		String[] newPos = pos.split(" ");
		int x = Integer.parseInt(newPos[1])-1, y = Integer.parseInt(newPos[0])-1;
		
		for (int i = 1; i <= 9; i++) {
			if ((y >= 0 && y < col) && (x >= 0 && x < row)) { // Out of range verification.
				if (grid[y][x] == CellType.BOMB) { // If there's a bomb increase the count.
					count++;
				}
			}
			
			if (i > 0 && i%3 == 0) { // Reset x and increment a column.
				y++;
				x -= 3;
			}

			x++;
		}
		
		userGrid[Integer.parseInt(newPos[0])][Integer.parseInt(newPos[1])] = ""+count;
		return count;
	}
	
	/**
	 * Displays the grid with the data of each cell.
	 */
	private static void displayGrid() {
		for (int y = 0; y < col; y++) {
			for (int x = 0; x < row; x++) {
				System.out.print("[");
				if (isGameOver) // Display if game is finished.
					System.out.print(grid[y][x].getValue());
				else
					System.out.print(userGrid[y][x]);
				System.out.print("] ");
			}
			System.out.println();
		}
	}
	
	/**
	 * Generate a 2d grid of col and row size and placing the bombs.
	 * @return The generated grid.
	 */
	private static CellType[][] generateGrid() {
		CellType[][] newGrid = new CellType[col][row];
		int bombs = bombsAmount;
		Random random = new Random();
		
		for (int y = 0; y < col; y++) {
			for (int x = 0; x < row; x++) {
				newGrid[y][x] = CellType.EMPTY;
				userGrid[y][x] = " ";
			}
		}
		
		while (bombs > 0) {
			int rY = random.nextInt(col);
			int rX = random.nextInt(row);
			
			if (newGrid[rY][rX] == CellType.EMPTY) {
				bombPos.add(rY+" "+rX);
				newGrid[rY][rX] = CellType.BOMB;
				bombs--;
			}
		}
		
		return newGrid;
	}
}
