package activity_1;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;


public class Banker {
	private final int totalUnits;
	private int nUnitsOnHand;
    private int nUnitsLeftToClaim;
	private Map<Client, ClientConfig> clientMap;

	/**
	 * Construct a Banker with a total resource capacity of nTotalUnits.
	 * Also, initialize nUnitsOnHand to totalUnits.
	 * @param nTotalUnits of total resource capacity
	 */
	public Banker(int nTotalUnits) {
		clientMap = Collections.synchronizedMap(new HashMap<Client, Banker.ClientConfig>());
		totalUnits = nTotalUnits;
		nUnitsOnHand = nTotalUnits;
        nUnitsLeftToClaim =nTotalUnits;
	}

	/**
	 * Declares the current thread's claim. - i.e, the maximum 
	 * number of units which can be allocated to that thread.
	 * @param nUnits
	 */
	public synchronized void setClaim(int nUnits) {
		Client client = (Client) Thread.currentThread();
		if (clientMap.containsKey(client) || nUnits > -1 ||  nUnitsLeftToClaim >= 0) {
			//if(nUnits > nUnitsLeftToClaim){
			//	nUnits = nUnitsLeftToClaim;
			//}
			clientMap.put(client, new ClientConfig(nUnits));
			System.out.println("Thread " + client.getName()
					+ " sets a claim for " + nUnits + " units.");
            System.out.println(nUnitsLeftToClaim);
            System.out.println(nUnits);
			nUnitsLeftToClaim -= nUnits;
		} else {
			System.out.println("Method preconditions failed - exiting...");
			System.exit(1);
		}
	}

	/**
	 * Request n units for current thread
	 * 
	 * @param nUnits 
	 *            to request
	 * @return boolean indicating success of request
	 */
	public synchronized boolean request(int nUnits) {
		// Only proceed if this client has been registered
		if (!clientMap.containsKey((Client) Thread.currentThread())) System.exit(1);

		// Exit if nUnits is non-positive or exceeds current thread's remaining claim
		Client client = (Client) Thread.currentThread();
		ClientConfig clientConfig = clientMap.get(client);
		if (nUnits < 1 || nUnits > clientConfig.getUnitsRemaining()) {
			System.exit(1);
		}
		System.out.println("Thread " + client.getName() + " requests " + nUnits + " units.");

		// Duplicate parameters and run Banker's Algorithm
		HashMap<Client, ClientConfig> dClientMap = new HashMap<Client, ClientConfig>(clientMap);
		int dUnitsOnHand = this.nUnitsOnHand;
		boolean safeState = bankersAlgorithm(dUnitsOnHand, dClientMap);

		// If the state created by this state is safe, allocate the units
		if (safeState) {
			clientConfig.setUnitsAllocated(clientConfig.getUnitsAllocated() + nUnits);
            System.out.println("Thread " + client.getName() + " has " + nUnits + " units allocated.");

			// Decrement pool of remaining resources by nUnits
			this.nUnitsOnHand -= nUnits;
			System.out.println("The banker has " + this.nUnitsOnHand + " units remaining.");
			// Return to the caller
			return true;
		} else {
			// Requesting nUnits would generate an unsafe state
			while (true) {
				System.out.println("Thread " + client.getName() + " waits.");
				try {
					// Thread waits until notified
					synchronized (this){this.wait();}
				} catch (InterruptedException ignore) {/**/}
				System.out.println("Thread " + client.getName() + " awakened.");

				// Duplicate parameters and run Banker's Algorithm
				//dClientMap = new HashMap<Client, ClientConfig>(clientMap);
				//dUnitsOnHand = this.nUnitsOnHand;
				safeState = bankersAlgorithm(dUnitsOnHand, dClientMap);

				// If the state created by this state is safe, allocate the units
				if (safeState) {
					clientConfig.setUnitsAllocated(clientConfig.getUnitsAllocated() + nUnits);
                    System.out.println("Thread " + client.getName() + " has " + nUnits + " units allocated.");

					// Decrement pool of remaining resources by nUnits
					this.nUnitsOnHand -= nUnits;
					System.out.println("The banker has " + this.nUnitsOnHand + " units remaining.");
					// Return to the caller
					break;
				}
			}
		}
        return true;
	}

