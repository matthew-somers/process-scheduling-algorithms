import java.util.*;

/**
 * A queue to simulate different process scheduling algorithms.
 * @author Matthew Somers
 *
 */
public class ProcessQueue 
{
	private static final int DELTA = 18;
	
	private ArrayList<Process> processlist;
	private int currentprocess;
	private int timeslices;
	private int idnext;
	
	private float avgturnaround;
	private float avgwaiting;
	private float avgresponse;
	private float avgthroughput;
	private ArrayList<Integer> timechart;
	
	/**
	 * Constructor for a processqueue.
	 * Fills in first 2 processes.
	 * @param timeslices2 how many time slices to run.
	 */
	public ProcessQueue(int timeslices2)
	{
		timeslices = timeslices2;
		processlist = new ArrayList<Process>();
		processlist.add(new Process(0, 0));
		Random randomGenerator = new Random();
		float arrival = randomGenerator.nextFloat() * DELTA;
		processlist.add(new Process(arrival, 1));
		idnext = 2;
		currentprocess = 0;
		
		avgturnaround = 0;
		avgwaiting = 0;
		avgresponse = 0;
		avgthroughput = 0;
		timechart = new ArrayList<Integer>();
	}
	
	/**
	 * Figues out which process will arrive next to set the currentprocess to that.
	 * Used for First Come, First Serve
	 * @param time current time the algorithm is at
	 */
	public void setcurrentprocessNextArrival(int time)
	{
		float lowestarrival = 9999999;
		for (int i = 0; i < processlist.size(); i++)
		{
			if (!processlist.get(i).isComplete())
			{
				if (processlist.get(i).getArrivalTime() < lowestarrival)
				{
					lowestarrival = processlist.get(i).getArrivalTime();
					currentprocess = i;
				}
			}
		}
	}
	
	/**
	 * Figures out which process to run next in order of run time.
	 * Used for Shortest Job First, and Shortest Remaining Time.
	 * @param time current time the algorithm is at
	 */
	public void setcurrentprocessNextRunTime(int time)
	{
		int lowestruntime = 9999999;
		int lowestprocess = 9999999;
		
		for (int i = 0; i < processlist.size(); i++)
		{
			if (!processlist.get(i).isComplete())
			{
				if (processlist.get(i).getRunTime() < lowestruntime && processlist.get(i).getArrivalTime() <= time)
				{
					lowestruntime = processlist.get(i).getRunTime();
					lowestprocess = i;
				}
			}
		}
		
		if (lowestprocess == 9999999)
			; //do nothing
		else
			currentprocess = lowestprocess;
	}
	
	/**
	 * Switches to next process in queue. Rolls over if at end.
	 * Used for Round Robin
	 * @param time current time the algorithm is at
	 */
	public void setcurrentprocessRR(int time)
	{
		if (processlist.size()-1 > currentprocess)
		{
			for (int i = currentprocess+1; i < processlist.size(); i++)
			{
				if (!processlist.get(i).isComplete() && processlist.get(i).getArrivalTime() <= time)
				{
					currentprocess = i;
					return;
				}
			}
		}
		else //process needs to wrap around
		{
			for (int i = 0; i <  currentprocess; i++)
			{
				if (!processlist.get(i).isComplete() && processlist.get(i).getArrivalTime() <= time)
				{
					currentprocess = i;
					return;
				}
			}
		}
	}
	
	/**
	 * Figures out which process to run next by priority.
	 * Used for Highest Priority Preemptive and NonPreemptive
	 * @param time current time the algorithm is at
	 */
	public void setcurrentprocessPriority(int time)
	{
		for (int i = 0; i < processlist.size(); i++)
		{
			if (processlist.get(i).getArrivalTime() <= time && (!processlist.get(i).isComplete()) )
			{
				if (processlist.get(i).getPriority() > processlist.get(currentprocess).getPriority())
				{
					currentprocess = i;
					return;
				}
			}
		}
		
		if (processlist.get(currentprocess).isComplete()) //nothing higher priority left
		{
			for (int i = 0; i < processlist.size(); i++)
			{
				if (processlist.get(i).getArrivalTime() <= time && (!processlist.get(i).isComplete()) )
				{
					currentprocess = i;
					return;
				}
			}
		}
	}
	
