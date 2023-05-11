package com.nycrera.stringsearch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BoyerMorre {
	private int comparisonCount = 0;
	private List<Integer> File;
	private List<Integer> Pattern;
	private List<Integer> Matches = new ArrayList<>();
	private HashMap<Integer, Integer> badCharacter = new HashMap<>();
	private HashMap<Integer, Integer> goodSuffix = new HashMap<>();

	public int getComparisonCount() {
		return comparisonCount;
	}

	public List<Integer> getMatches() {
		return Matches;
	}

	public HashMap<Integer, Integer> getBadCharacterTable() {
		return badCharacter;
	}

	public BoyerMorre(List<Integer> inFile, List<Integer> inPattern) {
		File = inFile;
		Pattern = inPattern;
		createBadCharacterTable();
		createGoodPrefixTable();
	}

	private void createBadCharacterTable() {
		int patternLength = Pattern.size();

		for (int i = 0; i < patternLength - 1; i++) {
			badCharacter.put(Pattern.get(i), patternLength - 1 - i);
		}

		badCharacter.put(0, patternLength);
	}

	private void createGoodPrefixTable() {
		for (int i=0;i<Pattern.size()-1;i++)
		{
			List<Integer> suffix = Pattern.subList(Pattern.size()-1-i,Pattern.size());
			List<Integer> rest = Pattern.subList(0,Pattern.size()-1); 
			List<Character> charList = new ArrayList<>();

			for (Integer intValue : suffix) {
			    char charValue = (char) intValue.intValue();
			    charList.add(charValue);
			}
			
			List<Character> charList2 = new ArrayList<>();

			for (Integer intValue : rest) {
			    char charValue = (char) intValue.intValue();
			    charList2.add(charValue);
			}

			int d = findSuffix(suffix, rest, Pattern.get(Pattern.size()-2-i));
			
			if (d == -1)
				d = Pattern.size();
			
			
			goodSuffix.put(suffix.size(), d);
		}
		
		
		for (Integer k : goodSuffix.keySet()) {
		    if (goodSuffix.get(k) == Pattern.size()) {
		        int d = findPrefix(k);
		        goodSuffix.put(k, d);
		    }
		}
	}
	
	private int findSuffix(List<Integer> suffix, List<Integer> rest, Integer preChar) {
				if (rest.size() < suffix.size())
					return -1;
				
				int i=rest.size()-1;
				int j=suffix.size()-1;
				boolean match = false;
				
				while (j>-1 && i>-1 && !match)
				{
					do
					{
						if (suffix.get(j)==rest.get(i))
						{
							match = true;
							j--;
							if (j>-1) // fix negative index
								i--;
						}
						else
						{
							match = false;
							j=suffix.size()-1;
						}
						
						if (j>i)
							return -1;
						
					}while (j>-1 && match);
					// check if the found suffix is preceded by the same character value
					if (i > 0 && match && preChar == rest.get(i-1))
					{
						// then, keep searching to start of the string
						match = false;
						j=suffix.size()-1;
					}
					
					if (match)	
						return Pattern.size()-1 -( i+suffix.size()-1);
					
					i--;
				}
				
				return -1;
	}
	
	private int findPrefix(int k) {
		for (int i=k-1; i>0; i--)
		{
			List<Integer> prefix = Pattern.subList(0,i);
			List<Integer> suffix = Pattern.subList(Pattern.size()-i,Pattern.size());
			
			if (prefix.equals(suffix) && goodSuffix.get(suffix.size()) != Pattern.size())
				return Pattern.size()-i;
		}
		
		return Pattern.size();
	}

	public void Run() {
		int stringLength = File.size();
		int patternLength = Pattern.size();
		int i = patternLength - 1; // Align pattern to string, start from rightmost char

		while (i < stringLength) {
			int j = patternLength - 1;

			while (j >= 0) { // keep looking until we find a mismatch or a full match
				comparisonCount++;
				if (Pattern.get(j) == File.get(i - patternLength + 1 + j)) { // Character match
					j--;
				} else { // Mismatch detected
					break;
				}
			}

			if (j == -1) {
				Matches.add(i - patternLength + 1); // Match detected
				i++;
			} else {
				Integer matchedCharacterCount = (patternLength - 1 - j);
				// d1 = Max(badCharacterHueristic - successfulmatchcount, 1)
				Integer badCharacterShift = Math.max(badCharacter.getOrDefault(File.get(i - patternLength + 1 + j), patternLength) - matchedCharacterCount, 1);
				
				Integer goodPrefixShift = goodSuffix.getOrDefault(matchedCharacterCount, -1);
				if(matchedCharacterCount == 0) { // if no match, use d1 as the pattern shift
					i+= badCharacterShift; 
				}else { // else use max(d1,d2)
					i += Math.max(badCharacterShift, goodPrefixShift);
				}
			}
		}
	}

	public void printBadCharacter() {
		System.out.println("--Bad Character Table--");
		for (Map.Entry<Integer, Integer> entry : badCharacter.entrySet()) {
			char charval = (char) entry.getKey().byteValue();
			String character;
			if (charval == 0)
				character = "Any Other";
			else
				character = String.valueOf(charval);
			int shiftAmount = entry.getValue();
			System.out.println(character + " : " + shiftAmount);
		}
		System.out.println("-----------------------");
	}

	public void printGoodPrefix() {
		System.out.println("--Good Prefix Table--");
		for (Map.Entry<Integer, Integer> entry : goodSuffix.entrySet()) {
			int shiftAmount = entry.getValue();
			System.out.println("d("+entry.getKey()+") : " + shiftAmount);
		}
		System.out.println("----------------------");
	}
}
