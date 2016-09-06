package Driver;
import javax.swing.UIManager;

import components.Puzzle;
import components.PuzzleMaker;
import components.PuzzleSolver;

/**
 * driver class for PuzzleMaker and PuzzleSolver
 * invokes a GUI menu for user to choose which mode
 * 
 * @author team t 
 *
 */
public class Driver {
	
	/**
	 * main method for the executable program
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		
		 try {
			    UIManager.setLookAndFeel( UIManager.getCrossPlatformLookAndFeelClassName() );
			 } catch (Exception e) {
			            e.printStackTrace();
			 }
		 	
		 	//initialize PuzzleMaker and PuzzleSolver objects
			PuzzleMaker pm = null;
			PuzzleSolver ps = null;

		GUIMenu.menu(pm,ps); 	// call menu */
	}
	
	
	/**
	 * 
	 * @param newPuzzle created puzzle from PuzzleMaker
	 */
	public static void saveNewPuzzle(Puzzle newPuzzle) {

		// save puzzle to DB
		Database.savePuzzle(newPuzzle);
	}


}
