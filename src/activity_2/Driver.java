package activity_2;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Driver class for the Dining Philosopher's problem
 * 
 * An arbitrary number of philosophers compete for an identical number of forks.
 * Each philosopher cycles between thinking and eating a designated number of
 * times.
 */
public class Driver {

	/**
	 * Program entry point. Creates an array of forks and assigns them to
	 * philosophers as appropriate.
	 * 
	 * @param args
	 *            : [-l] [np] [nt] [tm] [em] [-l] odd-numbered philosophers are
	 *            left handed default: all philosophers are right handed [np]
	 *            number of philosophers and forks default: 4 [nt] number of
	 *            think/eat cycles default: 10 [tm] think time in milliseconds
	 *            default: 0 [em] eat time in milliseconds default: 0
	 * 
	 * 
	 */

	private static boolean rightHanded = true;
	private static int np = 4;
	private static int nt = 10;
	private static int tm = 0;
	private static int em = 0;

	public static void main(String[] args) {
		// Set default values

		String arguments = "";
		for (int i = 0; i < args.length; i++) {
			arguments += (args[i] + " ");
		}

		String[] options = arguments.split("[ ]+");

		if (options[0] != null) {

			if (options[0].equals("-l")) {

				rightHanded = false;
				if (options[1] != null) {
					np = Integer.parseInt(options[1].trim());
					nt = Integer.parseInt(options[2].trim());
					tm = Integer.parseInt(options[3].trim());
					em = Integer.parseInt(options[4].trim());
				}
			} else if (options.length > 3) {
				np = Integer.parseInt(options[0].trim());
				nt = Integer.parseInt(options[1].trim());
				tm = Integer.parseInt(options[2].trim());
				em = Integer.parseInt(options[3].trim());
			}

		}
		
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
			/**
			 * if (!rightHanded && i % 2 == 1) { philosopher = new
			 * Philosopher(i, forks[lf], forks[i], false, nt, tm, em); } else {
			 * philosopher = new Philosopher(i, forks[lf], forks[i], false, nt,
			 * tm, em); }
			 **/

			if (rightHanded) {
				philosopher = new Philosopher(i, forks[lf], forks[i], true, nt,
						tm, em);
			} else {
				if (i % 2 == 0) {
					philosopher = new Philosopher(i, forks[lf], forks[i], true,
							nt, tm, em);
				} else {
					philosopher = new Philosopher(i, forks[lf], forks[i],
							false, nt, tm, em);
				}
			}
			philosophers.add(philosopher);
		}
		// Start philosophizing
		for (Philosopher philosopher : philosophers)
			philosopher.start();

	}

}
