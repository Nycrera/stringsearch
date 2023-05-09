package com.nycrera.stringsearch;

import java.io.*;
import java.util.*;

/**
 * String Search!
 *
 */
public class App {
	static List<Integer> file;
	static List<Integer> Pattern = new ArrayList<>();
	static List<Integer> Matches; // Sorted by nature
	static int comparisonCount;

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

		PrintResults("out.html");

		System.out.println("Algorithm: " + args[1]);
		System.out.println("Total Time: " + sw.getElapsedTime() + " ms");
		System.out.println("Total comparisons: " + comparisonCount);
		System.out.println("OutputFile: out.html");

	}

	// Given a list of matches, writes output file with mathing strings marked with
	// <mark> tags.
	public static void PrintResults(String filePath) throws IOException {
		int i = 0;
		int j = 0;
		boolean inTag = false;
		PrintWriter outFile = new PrintWriter(new FileWriter(filePath));
		while (i < file.size()) {
			if (file.get(i) == (int) '<')
				inTag = true; // TagSetter
			if (inTag && file.get(i) == (int) '>')
				inTag = false;

			if (Matches.size() > j && i == Matches.get(j)) {
				if (!inTag) { // Mark if not in html tag, else skip.
					outFile.write("<mark>");
				}
				for (int k = 0; k < Pattern.size(); k++) { // Print pattern
					outFile.write(file.get(i + k));
				}
				if (!inTag) { // Mark if not in html tag, else skip.
					outFile.write("</mark>");
				}

				i += Pattern.size();
				j++;
			} else {
				outFile.write(file.get(i));
				i++;
			}
		}
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
}
