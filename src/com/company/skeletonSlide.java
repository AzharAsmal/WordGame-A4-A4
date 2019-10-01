package com.company;

import javax.swing.*;

public class skeletonSlide extends Thread {

    /** Intializing instance variables */
    private static String currentInput = "";
    private static int totalWords;
    private static int completeWords;
    private static int missedWords;
    private static int score;
    //private static Score Score = new Score();
    private static boolean done = false;

    /** Method to refresh the score keeping GUI */
    public static synchronized void updateGUI(){
        completeWords = Score.getCaught();
        missedWords = Score.getMissed();
        score = Score.getScore();
        WordApp.updateScoreBoard(completeWords, missedWords, score);
    }

    @Override
    public void run(){
         boolean isDone = false;
         while (!isDone){
             if(done){
                 JOptionPane.showMessageDialog(null, "Thank for playing", "Well Done!", JOptionPane.INFORMATION_MESSAGE);
                 isDone = true;
                 done = false;
             }
         }
    }

    //reform

    public synchronized static void setCurrentInput(String word){
        currentInput = word;
        //return currentInput;
    }


    public synchronized static String getCurrentInput(){
        return currentInput;
    }


    public synchronized static int getCaughtWords(){
        return completeWords;
    }


    public synchronized static void resetCompleteWords(){
        completeWords = 0;
    }


    public synchronized static void incrementScore(){
        completeWords++;
    }


    public static int getTotal(){
        return totalWords;
    }


    public static void setTotal(int num){
        totalWords = num;
    }

    // checks if finished

    public synchronized static boolean isFinished(){
        return done;
    }

    // sets if game is finished

    public synchronized static void setFinished(boolean finished){
        done = finished;
    }

}
