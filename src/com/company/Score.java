package com.company;

import java.util.concurrent.atomic.AtomicInteger;

public class Score {
	private static int missedWords;
	private static int caughtWords;
	private static int gameScore;
	
	public Score() {
		missedWords=0;
		caughtWords=0;
		gameScore=0;
	}
		
	// all getters and setters must be synchronized
	
	public synchronized static int getMissed() {
		return missedWords;
	}

	public synchronized static int getCaught() {
		return caughtWords;
	}
	
	public synchronized static int getTotal() {
		return (missedWords+caughtWords);
	}

	public synchronized static int getScore() {
		return gameScore;
	}
	
	public synchronized static void missedWord() {
		missedWords++;
	}

	public synchronized static void caughtWord(int length) {
		caughtWords++;
		gameScore+=length;
	}

	public synchronized static void resetScore() {
		caughtWords=0;
		missedWords=0;
		gameScore=0;
	}
}
