package old_files;
import javax.swing.JOptionPane;

/**
 * puzzle solver class
 * 
 * @author Team T
 *
 */
public class PuzzleSolverGUI {

	/**
	 * constructor
	 * 
	 */
	public PuzzleSolverGUI() {
		
		// use created puzzle saved in a static variable
		
		int rows = PuzzleGUI.puzzleProblem.length;		// get created puzzle's length
		int cols = PuzzleGUI.puzzleProblem[0].length;
		
		int mode = 1;		//0 for maker, 1 for solver

		// create puzzle grid, passing the created puzzle to initialize marked cells, length, etc
		PuzzleGUI grid1 = new PuzzleGUI(rows,cols,mode, PuzzleGUI.puzzleProblem, PuzzleGUI.labelArrayLeft, PuzzleGUI.labelArrayTop);
		grid1.setVisible(true);	
		
		JOptionPane.showMessageDialog(null, "Click on cells that you think is in the puzzle");
		
	}
	
}
