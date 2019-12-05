package com.bham.pij.assignments.twit;

import java.awt.image.AreaAveragingScaleFilter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TweetCleaner {

	private static ArrayList<String> raw = new ArrayList<String>();
	private static ArrayList<String> cleaned = new ArrayList<String>();
	
	public static void main(String[] args) throws IOException {

		new TweetCleaner();

		System.out.println("Done.");
	}
	
	public TweetCleaner() throws IOException {

		loadRaw();
		
		clean();
		
		saveClean();
	}

	private void clean() {
		
		for (String line: raw) {
			
			String cln = clean(line);

			if (cln != null) {
				
				String[] toks = cln.split(" ");
				
				for (String s: toks) {
					addClean(s);			
				}	
			}
		}
	}

	public String clean(String input) {
		String output = "";
		for (String s : input.split(" ")) {

			//Removes tagged users.
			if (s.startsWith("@")) {
				continue;
			}

			//Removes hashtags
			if (s.startsWith("#")) {
				continue;
			}

			//Removes the RT text
			if (s.toLowerCase().equals("rt")) {
				continue;
			}

			//Cycle through each character to determine whether it has digits or punctuation.
			boolean containDigit = false;
			boolean containsPunctuation = false;
			List<Integer> punctuationLocations = new ArrayList<>();
			int counter = 0;
			for (char c : s.toCharArray()) {
				if (Character.isDigit(c)) {
					containDigit = true;
					break;
				} else if (!Character.isLetterOrDigit(c)) {
					containsPunctuation = true;

					//Adds an index of all punctuation marks in reverse order. That way when removing them from the word, the indexes stay the same.
					punctuationLocations.add(0,counter);
				}
				counter++;
			}

			if (containDigit) {
				continue;
			}

			//If it is a URL, reject (ALL URLs start with either http or https depending on the protocol).
			if (s.startsWith("https://")||s.startsWith("http://")) {
				continue;
			}

			//If it contains punctuation, check it against the exceptions and remove if it isn't included.
			if (containsPunctuation) {
				//If the word is hyphenated, remove the hyphen.
				if (s.contains("-")) {
					if (s.equals("-")) {
						continue;
					}
					s.replace("-","");
				} else {
					//Creating word array list from an array (so elements can be removed)
					List<Character> word = new ArrayList<>();
					for (char c : s.toCharArray()) {
						word.add(c);
					}

					//For everything that was detected as a punctuation mark, remove it except the specified exceptions.
					for (int i : punctuationLocations) {
						if (word.get(i).equals('!')||word.get(i).equals('?')||word.get(i).equals('\'')) {
							continue;
						}
						word.remove(i);
					}

					//Constructing the final word.
					String finalWord = "";
					for (char c : word) {
						finalWord += c;
					}

					//Setting s to the final word.
					s = finalWord;
				}

				//If it is an exclamation but is not at the end, remove.
				if ((!s.endsWith("!") && s.contains("!")) || s.equals("!")) {
					s = s.replace("!","");;
				}

				//If it is not a question but contains a question mark, remove.
				if ((!s.endsWith("?") && s.contains("?")) || s.equals("?")) {
					s = s.replace("?","");
				}
			}

			//If the entire word was deleted, then continue.
			if (s.equals(" ")||s.equals("")) {
				continue;
			}

			output += (s + " ");
		}

		//Remove a trailing space.
		output = output.trim();

		if (output.equals("")) {
			return null;
		}

		return output;
	}
	

	private void addClean(String clean) {
		
		cleaned.add(clean);
	}
	
	private void saveClean() throws FileNotFoundException {
		
		PrintWriter pw = new PrintWriter("cleaned.txt");
		
		for (String s: cleaned) {
			pw.print(s + " ");
		}
		
		pw.close();
		
	}
	
	private void loadRaw() throws IOException {
		
		BufferedReader br = new BufferedReader(new FileReader(new File("donald.txt")));
		
		String line = "";
		
		while ((line = br.readLine())!= null) {
			
			raw.add(line);
		
		}
		
		br.close();
	}
}
