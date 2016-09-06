package components;

import java.util.Arrays;

import javax.swing.JOptionPane;

import Driver.GUIMenu;
import Objects.Tile;

/**
 * 
 * updates:
 * added US6 code - highlight determined rows and columns
 * 
 * 
 * OBSERVER PATTERN
 * 
 * Subject (Model and Controller) - PuzzleSolver
 * Observer (View) - PuzzleSolverObserver
 * 
 * Observer pattern establishes a good design by decoupling 
 * the Views from the Observers
 * - one-to-many dependency between subject and (possibly )multiple
 * observers (dependents) that are notified and updated automatically
 * - MODEL implements the mechanics
 * - VIEWS implemented as Observers that are uncoupled as possible
 * 		from the Model components
 * 
 * 
 * ==================================
 * 
 * 
 * PuzzleSolverObserverClass
 * 
 * @author team t
 *
 */
public class PuzzleSolverObserver {
	
	protected PuzzleSolver subject;
	   
	public PuzzleSolverObserver (PuzzleSolver subject) {
		this.subject = subject;
		this.subject.attach(this);
	}
	
	
	/**
	 * what the observers will do upon notification
	 * of the subjects
	 * 
	 * several types of update
	 * 
	 * 1. PUZZLE_STATE
	 * 		rows/columns fully determined
	 * 
	 * 2. CHK_BOTTOM
	 * 		checkbox bottom was clicked
	 * 
	 * 3. CHK_RIGHT
	 * 		checkbox right was clicked
	 * 
	 */
	public void update(String updateType, Tile tileClicked) {

		if (updateType.equals("PUZZLE_STATE")) {
			displayFullyDeterminedRowColumn(tileClicked);
		}
		else if (updateType.equals("CHK_BOTTOM"))  {
			showAltSolnColumns();
		}
		else if (updateType.equals("CHK_RIGHT")) {
			showAltSolnRows();
		}
	}
	