	/**
	 * The key method where processes are run, and new ones are created.
	 * Maintains many stats, as well as timechart.
	 * @param time current time the algorithm is at
	 */
	public void TryRuncurrentprocess(int time)
	{
		if (processlist.get(currentprocess).getArrivalTime() > time || processlist.get(currentprocess).isComplete())
		{
			timechart.add(-1);
			return;
		}
		else
		{
			if (processlist.get(currentprocess).firstrun())
			{
				float responsetime = time - processlist.get(currentprocess).getArrivalTime();
				processlist.get(currentprocess).setresponsetime(responsetime);
			}
			
			processlist.get(currentprocess).Run();
			timechart.add(processlist.get(currentprocess).getID());
			
			if (processlist.get(currentprocess).isComplete())
			{
				float turnaround = (time - processlist.get(currentprocess).getArrivalTime());
				processlist.get(currentprocess).setturnaround(turnaround);
				float wait2 = (turnaround - processlist.get(currentprocess).getcopyruntime() );
				processlist.get(currentprocess).setwaittime(wait2);
				Random randomGenerator = new Random(); //1000000
				
				float arrival = time + (randomGenerator.nextFloat() * DELTA);
				if (arrival <= timeslices)
				{
					processlist.add(new Process(arrival, idnext));
					idnext++;
				}
			}
		}
	}
	
	/**
	 * Checks whether this processqueue is complete, or over time.
	 * @param time current time of the algorithm
	 * @return true if processqueue is complete, false otherwise
	 */
	public boolean allComplete(int time)
	{
		if (time >= 100)
		{
			for (int i = 0; i < processlist.size(); i++)
			{
				if (!processlist.get(i).isComplete() && processlist.get(i).active())
					return false;
			}
		}
		else //time < 100
		{
			for (int i = 0; i < processlist.size(); i++)
			{
				if (( (!processlist.get(i).isComplete() || processlist.get(i).active())))
					return false;
			}
		}

		return true;
	}
	
	/**
	 * Resets most values of this processqueue.
	 * Some stats are maintained to be overwritten in the two results() methods. 
	 */
	public void reset()
	{
		processlist.clear();
		processlist.add(new Process(0, 0));
		Random randomGenerator = new Random();
		float arrival = (randomGenerator.nextFloat() * DELTA);
		processlist.add(new Process(arrival, 1));
		idnext = 2;
		currentprocess = 0;
	}
	
	/**
	 * Checks whether the current process is complete.
	 * @return true if currentprocess is complete.
	 */
	public boolean currentprocessComplete()
	{
		return processlist.get(currentprocess).isComplete();
	}
	
	/**
	 * Reports results and timechart for a single run of the processqueue.
	 * Updates avg values and resets only timechart.
	 */
	public void reportresults()
	{
		//timechart
		//stats
		float completed = 0;
		float turnaround = 0;
		float waiting = 0;
		float response = 0;
		for (Process p : processlist)
		{
			if (p.isComplete())
			{
				completed++;
				turnaround += p.getturnaroundTime();
				waiting += p.getwaittime();
				response += p.getresponsetime();
			}
		}
		float throughput = completed / timeslices;
		turnaround /= completed;
		waiting /= completed;
		response /= completed;
		avgturnaround += turnaround;
		avgthroughput += throughput;
		avgwaiting += waiting;
		avgresponse += response;
		//System.out.println("Completed: " + completed);

		System.out.println("Single Avg Turnaround time: " + turnaround);
		System.out.println("Single Avg Throughput: " + throughput);
		System.out.println("Single Avg Waiting time: " + waiting);
		System.out.println("Single Avg Response time: " + response);
		
		System.out.print("Timechart:");
		for (int i = 0; i < timechart.size(); i++)
		{
			if ( (i % 10) == 0)
				System.out.println();
			if (timechart.get(i) == -1)
				System.out.print("[.]");
			else
				System.out.print("[" + timechart.get(i) + "]");
		}
		
		timechart.clear();
		System.out.println();
	}
	
	/**
	 * Reports average results at the end of running an algorithm a certain number of times.
	 * Resets average values at end.
	 * @param name the name of the algorithm to be added with the stats
	 * @param runs how many times the algorithm was run previously
	 * @return the string containing the stats
	 */
	public String reportavgresults(String name, int runs)
	{
		String results = "";
		avgturnaround /= runs;
		avgwaiting /= runs;
		avgthroughput /= runs;
		avgresponse /= runs;
		results += "Stats for " + name + ":\n";
		results += "Average turnaround time: " + avgturnaround + "\n";
		results += "Average waiting time: " + avgwaiting + "\n";
		results += "Average response time: " + avgresponse + "\n";
		results += "Average throughput: " + avgthroughput + "\n\n";
		
		avgturnaround = 0;
		avgwaiting = 0;
		avgresponse = 0;
		avgthroughput = 0;
		
		return results;
	}
}
