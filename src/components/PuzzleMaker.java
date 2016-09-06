package components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import javax.swing.JButton;
import javax.swing.JOptionPane;

import Objects.Board;
import Objects.Grid;
import Driver.*;


/**
 * OBSERVER PATTERN
 * 
 * Subject (Model and Controller) - PuzzleMaker
 * Observer (View) - PuzzleMakerObserver
 * 
 * PuzzleMaker class - SUBJECT of PuzzleMakerOBSERVER
 * 
 * As a subject, PuzzleMaker has multiple states 
 * that when modified, will notify the observers (VIEWS)
 * 
 * 	i.e. states are attributes/properties that can be modified and such
 *	 modifications to the states are displayed to the users 
 *	 via the observers (VIEWS)
 * 
 * States:
 * 
 * - leftHeadersState : current status of the left headers
 * 						denoted as puzzle data
 * 
 * - topHeadersState : current status of the top headers
 * 						denoted as puzzle data

 * -- US 4--
 * - row priorities array	
 * 
 * 			(e.g. for a 4x4 grid, an array of [2,0,1,3] means 
 * 			row#2 is highest priority (most number of marked tiles)
 * 			row#0 is 2nd highest
 * 			row#1 is 3rd highest
 * 			row#3 is 4th highest )
 * 	
 * - column priorities array  (same with row concept)
 * )
 * 
 * =========================================
 * 
 * PuzzleMaker class
 * 
 * each puzzle maker has a Puzzle (data) and a Board (GUI) components
 * 
 * 2 modes in puzzle-making
 * 1. solution-marking
 * 2. hint-marking
 * 
 * @author team t
 *
 */
public class PuzzleMaker {

	private Puzzle puzzle;						//puzzle data
	private Board puzzleBoard;					//interface
	
	private int mode;						// solution or tile-marking	
	private JButton saveButton = new JButton("SAVE");
	
	/*
	 * OBSERVER DESIGN PATTERN - Subject
	 * 
	 * Observable states of PuzzleMaker
	 *  
	 */
	private String[] leftHeadersState;		//left and top headers - constraints
	private String[] topHeadersState;

	private int[] rowPriorities;			// row and col priorities
	private int[] colPriorities;

	//list of observers attached to this subject
	private List<PuzzleMakerObserver> observers = new ArrayList<PuzzleMakerObserver>();		// observers that will observe this subject
	
	//########### OBSERVER PATTERN SUBJECT METHODS ##################
	//get and set states
	
	/**
	 * update the left headers based on the clicked row
	 * 
	 * ------------ALGORITHM---------------
	 * used a stack to check which cells are selected as part of solution 
	 * 
	 * if a cell is selected, push and increment counter
	 * pop if encountered a non-selected cell, get value
	 * 
	 * eg. X_X_ should be "1 1"
	 * 
	 * X_X_
	 * 0123
	 * 
	 * going through index
	 * 0: push 1 (marked)
		  1: pop 1 (not marked)
		  2: push 1 (marked)
		  3: pop 1 (not marked)
		  
		  -> "1 1"
		   
	 * @param x the row clicked
	 */
	public void setLeftHeadersState(int x) {
		
		Stack<Integer> dkStack = new Stack<Integer>();		// create stack for the algorithm
		String t="";										// accumulator of string
		
		dkStack.push(0);			// initialize cell count
	
		Grid grid = this.puzzleBoard.getGrid();
		
		//LEFT HEADER
		//go through each cell, check if marked
		for (int i=0; i< grid.getCols(); i++) {
			if (grid.getTileMarked(x, i))
				dkStack.push(dkStack.pop()+1);		// if consecutive marked, increment value
			else {
				int last1 = dkStack.pop();			// else pop and save value
				
				if (last1 != 0)
					t = t + last1 + " "; 
				dkStack.clear();					// reset stack
				dkStack.push(0);
			}
		}
		if (dkStack.peek() != 0)				// after loop, if it's not zero then get
			t = t  + dkStack.pop();
		
		this.puzzleBoard.setHeaderLeftS(x, t);

		//########## OBSERVER NOTIFY ###########
		this.notifyAllObservers("LEFT_HEADER");
		
	}
	
	/**
	 * @return the puzzleBoard
	 */
	public Board getPuzzleBoard() {
		return puzzleBoard;
	}


