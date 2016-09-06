package old_files;
import java.util.Scanner;

import javax.swing.JOptionPane;

/**
 * 
 * puzzle maker class
 * 
 * @author Team T
 *
 */
public class PuzzleMakerGUI {

	/**
	 * constructor
	 * 
	 * @param rows rows
	 * @param cols columns
	 */
	public PuzzleMakerGUI(int rows, int cols) {

		int mode = 0;			//0 for maker, 1 for solver

		// create a new puzzle grid, giving null as the other attributes to start creating
		PuzzleGUI grid1 = new PuzzleGUI(rows,cols,mode, null, null, null);
		grid1.setVisible(true);	
		
		JOptionPane.showMessageDialog(null, "Click on cells to set it as part of the puzzle");
	}
	
}
