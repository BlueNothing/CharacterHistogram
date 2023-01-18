

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.math3.stat.Frequency;
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.SwingWrapper;

public class CharacterHistogram {
	
	/*
	 * General strategy - 
	 * Take a text file as input (HTML or the like would also work).
	 * Probably want to treat it as a buffer?
	 * Pull each element from the buffer, identify it, count it, add it to a table.
	 * From there, the idea would be to represent the lines in the table as a histogram or similar measure of frequency.
	 * If the text file is composed of numbers, maybe add a test to approximate Benford's Law validity?
	 * 
	 * The data pulled from the buffer should be stored in a dictionary format of 'character' -> 'count of character'.
	 */
	
	public static void main(String[] args) throws IOException {
		System.out.println("Starting test!");
		boolean caseSensitive = true; //Add support for case-insensitive later.
		try (BufferedReader bufferedReader = new BufferedReader(new FileReader("input.txt"))) {
			Frequency freq = new Frequency();
			String line = bufferedReader.readLine();
			if(line == null) {
				System.out.println("Please ensure that the file you want to count the characters in is placed in the application's root directory as 'input.txt'. \n That is, the directory that contains the folders 'src' and 'bin'.");
			}
			HashMap<Character, Integer> outputRaw = new HashMap<Character, Integer>();
			if(caseSensitive) {
			while(line != null) {
				char[] lineData = line.toCharArray();
				for(char ch : lineData) {
						int count = outputRaw.getOrDefault(Character.valueOf(ch), 0);
						freq.addValue(ch);
						outputRaw.put(Character.valueOf(ch), count + 1);
				}
				line = bufferedReader.readLine();
			}
			} else if(!caseSensitive) {
				while(line != null) {
					char[] lineData = line.toCharArray();
					for(char ch : lineData) {
						Character.valueOf(ch);
						char chOut = Character.valueOf(Character.toUpperCase(ch));
						freq.addValue(chOut);
							int count = outputRaw.getOrDefault(chOut, 0);
							outputRaw.put(Character.valueOf(chOut), count + 1);
					}
					line = bufferedReader.readLine();
				}
			}
			System.out.println(outputRaw.toString());
			CategoryChart chart = new CategoryChartBuilder().width(800).height(600).title("Character Frequency").xAxisTitle("Character").yAxisTitle("Count in Text").build();
			ArrayList<String> keys = new ArrayList<String>();
			ArrayList<Integer> values = new ArrayList<Integer>();
			for(Character ch1 : outputRaw.keySet()) {
				keys.add(ch1.toString());
				values.add(outputRaw.get(ch1));
			}
			Collections.sort(keys);
			chart.addSeries("character distribution", keys, values);
			new SwingWrapper<>(chart).displayChart();
		}
		System.out.println("Test concluded!");
	}
}