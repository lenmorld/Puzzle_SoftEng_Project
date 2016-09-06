package old_files;



import javax.swing.JOptionPane;
import javax.swing.UIManager;

/**
 * main GUI driver
 * 
 * @author L_DIMANA
 *
 */
public class GUIDriver {

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
		menu(); 	// call menu
	}

	
	/**
	 * method that provides GUI dialogs to puzzle maker and solver
	 * 
	 */
	public static void menu() {
		
		int choice=-1;

		String code = JOptionPane.showInputDialog(
				null,
				"Choose an option:\n[0] Make Puzzle\n[1] Solve Puzzle\n[2] Exit", 
				"WELCOME TO PUZZLEMAKER 1.0", 
				JOptionPane.QUESTION_MESSAGE
				);

		//implement exception-handling 
		//throw general exception upon invalid input
		try{
			choice = Integer.parseInt(code);
		}
		catch(Exception e) {
			JOptionPane.showMessageDialog(null, "Invalid input");
			System.exit(0);
		}

		if (choice == 0)		//PUZZLE MAKER
		{
			//ask number of rows and columns
			String rows = JOptionPane.showInputDialog(
					null,
					"Enter number of rows", 
					"Number of rows", 
					JOptionPane.QUESTION_MESSAGE
					);


			String cols = JOptionPane.showInputDialog(
					null,
					"Enter number of cols", 
					"Number of columns", 
					JOptionPane.QUESTION_MESSAGE
					);

			try {
				PuzzleMakerGUI m = new PuzzleMakerGUI(Integer.parseInt(rows),Integer.parseInt(cols));
			}
			catch(Exception e) {
				JOptionPane.showMessageDialog(null, "Invalid input");
			}
		}
		else if (choice == 1)		//PUZZLE SOLVER
		{
			if (PuzzleGUI.puzzleProblem == null)
			{
				JOptionPane.showMessageDialog(null, "No puzzles created yet");
				menu();
			}
			else
			{
				PuzzleSolverGUI s = new PuzzleSolverGUI();
			}	
		}
		else		//EXIT
		{
			JOptionPane.showMessageDialog(null, "Thank you for playing");
			System.exit(0);
		}
	}

}
