package com.nycrera.stringsearch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Horspool {
	private int comparisonCount = 0;
	private List<Integer> File;
	private List<Integer> Pattern;
	private List<Integer> Matches = new ArrayList<>();
	private HashMap<Integer, Integer> badSuffix = new HashMap<>();

	public int getComparisonCount() {
		return comparisonCount;
	}

	public List<Integer> getMatches() {
		return Matches;
	}

	public HashMap<Integer, Integer> getBadSuffixTable() {
		return badSuffix;
	}

	public Horspool(List<Integer> inFile, List<Integer> inPattern) {
		File = inFile;
		Pattern = inPattern;
		createBadSuffixTable();
	}

	private void createBadSuffixTable() {
		int patternLength = Pattern.size();

		for (int i = 0; i < patternLength - 1; i++) {
			badSuffix.put(patternLength - 1 - i, i + 1);
		}

		badSuffix.put(0, patternLength);
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
				}else { // Mismatch detected
					break;
				}
			}

			if (j == -1) {
				Matches.add(i - patternLength + 1); // Match detected
				i++;
			} else {
				Integer badSuffixShift = badSuffix.getOrDefault(patternLength - j - 1, patternLength);
				i += badSuffixShift;
			}

		}
	}
}
