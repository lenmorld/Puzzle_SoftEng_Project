package Objects;
import javax.swing.JButton;


/**
 * this is the base tile, which is clickable
 * and changes colors by clicking
 * 
 * @author Team T 
 *
 */
public class Tile extends JButton {

	private boolean marked;				// cell flag for being marked as part of the puzzle
	private boolean selected;			// cell flag for being currently selected 
	private boolean hint;				// cell flag for being a hint (also implies being puzzle)
	
	//US 5
	private boolean altSol;				// cell flag for being an alternative solution
	
	/**
	 * constructor, init.  flags to false
	 * 
	 * @param label
	 */
	public Tile() {
		super("");
		marked = false;				//set default to not marked (not a part of the puzzle)
		hint = false;				//set default to not a hint 
		selected = false;			//set default to not selected (while playing the game)
		
		altSol = false;
	
	}
	
	
	/**
	 * parameterized constructor
	 * 
	 * @param marked
	 * @param hint
	 * @param selected
	 */
	public Tile(boolean marked, boolean hint, boolean selected) {
		this.marked = marked;
		this.hint = hint;
		this.selected = selected;
	}


	/**
	 * @return the marked
	 */
	public boolean isMarked() {
		return marked;
	}


	/**
	 * @param marked the marked to set
	 */
	public void setMarked(boolean marked) {
		this.marked = marked;
	}


	/**
	 * @return the selected
	 */
	public boolean isSelected() {
		return selected;
	}


	/**
	 * @param selected the selected to set
	 */
	public void setSelected(boolean selected) {
			this.selected = selected;
	}


	/**
	 * @return the hint
	 */
	public boolean isHint() {
		return hint;
	}


	/**
	 * @param hint the hint to set
	 */
	public void setHint(boolean hint) {
		this.hint = hint;
	}


	/**
	 * @return the altSol
	 */
	public boolean isAltSol() {
		return altSol;
	}


	/**
	 * @param altSol the altSol to set
	 */
	public void setAltSol(boolean altSol) {
		this.altSol = altSol;
	}

	
	/**
	 * 
	 * @return get if its altsol
	 */
	public boolean getAltSol() {
		return this.altSol;
	}
	
	
	
}