	/**
	 * US6 code
	 * highlight fully determined rows/columns
	 * that is, the ones that has only one possible solution
	 * 
	 * 
	 * @param tileClicked
	 */
	public void displayFullyDeterminedRowColumn(Tile tileClicked) {
		
		/*
		 * ######## UPDATE TYPE 1 - PUZZLE STATE ############
		 * 
		 * when a row or column gets fully DETERMINED (not solved)
		 * show to user by highlighting it
		 * 
		 * - check the alternative solutions of current row 
		 * 
		 */

		boolean[][] currentState =	this.subject.getPuzzleState();		//get current state of Board
		String[] headerLeft = this.subject.getHeaderLeft();			// get solutions for this puzzle
		String[] headerTop = this.subject.getHeaderTop();
		
		//call methods to get matching rows and columns
		boolean[] matchingRows = this.subject.getMatchingRows(currentState, headerLeft, this.subject.getNumRows());
		boolean[] matchingCols = this.subject.getMatchingCols(currentState, headerTop, this.subject.getNumCols());
		
		showAltSolnColumns();		//invoke showing of alt. sol
		showAltSolnRows();
		
				
		//display these changes
		//for now, just display on screen
		
		String mRows = "matching rows: ";
		String mCols = "matching cols: ";
		
		boolean allGood = true;		// flag for all rows/columns are good, init to false
		
		// go through the matching rows collecting the one that matched
		for (int i=0; i < matchingRows.length; i++) {
			if (matchingRows[i]) 
				mRows+= i + " ";
			else
				allGood = false;		// (at least) one column made the current grid not a solution
		}
		
		// go through the matching cols collecting the  one that matched
		for (int i=0; i < matchingCols.length; i++) {
			if (matchingCols[i])	
				mCols += i + " ";
			else 
				allGood = false;		// (at least) one column made the current grid not a solution
		}
		
		
		/*
		 * #highlight solved rows/cols
		 * #not needed for US6, might be useful in the future
		//highlight solved rows, cols
		//go through each cell and see if this is part of a 
		// determined row or col
		
		//go through each cell
		for (int i=0; i < matchingRows.length; i++) {		
			for (int j =0; j < matchingCols.length; j++) {
				
				//if matching row, highlight row
				if (matchingRows[i] )
					this.subject.getPuzzleBoard().getGrid().highlightTile(i,j,1, true);
				//if matching column, highlight column
				else if (matchingCols[j])
					this.subject.getPuzzleBoard().getGrid().highlightTile(i,j,2, true);
				//neither
				else
					this.subject.getPuzzleBoard().getGrid().highlightTile(i,j,0, false);
			}
		}*/

		
		//####################################
		// US6
		// highlight fully determined rows/columns
		// that is, the ones that has only one possible solutions
		//###################################

		//get coordinates of tile clicked
		String XY = tileClicked.getName();
		int x = Integer.parseInt(XY.substring(0,XY.indexOf(",")));
		int y = Integer.parseInt(XY.substring(XY.indexOf(",")+1,XY.length()));
		
		boolean [] altSolArray;

		//------------- rows ----------------
		
		//update Alt.sol status for highlighting
		for (int j=0; j < this.subject.getNumCols(); j++) {	
		//if this row is checked for alt.sol, process

			altSolArray = this.subject.checkPotentialSolutionsRows(this.subject.getPuzzleState(), x, this.subject.getHeaderTop(), this.subject.getHeaderLeft());
			if (altSolArray[j])		 //set as alt.sol (this would make it blue)
				this.subject.getPuzzleBoard().getGrid().setAltSolTilePlain(x, j, true);
			else		// not alt.sol, reset back to white
				this.subject.getPuzzleBoard().getGrid().setAltSolTilePlain(x, j, false);
		}
		
		//get newly processed alt.sol for highlighting
		boolean[][] a = this.subject.getPuzzleBoard().getGrid().getAltSolArray();
		
		int trueCounter = 0;		//init counter for true
		
		//go through all tiles of row
		for (int m=0; m<a[0].length;m++) {
			if (a[x][m] == true)		//if true, increment
				trueCounter++;
		}
		
		if (trueCounter == 1)		//only 1 solution, highlight
			for (int m=0; m<a[0].length;m++)
				this.subject.getPuzzleBoard().getGrid().highlightTile(x,m,1, true);
		else						//else remove highlight
			for (int m=0; m<a[0].length;m++)
				this.subject.getPuzzleBoard().getGrid().highlightTile(x,m,1, false);

		//------------------------------------------
		
		//------------- cols ----------------
		
		//update Alt.sol status for highlighting
		for (int j=0; j < this.subject.getNumRows(); j++) {	
		//if this row is checked for alt.sol, process

				altSolArray = this.subject.checkPotentialSolutionsCols(this.subject.getPuzzleState(), y, this.subject.getHeaderTop(), this.subject.getHeaderLeft());
				if (altSolArray[j])		 //set as alt.sol (this would make it blue)
					this.subject.getPuzzleBoard().getGrid().setAltSolTilePlain(j, y, true);
				else		// not alt.sol, reset back to white
					this.subject.getPuzzleBoard().getGrid().setAltSolTilePlain(j, y, false);

		}
		
		//get newly processed alt.sol for highlighting
		boolean[][] b = this.subject.getPuzzleBoard().getGrid().getAltSolArray();
		
		int trueCounterY = 0;		//init counter for true
		
		//go through all tiles of row
		for (int m=0; m<b.length;m++) {
			if (b[m][y] == true)		//if true, increment
				trueCounterY++;
		}
		
		if (trueCounterY == 1)		//only 1 solution, highlight
			for (int m=0; m<b.length;m++)
				this.subject.getPuzzleBoard().getGrid().highlightTile(m,y,2, true);
		else						//else remove highlight
			for (int m=0; m<b.length;m++)
				this.subject.getPuzzleBoard().getGrid().highlightTile(m,y,2, false);

		//------------------------------------------		
		
		//##########################################
		//display to user fully solved rows
		
		System.out.println("===AUTO NOTIFICATION OF SOLVED ROWS/COLS===");
		System.out.println(mRows + " " + mCols);
		
		//if reached here and allGood is true
		if (allGood) {
			JOptionPane.showMessageDialog(null, "you solved it. GOOD JOB!");
			GUIMenu.exitProgram();
		}
	}

	
	