	/**
	 * update top headers based on clicked column
	 * same algorithm as setLeftHeadersState
	 * 
	 * @param y clicked column
	 */
	public void setTopHeadersState(int y) {
		
		Stack<Integer> dkStack2 = new Stack<Integer>();
		dkStack2.push(0);
		String u="";

		Grid grid = this.puzzleBoard.getGrid();
		
		//TOP HEADER (same algorithm as left)
		//go through each cell, check if marked 
		for (int i=0; i< grid.getRows(); i++) {
			if ( grid.getTileMarked(i, y)  )	
				dkStack2.push(dkStack2.pop()+1);		// if consecutive marked, increment value
			else {
				int last1 = dkStack2.pop();			// else pop and save value
				if (last1 != 0)
					u = u + last1 + " " ; 
				dkStack2.clear();					// reset stack
				dkStack2.push(0);
			}
		}

		if (dkStack2.peek() != 0)			// after loop, if it's not zero then get
			u = u  + dkStack2.pop();

		this.puzzleBoard.setHeaderTopS(y, u);

		//########## OBSERVER NOTIFY ###########
		this.notifyAllObservers("TOP_HEADER");
	}
	
	
	//########## US 4 ################
	//enable or disable priorities
	
	public void enablePriorities() {
		
		System.out.println("Priorities enabled");
		
		// show priority options
		this.puzzleBoard.priorityOptionsToggle(true);
	}

	
	public void disablePriorities() {
	
		System.out.println("Priorities disabled");
		
		// hide priority options
		this.puzzleBoard.priorityOptionsToggle(false);
	}
	

	
	/**
	 * sets the priority rows/cols states
	 * using the passed arguments
	 * 
	 * uses algorithms in helper methods to record priority data
	 * into the rowPriorities and colPriorities array (PuzzleMaker members)
	 * 
	 * @param tilesMarkedEachRow
	 * @param tilesMarkedEachCol
	 */
	public void setMarkedRowsColsState(int[] tilesMarkedEachRow, int[] tilesMarkedEachCol) {

		/*get index of max row from the tilesMarkedEachRow, and tilesMarkedEachCol
		 and 2nd max, 3rd max, etc (as priorities)
		 and we should notify the puzzle maker of these IF Priorities are Enabled
		*/
		
		//ALGORITHM
		//we dont care about the number of tiles marked, we only care about their rank
		/*
		 * 
		 * lets say a grid has 
		 * 						index
		 * X _ X X		3		0
		 * _ X _ _	 	1		1
		 * X X X X		4		2
		 * _ _ X X		2		3
		 * 
		 * so we should have the rank/priority array   2, 0, 3, 1
		 * 
		 * so we have row 2 as highest priority, 0 as 2nd, and so on.
		 * 
		 * 

		 getPriorityArray() -> algorithm that produce array of indices 
		 sorted in order of number of tiles marked
		
		 for now, if rows/cols have the same number of tiles
		 they will still be ranked based on which one will be found first
		 i.e. there will be no ties (row 0 and row 3 both have 4 marked tiles)
		 		instead row 0 will be priority 0 (highest) and row 3 priority 1 (2nd highest)
		*/
		
		// set priority states
		this.rowPriorities = getPriorityArray(tilesMarkedEachRow);
		
		//for (int i=0; i < this.rowPriorities.length; i++) 
		//	System.out.println(this.rowPriorities[i]);
		
		this.colPriorities = getPriorityArray(tilesMarkedEachCol);
		
		//for (int i=0; i < this.colPriorities.length; i++) 
		//	System.out.println(this.colPriorities[i]);
		
		
		//since we have the priority arrays for rows and cols, we can notify the observer now to show
		// this visually
			
		//########## OBSERVER NOTIFY ###########
		this.notifyAllObservers("ROW_PRIORITY");	
		this.notifyAllObservers("COL_PRIORITY");
	}
	
	
	/**
	 * @return the rowPriorities
	 */
	public int[] getRowPriorities() {
		return rowPriorities;
	}

