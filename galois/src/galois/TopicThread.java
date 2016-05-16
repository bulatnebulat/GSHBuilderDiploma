package galois;

public class TopicThread extends Thread {
	   private Thread t;
	   private String threadName;
	   private ReadCSV csv;
	   
	   public TopicThread(ReadCSV csv, String name){
		   this.csv = csv;
	       threadName = name;
	       System.out.println("Creating " +  threadName );
	   }
	   public void run() {
	      System.out.println("Running " +  threadName );
	      try {
	    	  	csv.extractConcepts(threadName);
	            // Let the thread sleep for a while.
	            Thread.sleep(50);
	     } catch (InterruptedException e) {
	         System.out.println("Thread " +  threadName + " interrupted.");
	     }
	     System.out.println("Thread " +  threadName + " exiting.");
	   }
	   
	   public void start ()
	   {
	      System.out.println("Starting " +  threadName );
	      if (t == null)
	      {
	         t = new Thread (this, threadName);
	         t.start ();
	      }
	   }
}
