package com.nycrera.stringsearch;

import java.io.*;
import java.util.*;

/**
 * String Search!
 */
public class App {
	static List<Integer> file;
	static List<Integer> Pattern = new ArrayList<>();
	static List<Integer> Matches; // Sorted by nature
	static int comparisonCount;
	static int markedMatchCount = 0;
	static int unMarkedMatchCount = 0;

	public static void main(String[] args) throws Exception {
		if (args.length < 3) { // Three args needed
			System.out.println("Usage: stringsearch [filePath] [Algorithm(BF-HP-BM)] [pattern]");
			return;
		}
		file = readFile(args[0]);
		for (int i = 0; i < args[2].length(); i++) { // Convert from string to list of data.
			Pattern.add((int) args[2].charAt(i));
		}

		StopWatch sw = new StopWatch();
		switch (args[1].trim()) {
		case "BF":
			BruteForce bfScanner = new BruteForce(file, Pattern);
			sw.start();
			bfScanner.Run();
			sw.stop();
			Matches = bfScanner.getMatches();
			comparisonCount = bfScanner.getComparisonCount();
			break;
		case "HP":
			Horspool horspoolScanner = new Horspool(file, Pattern);
			sw.start();
			horspoolScanner.Run();
			sw.stop();
			Matches = horspoolScanner.getMatches();
			comparisonCount = horspoolScanner.getComparisonCount();
			horspoolScanner.printBadCharacter();
			break;
		case "BM":
			BoyerMorre BoyerScanner = new BoyerMorre(file, Pattern);
			sw.start();
			BoyerScanner.Run();
			sw.stop();
			Matches = BoyerScanner.getMatches();
			comparisonCount = BoyerScanner.getComparisonCount();
			BoyerScanner.printBadCharacter();
			BoyerScanner.printGoodPrefix();
			break;
		default:
			System.out.println("Invalid Algorithm");
			System.exit(0);
			break;
		}

		PrintResults("out.html"); // Also sets marked and unmarked match counts.

		System.out.println("Algorithm: " + args[1]);
		System.out.println("Total Time: " + sw.getElapsedTime() + " ms");
		System.out.println("Total comparisons: " + comparisonCount);
		System.out.println("Total match count is " + Matches.size());
		System.out.println("OutputFile: out.html");

	}

	// Given a list of matches, writes output file with mathing strings marked with
	// <mark> tags.
	public static void PrintResults(String filePath) throws IOException {
		int i = 0;
		int j = 0;
		boolean inTag = false;
		PrintWriter outFile = new PrintWriter(new FileWriter(filePath));
		StringBuilder sb = new StringBuilder(fileAsString());
		for(int l=Matches.size()-1; l>=0; l--) {
			int start = Matches.get(l);
			int end = start + Pattern.size();
			sb.insert(end,"</mark>");
			while(l >= 1 && (Matches.get(l-1) > Matches.get(l)-Pattern.size())) {
				l--;
			}
			sb.insert(Matches.get(l), "<mark>");
		}
		outFile.write(sb.toString());
		outFile.flush();
		outFile.close();
	}

	public static List<Integer> readFile(String file) {
		List<Integer> chars = new ArrayList<>();
		try (FileReader fr = new FileReader(file); BufferedReader br = new BufferedReader(fr);) {
			int ch;
			while ((ch = br.read()) != -1) {
				chars.add(ch);
			}
		} catch (FileNotFoundException e) {
			System.out.println("Missing html file");
			System.exit(0);
		} catch (IOException e) {
			System.out.println("Empty html file");
			System.exit(0);
		}
		return chars;
	}
	
	private static String fileAsString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int charValue : file) {
            char character = (char) charValue;
            stringBuilder.append(character);
        }
        return stringBuilder.toString();
	}
}
