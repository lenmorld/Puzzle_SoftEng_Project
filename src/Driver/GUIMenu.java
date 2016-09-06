package Driver;

import javax.swing.JOptionPane;


import components.PuzzleMaker;
import components.PuzzleMakerObserver;
import components.PuzzleSolver;
import components.PuzzleSolverObserver;
/**
 * the starting GUI Menu that lets user choose
 * which mode to enter
 * Puzzle Maker / Puzzle Solver
 * 
 * @author team t
 *
 */
public class GUIMenu {
	
	/**
	 * method that provides GUI dialog to choose puzzle maker and solver
	 * 
	 */
	public static void menu(PuzzleMaker pm, PuzzleSolver ps) {

		//option strings
		Object[] options = {"Puzzle Maker",
							"Puzzle Solver",
							"Exit"};
		
		//create dialog
		int choice = JOptionPane.showOptionDialog(
				null,
				"Puzzle Maker or Puzzle Solver", 
				"WELCOME TO PUZZLE MAKER/SOLVER 2.0", 
				JOptionPane.YES_NO_CANCEL_OPTION,
				JOptionPane.QUESTION_MESSAGE,
				 null,
				options,
				options[0]
				);

		if (choice == 0)		//PUZZLE MAKER
		{
				
				int rows = -1;		// initialize rows
				
				do {
					try {
					rows = Integer.parseInt(JOptionPane.showInputDialog(
							null,
							"Enter number of rows", 
							"Number of rows", 
							JOptionPane.QUESTION_MESSAGE
							));
					
						if (rows <= 0) throw new Exception();	//boundary limits exception
						if (rows > 25) throw new Exception();
					}
					catch(Exception e) {
						JOptionPane.showMessageDialog(null, "Invalid input. Please enter an integer larger than 0");
					}
						
					//loop until valid number given by user
				}while(rows <= 0 );
				
				
				int cols = -1;		// initialize cols		
				
				do {
					try {
						cols = Integer.parseInt(JOptionPane.showInputDialog(
								null,
								"Enter number of cols", 
								"Number of columns", 
								JOptionPane.QUESTION_MESSAGE
								));
						
						if (cols <= 0) throw new Exception();
						if (cols > 25) throw new Exception();
					}
					catch(Exception e) {
						JOptionPane.showMessageDialog(null, "Invalid input. Please enter an integer larger than 0");
					}
					//loop until valid number given by user
				}while(cols <= 0);
				
				
				if (rows == 1 && cols == 1)
				{
					JOptionPane.showMessageDialog(null, "Must have more than one tile");
					menu(pm,ps);
				}
					
				
				//now we have the valid rows and cols
				
				//subject
				pm = new PuzzleMaker(rows, cols);
				
				//observer
				PuzzleMakerObserver psm = new PuzzleMakerObserver(pm);
	
		}
		else if (choice == 1)		//PUZZLE SOLVER
		{
			//SelectPuzzleList spl = new SelectPuzzleList();
			SelectPuzzleList.createAndShowGUI();
			
			//this code could be eliminated if puzzle is selected, not randomly loaded
			//subject
			//ps = new PuzzleSolver(Database.loadPuzzle());	
			//create PuzzleSolverObserver attaching the PuzzleSolver object ######
			//PuzzleSolverObserver pso = 	new PuzzleSolverObserver(ps);
		}
		else		//EXIT
		{
				exitProgram();
		}
	} // end of menu
	
	
	public static void exitProgram() {
		JOptionPane.showMessageDialog(null, "Thank you for playing");
		System.exit(0);		//exit
	}
	
}
