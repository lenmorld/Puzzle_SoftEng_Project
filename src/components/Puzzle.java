package components;

/**
 * 
 * Puzzle - puzzle data
 * Board - GUI
 * 
 * Puzzle Class
 * -includes numerical data of each puzzle
 *  both used by PuzzleMaker and PuzzleSolver
 * 
 * puzzle represents the puzzle solutions and hints as 2D boolean arrays
 * 
 * 
 * @author team t
 *
 */
public class Puzzle {
	
	private boolean[][] puzzleSolution;		//puzzle array
	private boolean[][] puzzleHints;		//puzzle hints
	
	private String[] headerLeft;		//left and top headers
	private String[] headerTop;		
	
	//US4	-	row and column priorities
	private int[] rowPriorities;
	private int[] colPriorities;

	/**
	 * puzzle maker - new puzzle
	 * will be called by Puzzle Maker
	 * 
	 * @param rows
	 * @param cols
	 */
	public Puzzle(int rows, int cols) {

		// init. puzzle data arrays based on given rows and cols
		puzzleSolution = new boolean[rows][cols];
		puzzleHints = new boolean[rows][cols];
		
		headerLeft = new String[rows];
		headerTop = new String[cols];
		
		rowPriorities = new int[rows];
		colPriorities = new int[cols];
		
	}


	/**
	 * load saved puzzle
	 * 
	 * will be called by Puzzle Solver
	 * 
	 * @param savedPuzzle
	 */
	public Puzzle(Puzzle savedPuzzle) {		
		
		//SHALLOW COPY
		//puzzleSolution = savedPuzzle.getPuzzleSolution();
		//puzzleHints = savedPuzzle.getPuzzleHints();
		
		//DEEP COPY
		int rows = savedPuzzle.getPuzzleHints().length;
		int cols = savedPuzzle.getPuzzleHints()[0].length;
		
		//puzzleSolution = new boolean[rows][cols];
		puzzleHints = new boolean[rows][cols];
		
		for (int i=0; i < rows; i++) {
			for (int j=0; j < cols; j++) {
				puzzleHints[i][j] = savedPuzzle.puzzleHints[i][j];
				//puzzleSolution[i][j] = savedPuzzle.puzzleSolution[i][j];
			}
		}
		
		//init left and top headers
		headerLeft = new String[rows];
		headerTop = new String[cols];
		
		// copy left headers
		for (int i=0; i < rows; i++) {
			headerLeft[i] = savedPuzzle.headerLeft[i];
		}
		
		//copy top headers
		for (int j=0; j < cols; j++) {
			headerTop[j] = savedPuzzle.headerTop[j];
		}
		
		//US4 - copy row priorities of saved puzzle to this puzzle
		
		rowPriorities = new int[rows];
		
		for (int i=0; i < rowPriorities.length; i++) {
			rowPriorities[i] = savedPuzzle.rowPriorities[i];
		}
		
		colPriorities = new int[cols];
		
		for (int i=0; i < colPriorities.length; i++) {
			colPriorities[i] = savedPuzzle.colPriorities[i];
		}
		
	}
	
	
	/**
	 * Puzzle constructor for puzzle from database
	 * 
	 * @param row num of rows
	 * @param col num of cols
	 * @param hleft left headers
	 * @param htop top headers
	 * @param hints hints array
	 * @param pRow priority 
	 * @param pCol
	 */
	public Puzzle(int rows, int cols, String[] hleft, String[] htop, boolean[][] hints, String pRow, String pCol) {		

		// initialize puzzle hints array from passed hints array
		puzzleHints = new boolean[rows][cols];
		
		for (int i=0; i < rows; i++) {
			for (int j=0; j < cols; j++) {
				puzzleHints[i][j] = hints[i][j];
			}
		}
		
		//System.out.println(hleft);
		//System.out.println(htop);
		
		//init left and top headers
		headerLeft = new String[rows];
		headerTop = new String[cols];

		//initialize header objects from passed array	
		for (int i=0; i < hleft.length; i++) {
			headerLeft[i] = hleft[i];
		}
		
		for (int i=0; i < htop.length; i++) {
			headerTop[i] = htop[i];
		}
		
		
		//US4
		// parse priorityRow and priorityCol array
		
		//since row and col priorities are separated by " "
		//it is split and loaded into the array
		
		String[] strPRow = pRow.split(" ");
		
		rowPriorities = new int[rows];

		for (int i=0; i < rowPriorities.length; i++) {
			rowPriorities[i] = Integer.parseInt(strPRow[i]);
		}

		String[] strPCol = pCol.split(" ");
		
		colPriorities = new int[cols];
		
		for (int i=0; i < colPriorities.length; i++) {
			colPriorities[i] = Integer.parseInt(strPCol[i]);
		}
		
	}
	
