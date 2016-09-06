package Objects;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;

/**
 * checkbox for viewing priorities or not
 * 
 * @author team t
 *
 */
public class PrioritiesCheckboxPM extends JCheckBox {

	private boolean checked;
	
	/**
	 * constructor
	 * @param label text of checkbox
	 */
	public PrioritiesCheckboxPM(String label) {
		
		super(label);
		this.checked = false;
		
		this.addActionListener(new PrioritiesChkBoxListener());
	}

	/**
	 * @return the checked
	 */
	public boolean isChecked() {
		return checked;
	}

	/**
	 * @param checked the checked to set
	 */
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	
	/**
	 * inner class that listens to check box clicks
	 * 
	 * @author team t
	 *
	 */
	private class PrioritiesChkBoxListener implements ActionListener {

		/**
		 * what happens when this checkbox is clicked
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			
			PrioritiesCheckboxPM chkBoxClicked = (PrioritiesCheckboxPM)e.getSource();		//find out which checkbox clicked
			
			//toggle checkbox
			//i.e. if false, make true, if true, make false
			chkBoxClicked.setChecked( !chkBoxClicked.isChecked() );
			
			//get board that holds this object
			Board board = (Board) chkBoxClicked.getParent().getParent().getParent().getParent().getParent();		

				
			//if checked, enable priorities
			if (chkBoxClicked.isChecked())
				board.getPM().enablePriorities();
			else		//not checked, disable
				board.getPM().disablePriorities();

		}
		
		
	}
	
	
	
}
