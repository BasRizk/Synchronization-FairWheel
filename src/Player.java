/**
 * 
 * Each thread of @class Player is used to simulate one of the players who are
 * taking the ride.
 *
 *
 * => Suggested Design:
 * 
 *  A player has four main attributes: ID, waiting time, on-board flag, and a
 * ride-complete flag. Both flags, on-board and ride-complete, are initially false.
 * 
 *  A Player thread is initially put to sleep according to its waiting time.
 * Upon waking up, the player calls the operator to queue for the next ride.
 * 
 */
public class Player extends Thread {

	private int id;
	private int waitingTime;
	private boolean onBoard;
	private boolean rideComplete;

	public Player(int id,int waitingTime) {
		//id and waitingTime will be entered from the input file which could be done in the operator
		this.id = id;  
		this.waitingTime = waitingTime;
		onBoard = false;
		rideComplete = false;
	}
	public void run() {
		//here where we will run the thread player.
	}
	
}
