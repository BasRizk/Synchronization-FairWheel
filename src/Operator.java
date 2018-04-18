/**
 * 
 * A simulation of (Fair Wheel Ride) using java threads, implemented as a project under
 * the subject of Synchronization during the course-work of Operating Systems Course in
 * the German University In Cairo
 * 
 * @class Operator job is to operate the whole game, it can never sleep;
 * further more, Operator has to manage the boarding.
 * 
 * 
 * => Suggested Design:
 * 
 *  The operator is responsible for managing the simulation. It keeps an
 * instance of the wheel. It loads the wheel until it reaches capacity, then
 * runs the wheel and ends the ride.
 *  The operator creates, starts and keeps a list of player threads.
 *  The operator creates and starts the wheel thread.
 *  The operator terminates when all players have successfully completed their rides.
 * 
 * 
 * @authors Basem Rizk & Ibram Medhat
 *  
 */
public class Operator {
	
	Wheel fairWheel;
	
	public Operator() {
		
	}
}
