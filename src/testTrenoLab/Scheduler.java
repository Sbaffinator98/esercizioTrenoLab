package testTrenoLab;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;

public class Scheduler {

	public static void start() throws IOException {

		ArrayList<String> dataList = readData("csv/planned.csv", "csv/actual.csv");
		HashMap<String, Train> train = createTrain(dataList);
		actualTrueCsv(train);
		outputCsv(train);

	}

	private static HashMap<String, Train> createTrain(ArrayList<String> dataList) {
		HashMap<String, Train> processedTrain = new HashMap();
		for (int i = 0; i < dataList.size(); i++) {
			String dataRow = dataList.get(i);

			String[] parts = dataRow.split(",");
			String trainId = parts[0];
			if (!processedTrain.containsKey(trainId)) {

				Train newTrain = new Train(trainId);
				int delay = (int) ChronoUnit.SECONDS.between(stringToDate(parts[2]), stringToDate(parts[3]));
				newTrain.itinerary.addDataRow(parts[1], stringToDate(parts[2]), stringToDate(parts[3]), delay);
				processedTrain.put(trainId, newTrain);
			} else {
				int delay = (int) ChronoUnit.SECONDS.between(stringToDate(parts[2]), stringToDate(parts[3]));
				processedTrain.get(trainId).itinerary.addDataRow(parts[1], stringToDate(parts[2]),
						stringToDate(parts[3]), delay);
			}

		}
		System.out.println(processedTrain);
		return processedTrain;
	}

	public static ArrayList<String> readData(String pathCsv, String pathCsv2) {
		ArrayList<String> resultData = new ArrayList<String>();
		// -define .csv file
		String fileName = pathCsv;
		String fileName2 = pathCsv2;
		// -File class needed to turn fileName to actual file
		File file = new File(fileName);
		File file2 = new File(fileName2);

		try {
			// read with Scanner class
			Scanner inputStream = new Scanner(file);
			Scanner inputStream2 = new Scanner(file2);
			// hashNext() loops line-by-line
			inputStream.nextLine();
			inputStream2.nextLine();
			while (inputStream.hasNextLine()) {
				String data = inputStream.nextLine();
				String data2 = inputStream2.nextLine();
				String[] parts = data2.split(",");
				String output = parts[3];
				resultData.add(data + "," + output);
			}
			// after loop, close scanner
			inputStream.close();
			inputStream2.close();

		} catch (FileNotFoundException e) {

			e.printStackTrace();
		}

		return resultData;

	}

	public static LocalDateTime stringToDate(String stringDate) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZZZZZ", Locale.ITALIAN);
		LocalDateTime date = LocalDateTime.parse(stringDate, formatter);

		return date;
	}

	public static String dateToString(LocalDateTime date) {
		// i suppose the day of arrival is ALWAYS on the same day.
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss", Locale.ITALIAN);
		String stringDate = date.format(formatter);

		return stringDate;
	}

	public static void actualTrueCsv(HashMap<String, Train> trainMap) throws IOException {
		List<String[]> dataLines = new ArrayList<>();
		trainMap.forEach((key, value) -> {
			for (int i = 0; i < value.itinerary.trainPath.size(); i++) {
				dataLines.add(new String[] { value.trainNumber, value.itinerary.trainPath.get(i),
						dateToString(value.itinerary.plannedArrival.get(i)),
						dateToString(value.itinerary.actualArrival.get(i)),
						String.valueOf(value.itinerary.delay.get(i)) });
			}
		});

		MyUtils.writeCSV("csv/ActualTRUE.csv", dataLines, "Numero,Destinazione,Ora pianificata,Ora effettiva,Ritardo");
	}

	public static void outputCsv(HashMap<String, Train> trainMap) throws IOException {
		List<String[]> dataLines = new ArrayList<>();

		HashMap<String, Train> sortedTrainMap = orderdMapByDestination(trainMap);

		sortedTrainMap.forEach((key, value) -> {

			int i = value.itinerary.delay.size() - 1;
			dataLines.add(new String[] { value.trainNumber, value.itinerary.trainPath.get(i),
					dateToString(value.itinerary.plannedArrival.get(i)),
					dateToString(value.itinerary.actualArrival.get(i)), String.valueOf(value.itinerary.delay.get(i)) });

		});

		// Once the List is ordered by the delay of the train at destination, i take the
		// first 5 element
		MyUtils.writeCSV("csv/output.csv",dataLines.subList(0, 5),
				"Numero treno, Destinazione, Ora pianificata, Ora effettiva, Ritardo");
	}

	public static HashMap<String, Train> orderdMapByDestination(HashMap<String, Train> trainMap) {
		// Create a list of the entries in the trainMap
		List<Map.Entry<String, Train>> entries = new ArrayList<>(trainMap.entrySet());

		// Define a Comparator that compares the delay property of two Train objects in
		// descending order
		Comparator<Map.Entry<String, Train>> delayComparator = Comparator
				.comparing(entry -> entry.getValue().getDelayAtDestination());
		delayComparator = delayComparator.reversed();
		// Sort the entries in the list using the delayComparator
		Collections.sort(entries, delayComparator);

		// Create a LinkedHashMap to preserve the order of the sorted entries
		HashMap<String, Train> sortedTrainMap = new LinkedHashMap<>();
		for (Map.Entry<String, Train> entry : entries) {
			sortedTrainMap.put(entry.getKey(), entry.getValue());
		}
		return sortedTrainMap;
	}
}
