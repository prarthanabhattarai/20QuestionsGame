/**
 * Game Application
 */
import java.util.Scanner;

import javax.swing.JFrame;

import ListsAndStacks.BinTree;
import ListsAndStacks.BinTreeNode;
/**
 * @author Prarthana
 * Images obtained from http://www.blindenreport.de/wp-content/css/cartoon-globe.html
 *
 */
public class GameApplication {
	
	/***Fields***/
	static BinTree currentTree;
	static BinTreeNode currentNode;
	
	/**Main starts the game
	 * @param args
	 */
	public static void main(String[] args) {
		
		//Create a JFrame to hold the game Panel
		JFrame gameFrame = new JFrame("Play the Guessing Game!");
		
		//set size
		gameFrame.setSize(800, 600);
		
		//add an instance of gamePanel to gameFrame
		gameFrame.add(new GameGUIPanel());
		
		//exit normally on closing the window
		gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//show frame
		gameFrame.setVisible(true);
	 
	}

}
