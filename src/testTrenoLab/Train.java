package testTrenoLab;

public class Train {
	public String trainNumber;
	public Itinerary itinerary;

	public Train(String identificativo) {
		this.trainNumber = identificativo;
		this.itinerary = new Itinerary();
	}
	
	public int getDelayAtDestination() {
		return itinerary.delay.get(itinerary.delay.size()-1);
	}

}
