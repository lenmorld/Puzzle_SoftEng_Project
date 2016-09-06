package old_files;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Stack;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;


/*
 * TEAM T
 * 
 * COMP 354 Project
 * Iteration 1 Code
 * 
 * Andrea
 * Jonathan
 * Sebastien
 * Haochen
 * Martin
 * Erick
 * Andres
 * Lenmor
 * 
 * 
 * 1D-2D GCHQ Puzzle
 * The puzzle consists of a grid of dimension R (rows) and C (columns)
 * each cell can be either black or white
 * 
 * The grid includes a starting information of a few cells that you 
 * know are black
 * 
 * The puzzle includes headers to indicate the length of black runs that occur in that row
 * 
 * 
 */

/**
 * this class includes all the private inner classes that 
 * compose the puzzle, as well as the attributes and methods 
 * for creating and controlling behavior of puzzle
 * 
 * @author  Team T
 *
 */
public class PuzzleGUI extends JFrame {

	/* some static attributes that simulates saving of the puzzles
	*  for puzzle solver to be able to access created puzzles of the puzzle maker
	*/
	
	public static pLabel[] labelArrayLeft;		// headers
	public static pLabel[] labelArrayTop;
	
	public static boolean[][] puzzleSolution;	// created puzzle including solution cells
	public static boolean[][] puzzleProblem;	// created puzzle with only the hint cells shown
	
	private pButton[][] buttonArray;		// grid

	private int userMode;					// flag for puzzle maker create and hint, and solver
	private JButton saveButton;				


	/**
	 * this is each element in the header array 
	 * for both top and left
	 * 
	 * @author Team T 
	 *
	 */
	private class pLabel extends JLabel {

		/**
		 * constructor, init. size and position
		 * 
		 * @param label
		 */
		pLabel(String label) {
			super(label);
			this.setSize(75, 10);
			this.setHorizontalAlignment(JLabel.CENTER);
			this.setVerticalAlignment(JLabel.CENTER);
		}
	}

	
	/**
	 * this is the base button cell, which is clickable
	 * and changes colors by clicking
	 * 
	 * @author Team T 
	 *
	 */
	private class pButton extends JButton {

		boolean marked;				// cell flag for being marked as part of the puzzle
		boolean selected;			// cell flag for being currently selected 
		boolean hint;				// cell flag for being a hint (also implies being puzzle)

		/**
		 * constructor, init. label and flags to false
		 * 
		 * @param label
		 */
		pButton(String label) {
			super(label);
			marked = false;				//set default to not marked (not a part of the puzzle)
			hint = false;				//set default to no
			selected = false;			//set default to not selected (while paying the game)
		}
	}

	
	/**
	 * 
	 * called when button is clicked
	 * 
	 * implements ActionListener - override actionPerformed method
	 * that determines behavior upon clicking
	 * 
	 * 
	 * can be called in 3 modes
	 * 
	 * Puzzle Maker - SAVE
	 * Puzzle Maker picking hints - SAVE HINTS
	 * Puzzle Solver - CHECK
	 * 
	 * @author Team T
	 * 
	 */
	private class buttonListener implements ActionListener {

