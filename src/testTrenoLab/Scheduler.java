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
	
	private final static String HEADER_ACTUAL = "Numero,Destinazione,Ora pianificata,Ora effettiva,Ritardo";
	private final static String HEADER_OUTPUT ="Numero treno, Destinazione, Ora pianificata, Ora effettiva, Ritardo";
	
	public static void start() throws IOException {

		ArrayList<String> dataList = readData("csv/planned.csv", "csv/actual.csv");
		HashMap<String, Train> train = createTrain(dataList);
		actualTrueCsv(train);
		outputCsv(train);

	}
	
	/**
	 * 
	 * @param dataList
	 * @return
	 */
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
				processedTrain.get(trainId).itinerary.addDataRow(parts[1], stringToDate(parts[2]),stringToDate(parts[3]), delay);
			}
		}
		System.out.println(processedTrain);
		return processedTrain;
	}
	
	/**
	 * 
	 * @param pathCsv
	 * @param pathCsv2
	 * @return
	 */
	public static ArrayList<String> readData(String pathCsv, String pathCsv2) {
		ArrayList<String> resultData = new ArrayList<String>();
		String fileName = pathCsv;
		String fileName2 = pathCsv2;
		File file = new File(fileName);
		File file2 = new File(fileName2);

		try {
			Scanner inputStream = new Scanner(file);
			Scanner inputStream2 = new Scanner(file2);
			inputStream.nextLine();
			inputStream2.nextLine();
			while (inputStream.hasNextLine()) {
				String data = inputStream.nextLine();
				String data2 = inputStream2.nextLine();
				String[] parts = data2.split(",");
				String output = parts[3];
				resultData.add(data + "," + output);
			}
			inputStream.close();
			inputStream2.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return resultData;
	}
	
	/**
	 * Methods that transforms a String in a LocalDateTime type
	 * @param stringDate String that stand for a Date
	 * @return
	 */
	public static LocalDateTime stringToDate(String stringDate) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZZZZZ", Locale.ITALIAN);
		LocalDateTime date = LocalDateTime.parse(stringDate, formatter);
		return date;
	}
	
	/**
	 * Methods that transforms a LocalDateTime in a String type
	 * @param date LocalDateTime that need to be printed as a String
	 * @return
	 */
	public static String dateToString(LocalDateTime date) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss", Locale.ITALIAN);
		return date.format(formatter);
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
		MyUtils.writeCSV("csv/actualTRUE.csv", dataLines, HEADER_ACTUAL);
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
		MyUtils.writeCSV("csv/output.csv",dataLines.subList(0, 5),HEADER_OUTPUT);
	}

	public static HashMap<String, Train> orderdMapByDestination(HashMap<String, Train> trainMap) {
		List<Map.Entry<String, Train>> entries = new ArrayList<>(trainMap.entrySet());
		Comparator<Map.Entry<String, Train>> delayComparator = Comparator.comparing(entry -> entry.getValue().getDelayAtDestination());
		delayComparator = delayComparator.reversed();
		Collections.sort(entries, delayComparator);
		HashMap<String, Train> sortedTrainMap = new LinkedHashMap<>();
		for (Map.Entry<String, Train> entry : entries) {
			sortedTrainMap.put(entry.getKey(), entry.getValue());
		}
		return sortedTrainMap;
	}
}
