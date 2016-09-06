package components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JOptionPane;

import Objects.Board;
import Objects.Grid;
import Objects.PossibleSolutionCheckBoxBottom;
import Objects.Tile;

//OBSERVER DESIGN PATTERN - Subject

/**
 * OBSERVER PATTERN
 * 
 * Subject (Model and Controller) - PuzzleSolver
 * Observer (View) - PuzzleSolverObserver
 * 
 * As a subject, PuzzleSolver has multiple states 
 * that when modified, will notify the observers (VIEWS)
 * 
 * 
 * States:
 * 
 * 
 * -puzzleState		:  this is the current status of the puzzle solver's board
 * 						it denotes which tiles are marked
 * 
 * -checkBoxesState	: this is the current status of the puzzle solver's checkBoxes
 * 						particularly for enabling alternative solutions
 * 						for selected rows and columns
 * 
 * -others to come...
 * 
 * ====================================
 * 
 * PuzzleSolver class - SUBJECT of observer pattern
 * 
 * each puzzle solver has a savedPuzzle (DATA) and a Board (GUI) components
 * 
 * @author team t
 *
 */
public class PuzzleSolver  {

	private Puzzle savedPuzzle;			// puzzle saved in the system
	private Board puzzleBoard;			// GUI of that puzzle
	
	private JButton checkButton = new JButton("CHECK");

	/*
	 * OBSERVER DESIGN PATTERN - Subject
	 * 
	 * Observable states of PuzzleSolver
	 */
	private boolean[][] puzzleState;			// representation of marked tiles
	private boolean[] checkBoxBottomState;		// checked boxes in the bottom
	private boolean[] checkBoxRightState;		//checked boxes in the right
	
	//list of puzzle solver observers
	private List<PuzzleSolverObserver> observers = new ArrayList<PuzzleSolverObserver>();		// observers that will observe this subject
	
		//########### OBSERVER PATTERN SUBJECT METHODS ##################
	
		/**
		 * getPuzzleState
		 * 
		 * @return 2D boolean array that represents the current user-marked tiles (puzzle solver mode)
		 */
	   public boolean[][] getPuzzleState() {
		   return puzzleBoard.getSelectedTiles();
	   }
	   
	   /**
	    * get checkBoxBottomState
	    * 
	    * @return
	    */
	   public  boolean[] getCBBState() {
		   return this.checkBoxBottomState;
	   }   
	   
	   /**
	    * get checkBoxRightState
	    * 
	    * @return
	    */
	   public  boolean[] getCRBState() {
		   return this.checkBoxRightState;
	   }

	   
	   /**
	    * notify the observers attached to this PuzzleSolver
	    * that there was a change in the state 
	    */
	   public void setPuzzleState(Tile tileClicked) {
   
		// send notification to all observers, along with notification type
	      notifyAllObservers("PUZZLE_STATE", tileClicked);		
	   }

	   /**
	    * set state of bottom check box
	    * 
	    * @param row the row that was chosen to show alternative solutions for
	    */
	   public void setcheckBoxBottomState(int col, boolean checked) {
		   
		   //System.out.println("BOTTOM ALT SOL col: " + col + ":" + checked);
		   
		   //udpate value in chkBox array	   
		   this.checkBoxBottomState[col] = checked;
		   
		   notifyAllObservers("CHK_BOTTOM",null); 
	   }
	   
	   
	   /**
	    * set state of right check box
	    * 
	    * @param row the row that was chosen to show alternative solutions for
	    */
	   public void setcheckBoxRightState(int row, boolean checked) {
		   
		  // System.out.println("RIGHT ALT SOL row: " + row + ":" + checked);
		   
		   //udpate value in chkBox array	   
		   this.checkBoxRightState[row] = checked;
		   
		   notifyAllObservers("CHK_RIGHT", null);
	   }
	   
	   	//#######################################################
	   /**
	    * getSolutions
	    * 
	    * @return 2D boolean array that represents the solutions for this particular loaded puzzle
	    */
	   public boolean[][] getSolution () {   
		   return savedPuzzle.getPuzzleSolution();
	   }
	   
