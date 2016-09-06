package Objects;

import javax.swing.*;


/**
 * each checkbox determines if a current row/column will display
 * possible alternaative solutions
 * 
 * 
 * @author team t
 *
 */
public class PossibleSolutionCheckBox extends JCheckBox {

	
	private boolean checked;
	
	
	/**
	 * 
	 * constructor
	 * 
	 * init. properties
	 * 
	 * @param label
	 */
	public PossibleSolutionCheckBox(String label) {
		
		super(label);
		this.setSize(75, 10);
		//this.setName("PossibleSolutionCheckBox");
		this.setHorizontalAlignment(JCheckBox.LEFT);
		this.setVerticalAlignment(JCheckBox.CENTER);
		
		
		this.checked = false;		//init. to false
		
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
		this.checked = checked;		// set flag to false
		this.setSelected(checked); 	//graphically unchecked
	}
	
	
	
}
