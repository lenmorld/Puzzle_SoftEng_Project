package Objects;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

/**
 * possible solution check box for columns
 * appears at the bottom of the frame
 * 
 * @author team t
 *
 */
public class PossibleSolutionCheckBoxBottom extends JPanel {

	//################# US 5 ##################
	// Puzzle Solver -  simultaneously consider several potential solutions
	//						for a row (or a column)
	// checkbox for considering alternative solutions
	
	private PossibleSolutionCheckBox[] psChkBoxBottom;
	
	
	/**
	 * constructor
	 */
	public PossibleSolutionCheckBoxBottom(int cols) {
		
		
		// init. and setup checkbox array
		this.setLayout(new GridLayout(1, cols));

		psChkBoxBottom = new PossibleSolutionCheckBox[cols];	
		
		for (int i=0; i < cols; i++) {
			psChkBoxBottom[i] = new PossibleSolutionCheckBox("A.S.");
			psChkBoxBottom[i].setName(i + ""); 		// set name to current col
			psChkBoxBottom[i].addActionListener(new BottomCheckBoxListener() );			//set action listener (button function)

			this.add(psChkBoxBottom[i]);
	
		}	
	}
	
	

	/**
	 * clear all checkboxes
	 */
	public void clear() {
		
		for (int i=0; i < this.psChkBoxBottom.length; i++)
			this.psChkBoxBottom[i].setChecked(false);
	}
	

	/**
	 * inner class that listens to the check box clicks
	 * @author team t
	 *
	 */
	private class BottomCheckBoxListener implements ActionListener {

		
		/**
		 * what happens when one of the bottom checkboxes clicked
		 */
		@Override
		public void actionPerformed(ActionEvent e) {

			

			PossibleSolutionCheckBox chkBoxClicked = (PossibleSolutionCheckBox)e.getSource();		//find out which checkbox clicked

			int colClicked = Integer.parseInt(chkBoxClicked.getName());		//get columng clicked stored in name
			
			
			//toggle checked - if unchekced, set; if already checked, unset
			chkBoxClicked.setChecked(!chkBoxClicked.isChecked());
			
			
			//get board that holds this object
			Board board = (Board) chkBoxClicked.getParent().getParent().getParent().getParent().getParent().getParent().getParent();		
			
			//System.out.println(board.getClass());
			

			//call board method to enable alt. solutions for currently checked column
			board.getPS().setcheckBoxBottomState(colClicked, chkBoxClicked.isChecked());		

		}
		
	}
	
}