		/**
		 * @return the puzzleBoard
		 */
		public Board getPuzzleBoard() {
			return puzzleBoard;
		}
	   
	   /**
	    * @return the left header array
	    */
	   public String[] getHeaderLeft() {
		   return savedPuzzle.getHeaderLeft();
	   }
	   
	   /**
	    * @return the top header array
	    */
	   public String[] getHeaderTop() {
		   return savedPuzzle.getHeaderTop();
	   }

	   /**
	    * @return the number of rows in the board
	    */
	   public int getNumRows() {
		   return puzzleBoard.getNumRows();
	   }


	   /**
	    * @return the number of cols in the board
	    */
	   public int getNumCols() {
		   return puzzleBoard.getNumCols();
	   }
	   
	   //######### OBSERVER PATTERN ATTACH and NOTIFY METHODS ###########
	   
	   /**
	    * @param observer
	    */
	   public void attach(PuzzleSolverObserver observer){
	      observers.add(observer);		
	   }

	   public void notifyAllObservers(String notificationType, Tile tileClicked){
	      for (PuzzleSolverObserver observer : observers) {
	         observer.update(notificationType, tileClicked);
	      }
	   } 	
  
	  //################################################################

	/**
	 * constructor
	 * 
	 * loads a saved puzzle
	 * 
	 * 
	 * @param savedPuzzle
	 */
	public PuzzleSolver(Puzzle savedPuzzle) {
		

		this.savedPuzzle = new Puzzle(savedPuzzle);
		
		checkButton.addActionListener(new CheckButtonListener());
		
		puzzleBoard = new Board(savedPuzzle, checkButton, this);
		
		puzzleBoard.setVisible(true);
		
	
		
		// init checkboxes arrays
		checkBoxBottomState = new boolean[puzzleBoard.getNumCols()];		// checked boxes in the bottom
		checkBoxRightState = new boolean[puzzleBoard.getNumRows()];		//checked boxes in the right
	}

	

	/**
	 * CHECK button function
	 * 
	 * this gets executed when "CHECK" button is clicked
	 * 
	 * @author team t
	 *
	 */
	private class CheckButtonListener implements ActionListener{
		
		@Override
		public void actionPerformed(ActionEvent e) {
			
			// CHECK IF CURRENT GRID IS A SOLUTION
			boolean correct = checkPuzzle();
					
			JOptionPane.showMessageDialog(null, correct ? "CORRECT! GOOD JOB" : "Nope. Sorry. Try again");

			//exit if correct
			if (correct)
			{
				JOptionPane.showMessageDialog(null, "Thank you for playing");
				System.exit(0);
			}
		}
	}
	

	/**
	 * check if current grid is a solution
	 * 
	 * @param row rows
	 * @param col columns
	 * @return true or false if current grid is the solution
	 */
	
	public boolean checkPuzzle() {
		
		boolean[][] currentState =	getPuzzleState();		//get current state of Board
		String[] headerLeft = getHeaderLeft();			// get constraints for this puzzle
		String[] headerTop = getHeaderTop();
		
		//call methods to get matching rows and columns
		boolean[] matchingRows = getMatchingRows(currentState, headerLeft, getNumRows());
		boolean[] matchingCols = getMatchingCols(currentState, headerTop, getNumCols());
		
		boolean allRowColumnSatisfied = true;
		
		//go through all rows, if one row doesnt match, return false
		for (int i = 0; i < matchingRows.length; i++) {
			if (!(matchingRows[i])) {
				//System.out.print(matchingRows[i]);
				return false;
			}	
		}
		
		//go through all cols, if one col doesnt match, return false
		for (int i = 0; i < matchingCols.length; i++) {
			if (!(matchingCols[i])) {
				//System.out.print(matchingCols[i]);
				return false;
			}		
		}
		
		return allRowColumnSatisfied;
		
	}
	
