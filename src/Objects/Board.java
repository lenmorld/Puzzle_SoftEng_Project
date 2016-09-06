package Objects;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Stack;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;

import components.*;

/**
 * 
 * Puzzle - DATA
 * Board - GUI and interface
 * 
 * Board Class
 * 
 * Board provides the interface of the puzzle
 * as well as some methods it needs to react to user clicks
 * and update its GUI components
 * 
 * there are two modes for the behavior of the board and its components
 * 
 * 1.puzzle maker - board will be created from scratch
 * 
 * 2.puzzle solver - board will be loaded from a saved puzzle, loading the
 * 		hints array to the board, and keeping the solutions array for checking
 * 
 * @author team t
 *
 */

public class Board extends JFrame {
	
	private Grid grid;							// board has a puzzle grid
	private HeaderLeft headerLeft;					// left header	
	private HeaderTop headerTop;						// top header
	private BottomOptionPanel bOptionPanel;		// bottom panel that contains buttons for saving, checking, etc
	
	
	private String[] headerLeftS;				// headers data as states
	private String[] headerTopS;


	//add a panel for general messages and options on the top of window - PuzzleSolver or Maker
	private TopOptionPanel tOptionPanel;


	//################# US 5 ##################
	// Puzzle Solver -  simultaneously consider several potential solutions
	//						for a row (or a column)
	// checkbox for considering alternative solutions
	private PossibleSolutionCheckBoxBottom psChkBoxBottom;
	private PossibleSolutionCheckBoxRight psChkBoxRight;
	
	
	//######### US 4 ##############
	//PuzzleMaker - assigning priorities to rows and columns of the puzzle
	//				grid to guide the puzzle solver
	//labels (later on, checkboxes) for considering these alternative solutions
	private PrioritiesCheckboxPM pChkBoxPM;	
	private ColumnsPriorityLabel cpsBottom;
	private RowsPriorityLabel rpsRight;
	JLabel topMessagePriority;
	
	//by default, hide priorities on PuzzleSolver
	private boolean showPriorities = false;

	//needs a reference to the current PM or PS that has this object
	private PuzzleSolver ps;		
	private PuzzleMaker pm;


	/**
	 * @return PuzzleSolver that owns this Board
	 */
	public PuzzleSolver getPS() {
		return ps;
	}
	
	/**
	 * @return PuzzleMaker that owns this Board
	 */
	public PuzzleMaker getPM() {
		return pm;
	}

	/**
	 * set the priority label given the row and the text
	 * 
	 * @param i row
	 * @param p text
	 */
	public void setRowPriorityText(int i, int p) {
		this.rpsRight.setRowPriorityText(i, p);
	}
	
	/**
	 * set the priority label given the column and the text
	 * 
	 * @param i row
	 * @param p text
	 */
	public void setColPriorityText(int i, int p) {
		this.cpsBottom.setColPriorityText(i, p);
	}
	

	/**
	 * get current priority of a given row
	 * 
	 * @param i row
	 * @return
	 */
	public int getRowPriority(int i) {
		return this.rpsRight.getRowPriority(i);
	}
	
	/**
	 * get current priority of a given column
	 * 
	 * @param i column
	 * @return
	 */
	public int getColPriority(int i) {
		return this.cpsBottom.getColPriority(i);
	}
	

	
	/**
	 * marked tiles - as in part of the puzzle
	 * 
	 * @return boolean array of currently marked tiles 
	 */
	public boolean[][] getMarkedTiles() {
		return this.grid.getMarkedTiles();
	}
	
	
	/**
	 * @return boolean array of currently hinted tiles 
	 */
	public boolean[][] getHintedTiles() {
		return this.grid.getHintedTiles();
	}
	
	
	/**
	 * @return boolean array of currently selected tiles
	 */
	public boolean[][] getSelectedTiles() {
		return this.grid.getSelectedTiles();
	}
	
	
	/**
	 * 
	 * base constructor
	 * 
	 * initializes GUI properties of the frame
	 * function shared by PM and PS board constructors
	 * 
	 * @param row
	 * @param col
	 */
	public Board(int row, int col) {
		
		//create top panel
		this.tOptionPanel = new TopOptionPanel();
		//this.tOptionPanel.setLayout(new GridLayout(2,1));
		this.tOptionPanel.setLayout(new BorderLayout());
		
		//create string that will hold headers
		this.headerLeftS = new String[row];
		this.headerTopS = new String[col];
		
		//init String array to empty string
		for (int i=0; i< this.headerLeftS.length; i++)
			this.headerLeftS[i] = "";
		for (int i=0; i< this.headerTopS.length; i++)
			this.headerTopS[i] = "";

		//some JFrame initializers
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(new BorderLayout());

		//now create grid
		this.grid = new Grid(row, col);

		//create bottom panel for option buttons
		this.bOptionPanel = new BottomOptionPanel();
	}
	

