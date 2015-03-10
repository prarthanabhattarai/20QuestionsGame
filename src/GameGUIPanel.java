import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.swing.*;
import javax.imageio.*;

import java.lang.Object;
import java.net.URL;



//import from the new package
import ListsAndStacks.*;

/**GameGUIPanel encapsulates the GUI for the gama
 * @author Prarthana
 * Images obtained from http://www.blindenreport.de/wp-content/css/cartoon-globe.html
 */
public class GameGUIPanel extends JPanel implements ActionListener{
	
	
	/***Fields***/
	//import images files that will be used as icons
	//instantiate ImageIcon
	ImageIcon globeImage = new ImageIcon (getClass().getResource("world.gif"));
	ImageIcon earthImage = new ImageIcon (getClass().getResource("earth.jpg"));
	ImageIcon ansImage = new ImageIcon (getClass().getResource("answer.jpeg"));
	ImageIcon vicImage = new ImageIcon (getClass().getResource("vic.jpg"));

	//tree fields and treeNode fields to keep track of questions and answers
	static BinTree currentTree;
	static BinTreeNode currentNode;
	static BinTreeNode prevNode;
	DefaultBinTreeNode  newTreeNode;

	//create a JLabel that stores the pre-specified list of countries 
	JLabel countries;
	
	/***Constructor***/
	
	/**sets up GUI and initiates the game
	 * 
	 */
	public GameGUIPanel(){
		
		//invoke super class constructor
		super();
		
		//set background color to white
		setBackground(Color.WHITE);
		
		//initiate GUI
		initGUI();
		
		//initiateGame
		initGame();
		
		//set the Panel to be visible
		setVisible(true);
		
	}
	
	/***Methods***/
	
	/**
	 * initiates GUI for the game;
	 * Creates JLabels and JButton and add it to the panel
	 */
	public void initGUI(){
		//use the border layout
		setLayout(new FlowLayout());
		
		//create JLabel to store globe's image
		JLabel label = new JLabel();
		label.setIcon(globeImage);
		
		//create JLabel to show instructions for the game
		JLabel instructions = new JLabel("Pick a country. Click on play button when you are ready!");
		
		//create JLabel to show pre-specified list of countries
		countries = new JLabel("<html>1. Australia<br>2. Mexico<br>3. Scotland<br>4. Canada<br>5. China<br>6. Japan<br>7. Brazil<br>8. Zimbabwe<br>9. Ecuador<br>10. Argentina<br>11. New ZeaLand<br>12. Peru<br>13. Botswana<br>14. Italy<br>15. France<br>16. Romania<br>17. Sweden<br></html>");
		countries.setFont(new Font("Curlz MT", 2, 20));
		
		//create Jbutton that allows user to start the game
		JButton startButton = new JButton ("Play the game!");
		//add action listener to JButton
		startButton.addActionListener(this);
		
		//Add JComboBox that allows user to choose which version to play
		//invoke helper method to create a JComboBox
		add(createComboBox());
		
		//Add the previously created JLabels and JButtons to the Panel
		add(label);
		add(countries);
		add(startButton);
		add(instructions);
	}
	
	/**
	 * initiates currentTree and currentNode variables for the game;
	 */
	public void initGame(){
		
		//set currentTree to be the tree stored to countries.xml file
		currentTree = CommutativeExpressionReader.readCommutativeExpr( "countries.xml" );
		
		//set current node to be the root of current Tree
		currentNode = currentTree.getRoot();
	}
	
	/**
	 * Action Listener for the button to start game;
	 */
	public void actionPerformed(ActionEvent e) {
		
		//set current Node the the root of the current Tree
		currentNode = currentTree.getRoot();	
		
		//invoke helper method to show options to the users
		//the game starts here
		showOptions();		
	}
	
	
	