	/**
	 * Check if a constraint is currently satisfied in the configuration of the grid.
	 * The constraints are checked in order for a row/column by CheckSingleRowOrColumn() method.
	 * 
	 * @param rowOrColumn The current row or column being checked
	 * @param constraint The size of the constraint
	 * @param offset The position in the array rowOrColumn to start checking for the constraint
	 * @return a positive Integer if the constraint was satisfied. The value also corresponds to the offset for the next constraint to check if any.
	 * @return a negative Integer if the constraint is not satisfied.
	 * @see checkSingleRowOrColumn(boolean[] rowOrColumn, String header)
	 */
	
	public int checkSingleConstraint(boolean[] rowOrColumn, int constraint, int offset) {
		
		int count = 0; // how many black squares are in a row
		int new_offset = offset; // The position at which the algorithm will start checking for the constraint in the row or column
		
		for (int i = offset; i < rowOrColumn.length; i++) {
			if (rowOrColumn[i]) {		//if a square is black that increment the counter
				count++;
			}
			else if (count > 0)		//if current the square is white and the previous square was black, but the constraint not satisfied, then the constraint fails
				return -1;
			if ((count == constraint) && ( ((i+1) == rowOrColumn.length) || (!(rowOrColumn[i+1])) ) ) { //If there is a number of black square in a row corresponding to the constraint
				new_offset = i + 1;																		//and the next square is either a white square of the end of the row/column,
				return new_offset;																		//then the constraint passes and we return the next position to start to check for the next constraint 
			}
			else if ((count != constraint) && ( ((i+1) == rowOrColumn.length) ) ) {		//If we get to the end of a row/column and the constraint was not satisfied then, then the constraint fails
				return -1;
			}
		}
				
		return -1;
		
	}

	/**
	 * Check if all the constraint for a row of column are satisfied
	 * 
	 * @param rowOrColumn The current row or column being checked
	 * @param header The constraints for the current row or column
	 * @return a string with "t" if a constraint is satisfied and "f" is the constraint is not satisfied. t's and f's are separated by a space
	 */
	public String checkSingleRowOrColumn(boolean[] rowOrColumn, String header) {
		
		if (header.equals(""))		//if empty string, return
			return "";
		
		String rowOrColumn_satisfied = "";  
		String[] constraints = header.split(" "); //separate the constraints in the header to be checked individually			
		int offset = 0; //starting position
		int previous_offset= 0;
		
		
		
		int i = 0;		//correspond to the index of the constraint
		while(offset < rowOrColumn.length) { //keep looping until we get to the end of the row/column
						
			if (constraints[i].equals(""))		// if empty string, return
				return "";
			
			for (int j = offset; j < rowOrColumn.length - 1; j++) {
				if (rowOrColumn[offset] == false)
					offset++;
			}
			
			previous_offset = offset;
			offset = checkSingleConstraint(rowOrColumn, Integer.parseInt(constraints[i]), offset); //test for a single constraint and get the position to check for the next constraint
			i++;
			if (offset > 0 && i == constraints.length) { //if offset positive, then constraint passed. if we checked all the constraints, then all the constraint of this row/column have been satisfied
				for (int j = offset; j < rowOrColumn.length; j++) {
					if (rowOrColumn[j] == true) {		//all constraints have been satisfied, checking that all other tile on this array are false
						rowOrColumn_satisfied += "f";
						return rowOrColumn_satisfied;
					}
				}
				rowOrColumn_satisfied += "t";
				return rowOrColumn_satisfied;
			}
			else if ((offset >= rowOrColumn.length) && (i < constraints.length)) {		//if offset greater than the length of the row/column and all the constraints were not satisfied
				rowOrColumn_satisfied += "t f";											//then the last constraint fails
				return rowOrColumn_satisfied;
			}
			else if (offset > 0) {							//if offset positive, then the constraint passed, we loop to the next
				rowOrColumn_satisfied += "t ";
				for (int j = offset; j < rowOrColumn.length - 1; j++) {
					if (rowOrColumn[offset] == false)
						offset++;
				}
			}
			else if (offset < 0 && i >= constraints.length) {			//if offset negative, then constraint failed.
				rowOrColumn_satisfied += "f";
				return rowOrColumn_satisfied;
			}
			else if ((offset < 0) && (i < constraints.length)) {		//if offset negative(constraint fails), but other constraints can be satisfied, where do you set the offset to check for the next constraint?
				rowOrColumn_satisfied += "f ";
				offset = previous_offset;
				offset = checkSingleConstraint(rowOrColumn, Integer.parseInt(constraints[i]), offset);
				if (offset == -1) {
					offset = previous_offset;
					if (i == constraints.length - 1) {
						rowOrColumn_satisfied += "f";
						return rowOrColumn_satisfied;
					}
					for (int j = offset; j < rowOrColumn.length - 1; j++) {
						if (rowOrColumn[offset] == true)
							offset++;
					}
				}
				else if (offset > 0 && i == constraints.length - 1) { 
					rowOrColumn_satisfied += "t";
					return rowOrColumn_satisfied;
				}
				else if (offset > 0 && i < constraints.length - 1) {
					if (checkSingleConstraint(rowOrColumn, Integer.parseInt(constraints[i]), offset) > 0 && ( Integer.parseInt(constraints[i]) !=  Integer.parseInt(constraints[i+1])))
						continue;
					else
						offset = previous_offset;
				}
				else {
					offset = previous_offset;
				}
			}
			
		}
		return rowOrColumn_satisfied;
	}