	/**
	 * constructor for puzzle maker
	 * 
	 * 
	 * @param row number of rows
	 * @param col number of cols
	 * @param saveButton save button to be placed in the bottom panel
	 * @param ps PuzzleMaker object that owns this Board (PuzzleMaker HAS Board)
	 */
	public Board(int row, int col, JButton saveButton, PuzzleMaker pm) {

		this(row, col);
		
		// calculate size of panel based on passed parameter
		// we determine total size of panel from number of tiles, assuming
		// each tile will take a width of 100 and height of 50
		int width = (col * 80)  ;			
		int height = (row * 80) ;	
		
		this.setPreferredSize(new Dimension(width, height));

		//minimum size that the user can resize
		this.setMinimumSize(new Dimension(500,500));
		this.setMaximumSize(new Dimension(2000,2000) );
		
		
		this.pm = pm;			//get puzzleMaker object that owns this board
		this.grid.initPuzzleMakerGrid();			// create new grid

		// initialize headers
		this.headerLeft = new HeaderLeft(row);
		this.headerTop = new HeaderTop(col);

		//set-up bottom options
		this.bOptionPanel.add(saveButton, BorderLayout.PAGE_END);

		//#### US 4  ######
		//add top option for PM to enable/disable priority indications
		this.pChkBoxPM = new PrioritiesCheckboxPM("Priorities");
		this.pChkBoxPM.setHorizontalAlignment(JLabel.CENTER);
		JLabel topMessage = new JLabel("WELCOME Puzzle Maker");
		topMessage.setHorizontalAlignment(JLabel.CENTER);
		topMessagePriority = new JLabel("*P# = Priority # for row/col     ");
		topMessagePriority.setHorizontalAlignment(JLabel.CENTER);
		//topMessage.setHorizontalAlignment(JLabel.CENTER);
		
		//###### US 4  ########
		// showing priorities		
		cpsBottom = new ColumnsPriorityLabel(this.getNumCols());
		rpsRight = new RowsPriorityLabel(this.getNumRows());

		
		//hide priority options at startup
		this.priorityOptionsToggle(false);

		//set up top option panel
		this.tOptionPanel.add(topMessage, BorderLayout.PAGE_START);	
		this.tOptionPanel.add(pChkBoxPM, BorderLayout.LINE_START);
		this.tOptionPanel.add(topMessagePriority, BorderLayout.LINE_END);
		this.tOptionPanel.add(headerTop, BorderLayout.PAGE_END);
		

		//TOP COMPONENT GROUP ---------------------------------
		
		//group top components, bottom checkboxes and topOptions
		//JPanel topGroup = new JPanel();
		//topGroup.setLayout(new GridLayout(2,1));
		//topGroup.add(tOptionPanel);					
		//topGroup.add(headerTop);
		//-----------------------------------------------------------------
		
		
		//BOTTOM COMPONENT GROUP ---------------------------------------

		//group bottom components, bottom checkboxes and bottomOptions
		//JPanel bottomGroup = new JPanel();
		//bottomGroup.setLayout(new GridLayout(2,1));
		//bottomGroup.add(cpsBottom);					//put checkboxes in the bottm side
		//bottomGroup.add(bOptionPanel);

		bOptionPanel.add(cpsBottom, BorderLayout.PAGE_START);
		
		// place panels in the frame, indicating their position
		this.add(tOptionPanel, BorderLayout.PAGE_START);			//TOP		-top options + top headers
		this.add(headerLeft, BorderLayout.LINE_START);		//LEFT SIDE		-left headers
		this.add(grid, BorderLayout.CENTER);				//CENTER- MIDDLE	grid
		this.add(rpsRight, BorderLayout.LINE_END);			//RIGHT SIDE - right priorities	
		this.add(bOptionPanel, BorderLayout.SOUTH);			//BOTTOM - bottom priorities + bottom options
	}
	
	
	/** 
	 * if priority checkbox checked/unchecked, show/hide priority labels and note on the top
	 */
	public void priorityOptionsToggle(boolean show) {
		this.topMessagePriority.setVisible(show);
		this.cpsBottom.setVisible(show);
		this.rpsRight.setVisible(show);
	}


