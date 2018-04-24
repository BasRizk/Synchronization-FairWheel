import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

/**
 * 
 * A simulation of (Fair Wheel Ride) using java threads, implemented as a
 * project under the subject of Synchronization during the course-work of
 * Operating Systems Course in the German University In Cairo
 * 
 * @class Operator job is to operate the whole game, it can never sleep; further
 *        more, Operator has to manage the boarding.
 * 
 * @authors Basem Rizk & Ibram Medhat
 * 
 */
public class Operator {

	private Wheel fairWheel;
	private Thread threadWheel;
	private boolean wheelLoaded;
	private LinkedList<Player> playersQueue;
	private LinkedList<Player> allPlayers;
	private int totalNumOfPlayers;
	private EyesOnPlayers operatorEyes;

	static String output = "";
	
	public Operator(String gamePath) {
		playersQueue = new LinkedList<Player>();
		allPlayers = new LinkedList<Player>();
		totalNumOfPlayers = 0;
		operatorEyes = new EyesOnPlayers(this);
		readGame(gamePath);
		wheelLoaded = false;
	}

	
	public static void main(String[]args) {
		Operator operator = new Operator("input-1.txt");
		operator.work();
		System.out.println(output);
		
	}

	/**
	 * -> The operator runs a ride in one of two cases: either the wheel is full
	 * (capacity is reached) or the max wait time of the wheel is reached
	 * 
	 * -> It loads the wheel until it reaches capacity, then runs the wheel and ends
	 * the ride.
	 * 
	 * -> The operator creates and starts the wheel thread.
	 * 
	 * -> The operator terminates when all players have successfully completed their
	 * rides.
	 */
	private void work() {

		operatorEyes.start();
		threadWheel = new Thread(fairWheel);
		threadWheel.start();

		for(Player player : allPlayers) {
			player.start();
			//System.out.println("Player " + player.getPlayerId() + ", waitingTime = " + player.getWaitingTime());
		}
		

		while (true) {

			wheelLoaded = false;
			System.out.println("wheel start sleep, wheel loaded =" + wheelLoaded);
			System.out.println(System.currentTimeMillis());
			//fairWheel.endRide();
			threadWheel.run();
			System.out.println(System.currentTimeMillis());
			System.out.println("just after wheel slept, wheel loaded =" + wheelLoaded);
			LinkedList<Player> queueToEnterWheel = new LinkedList<Player>();
			do {
				if (fairWheel.isFull()) {
					System.out.println("Wheel is full, Let's go for a ride");
					System.out.println("Threads in this ride are: ");
					printPlayersOnRideIDs();
					break;					
				}
				
				if (!playersQueue.isEmpty()) {

					int currentPlayerID = playersQueue.getFirst().getPlayerId();
					
					System.out.println("passing player: " + currentPlayerID + " to the operator");
					
					System.out.println("before entering in Wheel, wheelLoaded is " + wheelLoaded);
					fairWheel.loadPlayers(playersQueue.getFirst());
					System.out.println("after entering in Wheel, wheelLoaded is " + wheelLoaded);

					allPlayers.remove(playersQueue.removeFirst());
				
					System.out.println("Player " + currentPlayerID + " on board, capacity: " + fairWheel.getNumOfOnBoard());
					
				} else {
					try {
						System.out.println("before main thread sleeps, wheelLoaded is " + wheelLoaded);
						Thread.sleep(10);
						System.out.println("after main thread wakes up, wheelLoaded is " + wheelLoaded);

					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
				
			} while (!wheelLoaded);
			
			if(!fairWheel.isFull()) {
				System.out.println("wheel end sleep \n");
			}
			
			//fairWheel.runRide();

			if (allPlayers.isEmpty()) {
				break;
			}
						
		}
		
	}
	
	private void printPlayersOnRideIDs() {
		for(Player p : fairWheel.getOnBoardPlayers()) {
			System.out.print(p.getPlayerId() + ", ");
		}
		System.out.println("\n");
	}


	/*
	try {
		Thread.sleep(this.fairWheel.getMaxWaitingTime()/2);
	} catch (InterruptedException e) {
		e.printStackTrace();
	}
	*/

	/**
	 * -> Initializes the Wheel
	 * 
	 * -> The operator creates, starts and keeps a list of player threads.
	 * 
	 * @param inputPath
	 *            : path of the input file
	 * @return boolean as indicator of operation done successfully
	 */
	private boolean readGame(String inputPath) {
		try {
			FileReader fileReader = new FileReader(new File(inputPath));
			BufferedReader buffer = new BufferedReader(fileReader);
			String line = null;

			int max_waiting_time = Integer.parseInt(buffer.readLine());
			this.fairWheel = new Wheel(max_waiting_time, this);

			this.totalNumOfPlayers = Integer.parseInt(buffer.readLine());
			
			while((line = buffer.readLine()) != null) {
				
				String [] playerData = line.split(",");	
				
				if(playerData.length == 2) {
					int thread_id = Integer.parseInt(playerData[0]);
					int waiting_time = Integer.parseInt(playerData[1]);
					Player newPlayer = new Player(thread_id, waiting_time, this);
					allPlayers.add(newPlayer);	

				}
				

			}

			buffer.close();
			return true;

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return false;
	}

	protected void addPlayerInQueue(Player queuedPlayer) {
		this.playersQueue.addLast(queuedPlayer);
		System.out.println("player wakes up: " + queuedPlayer.getPlayerId());
	}
	
	
	protected void wheelLoaded() {
		this.wheelLoaded = true;
		System.out.println("Wheel loaded == " + this.wheelLoaded);
	}

	public EyesOnPlayers getOperatorEyes() {
		return operatorEyes;
	}
	
	

}

/**
 * Child class implements to act as the operator eyes, so it can look up, any
 * player which calls to enter the queue, while the operator main thread is busy
 * working on operating the rides and letting queued players enter the fair
 * wheel.
 */

class EyesOnPlayers extends Thread {

	Operator operator;
	
	public EyesOnPlayers(Operator operator) {
		this.operator = operator;
	}

	public void addPlayerInQueue(Player playerToQueue) {
		this.operator.addPlayerInQueue(playerToQueue);
	}

	public void wheelLoaded() {
		this.operator.wheelLoaded();
	
	}

}
