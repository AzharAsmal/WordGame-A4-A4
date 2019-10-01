package com.company;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLOutput;
import java.util.concurrent.CountDownLatch;

import javax.swing.JButton;
import javax.swing.JPanel;

public class WordPanel extends JPanel implements Runnable {
	/** Create instance variables */
	public static volatile boolean done = false;
	private WordRecord[] words;
	private int noWords;
	private int maxY;
	private skeletonSlide slideGUI = new skeletonSlide();
	private Score score;
	private boolean caught = false;
	private boolean[] currentWords;
	private boolean endGame = false;

	/** Method to setup properties for the interface
	 * @param g Graphics object to apply properties to
	 */
	@Override
	public void paintComponent(Graphics g) {
		int width = getWidth();
		int height = getHeight();
		g.clearRect(0,0,width,height);
		g.setColor(Color.red);
		g.fillRect(0,maxY-10,width,height);
		g.setColor(Color.black);
		g.setFont(new Font("Consolata", Font.PLAIN, 26));
		//draw the words
		//animation must be added
		for (int i=0;i<noWords;i++){
			//g.drawString(words[i].getWord(),words[i].getX(),words[i].getY());
			g.drawString(words[i].getWord(),words[i].getX(),words[i].getY()+20);  //y-offset for skeleton so that you can see the words
		}
		   
	}
		
	WordPanel(WordRecord[] words, int maxY) {
		this.words=words; //will this work?
		noWords = words.length;
		done=false;
		slideGUI = new skeletonSlide();
		score = new Score();
		currentWords = new boolean[noWords];
		this.maxY=maxY;
	}
		
	public void run() {
		slideGUI.setFinished(false);
		score.resetScore();
		while (!done){
			for(int n = 0; n < words.length; n++){
				//WordRecord tempRec = words[n];
				while (currentWords[n] == false && !words[n].dropped()){
					words[n].drop(10);
					currentWords[n] = true;
					try{
						Thread.sleep(words[n].getSpeed());
						repaint();
						currentWords[n] = false;
					}
					catch (Exception e){
						System.out.println("Error");
					}
					if(words[n].dropped()){
						score.missedWord();
						words[n].resetWord();
						slideGUI.incrementScore();
						slideGUI.updateGUI();
						repaint();
						break;
					}
					String tempWord = words[n].getWord();
					if(slideGUI.getCurrentInput().equals(tempWord)){
						slideGUI.setCurrentInput("");
						caught = true;
						slideGUI.incrementScore();
						score.caughtWord(tempWord.length());
						slideGUI.updateGUI();
						words[n].resetWord();
						repaint();
						caught = false;
						break;
					}
					repaint();
					break;

				}
			}
			//After all loops complete check for game end
			if (slideGUI.getCaughtWords() == slideGUI.getTotal()){
				done = true;
				slideGUI.setFinished(done);
			}
		}
		for (int m = 0; m < words.length; m++){
			words[m].setY(0);
		}
		repaint();
	}

	public void setEndGame(boolean end) {
		done = end;
	}

	public boolean isDone(){
		return done;
	}
	/*public static void updateScoreBoard(int correctW, int missedW, int score){

	}*/
}


