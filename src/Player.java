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
	private EyesOnPlayers operatorEyes;

	public Player(int id, int waitingTime, Operator operator) {
		this.id = id;
		this.waitingTime = waitingTime;
		onBoard = false;
		rideComplete = false;
		operatorEyes = operator.getOperatorEyes();
	}

	/**
	 * A Player thread is initially put to sleep according to its waiting time.
	 * Upon waking up, the player calls the operator to queue for the next ride.
	 */
	@Override
	public void run() {
		try {
			
			sleep(this.waitingTime);
			Operator.output = Operator.output + "player wakes up : " + id;
			callOperator(this);
			
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private static synchronized void callOperator(Player playerToQueue) {
		playerToQueue.operatorEyes.addPlayerInQueue(playerToQueue);
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