	/**
	 * constructor for puzzle solver
	 * 
	 * @param savedPuzzle saved puzzle made earlier by a puzzle maker
	 * @param checkButton check button to be placed in the bottom panel
	 * @param ps PuzzleSolver object that owns this object (PuzzleSolver HAS Board)
	 */
	public Board(Puzzle savedPuzzle, JButton checkButton, PuzzleSolver ps)
	{

		//init using base const, supplying row and col count of the saved puzzle
		this(savedPuzzle.getPuzzleHints().length, savedPuzzle.getPuzzleHints()[0].length);			

		
		this.ps = ps;		//get puzzleSover object that owns this board
		
		// calculate size of panel based on passed parameter
		// we determine total size of panel from number of tiles
		int width = (this.getNumRows() * 120)  ;			
		int height = (this.getNumCols() * 75) ;	
		
		this.setPreferredSize(new Dimension(width, height));

		//minimum size that the user can resize
		this.setMinimumSize(new Dimension(width,500));
		this.setMaximumSize(new Dimension(width,1000) );

		
		// get hints from saved puzzle
		boolean[][] savedPuzzleHints = savedPuzzle.getPuzzleHints();
		
		// initialize grid supplying the saved puzzle hints
		this.grid.initPuzzleSolverGrid(savedPuzzleHints);		
		
		//create headers supplying number of rows/cols from saved puzzle
		this.headerLeft = new HeaderLeft(savedPuzzle.getHeaderLeft());
		this.headerTop = new HeaderTop(savedPuzzle.getHeaderTop());

		
		
		//add button to see next priority
		JButton pNextPR = new JButton("Next Priority");
		pNextPR.addActionListener(
				new PriorityButtonListener()
		);
		
		
		
		//add top option for PM to enable/disable priority indications
		JCheckBox pChkBoxPS = new JCheckBox("Priorities");
		pChkBoxPS.setSelected(false);
		pChkBoxPS.setHorizontalAlignment(JLabel.CENTER);
		pChkBoxPS.addActionListener(
			new ActionListener() {
				@Override		
				public void actionPerformed(ActionEvent arg0) {
					togglePrioritiesPS();		//toggle priority display
				}
			}
		);
		
		
		
		//CLEAR button for clering alternative solutions
		JButton clearAltSol = new JButton("Clear A.S.");
		clearAltSol.addActionListener(
				new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent arg0) {
						clearAltSol();						
					}
					
				}
		);
		
		
		bOptionPanel.setLayout(new GridLayout(1,5));
		
		
		// add checkSolutions button and clearAltSol in the bottom panel
		
		this.bOptionPanel.add(new JLabel(""));
		this.bOptionPanel.add(checkButton);	
		this.bOptionPanel.add(new JLabel(""));
		this.bOptionPanel.add(clearAltSol);
		this.bOptionPanel.add(new JLabel(""));
		
		
		//###### US 5 ########
		// checkbox for alternate solutions
		psChkBoxRight = new PossibleSolutionCheckBoxRight(this.getNumRows());			
		psChkBoxBottom = new PossibleSolutionCheckBoxBottom(this.getNumCols());		
		
		
		//####### US 4 #######
		/*
		 * TODO: Load saved priorities to puzzle solver
		 */
		
		cpsBottom = new ColumnsPriorityLabel(savedPuzzle.getColPriorities());
		rpsRight = new RowsPriorityLabel(savedPuzzle.getRowPriorities());
		
		
		//TOP COMPONENT GROUP ---------------------------------
		
		//#### US 4 and 5 ######
		//add option for PM to enable/disable priority indications
		//this.tOptionPanel.add(new JLabel("<html>WELCOME! Puzzle Solver <br> <br>*A.S. = activate Alternate Solutions for row/col</html>"));
		
		JLabel topMessage = new JLabel("WELCOME Puzzle Solver");
		topMessage.setHorizontalAlignment(JLabel.CENTER);
		JLabel topMessageAltSol = new JLabel("     *A.S. = Alternate Solutions for row/col     ");
		topMessageAltSol.setHorizontalAlignment(JLabel.CENTER);
		
		//set up top option panel
		this.tOptionPanel.add(topMessage, BorderLayout.PAGE_START);	
		//this.tOptionPanel.add(pChkBoxPS, BorderLayout.CENTER);
		this.tOptionPanel.add(pNextPR, BorderLayout.CENTER);
		this.tOptionPanel.add(topMessageAltSol, BorderLayout.LINE_START);	
		
		//group top components, bottom checkboxes and topOptions
		JPanel topGroup = new JPanel();
		topGroup.setLayout(new GridLayout(2,1));
		topGroup.add(tOptionPanel);			
		JPanel hTopPanel = new JPanel(new BorderLayout());
		hTopPanel.add(headerTop, BorderLayout.CENTER);
		hTopPanel.add(new JLabel("         "), BorderLayout.LINE_START);					//space filler left
		hTopPanel.add(new JLabel("                         "), BorderLayout.LINE_END);		// space filler right
		topGroup.add(hTopPanel);
		//-----------------------------------------------------------------
		
		//RIGHT COMPONENT GROUP ------------------------------
		JPanel rightGroup = new JPanel();
		rightGroup.setLayout(new GridLayout(1,2));
		rightGroup.add(rpsRight);
		rightGroup.add(psChkBoxRight);
		//----------------------------------------------------
		
		//BOTTOM COMPONENT GROUP ---------------------------------------
		//group bottom components, bottom checkboxes and bottomOptions
		JPanel bottomGroup = new JPanel();
		bottomGroup.setLayout(new GridLayout(2,1));
		bottomGroup.add(cpsBottom);
		bottomGroup.add(psChkBoxBottom);					//put checkboxes in the bottm side
		
		JPanel bottomPanel = new JPanel(new BorderLayout());
		bottomPanel.add(bottomGroup, BorderLayout.CENTER);
		bottomPanel.add(new JLabel("         "), BorderLayout.LINE_START);					//space filler left
		bottomPanel.add(new JLabel("                         "), BorderLayout.LINE_END);		// space filler right
		bottomPanel.add(bOptionPanel, BorderLayout.SOUTH);
		
		
		//bottomGroup.add(bOptionPanel);
		//-------------------------------------------------------------------
		// place panels in the frame, indicating their position

		this.add(topGroup, BorderLayout.NORTH);					//TOP		-top options + top headers
		this.add(headerLeft, BorderLayout.LINE_START);			//LEFT SIDE		-left headers
		this.add(grid, BorderLayout.CENTER);				//CENTER- MIDDLE	grid
		//this.add(psChkBoxRight, BorderLayout.LINE_END);		//RIGHT SIDE - right checkbox	
		this.add(rightGroup, BorderLayout.LINE_END);		//RIGHT SIDE - row priorities + right checkbox	
		this.add(bottomPanel, BorderLayout.SOUTH);			//BOTTOM - bottom checkboxes + column priorities + bottom options
	}
	
	
	/**
	 * 
	 * listener class for PriorityButtonListener
	 * 
	 * @author team t
	 *
	 */
	private class PriorityButtonListener implements ActionListener {

		//this function get called automatically when button clicked
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			nextPriority();	
		}
		
	}
	
	/**
	 * @param leftHeader
	 */
	public void loadLeftHeader(String[] leftHeader) {
		
		for (int i=0; i < leftHeader.length; i++) {
			//create then add label to array
			
			/*
			 labelArrayLeft[i] = new pLabel(" ");
			if (mode == 1) {		//SOLVER
				labelArrayLeft[i].setText(labelLeft[i].getText());	
			}
			labelPanelLeft.add(labelArrayLeft[i]);		// add to label array
			*/
		}
	}

	/**
	 * return number of rows that this board's grid has
	 * 
	 * @return
	 */
	public int getNumRows() {
		return grid.getRows();
	}
	
	/**
	 * return number of columns that this board's grid has
	 * 
	 * @return
	 */
	public int getNumCols() {
		return grid.getCols();
	}

	/**
	 * @return the grid
	 */
	public Grid getGrid() {
		return grid;
	}
	
	/**
	 * 
	 * @param x row
	 * @return header text for specified row
	 */
	public String getHeaderLeftCol(int x) {
		return this.headerLeft.getText(x);
	}
	
	/**
	 * 
	 * @param y col
	 * @return header text for specified col
	 */
	public String getHeaderTopRow(int y) {
		return this.headerTop.getText(y);
	}
	

	/**
	 * @return the headerLeft
	 */
	public HeaderLeft getHeaderLeft() {
		return headerLeft;
	}

	/**
	 * @return the headerTop
	 */
	public HeaderTop getHeaderTop() {
		return headerTop;
	}
	
	/**
	 * @return the headerLeftS
	 */
	public String[] getHeaderLeftS() {
		return headerLeftS;
	}

	/**
	 * @param headerLeftS the headerLeftS to set
	 */
	public void setHeaderLeftS(int x, String t) {
		this.headerLeftS[x] = t;
	}

	/**
	 * @return the headerTopS
	 */
	public String[] getHeaderTopS() {
		return headerTopS;
	}

	/**
	 * @param headerTopS the headerTopS to set
	 */
	public void setHeaderTopS(int y, String u) {
		this.headerTopS[y] = u;
	}
	
	/** 
	 * if priority checkbox checked/unchecked, show/hide priority labels
	 */
	public void togglePrioritiesPS () {
		
		//toggle show variable
		this.showPriorities = !this.showPriorities;
		
		//set top bottom and right priority labels visibility
		this.cpsBottom.setVisible(showPriorities);
		this.rpsRight.setVisible(showPriorities);
	}
	
	
	
	/**
	 * show next priority when button clicked
	 * 
	 */
	public void nextPriority() {
		
		//toggle show variable
		//this.showPriorities = !this.showPriorities;
		this.showPriorities = true;

		//get row priority array
		
		int[] rowPriorities =  this.rpsRight.getRowPriority();
		int[] colPriorities =  this.cpsBottom.getColPriority();
		
		//System.out.println(Arrays.toString(rowPriorities));	

		boolean[][] currentState =	this.getPS().getPuzzleState();		//get current state of Board
		String[] headerLeft = this.getPS().getHeaderLeft();			// get solutions for this puzzle
		String[] headerTop = this.getPS().getHeaderTop();

		//call methods to get matching rows and columns
		boolean[] matchingRows = this.getPS().getMatchingRows(currentState, headerLeft, this.getPS().getNumRows());
		boolean[] matchingCols = this.getPS().getMatchingCols(currentState, headerTop, this.getPS().getNumCols());	
		
		// create new priority array
		int[] arrangePrioritiesR = new int[rowPriorities.length];
		int[] arrangePrioritiesC = new int[colPriorities.length];
		
		// go through priorites starting from highest
		// arrange into new array by index
		for (int i=0; i < rowPriorities.length; i++ ) {
			arrangePrioritiesR[ rowPriorities[i] ] = i ;
		}
		
		
		// go through priorites starting from highest
		// arrange into new array by index
		for (int i=0; i < colPriorities.length; i++ ) {
			arrangePrioritiesC[ colPriorities[i] ] = i ;
		}
		
		
		//go through all priorities starting from highest - rows
		for (int i=0; i < arrangePrioritiesR.length; i++ ) {
			if ( matchingRows[arrangePrioritiesR[i]])	//if determined already, skip
				continue;
			else {		// inform user
				//System.out.println("NEXT PRIORITY: " + arrangePrioritiesR[i] );
				this.rpsRight.setVisible(arrangePrioritiesR[i], showPriorities);
				break;
			}
		}


		//go through all priorities starting from highest - cols
		for (int i=0; i < arrangePrioritiesC.length; i++ ) {
			if ( matchingCols[arrangePrioritiesC[i]])	//if determined already, skip
				continue;
			else {		// inform user
				//System.out.println("NEXT PRIORITY: " + arrangePrioritiesC[i] );
				this.cpsBottom.setVisible(arrangePrioritiesC[i], showPriorities);
				break;
			}
		}

	
	}

	
	/**
	 * clear alternative solutions
	 * 
	 */
	public void clearAltSol() {
	
		//get board grid
		
		//switch every blue blue tile to white tile
		Tile[][] tile = grid.getTile2D();
		
		//got through each tile
		for (int i=0; i < tile.length; i++)
		{
			for (int j=0; j < tile [0].length; j++)
			{
				if (tile [i][j].getBackground().equals(Color.BLUE))		// if blue or AltSol
					tile[i][j].setBackground(Color.WHITE);				//clear
			}
		}
		
		//clear all altSol checkbox
		this.psChkBoxBottom.clear();
		this.psChkBoxRight.clear();
	}

	
	
	/**
	 * called when a cell is clicked during puzzle making
	 * and headers needed to be updated
	 * 
	 * @param x x-coordinate of clicked cell
	 * @param y y-coordinate of clicked cell
	 */
	public void updateHeaders(int x, int y) {

		changeLeftHeaders(x);
		changeTopHeaders(y);
		
	}	// end of changeLabel method
	
	
	/**
	 * private helper method for changing left headers
	 * 
	 * @param x x coordinate of clicked tile
	 */
	private void changeLeftHeaders(int x) {
		
		Stack<Integer> dkStack = new Stack<Integer>();		// create stack for the algorithm
		String t="";				// accumulator of string
		
		dkStack.push(0);			// initialize cell count
		
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
		headerLeft.setText(x, t.trim());				// display as the header element text

	}
	
	
	/**
	 * private helper method for changing top headers
	 * 
	 * @param y y-coordinate of clicked tile
	 */
	private void changeTopHeaders(int y) {
		Stack<Integer> dkStack2 = new Stack<Integer>();
		dkStack2.push(0);
		String u="";
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

		headerTop.setText(y, u.trim());		// display as the header element text
	}

}	// end class