	/**
	 * @return the colPriorities
	 */
	public int[] getColPriorities() {
		return colPriorities;
	}
	
	
	/**
	 * returns the priority array given the array containing the number of tiles marked
	 * 
	 * lets say a grid has 
	 * 
	 * 			# of marked		index
	 * X _ X X		3			0
	 * _ X _ _	 	1			1
	 * X X X X		4			2
	 * _ _ X X		2			3
	 * 
	 * so we should have the rank/priority array   2, 0, 3, 1
	 * 
	 * row 2 as highest priority, 0 as 2nd, and so on.
	 * 
	 * @param tilesMarked
	 * @return
	 */
	private int[] getPriorityArray(int[] tilesMarked) {
		
		int[] priorityArrayRows = new int[tilesMarked.length];
		
		for (int k=0; k <  tilesMarked.length; k++) {
			
			int maxIndexRows  = getIndexOfMax(tilesMarked);
			
			priorityArrayRows[k] = maxIndexRows;
			
			tilesMarked[maxIndexRows] = -1;		//eliminate this option, for we recorded its index already in priority
			
		}
		return priorityArrayRows;
	}
	
	
	/**
	 * helper method that gets the index of max value in a given array
	 * 
	 * @param numArray
	 * @return
	 */
	private int getIndexOfMax(int[] numArray) {

		int maxIndex = 0;
		
		for (int k=0; k < numArray.length; k++) {
			if (numArray[k] >= numArray[maxIndex])
				maxIndex = k;
		}

		//System.out.println("MAX INDEX: " + maxIndex);
		return maxIndex;
		
	}

		//###############################################################
	
	
	   //######### OBSERVER PATTERN ATTACH and NOTIFY METHODS ###########
	   
	   public void attach(PuzzleMakerObserver observer){
	      observers.add(observer);		
	   }

