package testTrenoLab;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class MyUtils {

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
