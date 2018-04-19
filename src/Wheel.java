import java.util.ArrayList;

/**
 * 
 * @class Wheel is used to simulate the Fair Wheel; it extends thread as it may
 * sleep in case of no players are there to play.
 *
 *
 * => Suggested Design:
 *
 *  Wheel has four main attributes: capacity, count of currently on-board
 * players, list of currently on-board players and the maximum waiting time of
 * the wheel.  The wheel is put to sleep for max wait time upon start.
 *  A method load players() adds a player thread to the list of on-board players.
 *  A method run ride() updates the state of on-board threads to ride-complete.
 *  A method end ride() empties the list of on-board players and puts the wheel
 * to sleep until the next ride is run.
 */
public class Wheel extends Thread {
	private int capacity;
	private int numOfOnBoard;
	private ArrayList<Player> onBoardPlayers;
	private int maxWaitingTime;

	public Wheel() {
		capacity = 5; //the wheel has a capacity according to the description
		numOfOnBoard = 0; //the wheel is empty when first created
		onBoardPlayers = new ArrayList<Player>(); //Initialisation of the array of players 
		//maxWaitingTime to be determined according to the input file
	}
	@Override
	public void run() {
		//here where we will run the thread
	}
	public void loadPlayers(Player addedPlayer) {
		//TODO loading a player to the on-board players from the operator.
	}
	public void runRide() {
		//TODO updating the state of the players on-board to ride-complete.
	}
	public void endRide() {
		//TODO  empties the list of on-board players and puts the wheel  to sleep until the next ride is run.
	}
	
}
