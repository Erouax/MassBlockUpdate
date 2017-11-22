package net.erouax.blockupdate;

import org.bukkit.Chunk;

/**
 * Base interface for MassBlockUpdate implementations
 *
 * Interface methods:
 * <p>
 *     - Add a block and it's chunk to the queue with {@link MassBlockUpdate#addBlockAndChunk(int, int, int)}
 *
 *     - Add a block to the queue with {@link MassBlockUpdate#addBlock(double, double, double)}
 *
 *     - Add a block to the queue with {@link MassBlockUpdate#addBlock(int, int, int)}
 *
 *     - Add a chunk to the queue with {@link MassBlockUpdate#addChunk(Chunk)}
 *
 *     - Run the {@link net.erouax.blockupdate.BasicMassBlockUpdate.BlockUpdateTask}
 *          in debug mode with {@link MassBlockUpdate#runDebug()}
 *
 *     - Run the {@link net.erouax.blockupdate.BasicMassBlockUpdate.BlockUpdateTask}
 *          in production mode with {@link MassBlockUpdate#run()}
 * </p>
 */
public interface MassBlockUpdate {

	/**
	 * Add a block to the queue and the chunk it's in
	 *
	 * @param x - X position of the block
	 * @param y - Y position of the block
	 * @param z - Z position of the block
	 */
	void addBlockAndChunk(int x, int y, int z);

	/**
	 * Add a block to the queue
	 *
	 * @param x - X position of the block
	 * @param y - Y position of the block
	 * @param z - Z position of the block
	 */
	void addBlock(double x, double y, double z);

	/**
	 * Add a block to the queue
	 *
	 * @param x - X position of the block
	 * @param y - Y position of the block
	 * @param z - Z position of the block
	 */
	void addBlock(int x, int y, int z);

	/**
	 * Add a chunk to the queue
	 *
	 * @param chunk - Chunk to add
	 */
	void addChunk(Chunk chunk);

	/**
	 * Run the {@link net.erouax.blockupdate.BasicMassBlockUpdate.BlockUpdateTask} in debug mode
	 * Prints out the load the update puts on the server to console.
	 */
	void runDebug();

	/**
	 * Run the {@link net.erouax.blockupdate.BasicMassBlockUpdate.BlockUpdateTask} in production mode
	 */
	void run();

}