	   public void notifyAllObservers(String notificationType){
		      for (PuzzleMakerObserver observer : observers) {
		         observer.update(notificationType);
		      }
		} 	
	  //################################################################
	   
	
	/**
	 * constructor for PuzzleMaker
	 * 
	 * @param rows number of rows
	 * @param cols number of columns
	 */
	public PuzzleMaker(int rows, int cols) {
		
		//throw exception if given invalid number of rows/cols
		if (rows <= 0 || cols <= 0 || rows > 25 || cols > 25)
			throw  new IllegalArgumentException();

		
		puzzle = new Puzzle(rows, cols);					// create Puzzle
		puzzleBoard= new Board(rows,cols,saveButton, this);		//create Board
		
		saveButton.addActionListener(new SaveButtonListener());			// save button function
		puzzleBoard.setVisible(true);
		
		mode = 1;		//start puzzle maker as solution-marker
		
		//init. tile behavior to puzzle making setting
		puzzleBoard.getGrid().setMode(1);
	}
	
	
	/**
	 * 
	 * save button function
	 * i.e. this will execute when save button is clicked
	 * 
	 * @author team t
	 *
	 */
	private class SaveButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			savePuzzle();		// save
		}
	}
	
	/**
	 * save puzzle created by puzzle maker
	 * to a boolean 2D array
	 * 
	 * get all the marked cells
	 * 
	 */
	public void savePuzzle( ) {
		
		//check number of selected tiles
		int ctr=0;
		
		
		for (int i=0; i< puzzleBoard.getGrid().getRows(); i++) {
			for (int j=0; j< puzzleBoard.getGrid().getCols(); j++) {
				if (puzzleBoard.getGrid().getTileMarked(i, j))					// if marked, then tile is part of puzzle
					ctr++;
			}
		}
		
		//if no marked tiles, alert user, not allowed
		if (ctr == 0) {
			JOptionPane.showMessageDialog(null, "Must select at least 1 tile");
			return;
		}
		
		//loop through each tile in puzzleBoard, check if marked
		//set corresponding boolean element in the puzzle solution array
		
		for (int i=0; i< puzzleBoard.getGrid().getRows(); i++) {
			for (int j=0; j< puzzleBoard.getGrid().getCols(); j++) {
				if (puzzleBoard.getGrid().getTileMarked(i, j))					// if marked, then tile is part of puzzle
					puzzle.setPuzzleSolutionsTile(i,j,true);
				else 															// not marked
					puzzle.setPuzzleSolutionsTile(i,j,false);
			}
		}
		
		saveHeaders();				// call method to save headers		
		savePriorities();			// call method to save priorities	
		changeToHintMode();			// change mode to hint setting mode
	}	
	
	
	
	/**
	 * save left and top headers (constraints)
	 */
	private void saveHeaders() {
		
		// save left header
		for (int i=0; i < this.puzzleBoard.getNumRows(); i++) {
			//copy header text to the String array
			this.puzzle.setHeaderLeftText(i, this.puzzleBoard.getHeaderLeftCol(i)   );
		}
		
		// save top header
		for (int i=0; i < this.puzzleBoard.getNumCols(); i++) {
			//copy header text to the String array
			this.puzzle.setHeaderTopText(i, this.puzzleBoard.getHeaderTopRow(i)   );
		}
	}
	
	
	/**
	 * ======= US 4 ============
	 * save priorities for rows and columns
	 * 
	 */
	private void savePriorities() {
		
		//copy row priority array in Board to Puzzle data
		for (int i=0; i < this.puzzleBoard.getNumRows(); i++) {
			this.puzzle.setRowPriority(i, this.puzzleBoard.getRowPriority(i));
		}
		
		//copy col priority array in Board to Puzzle data
		for (int i=0; i < this.puzzleBoard.getNumCols(); i++) {
			this.puzzle.setColPriority(i, this.puzzleBoard.getColPriority(i));
		}
	}
	
	
	/**
	 * private helper method to change from solution-marking to hint-marking
	 * 
	 */
	private void changeToHintMode() {
		
		//now ask puzzle maker to select the hints that will be visible to the solver
		if (mode==1){
			JOptionPane.showMessageDialog(null, "SAVED!\nNow click on black cells that you want to appear as hints to the user");
			mode=2;								// change to hints mode	
			saveButton.setText("SAVE HINTS");	// change text button
			
			puzzleBoard.getGrid().setMode(2);	//change tile behavior for hint setting
		}
		else		// save hints mode
			saveHints();			
	}
	

	/**
	 * save puzzle hints to a boolean 2D array
	 * given the dimensions
	 * 
	 * @param row rows
	 * @param col columns
	 */
	public void saveHints() {
		

		//get count of Puzzle Tiles
		int ctr = 0;
		
		//go through grid, count puzzle tiles
		for (int i=0; i< puzzleBoard.getGrid().getRows(); i++) {
			for (int j=0; j< puzzleBoard.getGrid().getCols(); j++) {
				if (puzzleBoard.getGrid().getTileMarked(i, j))						//tile is a hint
					ctr++;
			}
		}
		
		//System.out.println("PUZZLE TILES: " + ctr);
		
		
		//get count of hints
		int ctrHint = 0;
		
		//go through grid, count hinted puzzle tiles
		for (int i=0; i< puzzleBoard.getGrid().getRows(); i++) {
			for (int j=0; j< puzzleBoard.getGrid().getCols(); j++) {
				if (puzzleBoard.getGrid().getTileHint(i, j))						//tile is a hint
					ctrHint++;
			}
		}
			
		//if hints selected equals number of puzle tiles alert user, not allowed
		if (ctr ==  ctrHint) {
			JOptionPane.showMessageDialog(null, "Cannot select all tiles as hints");
			return;
		}
		
		//now that there is at least un-hinted tile, save puzzle

		//loop through each tile in puzzleBoard, check if a hint
		//set corresponding boolean element in the puzzle hint array
		
		for (int i=0; i< puzzleBoard.getGrid().getRows(); i++) {
			for (int j=0; j< puzzleBoard.getGrid().getCols(); j++) {
				if (puzzleBoard.getGrid().getTileHint(i, j))						//tile is a hint
					puzzle.setPuzzleHintsTile(i,j,true);
				else 
					puzzle.setPuzzleHintsTile(i,j,false);
			}
		}
		
		finishedPuzzleMaking ();		// finish puzzle making
	}
	
	
	/**
	 * private helper method to perform tasks after puzzle making
	 * 
	 */
	private void finishedPuzzleMaking () {
		
		JOptionPane.showMessageDialog(null, "HINTS SAVED!\nNow You can now solve the puzzle");		// inform user
		
		mode = 1;			//reset mode flag to puzzle making
		puzzleBoard.getGrid().setMode(1);		//reset tile behavior to puzzle maker setting
		
		Driver.saveNewPuzzle(puzzle);			//save puzzle

		puzzleBoard.setVisible(false);			// hide board for we dont need it for now
		
		GUIMenu.menu(this, null);			//go back to menu
	}
} //end class
