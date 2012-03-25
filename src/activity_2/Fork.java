package activity_2;

/**
 * State object representing a standard American dinner fork.
 * 
 * In this implementation, acquisition is exclusive to the calling thread.
 * Release may be called by any thread.
 */
public class Fork implements IFork {
	private boolean allocated = false;

	/**
	 * Exclusively acquire this fork.
	 * 
	 * This is a blocking method - i.e., if this fork is already allocated when
	 * this method is called, the calling thread waits.
	 */
	public void acquire() {
		if (this.allocated) {
			try {
				synchronized (this) {
					System.out.println("PHIL WAITS");
					this.wait();

				}
			} catch (InterruptedException ie) {
				// TODO do we ignore or return something??
				System.out.println("Phil punched another Phil in the face!");
			}
			acquire();
		}
		this.allocated = true;
	}

	/**
	 * Set this fork's allocation status to "false".
	 */
	public void release() {
		this.allocated = false;
		synchronized (this) {
			this.notifyAll();
		}
	}

}
