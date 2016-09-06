package Driver;
/*
 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
 
import components.PuzzleSolver;
import components.PuzzleSolverObserver;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
 
/* ListDemo.java requires no other files. */
public class SelectPuzzleList extends JPanel
                      implements ListSelectionListener {
	private static JFrame frame;
	private JList list;
    private DefaultListModel listModel;
    private int[][] entries;
    
    private static final String selectString = "Select";
    private JButton selectButton;
 
    private PuzzleSolver ps = null;
    
    public SelectPuzzleList() {
        super(new BorderLayout());
 
        listModel = new DefaultListModel();
        
        entries = Database.selectPuzzle();
        for(int i = 0; i < entries.length; i++){
        	String f = "";
        	if(entries[i][0] < 10){f = " ";}
        	String s = "ID: " + f + entries[i][0] + "  Complexity: " + entries[i][1] + "  Size: " + entries[i][2] + "x" + entries[i][3];
        	listModel.addElement(s);
        }
 
        //Create the list and put it in a scroll pane.
        list = new JList(listModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setSelectedIndex(0);
        list.addListSelectionListener(this);
        list.setVisibleRowCount(5);
        JScrollPane listScrollPane = new JScrollPane(list);
        listScrollPane.setPreferredSize(new Dimension(200,300));
        
        selectButton = new JButton(selectString);
        selectButton.setActionCommand(selectString);
        selectButton.addActionListener(new SelectListener());

 
        //Create a panel that uses BoxLayout.
        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new BoxLayout(buttonPane,BoxLayout.LINE_AXIS));
        buttonPane.add(selectButton);
        buttonPane.add(Box.createHorizontalStrut(5));
        buttonPane.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
 
        add(listScrollPane, BorderLayout.CENTER);
        add(buttonPane, BorderLayout.PAGE_END);
    }
 
    
    /**
     * inner class listener for PuzzleList
     * 
     * @author team t
     *
     */
    class SelectListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            //This method can be called only if
            //there's a valid selection
            //so go ahead and remove whatever's selected.
            int index = list.getSelectedIndex();
            System.out.println("index = " + (entries[index][0]));
            
    		ps = new PuzzleSolver(Database.loadPuzzle(entries[index][0]));	
    		
			//create PuzzleSolverObserver attaching the PuzzleSolver object ######
			PuzzleSolverObserver pso = 	new PuzzleSolverObserver(ps);
			fHide();
			
            int size = listModel.getSize();
 
            if (size == 0) { //Nobody's left, disable firing.
                selectButton.setEnabled(false);
 
            } else { //Select an index.
                if (index == listModel.getSize()) {
                    //removed item in last position
                    index--;
                }
 
                list.setSelectedIndex(index);
                list.ensureIndexIsVisible(index);
            }
        }
    }
 
    //This method is required by ListSelectionListener.
    public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting() == false) {
 
            if (list.getSelectedIndex() == -1) {
            //No selection, disable select button.
                selectButton.setEnabled(false);
 
            } else {
            //Selection, enable the select button.
                selectButton.setEnabled(true);
            }
        }
    }
 
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    public static void createAndShowGUI() {
        //Create and set up the window.
        frame = new JFrame("ListDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 
        //Create and set up the content pane.
        JComponent newContentPane = new SelectPuzzleList();
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);
 
        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }
    
    public static void fHide(){
    	frame.setVisible(false);
    }
    
} // end class