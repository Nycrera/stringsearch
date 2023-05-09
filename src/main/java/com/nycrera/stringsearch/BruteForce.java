package com.nycrera.stringsearch;

import java.util.ArrayList;
import java.util.List;

public class BruteForce {
	private int comparisonCount = 0;
	private List<Integer> File;
	private List<Integer> Pattern;
	private List<Integer> Matches = new ArrayList<>();

	public int getComparisonCount() {
		return comparisonCount;
	}
	
	public List<Integer> getMatches(){
		return Matches;
	}

	public BruteForce(List<Integer> inFile, List<Integer> inPattern) {
		File = inFile;
		Pattern = inPattern;
	}

	public void Run() {
		int sLen = File.size();
		int pLen = Pattern.size();
		for (int i = 0; i <= sLen - pLen; i++) {
			boolean match = true;
			for (int j = 0; j < pLen; j++) {
				comparisonCount += 1;
				if (File.get(i + j) != Pattern.get(j)) {
					match = false;
					break;
				}
			}
			if (match) {
				Matches.add(i);
			}
		}
	}

}
