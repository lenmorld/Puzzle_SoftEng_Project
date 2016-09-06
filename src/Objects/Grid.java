package Objects;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.Border;

import components.*;


/**
 * UPDATE: 
 * US7 included - dragging regions horizontally, vertically for PuzzleSolver
 * 
 * 
 * Grid is composed of tiles
 * represented in GUI as a JPanel with GridLayout
 * 
 * @author Team T
 *
 */
public class Grid extends JPanel {

	private int rows;
	private int cols;
	private int mode;		// puzzle making or puzzle solving

	private Tile[][] tile2D;
	
	Tile dummyTile = new Tile();

	/**
	 * default constructor
	 * 
	 */
	public Grid(int r, int c) {
		this.rows = r;
		this.cols = c;
		this.tile2D = new Tile[r][c];

		// set GUI layout
		this.setLayout(new GridLayout(r,c));

		mode = 1;		//puzzle making
	}


	/**
	 * @return the rows
	 */
	public int getRows() {
		return rows;
	}

	

	/**
	 * @return the tile2D
	 */
	public Tile[][] getTile2D() {
		return tile2D;
	}


	/**
	 * @param rows the rows to set
	 */
	public void setRows(int rows) {
		this.rows = rows;
	}


	/**
	 * @return the cols
	 */
	public int getCols() {
		return cols;
	}


	/**
	 * @param cols the cols to set
	 */
	public void setCols(int cols) {
		this.cols = cols;
	}


	/**
	 * @return the grid
	 */
	public Tile[][] getGrid() {
		return tile2D;
	}


	/**
	 * @param grid the grid to set
	 */
	public void setGrid(Tile[][] grid) {
		this.tile2D = grid;
	}

	
	/**
	 * @return 2D boolean array of selected tiles
	 */
	public boolean[][] getSelectedTiles() {
		
		boolean[][] tilesSelected = new boolean[rows][cols];
		
		for (int i=0; i < this.rows; i++) {
			for (int j=0; j < this.cols; j++) {
				
				if (getTileSelected(i,j) )
						 tilesSelected[i][j] = true;
				else
					tilesSelected[i][j] = false;
			}
		}
		
		return tilesSelected;
	}
	
	
	
	
	/**
	 * @return 2D boolean array of marked tiles
	 */
	public boolean[][] getMarkedTiles() {
		
		boolean[][] tilesMarked = new boolean[rows][cols];
		
		for (int i=0; i < this.rows; i++) {
			for (int j=0; j < this.cols; j++) {
				
				if (getTileMarked(i,j) )
						 tilesMarked[i][j] = true;
				else
					tilesMarked[i][j] = false;
			}
		}
		
		return tilesMarked;
	}
	
	
	
	/**
	 * @return 2D boolean array of hinted tiles
	 */
	public boolean[][] getHintedTiles() {
		
		boolean[][] tilesHinted = new boolean[rows][cols];
		
		for (int i=0; i < this.rows; i++) {
			for (int j=0; j < this.cols; j++) {
				
				if (getTileHint(i,j) )
						 tilesHinted[i][j] = true;
				else
					tilesHinted[i][j] = false;
			}
		}
		
		return tilesHinted;
	}

	
	/**
	 * @return the mode
	 */
	public int getMode() {
		return mode;
	}


	/**
	 * @param mode the mode to set
	 */
	public void setMode(int mode) {
		this.mode = mode;
	}

	// getters for a particular tile in the grid

	public boolean getTileMarked(int row, int col) {
		return tile2D[row][col].isMarked();
	}

	public boolean getTileSelected(int row, int col) {
		return tile2D[row][col].isSelected();
	}

	public boolean getTileHint(int row, int col) {
		return tile2D[row][col].isHint();
	}


	// setters for a particular tile in the grid

	public void setTileMarked(int row, int col, boolean b) {
		tile2D[row][col].setMarked(b);
	}

	public void setTileSelected(int row, int col, boolean b) {
		tile2D[row][col].setSelected(b);
	}

	public void setTileHint(int row, int col, boolean b) {
		tile2D[row][col].setHint(b);
	}
	
	/**
	 * init. puzzle maker grid
	 */
	public void initPuzzleMakerGrid() {
		puzzleMakerTileMarker();
	}


	/**
	 * create and initialize each tile in the grid
	 */
	private void puzzleMakerTileMarker () {
		
		for (int i=0; i < this.rows; i++) {
			for (int j=0; j < this.cols; j++) {

				//create and init. each button
				tile2D[i][j] = new Tile();
				tile2D[i][j].setName(i + ","  + j);
			
				tile2D[i][j].setBackground(Color.WHITE);
				tile2D[i][j].addActionListener(new TileListener());

				this.add(tile2D[i][j]);		//add to grid
			}
		}	

	}