	/**
	 * check whether given index is a "true" value
	 * in the boolean array
	 * 
	 * @param index
	 * @param array
	 */
	private boolean in(int index, boolean[] array) {
	
		for (int i=0; i < array.length; i++) {
			if (array[i])
				return true;
		}
		
		return false;
	}
	
	
	/**
	 * US5 - show alternate solutions for columns
	 * when AS checkboxes are checked/unchecked
	 * this method determines the alternate solutions 
	 * and makes them blue
	 * 
	 */
	public void showAltSolnColumns() {
		
		
		//########### UPDATE TYPE 2 - Show alt. solution for chosen Column ######
		
		boolean[] chkBoxBottom = subject.getCBBState();
		boolean[] altSolArray;
		
		for (int i=0; i< chkBoxBottom.length; i++) {
						
			for (int j=0; j < this.subject.getNumRows(); j++) {
				
				//if this row is checked for alt.sol, process
				if (chkBoxBottom[i]) {
					altSolArray = this.subject.checkPotentialSolutionsCols(this.subject.getPuzzleState(), i, this.subject.getHeaderTop(), this.subject.getHeaderLeft());
					if (altSolArray[j])		 //set as alt.sol (this would make it blue)
						this.subject.getPuzzleBoard().getGrid().setAltSolTile(j, i, true);
					else		// not alt.sol, reset back to white
						this.subject.getPuzzleBoard().getGrid().setAltSolTile(j, i, false);
				}	
				else {		//not checked, reset tiles to normal
					this.subject.getPuzzleBoard().getGrid().setAltSolTile(j, i, false);
				}
			}
			
			
			/*
			 * ### TESTING CODE
			 * 
			//if current row is selected to view alt.sol. print 
			if (chkBoxBottom[i]) {
			
				System.out.println("===================================");
				System.out.println("ALTERNATIVE SOLUTIONS for selected column");
				
				boolean[][] a = this.subject.getPuzzleBoard().getGrid().getAltSolArray();
				
				for (int m=0; m<a.length;m++) {
							System.out.print(a[m][i] + " ");
				}
				System.out.println("\n==================================");
			}*/
		}

	} //end method
	
	/**
	 * US5 - show alternate solutions for rows
	 * when AS checkboxes are checked/unchecked
	 * this method determines the alternate solutions 
	 * and makes them blue
	 * 
	 */
	public void showAltSolnRows() {
		
		//########### UPDATE TYPE 3 - Show alt. solution for chosen row ######
		
		boolean[] chkBoxRight = subject.getCRBState();
		
		//TODO: SHOW POSSIBLE SOLUTION ROWS, MAKE THEM BLUE


		//go through checked rows
		for (int i=0; i< chkBoxRight.length; i++) {
			//create an array that will hold the alt.sol for this row
			boolean[] altSolArray;

			/*
			 * go through alt-sol array
			 * clickedRow - is the row where the user wants als.sol
			 * i - goes through columns
			 * 
			 */

			for (int j=0; j < this.subject.getNumCols(); j++) {
				
				//if this row is checked for alt.sol, process
				if (chkBoxRight[i]) {
					altSolArray = this.subject.checkPotentialSolutionsRows(this.subject.getPuzzleState(), i, this.subject.getHeaderTop(), this.subject.getHeaderLeft());
					if (altSolArray[j])		 //set as alt.sol (this would make it blue)
						this.subject.getPuzzleBoard().getGrid().setAltSolTile(i, j, true);
					else		// not alt.sol, reset back to white
						this.subject.getPuzzleBoard().getGrid().setAltSolTile(i, j, false);
				}	
				else {		//not checked, reset tiles to normal
					this.subject.getPuzzleBoard().getGrid().setAltSolTile(i, j, false);
				}
				
			}		

			/*
			 * 
			 * ### TESTING CODE
			 * 
			//if current row is selected to view alt.sol. print 
			if (chkBoxRight[i]) {
			
				System.out.println("===================================");
				System.out.println("ALTERNATIVE SOLUTIONS for selected row");
				
				boolean[][] a = this.subject.getPuzzleBoard().getGrid().getAltSolArray();
				
				for (int m=0; m<a[0].length;m++) {
							System.out.print(a[i][m] + " ");
				}
				System.out.println("\n==================================");
			}*/
			
		}
	} // end method
	
	
	
} //end class