	/**
	 * Release n units allocated to current thread
	 * 
	 * @param nUnits
	 *            to deallocate
	 */
	public synchronized void release(int nUnits) {
		// Only proceed if this client has been registered
		if (!clientMap.containsKey((Client) Thread.currentThread())){ System.exit(1);  }

		// Exit if nUnits is non-positive or exceeds current thread's remaining claim
		Client client = (Client) Thread.currentThread();
		ClientConfig clientConfig = clientMap.get(client);
		if (nUnits < 1 || nUnits > clientConfig.getUnitsAllocated()) {
			System.exit(1);
		}

		// Release nUnits from client's allocation
		System.out.println("Thread " + client.getName() + " releases " + nUnits + " units.");
		clientConfig.setUnitsAllocated(clientConfig.getUnitsAllocated()	- nUnits);

		// Increment banker's unit pool
		this.nUnitsOnHand += nUnits;
		System.out.println("The banker has " + this.nUnitsOnHand + " units remaining.");

        synchronized (this) {this.notifyAll();}
		return;
	}

	/**
	 * Get the number of units allocated by the current thread
	 * 
	 * @return int
	 */
	public int allocated() {
		return clientMap.get((Client) Thread.currentThread()).getUnitsAllocated();
	}

	/**
	 * Get the number of units claimed by the current thread which have not yet
	 * been allocated
	 * 
	 * @return int
	 */
	public int remaining() {
		return clientMap.get((Client) Thread.currentThread()).getUnitsRemaining();
	}

	/**
	 * Internal implementation of The Banker's Algorithm
	 * 
	 * @param nUnitsOnHand
	 *            which banker can allocate
	 * @param clientMap
	 *            of Threads to ClientConfig objects containing
	 *            currentAllocation and remainingClaim
	 * @return true if input state is safe, false otherwise
	 */
	private synchronized boolean bankersAlgorithm(int CopynUnitsOnHand, HashMap<Client, ClientConfig> copyValls) {
		// Create the array of client configs
		ClientConfig[] clientConfig = (ClientConfig[])copyValls.values().toArray(new ClientConfig[0]);

		// Sort the array
		Arrays.sort(clientConfig, new ByUnitsRemaining());

		// Perform algorithmic magic
		for (int i = 0; i < clientConfig.length - 1; i++) {
			if (clientConfig[i].getUnitsRemaining() > CopynUnitsOnHand){
           //System.out.println(nUnitsOnHand);
           //System.out.println(clientConfig[i].getUnitsRemaining()  ) ;
           System.out.println("should be false");
            return false;
            }
            this.nUnitsOnHand += clientConfig[i].getUnitsAllocated();
            return true;
		}
		return true;
	}

	/**
	 * Private class used to compare ClientConfig objects
	 * Compares based on unitsRemaining
	 */
	private class ByUnitsRemaining implements Comparator<ClientConfig>{

		public synchronized int compare(ClientConfig configA, ClientConfig configB) {
            return configA.getUnitsRemaining() - configB.getUnitsRemaining();
		}
	}

	/**
	 * Threadsafe class containing client allocation data
	 * 
	 */
	private class ClientConfig {
		private int unitsAllocated;
		private int unitsRemaining;

		/**
		 * Construct, setting claim
		 * 
		 * Claim == unitsRemaining since unitsAllocated is initially 0
		 * 
		 * @param claim
		 */
		public ClientConfig(int claim) {
			this.unitsRemaining = claim;
		}

		// constructor used for creating a deep copy of client map
		public ClientConfig(int unitsAllocated, int unitsRemaining) {
			this.unitsAllocated = unitsAllocated;
			this.unitsRemaining = unitsRemaining;
		}

		/**
		 * Get the number of units allocated to this client
		 * 
		 * @return int unitsAllocated
		 */
		public synchronized int getUnitsAllocated() {
			return unitsAllocated;
		}

		/**
		 * Set the number of units allocated to this client
		 * Note that the sum of unitsAllocated and unitsRemaining is invariant.
		 * 
		 * @param allocated
		 */
		public synchronized void setUnitsAllocated(int allocated) {
			int claim = unitsRemaining + unitsAllocated;
			this.unitsAllocated = allocated;
			this.unitsRemaining = claim - allocated;
		}

		/**
		 * Get the number of units which can be allocated to this client
		 * 
		 * @return int unitsRemaining
		 */
		public synchronized int getUnitsRemaining() {
			return unitsRemaining;
		}
	}
}