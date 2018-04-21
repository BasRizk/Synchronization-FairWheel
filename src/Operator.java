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
		fairWheel.start();

		for(Player player : allPlayers) {
			player.start();
			//System.out.println("Player " + player.getPlayerId() + ", waitingTime = " + player.getWaitingTime());
		}
		
		output = output + "Wheel start sleep \n\n";
		System.out.println(output);

		while (true) {

			wheelLoaded = false;

			do {
				fairWheel.endRide();
				if (fairWheel.isFull()) {
					break;
					
				}
				
				if (!playersQueue.isEmpty()) {

					output = output + "Passing player " + playersQueue.getFirst().getPlayerId() + " to the operator \n\n";
					System.out.println("Passing player " + playersQueue.getFirst().getPlayerId() + " to the operator\n");
	
					fairWheel.loadPlayers(playersQueue.getFirst());
					allPlayers.remove(playersQueue.removeFirst());
					
				} else {
					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
				
			} while (!wheelLoaded);
			
			fairWheel.runRide();

			if (allPlayers.isEmpty()) {
				break;
			}
			

			System.out.println("Ride Complete.\n");
			
		}
		
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
	}
	
	protected void wheelLoaded() {
		this.wheelLoaded = true;
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