	/**
	 * from two 2D boolean arrays, check which rows match with their constraints
	 * and return an array which represent this current state
	 * "snapshot"
	 * 
	 * @param currentState current configuration of the grid by the puzzle solver
	 * @param headerLeft The constraint for all the rows
	 * @param numRows number of rows
	 * @return an array of strings, each index represent a row with the information about which constraints are satisfied and which one are not.
	 */

	public String[] rowSnapShot(boolean[][] currentState, String[] headerLeft, int numRows) {
		
		String[] matchingRows = new String[numRows]; 			// init array		
		
		for (int i = 0; i < currentState.length; i++) {
			matchingRows[i] = checkSingleRowOrColumn(currentState[i], headerLeft[i]); //for each row check for all its constraint and whether they are satisfied
		}

		return matchingRows;		
	}
	
	
	/**
	 * from two 2D boolean arrays, check which columns match
	 * and return an array which represent this current state
	 * "snapshot"
	 * 
	 * @param currentState current configuration of the grid by the puzzle solver
	 * @param headerTop The constraint for all the columns
	 * @param numCols number of columns
	 * @return an array of strings, each index represent a column with the information about which constraints are satisfied and which one are not.
	 */
	public String[] colSnapShot(boolean[][] currentState, String[] headerTop, int numCols) {
		
		String[] matchingCols = new String[numCols];		// init array
		
		boolean[] cols = new boolean[currentState.length];  //we have to create the column array by looping through the rows so it can be passed as a single dimension array to 
															//the rows so it can be passed as a single dimension array to checkSingleRowOrColumn(cols, headerTop[i])
		for (int i = 0; i < currentState[0].length; i++) {
			for (int j = 0; j < currentState.length; j++) {
				cols[j] = currentState[j][i];
			}
			
			matchingCols[i] = checkSingleRowOrColumn(cols, headerTop[i]); //for each column check for all its constraint and whether they are satisfied
		}
			
		return matchingCols;
	}
	
	
	/**
	 * Create a array of boolean. If all the constraints of a row are satisfied, then the whole row is satisfied. 
	 * If a single constraint fails, then the whole row fails. It will parse the result from rowSnapShot(currentState, headerLeft, numRows)
	 * 
	 * @param currentState current configuration of the grid by the puzzle solver
	 * @param headerLeft The constraint for all the rows
	 * @param numRows number of rows
	 * @return an array of boolean, each index represent a row and whether it passes or not.
	 * @see rowSnapShot(currentState, headerLeft, numRows)
	 */
	public boolean[] getMatchingRows(boolean[][] currentState, String[] headerLeft, int numRows) {
		
		boolean[] matchingRows = new boolean[numRows];
		
		String[] rowSnapShot = rowSnapShot(currentState, headerLeft, numRows); 
		
		for (int i = 0; i < rowSnapShot.length; i++) {
			String[] temp = rowSnapShot[i].split(" "); 		//split the string of constraint into an array of constraint
			matchingRows[i] = true;
			for (int j = 0; j < temp.length; j++) {
				if (temp[j].equals("f"))					//if a single constraint is set to false the whole row is set to false
					matchingRows[i] = false;
			}
		}
		
		return matchingRows;
	}
	