	/**
	 * initialize puzzle solver grid using 
	 * the puzzle loaded from DB
	 * 
	 * @param puzzleToSolve puzzle from DB
	 */
	public void initPuzzleSolverGrid(boolean[][] puzzleToSolve) {

		mode = 3;		//set tile behavior mode to solving
		
		for (int i=0; i < this.rows; i++) {
			for (int j=0; j < this.cols; j++) {
				//create and init. each button

				
				
				
				// if current cell is marked as a hint
				if (puzzleToSolve[i][j] == true){			
					tile2D[i][j] = new Tile();
					tile2D[i][j].setName(i + ","  + j);
					tile2D[i][j].setBackground(Color.BLACK);		//GREEN before
					tile2D[i][j].addActionListener(new TileListener());
					tile2D[i][j].setMarked(true);
					tile2D[i][j].setSelected(true);
				}
				else {			// not a hint
					tile2D[i][j] = new Tile();
					tile2D[i][j].setName(i + ","  + j);
					tile2D[i][j].setBackground(Color.WHITE);
					tile2D[i][j].addActionListener(new TileListener());
					
					
					//##US 7, add mouse listener to listen for mouse drags
					tile2D[i][j].addMouseMotionListener(	new MouseMotionEventDemo());
					
					tile2D[i][j].setMarked(false);
				}
				this.add(tile2D[i][j]);		// add to grid
			}
		}
	}


	/**
	 * executed when a tile is clicked in puzzle-making mode
	 * 
	 * 
	 * @param tileClicked
	 */
	private void puzzleMakingMarks (Tile tileClicked) {

		tileClicked.setSelected(!tileClicked.isSelected()) ;			//toggle selected flag

		if (tileClicked.isSelected()){							//black if selected
			tileClicked.setBackground(Color.BLACK);
			tileClicked.setMarked(true);						// marked but not hint yet
			tileClicked.setHint (false);
		}
		else	 {									
			tileClicked.setBackground(Color.WHITE);			//white if unselected
			tileClicked.setMarked(false);						// reset flags
			tileClicked.setHint(false);
		}			
	}

	
	/**
	 * executed when a tile is clicked in hint-setting mode
	 * 
	 * 
	 * @param tileClicked
	 */
	private void puzzleMakingHints (Tile tileClicked) {

		if (tileClicked.isMarked()){							// if selected

			tileClicked.setHint(!tileClicked.isHint());		// toggle hint flag

			if (tileClicked.isHint()){						// blue if hint
				tileClicked.setBackground(Color.BLUE);
				tileClicked.setHint(true);	
			}
			else
			{
				tileClicked.setBackground(Color.BLACK);	// black if not hint
				tileClicked.setHint(false);				// reset hint flag
			}
		}
	}

	
	/**
	 * private helper method
	 * 
	 * executed when a tile is clicked in puzzle-solving mode
	 * 
	 * @param x
	 * @param y
	 * @param tileClicked tile that was clicked
	 * @param e action event which determines which tile was clicked
	 */
	private void puzzleSolver(int x, int y, Tile tileClicked) {
		// in puzzle solving mode, marked cells are the hints
		
		tileClicked.setSelected (!tileClicked.isSelected());			//toggle selected flag

		if (tileClicked.isMarked())					//disregard if cell hints are clicked
			return;

		if (tileClicked.isSelected())	{				//green if selected
			tileClicked.setBackground(Color.BLACK);		//GREEN BEFORE
			tileClicked.setSelected(true);
		}
		else { 								//white if unselected
			tileClicked.setBackground(Color.WHITE);	
			tileClicked.setSelected(false);
		}
		gridUpdatePSolver(x,y, tileClicked.isSelected(), tileClicked);		//update grid 
	}
	
	/**
	 * 
	 * inner class that listens to clicks in any tile
	 * 
	 * @author team t 
	 *
	 */
	private class TileListener implements ActionListener {

