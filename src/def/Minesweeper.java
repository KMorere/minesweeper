package def;

import java.util.Random;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;

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
		if (cells == 0)
			System.out.println("Finished.");
		displayGrid();
	}
	
	/**
	 * Select a cell in the grid and see if there's a bomb or not.
	 * @param input Input of the user.
	 */
	private static void guess(String input) {
		int yPos = splitCoordinates(input)[0], xPos = splitCoordinates(input)[1];
		
		if (isValidCoordinates(yPos, xPos)) {
			switch (grid[yPos][xPos]) {
			case EMPTY:
				if (getCount(input) == 0) {
					chainDiscover(input);
				}
				//cells--;
				break;
			case BOMB:
				isGameOver = true;
				cells = 0;
				break;
			default:
				break;
			}
		}
		else
			tryInput();
	}
	
	/**
	 * Reveal all the empty tiles in a chain.
	 */
	private static ArrayList<String> empty = new ArrayList<String>();
	private static void chainDiscover(String input) {
		int y = splitCoordinates(input)[0]-1, x = splitCoordinates(input)[1]-1;
		
		for (int i = 1; i <= 9; i++) {
			if (isValidCoordinates(y, x)) {
				if (userGrid[y][x] == " " && !empty.contains(""+y + " "+x)) {
					empty.add(""+y + " "+x);
					userGrid[y][x] = ""+getCount(""+(y+1) + " "+(x+1));
					chainDiscover(""+(y+1) + " "+(x+1));
					cells--;
				}
			}
			
			if (i > 0 && i%3 == 0) {
				y++;
				x -= 3;
			}

			x++;
		}
	}
	
	/**
	 * Get the number of bombs around the user's input.
	 * @param pos The user's coordinates.
	 * @return Return the amount of bombs around.
	 */
	private static int getCount(String pos) {
		int count = 0;
		int y = splitCoordinates(pos)[0]-1, x = splitCoordinates(pos)[1]-1;
		
		for (int i = 1; i <= 9; i++) {
			if (isValidCoordinates(y, x)) { // Out of range verification.
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

		y = splitCoordinates(pos)[0];
		x = splitCoordinates(pos)[1];
		if (isValidCoordinates(y, x))
			userGrid[y][x] = ""+count;
		return count;
	}
	
	/**
	 * Split the given coordinates in two.
	 * @param input
	 * @return
	 */
	private static int[] splitCoordinates(String input) {
		String[] newPos = input.split(" ");
		return new int[] {Integer.parseInt(newPos[0])-1, Integer.parseInt(newPos[1])-1};
	}
	
	private static boolean isValidCoordinates(int y, int x) {
		return (y >= 0 && y < col) && (x >= 0 && x < row);
	}
	
	/**
	 * Displays the grid with the data of each cell.
	 */
	private static void displayGrid() {
		for (int y = -1; y < col; y++) {
			if (y >= 0) {
				System.out.print("[");
				System.out.printf("%2s", (y+1));
				System.out.print("] ");
			}
			else if (y < 0) {
				System.out.print("     ");
			}
			for (int x = 0; x < row; x++) {
				if (y >= 0) {
					System.out.print("[");
					if (isGameOver) // Display if game is finished.
						System.out.print(grid[y][x].getValue());
					else
						System.out.print(userGrid[y][x]);
					System.out.print("] ");
				}
				else {
					System.out.print("[");
					System.out.print((x+1));
					System.out.print("] ");
				}
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