		@Override
		/**
		 * what happens when cell is clicked
		 * 
		 */
		public void actionPerformed(ActionEvent e) {
			
			//usermode 2: PUZZLE MAKER IS SETTING HINTS
			if (userMode == 2){

				pButton btnClicked = (pButton)e.getSource();		//find out which button clicked
				//System.out.println(btnClicked.getName());			//print name (coordinates)

				if (btnClicked.marked){							// if selected

					btnClicked.hint = !btnClicked.hint;			// toggle hint flag

					if (btnClicked.hint){						// blue if hint
						btnClicked.setBackground(Color.BLUE);
						btnClicked.hint = true;	
					}
					else
					{
						btnClicked.setBackground(Color.BLACK);	// black if not hint
						btnClicked.hint = false;				// reset hint flag
					}
				}
				
				// get X and Y coordinates of clicked button
				String XY = btnClicked.getName();
				int x = Integer.parseInt(XY.substring(0,XY.indexOf(",")));
				int y = Integer.parseInt(XY.substring(XY.indexOf(",")+1,XY.length()));
				//System.out.println(x + " " + y);
			}
			
			// usermode 1:PUZZLE MAKER STARTS TO CREATE PUZZLE
			else if (userMode == 1) {		

				pButton btnClicked = (pButton)e.getSource();		//find out which button clicked
				//System.out.println(btnClicked.getName());			//print name (coordinates)

				btnClicked.selected = !btnClicked.selected;			//toggle selected flag

				if (btnClicked.selected){							//black if selected
					btnClicked.setBackground(Color.BLACK);
					btnClicked.marked = true;						// marked but not hint yet
					btnClicked.hint = false;
				}
				else	 {									
					btnClicked.setBackground(Color.WHITE);			//white if unselected
					btnClicked.marked = false;						// reset flags
					btnClicked.hint = false;
				}			
				
				// get X and Y coordinates of clicked button
				String XY = btnClicked.getName();
				int x = Integer.parseInt(XY.substring(0,XY.indexOf(",")));
				int y = Integer.parseInt(XY.substring(XY.indexOf(",")+1,XY.length()));
				//System.out.println(x + " " + y);

				// call method to change labels
				changeLabels(x,y);
			}
			// usermode 3: PUZZLE SOLVER
			else if (userMode == 3)		
			{
				// in puzzle solving mode, marked cells are the hints
				
				pButton btnClicked = (pButton)e.getSource();		//find out which button clicked
				//System.out.println(btnClicked.getName());			//print name (coordinates)

				btnClicked.selected = !btnClicked.selected;			//toggle selected flag

				if (btnClicked.marked)					//disregard if cell hints are clicked
					return;

				if (btnClicked.selected)					//green if selected
					btnClicked.setBackground(Color.GREEN);
				else 								//white if unselected
					btnClicked.setBackground(Color.WHITE);			

				// get X and Y coordinates of clicked button
				String XY = btnClicked.getName();
				int x = Integer.parseInt(XY.substring(0,XY.indexOf(",")));
				int y = Integer.parseInt(XY.substring(XY.indexOf(",")+1,XY.length()));
				//System.out.println(x + " " + y);
				
			}
		}
	}

	
	/**
	 * 
	 * constructor for Puzzle
	 * 
	 * @param row num of rows
	 * @param col num of columns
	 * @param mode maker or solver
	 * @param made boolean array of puzzle if accepting a made puzzle
	 * @param labelLeft left headers
	 * @param labelTop top headers
	 */
	public PuzzleGUI(final int row, final int col, int mode, boolean[][] made, pLabel[] labelLeft, pLabel[] labelTop) {

		// if mode 0 (maker), set to begin puzzle making
		// if mode 1 (solver), set to begin puzzle solving
		userMode = (mode==0) ? 1 : 3;

		//calculate size of panel based on passed parameter
		// we determine total size of panel from number of buttons, assuming
		// each button will take a width of 100 and height of 50
		int width = row * 75;			
		int height = col * 75;
		this.setSize(width, height);

		//some JFrame initializers
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(new BorderLayout());

		//now create buttonGrid
		JPanel buttonGrid = new JPanel();
		buttonArray = new pButton[row][col];

		//create a grid based on given rows and cols
		buttonGrid.setLayout(new GridLayout(row,col));

		//puzzle maker
		if (mode==0){		 
			for (int i=0; i < row; i++) {
				for (int j=0; j < col; j++) {
					
					//create and init. each button
					buttonArray[i][j] = new pButton("");
					buttonArray[i][j].setName(i + ","  + j);
					buttonArray[i][j].setBackground(Color.WHITE);
					buttonArray[i][j].addActionListener(new buttonListener());

					buttonGrid.add(buttonArray[i][j]);		//add to grid
				}
			}
		}	
		// puzzle solver
		else{			

			for (int i=0; i < row; i++) {
				for (int j=0; j < col; j++) {
					//create and init. each button
					
					// if current cell is marked as a hint
					if (made[i][j] == true){			
						buttonArray[i][j] = new pButton("");
						buttonArray[i][j].setName(i + ","  + j);
						buttonArray[i][j].setBackground(Color.GREEN);
						buttonArray[i][j].addActionListener(new buttonListener());
						buttonArray[i][j].marked = true;
					}
					else {			// not a hint
						buttonArray[i][j] = new pButton("");
						buttonArray[i][j].setName(i + ","  + j);
						buttonArray[i][j].setBackground(Color.WHITE);
						buttonArray[i][j].addActionListener(new buttonListener());
						buttonArray[i][j].marked = false;
					}
					buttonGrid.add(buttonArray[i][j]);		// add to grid
				}
			}
		}

		
		// init. panels that hold the headers on top and left

		// start with left header labels
		JPanel labelPanelLeft = new JPanel();

		// init. and setup header array
		labelPanelLeft.setLayout(new GridLayout(row, 1));
		labelArrayLeft = new pLabel[row];	

		for (int i=0; i < row; i++) {
			//create then add label to array
			
			labelArrayLeft[i] = new pLabel(" ");
			if (mode == 1) {		//SOLVER
				labelArrayLeft[i].setText(labelLeft[i].getText());	
			}
			labelPanelLeft.add(labelArrayLeft[i]);		// add to label array
		}


		// set top header labels
		JPanel labelPanelTop = new JPanel();

		// init. and setup header array
		labelPanelTop.setLayout(new GridLayout(1, col));
		labelArrayTop = new pLabel[col];	

		for (int i=0; i < col; i++) {
			// create then add label to array
			
			labelArrayTop[i] = new pLabel(" ");

			if (mode == 1) {		//SOLVER
				labelArrayTop[i].setText(labelTop[i].getText());	
			}
			labelPanelTop.add(labelArrayTop[i]); 		// add to label array
		}


		//add another panel for save/load button 
		JPanel optionsPanelBottom = new JPanel();	// place in bottom
		saveButton = new JButton("");

		//puzzle maker - give some save options
		if (mode==0){
			
			saveButton.setText("SAVE");
			
			// set what button does when puzzle maker clicks SAVE
			saveButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					// call method to save the puzzle
					savePuzzle(row,col);
				}
			});
		}
		// puzzle solver
		else
		{
			saveButton.setText("CHECK");
			
			// set what button does when puzzle maker clicks CHECK
			saveButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {

					// CHECK IF CURRENT GRID IS A SOLUTION
					boolean correct = checkPuzzle(row,col);
	
					JOptionPane.showMessageDialog(null, correct ? "CORRECT! GOOD JOB" : "Nope. Sorry. Try again");

					//exit if correct
					if (correct)
					{
						JOptionPane.showMessageDialog(null, "Thank you for playing");
						System.exit(0);
					}
						
				}
			});
		}

		optionsPanelBottom.add(saveButton);			// add button to frame

		this.add(labelPanelTop, BorderLayout.PAGE_START);		//top header
		this.add(labelPanelLeft, BorderLayout.LINE_START);		//left header

		this.add(buttonGrid, BorderLayout.CENTER);			//add grid to frame
		this.add(optionsPanelBottom, BorderLayout.SOUTH);	//add extra panel to frame
	}


	/**
	 * save puzzle to a boolean 2D array
	 * given the dimensions
	 * 
	 * @param row rows
	 * @param col columns
	 */
	public void savePuzzle(int row, int col) {

		//save the puzzle created by the puzzle maker
		// using a boolean 2D array
		puzzleSolution = new boolean[row][col];

		//loop through each cell and set corresponding boolean element in the array
		// based on puzzle maker's selected cells
		
		for (int i=0; i< row; i++) {
			for (int j=0; j< col; j++) {
				if (buttonArray[i][j].marked)			// marked cell is part of puzzle
					puzzleSolution[i][j] = true;
				else 
					puzzleSolution[i][j] = false;
			}
		}

		//now ask puzzle maker to select the hints that will be visible to the solver
		if (userMode == 1){
			JOptionPane.showMessageDialog(null, "SAVED!\nNow click on black cells that you want to appear as hints to the user");
			userMode = 2;						// set hints
			saveButton.setText("SAVE HINTS");	// change button text to save hints
		}
		else{
			saveHints(row, col);		// save the hints set by puzzle maker
		}

	}



	/**
	 * save puzzle hints to a boolean 2D array
	 * given the dimensions
	 * 
	 * @param row rows
	 * @param col columns
	 */
	public void saveHints(int row, int col) {

		//save the puzzle hints created by the puzzle maker
		// using a boolean 2D array
		
		puzzleProblem = new boolean[row][col];

		//loop through each cell and set corresponding boolean element in the array
		// based on puzzle maker's selected cells

		for (int i=0; i< row; i++) {

			for (int j=0; j< col; j++) {
				if (buttonArray[i][j].hint)				//hinted cell is part of hints
					puzzleProblem[i][j] = true;
				else 
					puzzleProblem[i][j] = false;
			}
		}

		JOptionPane.showMessageDialog(null, "HINTS SAVED!\nNow You can solve the puzzle");
		userMode = 3;			//go to puzzle solver mode

		this.setVisible(false);		//hide
		GUIDriver.menu();			//go back to menu
	}	



	/**
	 * check if current grid is a puzzle
	 * 
	 * @param row rows
	 * @param col columns
	 * @return true or false if current grid is the solution
	 */
	public boolean checkPuzzle(int row, int col) {

		
		//loop through each cell and check user selected cells
		// if they match solution (boolean 2D array)
		for (int i=0; i< row; i++) {

			for (int j=0; j< col; j++) {

				if (puzzleProblem[i][j])					//if given as a hint, mark this cell as good
					buttonArray[i][j].selected = true;

				if (buttonArray[i][j].selected && !puzzleSolution[i][j])	//if users selected grid is not part of solution
					return false;

				if (!buttonArray[i][j].selected && puzzleSolution[i][j])	//if users didn't select a grid which is a solution
					return false;
			}
		}
		return true;	// if it passed all the test, its good
	}


	/**
	 * called when a cell is clicked during puzzle making
	 * and headers needed to be updated
	 * 
	 * @param x x-coordinate of clicked cell
	 * @param y y-coordinate of clicked cell
	 */
	public void changeLabels(int x, int y) {

		// create 2 stacks for the algorithm, one for left one for top header
		Stack<Integer> dkStack = new Stack<Integer>();
		Stack<Integer> dkStack2 = new Stack<Integer>();
		
		/*
		 * ALGORITHM:
		 * 
		 * used a stack to check which cells are selected as part of solution
		 * 
		 * if a cell is selected, push and increment counter
		 * pop if encountered a non-selected cell
		 * get value
		 * 
		 * eg. X_X_ should be "1 1"
		 * 
		 * X_X_
		 * 0123
		 * 
		 * going through index
		 * 0: push 1 (marked)
 		 * 1: pop 1 (not marked)
 		 * 2: push 1 (marked)
 		 * 3: pop 1 (not marked)
 		 * 
 		 * 
 		 * -> "1 1"
		 * 
		 * 
		 */

		dkStack.push(0);
		dkStack2.push(0);
		String t="", u="";
		
		//LEFT HEADER
		//go through each cell, check if marked
		for (int i=0; i< buttonArray[x].length; i++) {
			if (buttonArray[x][i].marked){	
				dkStack.push(dkStack.pop()+1);		// if consecutive marked, increment value
			}
			else {
				int last1 = dkStack.pop();			// else pop and save value

				if (last1 != 0)
					t = t + last1 + " "; 
				
				dkStack.clear();				// reset stack
				dkStack.push(0);
			}
		}

		if (dkStack.peek() != 0)				// after loop, if it's not zero then get
			t = t  + dkStack.pop();
		
		labelArrayLeft[x].setText(t);			// display as the header element text
	
		
		//TOP HEADER (same algorithm as left)
		//go through each cell, check if marked 
		for (int i=0; i< buttonArray.length; i++) {
			if (buttonArray[i][y].marked){	
				dkStack2.push(dkStack2.pop()+1);		// if consecutive marked, increment value
			}
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

		labelArrayTop[y].setText(u);		// display as the header element text

		
	}	// end of changeLabel method

} // end PuzzleGUI class