		@Override
		/**
		 * what happens when cell is clicked
		 */
		public void actionPerformed(ActionEvent e) {

			Tile tileClicked = (Tile)e.getSource();		//find out which button clicked
			//System.out.println(btnClicked.getName());			//print name (coordinates)

			// get X and Y coordinates of clicked button
			String XY = tileClicked.getName();
			int x = Integer.parseInt(XY.substring(0,XY.indexOf(",")));
			int y = Integer.parseInt(XY.substring(XY.indexOf(",")+1,XY.length()));
			//System.out.println(x + " " + y);

			if (mode == 1)		// if puzzle making
			{	
				// call helper method for puzzle maker
				puzzleMakingMarks(tileClicked);

				//Tile tile = (Tile)e.getSource();
				Grid grid = (Grid) tileClicked.getParent();
				Board board = (Board) grid.getParent().getParent().getParent().getParent();

				//update the headers based on tile clicked
				//board.updateHeaders(x, y);
				headerUpdatePMaker(x,y, tileClicked);
				
				//determine num of rows and cols marked
				calculateMarkedRowsCols( tileClicked);
			}
			else if (mode == 2)			// hint-setting				
				//call helper method for setting hints
				puzzleMakingHints(tileClicked);
			else if (mode == 3)  {		//solver
				
				//call helper method for puzzle solving
				puzzleSolver(x,y,tileClicked);
	
			}
		}
	} // end of TileListener class

	/**
	 * ###### US 7, allows dragging of tiles
	 * 
	 * listens to mouse events such as dragging
	 * 
	 * @author team t
	 *
	 */
	public class MouseMotionEventDemo implements MouseMotionListener {
		//...in initialization code:
		//Register for mouse events on blankArea and panel.
		//blankArea.addMouseMotionListener(this);
		//addMouseMotionListener(this);
		
		/**
		 * not implemented, but have to be included
		 */
		public void mouseMoved(MouseEvent e) {}
		
		Tile previousSource = null;
		
		public void mouseDragged(MouseEvent e) {

			try {
			//get height and width of the tile array
			int height = tile2D[0][0].getHeight(),
				width = tile2D[0][0].getWidth();	
		
			//go through each tile
			for(int i = 0 ; i < rows; i++)
				for(int j = 0; j < cols; j++)
					if(e.getSource() == tile2D[i][j]){			//if this is the one clicked
						int sourceY = i*height,					//get the starting source
							sourceX = j*width,
							mouseX = sourceX + e.getX(),
							mouseY = sourceY + e.getY(),
							tileJ = mouseX/width,				//calculate against width and height
							tileI = mouseY/height;
						//System.out.println("i="+i+"j="+j+"\ttileI="+tileI+"tileJ="+tileJ); //debugging
						if(previousSource != tile2D[tileI][tileJ]){				// if not equals to source (moved)
							e.setSource(tile2D[tileI][tileJ]);					// get handle for new one
							new TileListener().actionPerformed(					// generate an action event
									new ActionEvent(tile2D[tileI][tileJ], 69, "be good"));
							previousSource = tile2D[tileI][tileJ];			//set as the previous one
						}
						return;
					}	
			}
			catch(Exception ex) {		//catch exceptions
				System.out.println("Mouse dragging");
			}
		}
	}
	
	
	/**
	 * ----OBSERVER PATTERN component------
	 * 
	 * sets the state of the PuzzleMaker subject
	 * after a tile is clicked
	 * 
	 * @param tileClicked the particular tile that was clicked by user
	 */
	public void calculateMarkedRowsCols(Tile tileClicked) {
		
		//call helper methods to get marked tiles for each row and col
		int[] numMarkedRows = getNumMarkedTilesEachRow();	
		int[] numMarkedCols = getNumMarkedTilesEachCol();

		//get the grid and board that has the clicked tile
		Grid grid = (Grid) tileClicked.getParent();
		Board board = (Board) grid.getParent().getParent().getParent().getParent();

		//pass this info as a state update to the PuzzleMaker 
		board.getPM().setMarkedRowsColsState(numMarkedRows, numMarkedCols);

	}

	
	/**
	 * @return number of marked tiles on ecah row
	 */
	public int[] getNumMarkedTilesEachRow() {
		
		int[] numMarkedRows = new int[this.getRows()];
		
		//go through rows printing how many rows are marked for each row
		for (int i =0; i < this.getRows(); i++)
		{
			int ctr = 0;
			
			for (int j=0; j < this.getCols(); j++) 
			{
				if (this.getTileMarked(i, j))		//if this one marked, increment
					ctr++;
			}
			//System.out.println("ROW " + i + " : " + ctr + " marked ");	
			numMarkedRows[i] = ctr;
		}
		return numMarkedRows;		
	}

	/**
	 * @return the number of marked tiles in each column
	 */
	public int[] getNumMarkedTilesEachCol() {
		
		//COLS
		int[] numMarkedCols = new int[this.getCols()];
		
		//go through cols printing how many cols are marked for each row
		for (int j=0; j < this.getCols(); j++) 
		{
			int ctr = 0;
			
			for (int i=0; i < this.getRows(); i++)
			{
				if (this.getTileMarked(i, j))		//if this one marked, increment
					ctr++;
			}
			//System.out.println("COL " + j + " : " + ctr + " marked ");
			numMarkedCols[j] = ctr;
		}
		return numMarkedCols;
	}

	
	
