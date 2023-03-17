package testTrenoLab;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class MyUtils {

	/**
	 * Given a List<String[]> and a Header and a path, it will print a .csv file in the desired outpath
	 * @param filePath String Where the output file need to be printed from the project directory
	 * @param dataLines List<String[]> A list of String needed to be printed
	 * @param header String A String that will be printed for 
	 */
	public static void writeCSV(String filePath, List<String[]> dataLines, String header) {
		StringBuilder csvString = new StringBuilder();
		csvString.append(header + "\n");
		for (String[] row : dataLines) {
			for (int i = 0; i < row.length; i++) {
				csvString.append(row[i]);
				if (i != row.length - 1) {
					csvString.append(",");
				}
			}
			csvString.append("\n");
		}

		try (FileWriter writer = new FileWriter(filePath)) {
			writer.write(csvString.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