	/**
	 * Create a array of boolean. If all the constraints of a column are satisfied, then the whole column is satisfied. 
	 * If a single constraint fails, then the whole column fails. It will parse the result from colSnapShot(currentState, headerTop, numCols)
	 * 
	 * @param currentState current configuration of the grid by the puzzle solver
	 * @param headerTop The constraint for all the columns
	 * @param numCols number of columns
	 * @return an array of boolean, each index represent a row and whether it passes or not.
	 * @see colSnapShot(currentState, headerTop, numRows)
	 */
	public boolean[] getMatchingCols(boolean[][] currentState, String[] headerTop, int numCols) {
		
		boolean[] matchingCols = new boolean[numCols];
		
		String[] colSnapShot = colSnapShot(currentState, headerTop, numCols);
		
		for (int i = 0; i < colSnapShot.length; i++) {
			String[] temp = colSnapShot[i].split(" ");			//split the string of constraint into an array of constraint
			matchingCols[i] = true;
			for (int j = 0; j < temp.length; j++) {
				if (temp[j].equals("f"))						//if a single constraint is set to false the whole column is set to false
					matchingCols[i] = false;
			}
		}
		
		return matchingCols;
	}
	
	/**
	 * When the checkbox for potential solution is checked for some row, then this method will take a "snapshot"
	 * of the current configuration of the grid. This means that it will check which constraints are currently 
	 * satisfied by the configuration of the grid. It will then try all the white square of the checked row
	 * and determine if this could be a potential solution. It is a potential solution, if all constraint satisfied
	 * before trying a potential solution are still satisfied. If a single constraint is lost by trying a potential solution,
	 * then this potential solution will not be considered.
	 * 
	 * @param currentState current configuration of the grid by the puzzle solver
	 * @param index row to check for potential solutions
	 * @param headerTop constraints of the columns
	 * @param headerLeft constraints of the rows
	 * @return a boolean array with all the potential solutions of the row set to true and false otherwise.
	 */	
	
	public boolean[] checkPotentialSolutionsRows(boolean[][] currentState, int index, String[] headerTop, String[] headerLeft) {
		
		boolean[] potentialSolution = new boolean[currentState[0].length];
		
		String[] rowBefore = rowSnapShot(currentState,headerLeft, currentState.length);		//check which row constraints are satisfied before trying potential solutions 
		String[] colBefore = colSnapShot(currentState,headerTop, currentState[0].length);	//check which columns constraints are satisfied before trying potential solutions 
		
		String[] rowAfter;
		String[] colAfter;
		
		
		for (int i = 0; i < currentState[0].length; i++) {
						
			if (!currentState[index][i]) {
				currentState[index][i] = true;
				rowAfter = rowSnapShot(currentState,headerLeft, currentState.length);		//check which row constraints are satisfied after trying potential solutions
				colAfter = colSnapShot(currentState,headerTop, currentState[0].length);		//check which columns constraints are satisfied after trying potential solutions 
				potentialSolution[i] = compareBeforeAndAfter(rowBefore,rowAfter, colBefore,colAfter); //snapshots before and after
				currentState[index][i] = false;		//set back the current tile to false(white) before trying for the next potential solution 
			}
			else {
				potentialSolution[i] = false;
			}
		}
		
		
		//System.out.println(Arrays.toString(a));
		
		return potentialSolution;
		
	}
	
