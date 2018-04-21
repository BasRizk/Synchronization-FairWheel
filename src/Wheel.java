import java.util.ArrayList;

/**
 * 
 * @class Wheel is used to simulate the Fair Wheel; it extends thread as it may
 * sleep in case of no players are there to play.
 * 
 */
public class Wheel extends Thread {
	private int capacity;
	private int numOfOnBoard;
	private ArrayList<Player> onBoardPlayers;
	private int maxWaitingTime;
	private EyesOnPlayers operatorEyes;

	public Wheel(int input_maxWaitingTime, Operator operator) {
		this.capacity = 5; // According the descriptions
		this.numOfOnBoard = 0;
		this.onBoardPlayers = new ArrayList<Player>();
		this.maxWaitingTime = input_maxWaitingTime;
		this.operatorEyes = operator.getOperatorEyes();
	}

	/*
	 * The wheel is put to sleep for max wait time upon start.
	 */
	@Override
	public void run() {
		try {
			sleep(maxWaitingTime);
			rideLoaded();
		} catch (InterruptedException e) {
			System.out.println("Interrupt once the ride is loaded at first ride.");
			e.printStackTrace();
		}
	}
	
	
	/**
	 * A method loadPlayers() adds a player thread to the list of on-board players.
	 * 
	 * @param addedPlayer
	 * @returns boolean as indicator if player has been loaded or not.
	 */
	public boolean loadPlayers(Player addedPlayer) {
		
		if(numOfOnBoard < capacity) {
			this.onBoardPlayers.add(addedPlayer);
			numOfOnBoard++;
			return true;
		}
		
		return false;
		
	}

	/**
	 * A method runRide() updates the state of on-board threads to ride-complete.
	 */
	public void runRide() {
		for (Player player : onBoardPlayers) {
			player.setRideComplete(true);
		}
		
	}

	/**
	 * A method endRide() empties the list of on-board players and puts the wheel
	 * to sleep until the next ride is run.
	 * 
	 * The wheel is put to sleep for max wait time upon start.
	 */
	public void endRide() {

		for (Player player : onBoardPlayers) {
			player.setOnBoard(false);
		}
		this.onBoardPlayers.clear();
		numOfOnBoard = 0;
		try {
			sleep(maxWaitingTime);
			//System.out.println("Wheel is saying ride is loaded");
			rideLoaded();
			//System.out.println("Loading signal sent successfully");
		} catch (InterruptedException e) {
			System.out.println("Interrupt after ending the ride.");
			e.printStackTrace();
		}
		
	}
	
	private void rideLoaded() {
		this.operatorEyes.wheelLoaded();
	}
	
	public boolean isFull() {
		return numOfOnBoard == capacity;
	}

	public int getNumOfOnBoard() {
		return numOfOnBoard;
	}

	public ArrayList<Player> getOnBoardPlayers() {
		return onBoardPlayers;
	}

	public int getMaxWaitingTime() {
		return maxWaitingTime;
	}

}
