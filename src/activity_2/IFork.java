package activity_2;

/**
 * Interface for all tine-based cutlery.
 *
 * Supports acquisition and release.
 */
public interface IFork {

	/**
	 * Current thread explicitly acquires this fork
	 */
    public void acquire();

    /**
     * Re-set acquisition status to "false"
     */
    public void release();
}
