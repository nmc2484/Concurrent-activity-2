package activity_2;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Driver class for the Dining Philosopher's problem 
 * 
 * An arbitrary number of philosophers compete for an identical
 * number of forks. Each philosopher cycles between thinking and eating
 * a designated number of times.
 */
public class Driver {

	/**
	 * Program entry point. Creates an array of forks and assigns
	 * them to philosophers as appropriate. 
	 * 
	 * @param args: [-l] [np] [nt] [tm] [em]
	 * [-l] odd-numbered philosophers are left handed
	 * 		default: all philosophers are right handed
	 * [np] number of philosophers and forks
	 * 		default: 4
	 * [nt] number of think/eat cycles
	 * 		default: 10
	 * [tm] think time in milliseconds
	 * 		default: 0
	 * [em] eat time in milliseconds
	 * 		default: 0
	 */
	public static void main(String[] args){
		// Set default values
		boolean rightHanded = true;
		int np = 4;
		int nt = 10;
		int tm = 0;
		int em = 0;
		
		// TODO read in command line args

		// Create np forks
		Fork[] forks = new Fork[np];
		for (int i = 0; i < np; i++) {
			forks[i] = new Fork();
		}

		// Create np philosophers and assign forks
		ArrayList<Philosopher> philosophers = new ArrayList<Philosopher>(np);
		for (int i = 0; i < np; i++) {
			int lf = (np + i - 1) % np;
			Philosopher philosopher;
			if (!rightHanded && i % 2 == 1) {
				philosopher = new Philosopher(i, forks[lf], forks[i], false, nt, tm, em);
			} else {
				philosopher = new Philosopher(i, forks[lf], forks[i], false, nt, tm, em);
			}
			philosophers.add(philosopher);
		}
		// Start philosophizing 
		for (Philosopher philosopher : philosophers) philosopher.start();
	}
}
