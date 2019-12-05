package com.bham.pij.assignments.twit;

import java.util.ArrayList;
import java.util.Random;

public class Word {
	
	/**
	 * The word represented by this object.
	 * Regardless of how many times the word is found in the text, it is represented
	 * by one object only. 
	 */
	private String word;
	/**
	 * The frequency with which this word appears in the file.
	 */
	private int frequency;
	/**
	 * A list of words (represented as strings) that follow this word in the text. 
	 */
	private ArrayList<String> followers;
	/**
	 * A random number generator.
	 */
	private Random rand = new Random();
	
	/**
	 * Constructor. Builds this word from a String representing the word. 
	 * @param word The word represented.
	 */
	public Word(String word) {
		this.word = word;
		followers = new ArrayList<String>();
	}
	
	/**
	 * Adds a word (as a String) that follows this word in the text.
	 * For example, if this word is 'the' and the word 'best' is found to follow it, 
	 * 'best' would be added as a follower.
	 * @param follower The word that follows this word in the text.
	 */
	public void addFollower(String follower) {
		followers.add(follower);
	}
	
	/**
	 * Gets a random follower.
	 * This method returns one of this word's followers, chosen randomly.
	 * @return A random follower.
	 */
	public String getRandomFollower() {
		
		if (followers.size() == 0) {
			return null;
		}
		
		if (followers.size() == 1) {
			return followers.get(0);
		}
		
		int r = rand.nextInt(followers.size()-1);
		
		return followers.get(r);
	}

	/**
	 * This method increments the frequency of occurrence of this word.
	 * This method should be called every time this word is found in the text.
	 */
	public void incrementFrequency() {
		frequency = getFrequency() + 1;
	}
	
	/**
	 * Returns the word represented by this object.
	 * @return The word.
	 */
	public String getWord() {
		return word;
	}
	
	/**
	 * Gets the frequency with which this word appears in the text. 
	 * @return The frequency.
	 */
	public int getFrequency() {
		return frequency;
	}
}
