import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

/**
 * 
 * A simulation of (Fair Wheel Ride) using java threads, implemented as a project under
 * the subject of Synchronization during the course-work of Operating Systems Course in
 * the German University In Cairo
 * 
 * @class Operator job is to operate the whole game, it can never sleep;
 * further more, Operator has to manage the boarding.
 *  
 * @authors Basem Rizk & Ibram Medhat
 *  
 */
public class Operator {
	
	private Wheel fairWheel;
	private LinkedList<Player> playersQueue;
	private LinkedList<Player> allPlayers;
	private int totalNumOfPlayers;
	private EyesOnPlayers operatorEyes;
	
	public Operator(String gamePath) {
		playersQueue = new LinkedList<Player>();
		allPlayers = new LinkedList<Player>();
		operatorEyes = new EyesOnPlayers(this);
		readGame(gamePath);
	}
	
	public static void main(String[]args) {
		
		Operator operator = new Operator("input1");
		operator.work();
		
	}
	
	/**
	 * -> The operator runs a ride in one of two cases: either the wheel
	 *    is full (capacity is reached) or the max wait time of the 
	 *    wheel is reached
	 * 
	 * -> It loads the wheel until it reaches capacity, then
	 * 	  runs the wheel and ends the ride.
	 *    
	 * -> The operator creates and starts the wheel thread.
	 *    
	 * -> The operator terminates when all players have successfully
	 *    completed their rides.
	 */
	private void work() {
		
		operatorEyes.start();
		fairWheel.start();
		
		while (true) {
			
			int timeToWait = 0;
			while(fairWheel.getMaxWaitingTime() > timeToWait) {

				if(fairWheel.isFull()) {
					break;
				}
				
				if(!playersQueue.isEmpty()) {
					fairWheel.loadPlayers(playersQueue.peek());
					allPlayers.remove(playersQueue.pop());
				}

				timeToWait++; // TODO current time difference -> incremented for now
				
			}
			
			fairWheel.runRide();
			fairWheel.endRide();	
			
			if(allPlayers.isEmpty()) {
				break;
			}
		}

		
	}
	
	/**
	 * -> Initializes the Wheel
	 * 
	 * -> The operator creates, starts and keeps a list of player threads.
	 * 
	 * @param inputPath : path of the input file
	 * @return boolean as indicator of operation done successfully
	 */
	private boolean readGame(String inputPath) {
		try {
			FileReader fileReader = new FileReader(new File(inputPath));
			BufferedReader buffer = new BufferedReader(fileReader);
			String line = null;
			
			int max_waiting_time = Integer.parseInt(buffer.readLine());
			this.fairWheel = new Wheel (max_waiting_time);
			
			this.totalNumOfPlayers = Integer.parseInt(buffer.readLine());
			
			while((line = buffer.readLine()) != null) {
				String [] playerData = line.split(" ");				
				int thread_id = Integer.parseInt(playerData[0]);
				int waiting_time = Integer.parseInt(playerData[1]);
				Player newPlayer = new Player(thread_id, waiting_time, this);
				allPlayers.add(newPlayer);	
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
		playersQueue.push(queuedPlayer);
	}
	
	public EyesOnPlayers getOperatorEyes() {
		return operatorEyes;
	}

}

/**
 * Child class implements to act as the operator eyes, so it can look up,
 * any player which calls to enter the queue, while the operator main thread
 * is busy working on operating the rides and letting queued players enter the 
 * fair wheel.
 */

class EyesOnPlayers extends Thread {
	
	Operator operator;
	
	public EyesOnPlayers(Operator operator) {
		this.operator = operator;
	}	
	
	public void addPlayerInQueue(Player playerToQueue) {
		this.operator.addPlayerInQueue(playerToQueue);
	}
	
	
	
}
