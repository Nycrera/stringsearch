package com.nycrera.stringsearch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Horspool {
	private int comparisonCount = 0;
	private List<Integer> File;
	private List<Integer> Pattern;
	private List<Integer> Matches = new ArrayList<>();
	private HashMap<Integer, Integer> badCharacter = new HashMap<>();

	public int getComparisonCount() {
		return comparisonCount;
	}

	public List<Integer> getMatches() {
		return Matches;
	}

	public HashMap<Integer, Integer> getBadCharacterTable() {
		return badCharacter;
	}

	public Horspool(List<Integer> inFile, List<Integer> inPattern) {
		File = inFile;
		Pattern = inPattern;
		createBadCharcterTable();
	}

	private void createBadCharcterTable() {
		int patternLength = Pattern.size();

		for (int i = 0; i < patternLength - 1; i++) {
			badCharacter.put(Pattern.get(i), patternLength - 1 - i);
		}

		badCharacter.put(0, patternLength);
	}

	public void Run() {
		int stringLength = File.size();
		int patternLength = Pattern.size();
		int i = patternLength - 1; // Align pattern to string, start from rightmost char

		while (i < stringLength) {
			int j = patternLength - 1;

			while (j >= 0) {
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
				Integer badCharacterShift = badCharacter.getOrDefault(Pattern.get(patternLength - 1), patternLength);
				i += badCharacterShift;
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
}
