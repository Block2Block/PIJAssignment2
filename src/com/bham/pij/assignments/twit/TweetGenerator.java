package com.bham.pij.assignments.twit;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TweetGenerator {
    
    private static final int TWEET_LENGTH = 30;
    private static ArrayList<Word> words;
    private static Random random = new Random();
    
    public static void main(String[] args) throws IOException {

    	new TweetGenerator();
    	
        System.out.println("Done.");
    }
    
    public TweetGenerator() throws IOException {

        ArrayList<String> cleaned = loadData();
        
        words = findWords(cleaned);
        
        System.out.println(createTweet(TWEET_LENGTH));
    }
    
    private ArrayList<String> loadData() throws IOException {
        
        ArrayList<String> data = new ArrayList<String>();
        
        BufferedReader br = new BufferedReader(new FileReader(new File("cleaned.txt")));
        
        String line = "";
        
        while ((line = br.readLine())!= null) {
            
            String[] tokens = line.split(" ");
            
            for (String t: tokens) {
                data.add(t);
            }
        }
        
        br.close();
        
        return data;
        
    }
    
    public String createTweet(int numWords) {

        //Create a string builder to more easily create a string.
        StringBuilder sb = new StringBuilder();
        if (numWords < 1) {
            return null;
        }

        //Get the first word and append it to the string builder.
        Word previousWord = words.get(random.nextInt(numWords-1));
        sb.append(previousWord.getWord() + " ");

        //While we haven't hit the word limit, get a random follower and append it.
        for (numWords--;numWords > 0;numWords--) {
            String follower = previousWord.getRandomFollower();

            //Get a new random word and set this to word to the follower.
            if (follower == null) {
                follower = words.get(random.nextInt(numWords-1)).getWord();
            }

            //Append the new follower, and set the previous word to the current follower.
            sb.append(follower + " ");
            previousWord = getWord(follower);
        }

        //Get the built string and trim trailing and leading spaces.
        return sb.toString().trim();
    }

    private Word getWord(String word) {
        
        for (Word w: words) {
            if (w.getWord().equalsIgnoreCase(word)) {
                return w;
            }
        }
        return null;
    }

    public ArrayList<Word> findWords(ArrayList<String> cleaned) {
        //Create the tracker to track which words have been found.
        ArrayList<Word> addWords = new ArrayList<>();
        ArrayList<String> foundWords = new ArrayList<>();

        //Go through each of the cleaned words.
        int counter = 0;
        for (String s : cleaned) {
            s = s.toLowerCase();

            //If the word has already been found (it already has a word object).
            if (foundWords.contains(s)) {
                //Get the word object (which is returned as a reference so I do not need to put it back in) and increment the value and add a follower.
                Word word = addWords.get(foundWords.indexOf(s));
                word.incrementFrequency();
                if (cleaned.size() != (counter + 1)) {
                    word.addFollower(cleaned.get(counter + 1));
                }

            } else {
                //Create a new word object, set its follower (providing that it isn't the last item in the list) and add it to the list of found words.
                Word word = new Word(s);
                if (cleaned.size() != (counter + 1)) {
                    word.addFollower(cleaned.get(counter + 1));
                }
                word.incrementFrequency();
                addWords.add(word);
                foundWords.add(s);
            }

            counter++;
        }

        return addWords;
    }
}
