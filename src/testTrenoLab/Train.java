package testTrenoLab;

public class Train {
	public String trainNumber;
	public Itinerary itinerary;

	public Train(String identificativo) {
		this.trainNumber = identificativo;
		this.itinerary = new Itinerary();
	}
	
	public int getDelayAtDestination() {
		
		int delay = this.itinerary.delay.get(this.itinerary.delay.size()-1);
		return delay;
	}

}
