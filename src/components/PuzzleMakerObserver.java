package components;


/**
 * 
 * OBSERVER PATTERN
 * 
 * Subject (Model and Controller) - PuzzleMaker
 * Observer (View) - PuzzleMakerObserver
 * 
 * Observer pattern establishes a good design by decoupling 
 * the Views from the Observers
 * - one-to-many dependency between subject and (possibly )multiple
 * 	  observers (dependents) that are notified and updated automatically
 * - MODEL implements the mechanics
 * - VIEWS implemented as Observers that are uncoupled as possible
 * 		from the Model components
 * 
 * ===========================================
 * 
 * PuzzleMakerObserverClass
 * 
 * acts as VIEW, Observes the PuzzleMaker and displays the
 * changes in its states
 * 
 * @author team t
 *
 */
public class PuzzleMakerObserver {
	
	protected PuzzleMaker subject;
	
	public PuzzleMakerObserver (PuzzleMaker subject) {
		this.subject = subject;
		this.subject.attach(this);			//attach this observer to the sibject
	}
	
	
	/**
	 * what the observers will show/update upon notification
	 * of the subjects
	 * 
	 * several types of update
	 * 
	 * 1. LEFT_HEADER
	 * 		left header updates based on selected tiles
	 * 
	 * 2. TOP_HEADER
	 * 		top header updates based on selected tiles
	 * 
	 * 3. ROW_PRIORITY
	 * 		row priorities
	 * 
	 * 4. COL_PRIORITY
	 * 		column priorities
	 * 
	 * 
	 */
	public void update(String updateType) {
		
		if (updateType.equals("LEFT_HEADER")) {
			displayLeftHeader();
		}
		else if (updateType.equals("TOP_HEADER"))  {
			displayTopHeader();
		}
				//PRIORITIES 
		else if (updateType.equals("ROW_PRIORITY")) {
			displayRowPriorities();
		}
		else if (updateType.equals("COL_PRIORITY")) {
			displayColPriorities();
		}
	}
	
	
	/**
	 * display left header getting constraints array from board
	 */
	public void displayLeftHeader() {
		String[] leftHeader = this.subject.getPuzzleBoard().getHeaderLeftS();

		for (int i=0; i < leftHeader.length; i++) 
			this.subject.getPuzzleBoard().getHeaderLeft().setText(i,  leftHeader[i]);		
	}
	
	/**
	 * display top header getting data from board
	 */
	public void displayTopHeader() {
		String[] topHeader = this.subject.getPuzzleBoard().getHeaderTopS();
		
		for (int i=0; i < topHeader.length; i++)
			this.subject.getPuzzleBoard().getHeaderTop().setText(i,  topHeader[i]);
	}
	

	
	/**
	 * display the row priorities
	 * based on the saved state - rows priority array
	 * 
	 */
	public void displayRowPriorities() {
		
		//display priorities in the right header
		
		//go through each label in the label Array, and set its text
		//from the rowPriorities array computed before
		//e.g. array looks like this
		// 2 0 1 3
		//which means row#2 is 1st priority, row#0 is 2nd, ...
		
		for (int i=0; i < this.subject.getRowPriorities().length; i++) {

			//i+1 bec. its 0-based, priority starts at #1
			this.subject.getPuzzleBoard().setRowPriorityText(this.subject.getRowPriorities()[i],  i );
		}	
	}
	
	
	/**
	 * display the column priorities
	 * based on the saved state - col priority array
	 * 
	 */
	public void displayColPriorities() {
		
		for (int i=0; i < this.subject.getColPriorities().length; i++) {

			//i+1 bec. its 0-based, priority starts at #1
			this.subject.getPuzzleBoard().setColPriorityText(this.subject.getColPriorities()[i],   i );
		}
		
	}
	
	

}