	/**
	 * update puzzle maker states (leftHeader and topHeader)
	 * based on selected tiles
	 * 
	 * @param x
	 * @param y
	 * @param tileClicked
	 */
	public void headerUpdatePMaker(int x, int y, Tile tileClicked) {
		
		//update marked for tiles
		//Tile tile = (Tile)e.getSource();
		Grid grid = (Grid) tileClicked.getParent();
		Board board = (Board) grid.getParent().getParent().getParent().getParent();
		
		//############ OBSERVER PATTERN HELPER #############
		// change states of puzzle maker's left headers and top headers 
		// this change of state will trigger notification observers of change in headers
		// pass the x-y coordinate of clicked tile

		board.getPM().setLeftHeadersState(x);
		board.getPM().setTopHeadersState(y);
	}
	
	
	/**
	 * 
	 * updates the grid, also sends the updates to the Observer
	 * which will trigger the notification and change of View
	 * 
	 * @param x
	 * @param y
	 * @param b
	 * @param tileClicked
	 */
	public void gridUpdatePSolver(int x, int y, boolean b, Tile tileClicked){
		
		//update marked for tiles
		//Tile tile = (Tile)e.getSource();
		Grid grid = (Grid) tileClicked.getParent();
		Board board = (Board) grid.getParent().getParent().getParent().getParent();

		// get selected tiles
		boolean[][] tilesSelected = this.getSelectedTiles();

		/*
		for (int i=0; i < tilesMarked.length; i++) {
			for (int j=0; j < tilesMarked[0].length; j++) {
				System.out.print(tilesMarked[i][j]);
			}
			System.out.println();
		}*/

		//############ OBSERVER PATTERN HELPER #############
		// call setState -> notify to notify observers of change in board
		// pass the array of tiles marked

		board.getPS().setPuzzleState(tileClicked);
	}




	/**
	 * US 5
	 * set tile as alternative solution
	 * make it blue
	 */
	public void setAltSolTile(int row, int col, boolean b) {
		
		this.tile2D[row][col].setAltSol(b);
		
		//if true
		if (b){		// if hint or marked already, leave it
			if (tile2D[row][col].isHint() || tile2D[row][col].isMarked() || tile2D[row][col].isSelected() )
				;
			else
				this.tile2D[row][col].setBackground(Color.BLUE);
		}	
		else	//else, reset
		{
			// if hint or marked already, leave it
			if (tile2D[row][col].isHint() || tile2D[row][col].isMarked() || tile2D[row][col].isSelected())
				this.tile2D[row][col].setBackground(Color.BLACK);
			else	// if not, then its a regular tile
				this.tile2D[row][col].setBackground(Color.WHITE);
		}
	}
	
	
	/**
	 * US 6 helper
	 * setAltSolution status but dont display
	 * 
	 * @param row
	 * @param col
	 * @param b
	 */
	public void setAltSolTilePlain(int row, int col, boolean b) {
		this.tile2D[row][col].setAltSol(b);	
	}	
	
	
	/**
	 * get alternative solution array
	 * 
	 * @return alt.sol array
	 */
	public boolean[][] getAltSolArray() {
		
		boolean[][] altSolArray = new boolean[rows][cols];
	
		for (int i=0; i < rows; i++) {
			for (int j=0; j < cols; j++) {
				altSolArray[i][j] = tile2D[i][j].getAltSol();
			}
		}
	
		return altSolArray;
	}
	
	
	/**
	 * #US6 - highlight row being fully determined
	 * highlight tile 
	 * 
	 * @param r row 
	 * @param b set/unset
	 */
	public void highlightTile(int r, int c, int mode, boolean b) {

		// if this is to be highlighted
		if (b) {
			if (mode == 1)		//row
				this.tile2D[r][c].setBorder(BorderFactory.createLineBorder(Color.GREEN, 2));
			else if (mode == 2)	//col
				this.tile2D[r][c].setBorder(BorderFactory.createLineBorder(Color.BLUE, 2));
			
			//this.tile2D[r][c].setForeground(Color.RED);
			//this.tile2D[r][c].setText("D");
			
		}	// clear highlight
		else {
			//dummyTile has a regular border
			this.tile2D[r][c].setBorder(dummyTile.getBorder());
			//this.tile2D[r][c].setForeground(Color.BLACK);
			//this.tile2D[r][c].setText("");
		}
		
	}
	
	
	
	
}