	/**
	 * recursive method to show options to the user
	 * allows user to play the game
	 */
	public void showOptions(){
		 
		//options is an array of strings, representing each option
		String[] options={"no","yes"};	 
		
		 //base case: if current node is a leaf,
		 if (currentNode.isLeaf()){
			 
			//show that user that computer's guess is the data in currentNode
			//prompt user to respond if the guess was right
			//store user's response in choice
			int choice= JOptionPane.showOptionDialog(this,"You picked "+currentNode.getData()+", is that right? ","My Guess",0,1, ansImage,options,null);
			
			//based on user's response
			switch(choice){
			
			//if the guess was wrong,
			case 0:
				//System.out.println("You chose No");
				
				//allow user to enter new question to determine the country correctly
				//invoke helper function for this				
				enterQuestion();
				
				break;
				
			//if the guess was right	
			case 1:				
				//System.out.println("Yes!! I win!");
				
				//show a message to the user that computer has won the game
				JOptionPane.showMessageDialog(new JFrame(), "Yes, I win the game!!", "Victory", JOptionPane.INFORMATION_MESSAGE, vicImage);
				
				break;
			}		
		 }

		 
		 //recursive case: if the current node is not a leaf,
		 //there are more questions to be answered
		 else {
			 
			//prompt user to answer a question about the country picked
			//store user's answer as result
			int result = JOptionPane.showOptionDialog(this,currentNode.getData(),"Answer the new question",0,1, earthImage,options,null);
			
			//based on result,
			switch (result){
			
			//if the user's answer was no,
			 case 0:
			   //System.out.println("You chose No");
				 
			   //set the current node to be previous node
			   prevNode = currentNode;
			   
			   //set the current node to be the current node's right child
			   currentNode=currentNode.getRightChild();
	           
	           //invoke the function recursively
			   showOptions();
	           break;
	         
	         //if the user's answer was yes
			 case 1:
			   //System.out.println("You chose Yes");	 
			
			   //set the current node to be previous node
			   prevNode = currentNode;
			   
			   //set the current node to be the current node's left child
	    	   currentNode=currentNode.getLeftChild();
	           
	    	   //invoke the function recursively
	           showOptions();
	           break;
			 }
		 }	
	}
	
	/**
	 * helper method that allows user to enter new questions
	 * when computer's guess is incorrect
	 */
	public void enterQuestion(){
		
		//prompt user to enter which country they had picked
		//store the input as userGuess
		String userGuess = JOptionPane.showInputDialog(this, "What country were you thinking?");

		//prompt user to enter a yes/no question for the country
		//store the input as question
		String question = JOptionPane.showInputDialog(this, "Enter a yes/no question that would determine your country");

		//prompt user to enter right answer for the question
		//store the input as answer
		String answer = JOptionPane.showInputDialog(this, "Is the answer to your question yes or no?");
		
		//proceed further only if user has entered the three required inputs
		//this prevents null pointer errors when cancel button is pressed
		if (answer!=null && question!=null && userGuess!=null){
		
			//create a new tree node only if user entered valid inputs
			//this prevents forming of new tree nodes that have empty data
			if (!answer.isEmpty() && !question.isEmpty() && !userGuess.isEmpty()){
			
			//create newTreeNode with root as the question entered by user
			newTreeNode = new DefaultBinTreeNode(question);
			
			//if the right answer to the question is yes
			if (answer.equalsIgnoreCase("yes")){
				
				//set path to be taken if the answer is yes
				newTreeNode.setLeftChild(new DefaultBinTreeNode (userGuess));
				
				//set path to be taken if the answer is no
				newTreeNode.setRightChild(new DefaultBinTreeNode (currentNode.getData()));
				
			}
			
			//if the right answer to the question is no
			if (answer.equalsIgnoreCase("no")){
				
				//set path to be taken if the answer is yes
				newTreeNode.setLeftChild(new DefaultBinTreeNode (currentNode.getData()));
				
				//set path to be taken if the answer is no
				newTreeNode.setRightChild(new DefaultBinTreeNode (userGuess));
			}
			
			//invoke helper method to place the newly formed tree node to current tree
			placeNewTreeNode();
			}
		}
		
		//System.out.println(currentTree.inorderString());
		CommutativeExpressionWriter.writeCommutativeExpr( currentTree, "Countries_New.xml" );
	}

	/**
	 * helper method to place a new Tree Node to a Binary Tree
	 */
	public void placeNewTreeNode(){
		//if the current node was previous node's right child
		if (currentNode.equals(prevNode.getRightChild())){
			
			//replace the previous node's right child with newTreeNode
			prevNode.setRightChild(newTreeNode);}
		
		//if the current node was previous node's left child
		if (currentNode.equals(prevNode.getLeftChild())){
			
			//replace the previous node's left child with newTreeNode
			prevNode.setLeftChild(newTreeNode);}
	}
	
	/**
	 * Helper method to create ComboBox that allows user to choose which version to play
	 * returns JComboBox
	 */
	public JComboBox createComboBox(){
		
		//create array of strings that shows available options
		String[] versions = { "Version 1", "Version 2"};

		//create a comboBox that contains versions as options
		JComboBox versionList = new JComboBox(versions);
		
		//default option is the fisrt choice, Version 1
		versionList.setSelectedIndex(0);
		
		//add action listener to the comboBox
		versionList.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				
				//get the version chosen by the user
				JComboBox cb = (JComboBox)e.getSource();
		        String userChoice = (String)cb.getSelectedItem();
		        
		        //if version 1 is chosen, set the label countries to be visible
		        if (userChoice.equals("Version 1")){
		        	countries.setVisible(true);
		        }
		        
		        //if version 1 is chosen, set the label countries to be invisible
		        if (userChoice.equals("Version 2")){
		        	countries.setVisible(false);
		        }		
			}
			
		});
		//return the comboBox
		return versionList;
	}

}
