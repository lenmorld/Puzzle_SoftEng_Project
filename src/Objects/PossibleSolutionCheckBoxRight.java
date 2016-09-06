package Objects;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

/**
 * possible solution check box for rows
 * appears at the right side of the frame
 * 
 * @author team t
 *
 */
public class PossibleSolutionCheckBoxRight extends JPanel {
	
	//################# US 5 ##################
	// Puzzle Solver -  simultaneously consider several potential solutions
	//						for a row (or a column)
	// checkbox for considering alternative solutions
	private PossibleSolutionCheckBox[] psChkBoxRight;

	/**
	 * 
	 * constructor
	 * 
	 */
	public PossibleSolutionCheckBoxRight(int rows) {
		

		// init. and setup checkbox array
		this.setLayout(new GridLayout(rows, 1));
		
		
		psChkBoxRight = new PossibleSolutionCheckBox[rows];	
		
		for (int i=0; i < rows; i++) {
			psChkBoxRight[i] = new PossibleSolutionCheckBox("A.S.");

			psChkBoxRight[i].setName(i + ""); 		// set name to current row
			
			// add action listener / chkbox function
			psChkBoxRight[i].addActionListener(new RightCheckBoxListener() );
			
			this.add(psChkBoxRight[i]);
	
		}
		
	}


	/**
	 * clear all checkboxes
	 */
	public void clear() {
		
		for (int i=0; i < this.psChkBoxRight.length; i++)
		{
			this.psChkBoxRight[i].setChecked(false);
		}

	}
	
	/**
	 * what happens when one of the right checkboxes clicked
	 * 
	 * @author team t
	 *
	 */
	private class RightCheckBoxListener implements ActionListener {


		@Override
		public void actionPerformed(ActionEvent e) {

			PossibleSolutionCheckBox chkBoxClicked = (PossibleSolutionCheckBox)e.getSource();		//find out which checkbox clicked

			int rowClicked = Integer.parseInt(chkBoxClicked.getName());		//get column clicked stored in name
			
			
			//toggle checked - if unchekced, set; if already checked, unset
			chkBoxClicked.setChecked(!chkBoxClicked.isChecked());
			
			//System.out.println("row: " + rowClicked);
			
			//get board that holds this object
			Board board = (Board) chkBoxClicked.getParent().getParent().getParent().getParent().getParent().getParent();		
			
			
			//call board method to enable alt. solutions for currently checked row, and current status (CHECKED or UNCHECKED
			board.getPS().setcheckBoxRightState(rowClicked, chkBoxClicked.isChecked() );

		}
		
	}

}
