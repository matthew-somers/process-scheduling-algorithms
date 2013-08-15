import java.util.Random;

public class Process 
{
	private float arrivalTime;
	private int runTime;
	private int copyrunTime;
	private int priority;
	private float waittime;
	private boolean active;
	private int id;
	private float turnaround;
	private float responsetime;
	
	/**
	 * A simulation of a process.
	 * @param arrival when this process arrives
	 * @param id2 this process's id
	 */
	public Process(float arrival, int id2)
	{
		Random randomGenerator = new Random();

		arrivalTime = arrival;
		runTime = (randomGenerator.nextInt(10) + 1);
		copyrunTime = runTime;
		priority = (randomGenerator.nextInt(4) + 1);
		id = id2;
		waittime = 0;
		turnaround = 0;
	}
	
	/**
	 * getter for turnaround time
	 * @return when this process is completed
	 */
	public float getturnaroundTime() { return turnaround; }
	
	/**
	 * getter for this process's arrival time
	 * @return arrival time
	 */
	public float getArrivalTime() { return arrivalTime; }
	
	/**
	 * getter for run time
	 * @return run time
	 */
	public int getRunTime() { return runTime; }
	
	/**
	 * getter for this process's priority
	 * @return priority
	 */
	public int getPriority() { return priority; }
	
	/**
	 * Checks whether this process has been run at least once.
	 * @return true if it has been run before, false otherwise
	 */
	public boolean active() { return active; }
	
	/**
	 * getter for this process's id
	 * @return id
	 */
	public int getID() { return id; }
	
	/**
	 * getter for how long this process has waited
	 * @return waittime
	 */
	public float getwaittime() { return waittime; }
	
	/**
	 * getter for response time
	 * @return responsetime
	 */
	public float getresponsetime() { return responsetime; }
	
	/**
	 * Returns a process's original run time
	 * @return the saved copy of run time
	 */
	public int getcopyruntime() { return copyrunTime; }
	/**
	 * setter for turnaround time
	 * @param time the turnaround time to be set
	 */
	public void setturnaround(float time)
	{
		turnaround = time;
	}
	
	/**
	 * setter for waittime
	 * @param time the waittime to be set
	 */
	public void setwaittime(float time)
	{
		waittime = time;
	}
	
	/**
	 * setter for responsetime
	 * @param time the responsetime to be set
	 */
	public void setresponsetime(float time)
	{
		responsetime = time;
	}
	
	/**
	 * Checks whether this process is complete
	 * @return true if process is complete, false otherwise
	 */
	public boolean isComplete() 
	{
		if (runTime == 0)
			return true;
		else
			return false;
	}
	
	/**
	 * "Runs" this simulated process.
	 */
	public void Run()
	{
		if (!active)
			active = true;
		runTime--;
	}
	
	/**
	 * Checks whether this is the first time this process has been run.
	 * @return true if this is the first time it has been run
	 */
	public boolean firstrun()
	{
		if (runTime == copyrunTime)
		{
			System.out.println("[" + id + "] arrives at " + arrivalTime
					+ " and runs for " + runTime +", priority: " + priority);
			return true;
		}
		else
			return false;
	}
}
