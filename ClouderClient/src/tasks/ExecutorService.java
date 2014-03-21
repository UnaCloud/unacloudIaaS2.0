package tasks;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ExecutorService {
	public static int POOL_THREAD_SIZE = 2;
	/**
     * A pool of threads used to attend UnaCloud server requests
     */
    private static Executor poolExe;
    public static void execute(Runnable run){
    	if(poolExe==null)poolExe = Executors.newFixedThreadPool(POOL_THREAD_SIZE);
        poolExe.execute(run);
    }
    
}
