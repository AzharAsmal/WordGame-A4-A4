package com.company;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.IOException;


import java.util.Locale;
import java.util.Scanner;
import java.util.concurrent.*;
//model is separate from the view.

public class WordApp {
//shared variables
	private static int noWords=4;
	private static int totalWords;

   	private static int frameX=1000;
	private static int frameY=600;
	private static int yLimit=480;

	private static WordDictionary dict = new WordDictionary(); //use default dictionary, to read from file eventually

	private static WordRecord[] words;
	static volatile boolean done;  //must be volatile
	private static Score score = new Score();

	private static WordPanel wordPanel;
	private static Thread[] threads;
	//static Runnable[] runnables;
	private static skeletonSlide slideGUI = new skeletonSlide();
	static Scanner input;

	//Buttons
	private static JButton startBtn, endBtn, restartBtn, exitBtn;
	private static JLabel missedLabel, correctLabel, scoreLabel;

	
	
	
	public synchronized static void setupGUI(int frameX, int frameY, int yLimit) {
		// Frame init and dimensions
    	JFrame frame = new JFrame("WordGame"); 
    	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	frame.setSize(frameX, frameY);
    	
      	JPanel g = new JPanel();
        g.setLayout(new BoxLayout(g, BoxLayout.PAGE_AXIS)); 
      	g.setSize(frameX,frameY);
 
    	//slideGUI = new skeletonSlide();
		wordPanel = new WordPanel(words,yLimit);
		wordPanel.setSize(frameX,yLimit+100);
	    g.add(wordPanel);
	    
	    JPanel txt = new JPanel();
	    txt.setLayout(new BoxLayout(txt, BoxLayout.LINE_AXIS)); 
	    correctLabel =new JLabel("Caught: " + score.getCaught() + "    ");
	    missedLabel =new JLabel("Missed: " + score.getMissed()+ "    ");
	    scoreLabel =new JLabel("Score: " + score.getScore()+ "    ");
	    //mayb try to make labels
	    txt.add(correctLabel);
	    txt.add(missedLabel);
	    txt.add(scoreLabel);
    
	    //[snip]
  
	    final JTextField textEntry = new JTextField("",20);
	    textEntry.addActionListener(evt -> {
		  	String text = textEntry.getText();
		  	slideGUI.setCurrentInput(text);
		  	//[snip]
		  	//add
		  	textEntry.setText("");
		  	textEntry.requestFocus();
	  	});
	   
	    txt.add(textEntry);
	    txt.setMaximumSize( txt.getPreferredSize() );
	    g.add(txt);
	    
	    JPanel b = new JPanel();
	    b.setLayout(new BoxLayout(b, BoxLayout.LINE_AXIS));
	    startBtn = new JButton("Start");

	    // add the listener to the jbutton to handle the "pressed" event
		startBtn.addActionListener(e -> {
			//[snip]
			//add
			startGame();

			textEntry.requestFocus();  //return focus to the text entry field
		});

		restartBtn = new JButton("Restart");
		// add the listener to the jbutton to handle the "pressed" event
		startBtn.addActionListener(e -> {
			//[snip]
			//add
			restartGame(true);
			/**add*/
			textEntry.requestFocus();  //return focus to the text entry field
		});

		endBtn = new JButton("End");;
		// add the listener to the jbutton to handle the "pressed" event
		endBtn.addActionListener(e -> {
			//[snip]
			endGame(true);
		});
		/** Doc need */
		exitBtn = new JButton("Exit");
		exitBtn.addActionListener(e -> {
			//[snip]
			System.exit(0);
		});
		b.add(startBtn);
		b.add(endBtn);
		b.add(restartBtn);
		b.add(exitBtn);
		
		g.add(b);
    	
      	frame.setLocationRelativeTo(null);  // Center window on screen.
      	frame.add(g); //add contents to window
        frame.setContentPane(g);     
       	//frame.pack();  // don't do this - packs it into small space
        frame.setVisible(true);

		
	}

	
private static String[] getDictFromFile(String filename) {
		String [] dictStr = null;
		try {
			Scanner dictReader = new Scanner(new FileInputStream(filename));
			int dictLength = dictReader.nextInt();
			//System.out.println("read '" + dictLength+"'");

			dictStr=new String[dictLength];
			for (int i=0;i<dictLength;i++) {
				dictStr[i]=new String(dictReader.next());
				//System.out.println(i+ " read '" + dictStr[i]+"'"); //for checking
			}
			dictReader.close();
		} catch (IOException e) {
	        System.err.println("Problem reading file " + filename + " default dictionary will be used");
	    }
		return dictStr;

	}

	public static void main(String[] args) {

		input = new Scanner(System.in);

		//deal with command line arguments
		totalWords = Integer.parseInt(args[0]);  //total words to fall
		noWords = Integer.parseInt(args[1]); // total words falling at any point
		slideGUI.setTotal(totalWords);
		assert(totalWords>=noWords); // this could be done more neatly
		String[] tmpDict=getDictFromFile(args[2]); //file of words
		if (tmpDict!=null)
			dict= new WordDictionary(tmpDict);

		WordRecord.dict=dict; //set the class dictionary for the words.
		
		words = new WordRecord[noWords];  //shared array of current words
		
		//[snip]
		
		setupGUI(frameX, frameY, yLimit);  
    	//Start WordPanel thread - for redrawing animation

		int x_inc=(int)frameX/noWords;
	  	//initialize shared array of current words

		for (int i=0;i<noWords;i++) {
			words[i]=new WordRecord(dict.getNewWord(),i*x_inc,yLimit);
		}
	}

	/** Method to set up at game start */
	private static void startGame(){
		startBtn.setVisible(false);
		restartBtn.setVisible(true);
		endBtn.setVisible(true);
		wordPanel.setEndGame(false);
		threads = new Thread[noWords];
		for(int i = 0; i < noWords; i++){
			threads[i] = new Thread(wordPanel);
			threads[i].start();
		}

		slideGUI = new skeletonSlide();
		slideGUI.start();
		//textstuff
	}

	/** Method to restart game when restart is pressed */
	private static void restartGame(boolean restart){
		startBtn.setVisible(false);
		restartBtn.setVisible(true);
		endBtn.setVisible(true);
		wordPanel.setEndGame(false);

		score.resetScore();
		slideGUI.resetCompleteWords();

		threads = new Thread[noWords];
		for(int i = 0; i < noWords; i++){
			threads[i] = new Thread(wordPanel);
			threads[i].start();
		}

		slideGUI = new skeletonSlide();
		slideGUI.start();
		//textstuff
	}

	/** Method to process end of game events */
	private static void endGame(boolean end){
		updateScoreBoard(score.getCaught(), score.getMissed(), score.getScore());
		restartBtn.setVisible(false);
		startBtn.setVisible(end);
		wordPanel.setEndGame(end);
		Score.resetScore();

		slideGUI.resetCompleteWords();
	}

	public static void updateScoreBoard(int correctW, int missedW, int score){
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				correctLabel.setText(String.format("Caught: %d  ", correctW));
				missedLabel.setText(String.format("Missed: %d  ", missedW));
				scoreLabel.setText(String.format("Score: %d  ", score));
			}
		});
	}

}