	/**
	 * @return the headerLeft
	 */
	public String[] getHeaderLeft() {
		return headerLeft;
	}


	/**
	 * @param headerLeft the headerLeft to set
	 */
	public void setHeaderLeft(String[] headerLeft) {
		this.headerLeft = headerLeft;
	}
	
	
	
	public void setHeaderLeftText(int x, String s) {
		this.headerLeft[x] = s;
	}
	
	
	
	public void setHeaderTopText(int y, String s) {
		this.headerTop[y] = s;
	}


	/**
	 * @return the headerTop
	 */
	public String[] getHeaderTop() {
		return headerTop;
	}


	/**
	 * @param headerTop the headerTop to set
	 */
	public void setHeaderTop(String[] headerTop) {
		this.headerTop = headerTop;
	}


	/**
	 * @return the puzzleSolution
	 */
	public boolean[][] getPuzzleSolution() {
		return puzzleSolution;
	}


	/**
	 * @param puzzleSolution the puzzleSolution to set
	 */
	public void setPuzzleSolution(boolean[][] puzzleSolution) {
		this.puzzleSolution = puzzleSolution;
	}


	/**
	 * @return the puzzleHints
	 */
	public boolean[][] getPuzzleHints() {
		return puzzleHints;
	}

	/**
	 * @param puzzleHints the puzzleHints to set
	 */
	public void setPuzzleHints(boolean[][] puzzleHints) {
		this.puzzleHints = puzzleHints;
	}
	
	
	//######## Solutions and hints TILE getters and setters ########
	
	/**
	 * 
	 * @param row
	 * @param col
	 * @param hint
	 */
	public void setPuzzleHintsTile(int row, int col, boolean hint) {
		this.puzzleHints[row][col] = hint;
	}
	
	/**
	 * 
	 * @param row
	 * @param col
	 * @return
	 */
	public boolean getPuzzleHintsTile(int row, int col) {
		return puzzleHints[row][col];
	}
	
	/**
	 * 
	 * @param row
	 * @param col
	 * @param sol
	 */
	public void setPuzzleSolutionsTile(int row, int col, boolean sol) {
		this.puzzleSolution[row][col] = sol;
	}
	
	
	/**
	 * @return the rowPriorities
	 */
	public int[] getRowPriorities() {
		return rowPriorities;
	}





	/**
	 * @param rowPriorities the rowPriorities to set
	 */
	public void setRowPriorities(int[] rowPriorities) {
		this.rowPriorities = rowPriorities;
	}





	/**
	 * @return the colPriorities
	 */
	public int[] getColPriorities() {
		return colPriorities;
	}





	/**
	 * @param colPriorities the colPriorities to set
	 */
	public void setColPriorities(int[] colPriorities) {
		this.colPriorities = colPriorities;
	}





	/**
	 * 
	 * @param row
	 * @param col
	 * @return
	 */
	public boolean getPuzzleSolutionsTile(int row, int col) {
		return puzzleSolution[row][col];
	}
	
	
	
	
	//US4 methods
	
	/**
	 * set row priority 
	 * @param row
	 * @param p
	 */
	public void setRowPriority(int row, int p) {
		this.rowPriorities[row] = p;
	}
	
	
	/**
	 * set column priority
	 * @param col
	 * @param p
	 */
	public void setColPriority(int col, int p) {
		this.colPriorities[col] = p;
	}
	
	
	/**
	 * get row priority
	 * @param row
	 * @return
	 */
	public int getRowPriority(int row) {
		return this.rowPriorities[row];
	}
	
	/**
	 * get column priority
	 * @param col
	 * @return
	 */
	public int getColPriority(int col) {
		return this.colPriorities[col];
	}

}
