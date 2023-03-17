package testTrenoLab;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Itinerary {

	public ArrayList<String> trainPath;
	public ArrayList<LocalDateTime> plannedArrival;
	public ArrayList<LocalDateTime> actualArrival;
	public ArrayList<Integer> delay;

	public Itinerary() {
		this.trainPath = new ArrayList<String>();
		this.plannedArrival = new ArrayList<LocalDateTime>();
		this.actualArrival = new ArrayList<LocalDateTime>();
		this.delay  = new ArrayList<Integer>();
	}

	public void addDataRow(String stazione, LocalDateTime previsto, LocalDateTime attuale,int delay) {
		addStation(stazione);
		addPlanned(previsto);
		addActual(attuale);
		addDelay(delay);
	}

	public void addStation(String stazione) {
		this.trainPath.add(stazione);
	}

	public void addPlanned(LocalDateTime previsto) {
		this.plannedArrival.add(previsto);
	}

	public void addActual(LocalDateTime attuale) {
		this.actualArrival.add(attuale);
	}
	public void addDelay(int delay) {
		this.delay.add(delay);
	}

}