	/**
	 * When the checkbox for potential solution is checked for some column, then this method will take a "snapshot"
	 * of the current configuration of the grid. This means that it will check which constraints are currently 
	 * satisfied by the configuration of the grid. It will then try all the white square of the checked column
	 * and determine if this could be a potential solution. It is a potential solution, if all constraint satisfied
	 * before trying a potential solution are still satisfied. If a single constraint is lost by trying a potential solution,
	 * then this potential solution will not be considered.
	 * 
	 * @param currentState current configuration of the grid by the puzzle solver
	 * @param index column to check for potential solutions
	 * @param headerTop constraints of the columns
	 * @param headerLeft constraints of the rows
	 * @return a boolean array with all the potential solutions of the column set to true and false otherwise.
	 */	
	
	public boolean[] checkPotentialSolutionsCols(boolean[][] currentState, int index, String[] headerTop, String[] headerLeft) {
		
		boolean[] potentialSolution = new boolean[currentState.length];
		
		String[] rowBefore = rowSnapShot(currentState,headerLeft, currentState.length);			//check which row constraints are satisfied before trying potential solutions 
		String[] colBefore = colSnapShot(currentState,headerTop, currentState[0].length);		//check which columns constraints are satisfied before trying potential solutions 
		
		String[] rowAfter;
		String[] colAfter;
		
		
		for (int i = 0; i < currentState.length; i++) {
			if (!currentState[i][index]) {
				currentState[i][index] = true;
				rowAfter = rowSnapShot(currentState,headerLeft, currentState.length);			//check which row constraints are satisfied after trying potential solutions
				colAfter = colSnapShot(currentState,headerTop, currentState[0].length);			//check which columns constraints are satisfied after trying potential solutions 
				potentialSolution[i] = compareBeforeAndAfter(rowBefore,rowAfter, colBefore,colAfter);	//snapshots before and after
				currentState[i][index] = false;					//set back the current tile to false(white) before trying for the next potential solution 
			}
		}
		
		return potentialSolution;
		
	}
	
	/**
	 * This method will compare "snapshot" from the constraints of the rows and columns before and after
	 * and determine if some constraint were lost by trying a potential solution. In that case, it will return
	 * false and true otherwise.
	 * 
	 * @param rowBefore snapshots of rows before trying a potential solution
	 * @param rowAfter snapshots of columns before trying a potential solution
	 * @param colBefore snapshots of rows after trying a potential solution
	 * @param colAfter snapshots of columns after trying a potential solution
	 * @return boolean corresponding to whether or not the potential solution being tested can be considered
	 * @see rowSnapShot(boolean[][] currentState, String[] headerLeft, int numRows)
	 * @see colSnapShot(boolean[][] currentState, String[] headerTop, int numCols)
	 */
	
	public boolean compareBeforeAndAfter(String[] rowBefore, String[] rowAfter, String[] colBefore, String[] colAfter) {
		
		boolean isPotentialSolution = true;
		
		for (int i = 0; i < rowBefore.length; i++) { // check row snapshot before and after
			String[] before = rowBefore[i].split(" ");
			String[] after = rowAfter[i].split(" ");

			for (int j = 0; j < before.length && j < after.length; j++) {
				if (before[j].equals("t") && after[j].equals("f")) { 	//if a constraint was true before and false after, then the potential solution fails
					isPotentialSolution = false;
				}
			}
			
		}
		
		for (int i = 0; i < colBefore.length; i++) {	// check column snapshot before and after
			String[] before = colBefore[i].split(" ");
			String[] after = colAfter[i].split(" ");
			for (int j = 0; j < before.length && j < after.length; j++) {
				if (before[j].equals("t") && after[j].equals("f")) {	//if a constraint was true before and false after, then the potential solution fails
					isPotentialSolution = false;
				}
			}
			
		}
		
		return isPotentialSolution;
	}
	
	
	

}
