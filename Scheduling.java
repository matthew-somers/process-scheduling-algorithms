import java.util.*;

/**
 * Simulator for various process scheduling algorithms
 * @author Matthew Somers
 *
 */
public class Scheduling
{
	static final int TIMESLICES = 100;
	static final int RUNS = 5;
	
	/**
	 * @param args not used
	 */
	public static void main(String[] args) 
	{
		ProcessQueue pq = new ProcessQueue(TIMESLICES);
		String results = "";
		
		for (int i = 1; i <= RUNS; i++)
			FirstCome(pq);
		results += pq.reportavgresults("First Come First Serve", RUNS);
		
		for (int i = 1; i <= RUNS; i++)
			ShortestJob(pq);
		results += pq.reportavgresults("Shortest Job First", RUNS);
		
		for (int i = 0; i < RUNS; i++)
			ShortestRemainingTime(pq);
		results += pq.reportavgresults("Shortest Remaining Time", RUNS);
		
		for (int i = 0; i < RUNS; i++)
			RoundRobin(pq);
		results += pq.reportavgresults("Round Robin", RUNS);
		
		for (int i = 0; i < RUNS; i++)
			HighestPriPre(pq);
		results += pq.reportavgresults("Highest Priority Preemptive", RUNS);
		
		for (int i = 0; i < RUNS; i++)
			HighestPriNonPre(pq);
		results += pq.reportavgresults("Highest Priority Nonpreemptive", RUNS);
		
		System.out.println(results);
	}
	
	/**
	 * First Come First Serve Method
	 * @param pq the processqueue to be operated on
	 */
	public static void FirstCome(ProcessQueue pq)
	{	
		System.out.println("First come, first serve:");
		for (int i = 0; i < 200; i++)
		{
			if (pq.allComplete(i))
				break;
			if (pq.currentprocessComplete())
				pq.setcurrentprocessNextArrival(i);
			
			pq.TryRuncurrentprocess(i);
		}
		
		pq.reportresults();
		pq.reset();
		System.out.println();
	}
	
	/**
	 * Shortest Job first, (non preemptive)
	 * @param pq the processqueue to be operated on
	 */
	public static void ShortestJob(ProcessQueue pq)
	{		
		System.out.println("Shortest Job First:");
		for (int i = 0; i < 200; i++)
		{
			if (pq.allComplete(i))
				break;
			if (pq.currentprocessComplete())
				pq.setcurrentprocessNextRunTime(i);
			
			pq.TryRuncurrentprocess(i);
		}
		
		pq.reportresults();
		pq.reset();
		System.out.println();
	}
	
	/**
	 * Shortest remaining time (can preempt a process if new one takes less time)
	 * @param pq the processqueue to be operated on
	 */
	public static void ShortestRemainingTime(ProcessQueue pq)
	{
		System.out.println("Shortest Remaining Time:");
		for (int i = 0; i < 200; i++)
		{
			if (pq.allComplete(i))
				break;
			pq.setcurrentprocessNextRunTime(i);
			
			pq.TryRuncurrentprocess(i);
		}
		
		pq.reportresults();
		pq.reset();
		System.out.println();
	}
	
	/**
	 * Round Robin (switches each turn as long as another process in queue)
	 * @param pq the processqueue to be operated on
	 */
	public static void RoundRobin(ProcessQueue pq)
	{
		System.out.println("Round Robin:");
		for (int i = 0; i < 200; i++)
		{
			pq.setcurrentprocessRR(i);
			
			if (pq.allComplete(i))
				break;
			
			pq.TryRuncurrentprocess(i);
		}
		
		pq.reportresults();
		pq.reset();
		System.out.println();
	}
	
	/**
	 * Highest Priority with preemption if higher priority process enters queue
	 * @param pq the processqueue to be operated on
	 */
	public static void HighestPriPre(ProcessQueue pq)
	{
		System.out.println("Highest Priority Preemptive:");
		for (int i = 0; i < 200; i++)
		{
			if (pq.allComplete(i))
				break;
			
			pq.setcurrentprocessPriority(i);
			
			pq.TryRuncurrentprocess(i);
		}
		
		pq.reportresults();
		pq.reset();
		System.out.println();
	}
	
	/**
	 * Highest priority without preemption
	 * @param pq the processqueue to be operated on
	 */
	public static void HighestPriNonPre(ProcessQueue pq)
	{
		System.out.println("Highest Priority NonPreemptive:");
		for (int i = 0; i < 200; i++)
		{
			if (pq.allComplete(i))
				break;
			if (pq.currentprocessComplete())
				pq.setcurrentprocessPriority(i);
			
			pq.TryRuncurrentprocess(i);
		}
		
		pq.reportresults();
		pq.reset();
		System.out.println();
	}

}
