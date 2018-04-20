/**
 * 
 * Each thread of @class Player is used to simulate one of the players who are
 * taking the ride.
 * 
 */
public class Player extends Thread {

	private int id;
	private int waitingTime;
	private boolean onBoard;
	private boolean rideComplete;

	public Player(int id, int waitingTime) {
		this.id = id;
		this.waitingTime = waitingTime;
		onBoard = false;
		rideComplete = false;
	}

	/**
	 * A Player thread is initially put to sleep according to its waiting time.
	 * Upon waking up, the player calls the operator to queue for the next ride.
	 */
	@Override
	public void run() {
		try {
			
			sleep(this.waitingTime);
			// TODO call operator to queue for next ride
			
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public int getPlayerId() {
		return id;
	}

	public int getWaitingTime() {
		return waitingTime;
	}


	public boolean isOnBoard() {
		return onBoard;
	}

	public void setOnBoard(boolean onBoard) {
		this.onBoard = onBoard;
	}

	public boolean isRideComplete() {
		return rideComplete;
	}

	public void setRideComplete(boolean rideComplete) {
		this.rideComplete = rideComplete;
	}
	
}
