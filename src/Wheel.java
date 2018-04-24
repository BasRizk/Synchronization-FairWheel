import java.util.LinkedList;

/**
 * 
 * @class Wheel is used to simulate the Fair Wheel; it extends thread as it may
 * sleep in case of no players are there to play.
 * 
 */
public class Wheel extends Thread implements Runnable {
	private int capacity;
	private int numOfOnBoard;
	private LinkedList<Player> onBoardPlayers;
	private int maxWaitingTime;
	private EyesOnPlayers operatorEyes;

	public Wheel(int input_maxWaitingTime, Operator operator) {
		this.capacity = 5; // According the descriptions
		this.numOfOnBoard = 0;
		this.onBoardPlayers = new LinkedList<Player>();
		this.maxWaitingTime = input_maxWaitingTime;
		this.operatorEyes = operator.getOperatorEyes();
	}

	
	@Override
	public void run() {
		runRide();
		endRide();

		return;
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
		waitForNextRide();
		
	}
	
	private void waitForNextRide() {
		try {
			Thread.sleep(maxWaitingTime);
			rideLoaded();
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

	public LinkedList<Player> getOnBoardPlayers() {
		return onBoardPlayers;
	}

	public int getMaxWaitingTime() {
		return maxWaitingTime;
	}



}
