import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

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
	private LinkedList<Player> playersQueueForRide;

	private int totalNumOfPlayers;
	private EyesOnPlayers operatorEyes;

	private String output = "";
	private String inputPath;
	
	public Operator(String gamePath) {
		inputPath = gamePath;
		playersQueue = new LinkedList<Player>();
		allPlayers = new LinkedList<Player>();
		playersQueueForRide = new LinkedList<Player>();
		totalNumOfPlayers = 0;
		operatorEyes = new EyesOnPlayers(this);
		readGame(inputPath);
		wheelLoaded = false;
	}

	
	public static void main(String[]args) {
		Operator operator = new Operator("input-2.txt");
		operator.work();
		
		try {
			operator.writeOutput();
		} catch (IOException e) {
			System.out.println("Error Writing the output file.");
			e.printStackTrace();
		}
		
	}

	private void writeOutput() throws IOException {
		
		String numberOfInput = "";
		
		if(inputPath.split("-").length >= 2) {
			numberOfInput = inputPath.split("-")[1].split("\\.")[0];
		}
		
		FileWriter fileWriter = new FileWriter("output-" + numberOfInput + ".txt");

	    BufferedWriter writer = new BufferedWriter(fileWriter);
	    writer.write(output);
	    writer.close();
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
		//threadWheel.start();

		boolean playersStarted = false;
		
		//CompletableFuture.runAsync(fairWheel::endRide);
		//fairWheel.endRide();

		while (true) {

			wheelLoaded = false;
			System.out.println("wheel start sleep");
			addInOutput("wheel start sleep");
			//System.out.println(System.currentTimeMillis());
			//fairWheel.endRide();
			threadWheel.run();
			
			if(!playersStarted) {
				for(Player player : allPlayers) {
					player.start();
				}
				playersStarted = true;
			}
			
			
			//threadWheel.run();
			//System.out.println(System.currentTimeMillis());
			//System.out.println("just after wheel slept, wheel loaded =" + wheelLoaded);
			
			//LinkedList<Player> queueToEnterWheef = new LinkedList<Player>();
			
			do {
				/*
				//System.out.println("fairWheel is full : " + fairWheel.isFull() + "  " + fairWheel.getNumOfOnBoard());
				if (fairWheel.isFull()) {
					System.out.println("Wheel is full, Let's go for a ride");
					System.out.println("Threads in this ride are: ");
					printPlayersOnRideIDs(); //TODO be modified after the wheel wakes up
					break;					
				}
				
				if (!playersQueue.isEmpty()) {

					//int currentPlayerID = playersQueue.getFirst().getPlayerId();
					
					//System.out.println("passing player: " + currentPlayerID + " to the operator");
					
					//System.out.println("before entering in Wheel, wheelLoaded is " + wheelLoaded);
					//fairWheel.loadPlayers(playersQueue.getFirst());
					//System.out.println("after entering in Wheel, wheelLoaded is " + wheelLoaded);

					//System.out.println("All Players now are " + allPlayers.size());
					//System.out.println("Player " + currentPlayerID + " on board, capacity: " + fairWheel.getNumOfOnBoard());
					
				} else {
					try {
						//System.out.println("before main thread sleeps, wheelLoaded is " + wheelLoaded);
						Thread.sleep(10);
						//System.out.println("after main thread wakes up, wheelLoaded is " + wheelLoaded);

					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				*/
				
				if(playersQueue.size() >= 5) {
					threadWheel.interrupt();
					break;
				}
				
				try {
					Thread.sleep(1);

				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				
			} while (!wheelLoaded);
			
			while(!playersQueue.isEmpty()) {
				Player p = playersQueue.getFirst();
				if(p == null || !fairWheel.loadPlayers(p)) {
					break;
				}
				playersQueue.remove(p);
				allPlayers.remove(p);
				System.out.println("Player " + p.getPlayerId() + " on board, capacity: " + fairWheel.getNumOfOnBoard());
				addInOutput("Player " + p.getPlayerId() + " on board, capacity: " + fairWheel.getNumOfOnBoard());
			}
			
			if(!fairWheel.isFull()) {
				System.out.println("wheel end sleep \n");
				addInOutput("wheel end sleep \n");

			} else {
				System.out.println("Wheel is full, Let's go for a ride");
				System.out.println("Threads in this ride are: ");
				addInOutput("Wheel is full, Let's go for a ride");
				addInOutput("Threads in this ride are: ");
				printPlayersOnRideIDs(); 
			}
			
			//fairWheel.runRide();

			if (allPlayers.isEmpty()) {
				break;
			}
						
		}
		
	}
	
	private void printPlayersOnRideIDs() {
		String playersIDs = "";
		for(Player p : fairWheel.getOnBoardPlayers()) {
			System.out.print(p.getPlayerId() + ", ");
			playersIDs+=p.getPlayerId() + ", ";
		}
		System.out.println("\n");
		playersIDs+= "\n";
		addInOutput(playersIDs);
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
		System.out.println("player wakes up: " + queuedPlayer.getPlayerId());
		addInOutput("player wakes up: " + queuedPlayer.getPlayerId());
		System.out.println("passing player: " + queuedPlayer.getPlayerId() + " to the operator");
		addInOutput("passing player: " + queuedPlayer.getPlayerId() + " to the operator");
		this.playersQueue.addLast(queuedPlayer);
	}
	
	private void addInOutput(String string) {
		output+= string + "\n";
	}
	
	
	protected void wheelLoaded() {
		this.wheelLoaded = true;
		//System.out.println("Wheel loaded == " + this.wheelLoaded);
	}

	public EyesOnPlayers getOperatorEyes() {
		return operatorEyes;
	}


	public LinkedList<Player> getPlayersQueueForRide() {
		return playersQueueForRide;
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

	public LinkedList<Player> getPlayersQueueForRide() {
		return this.operator.getPlayersQueueForRide();
	}
	

}
