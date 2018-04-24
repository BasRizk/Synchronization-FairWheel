import java.util.LinkedList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * 
 * @class Wheel is used to simulate the Fair Wheel; it extends thread as it may
 * sleep in case of no players are there to play.
 * 
 */
public class Wheel implements Runnable {
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

	/*
	 * The wheel is put to sleep for max wait time upon start.
	 */
	/*
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
	*/
	@Override
	public void run() {
		//onBoardPlayers = operatorEyes.getPlayersQueueForRide();
		//System.out.println("num of on board before run = " + numOfOnBoard );
		runRide();
		endRide();
		//System.out.println("num of on board after run " + numOfOnBoard );

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
			//System.out.println("player added");
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
		//System.out.println("players on ride = " + onBoardPlayers.size());
		CompletableFuture.runAsync(this::waitForNextRide);

		
	}
	
	private void waitForNextRide() {
		try {
			Thread.sleep(maxWaitingTime);
			//System.out.println("Wheel is saying ride is loaded, and just woke up");
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

	public LinkedList<Player> getOnBoardPlayers() {
		return onBoardPlayers;
	}

	public int getMaxWaitingTime() {
		return maxWaitingTime;
	}



}
