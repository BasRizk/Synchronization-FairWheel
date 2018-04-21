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
	private boolean rideOn;

	public Wheel(int input_maxWaitingTime) {
		this.capacity = 5; // According the descriptions
		this.numOfOnBoard = 0;
		this.onBoardPlayers = new ArrayList<Player>();
		this.maxWaitingTime = input_maxWaitingTime;
	}

	/*
	 * The wheel is put to sleep for max wait time upon start.
	 */
	@Override
	public void run() {
		try {
			sleep(maxWaitingTime);
		} catch (InterruptedException e) {
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
		// TODO updating the state of the players on-board to ride-complete.
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
		// TODO review
		for (Player player : onBoardPlayers) {
			player.setOnBoard(false);
		}
		this.onBoardPlayers.clear();
		numOfOnBoard = 0;
		try {
			sleep(maxWaitingTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
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
	
	public boolean isRideOn() {
		return rideOn;
	}

}
