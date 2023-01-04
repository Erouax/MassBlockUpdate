package net.erouax.blockupdate;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.IBlockData;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Basic implementation of {@link MassBlockUpdate}
 * Contains simple methods to change large amounts of blocks within seconds
 */
@RequiredArgsConstructor
public class BasicMassBlockUpdate implements MassBlockUpdate {

	protected final Deque<BlockPosition> blockQueue = new ArrayDeque<>();
	protected final Set<Chunk> chunkQueue = new HashSet<>();

	protected final Material material;
	protected final World world;

	private final Plugin plugin;

	private final int rateLimit;

	@Override public void addBlockAndChunk(int x, int y, int z) {
		this.addChunk(this.world.getChunkAt(this.world.getBlockAt(x, y, z)));
		this.addBlock(x, y, z);
	}

	@Override public void addBlock(double x, double y, double z) {
		this.blockQueue.add(new BlockPosition(x, y, z));
	}

	@Override public void addBlock(int x, int y, int z) {
		this.blockQueue.add(new BlockPosition(x, y, z));
	}

	@Override public void addChunk(Chunk chunk) {
		this.chunkQueue.add(chunk);
	}

	@Override public void run() {
		new BlockUpdateTask(this.blockQueue, this.rateLimit).runTaskTimer(this.plugin, 0L, 1L);
	}

	@Override public void runDebug() {
		new BlockUpdateTask(this.blockQueue, this.rateLimit, true).runTaskTimer(this.plugin, 0L, 1L);
	}

	private final class BlockUpdateTask extends BukkitRunnable {

		private final Deque<BlockPosition> positions;
		private final List<Long> loadTimes;

		private final net.minecraft.server.v1_8_R3.World nmsWorld;
		private final IBlockData blockData;

		private final boolean debug;
		private final int rateLimit;

		private int ticks;

		private BlockUpdateTask(Deque<BlockPosition> positions, int rateLimit) {
			this(positions, rateLimit, false);
		}

		@SuppressWarnings("deprecation")
		private BlockUpdateTask(Deque<BlockPosition> positions, int rateLimit, boolean debug) {
			this.positions = positions;
			this.rateLimit = rateLimit;
			this.debug = debug;

			this.blockData = net.minecraft.server.v1_8_R3.Block.getByCombinedId(material.getId());
			this.nmsWorld = ((CraftWorld) world).getHandle();

			this.loadTimes = new ArrayList<>();
		}

		private void updateBlock(BlockPosition position) {
			net.minecraft.server.v1_8_R3.Chunk chunk =
					this.nmsWorld.getChunkAt(position.getX() >> 4, position.getZ() >> 4);

			chunk.a(position, this.blockData);
		}

		@SuppressWarnings("deprecation")
		private void onFinish() {
			for (Chunk chunk : chunkQueue) {
				chunk.getWorld().refreshChunk(chunk.getX(), chunk.getZ());
			}

			if (!this.debug) {
				return;
			}

			long total = 0;

			for (Long l : this.loadTimes) {
				total += l;
			}

			double average = (double) total / (double) this.loadTimes.size();

			System.out.println(" --------- MASS BLOCK UPDATE ---------");
			System.out.println(" Added load per tick: " + average + " ms");
			System.out.println(" Total ticks run: " + this.ticks);
			System.out.println(" -------------------------------------");
		}

		@Override public void run() {
			long start = System.currentTimeMillis();

			for (int i = 0; i < this.rateLimit; i++) {
				if (this.positions.isEmpty()) {
					break;
				}

				BlockPosition position = this.positions.remove();
				this.updateBlock(position);
			}

			long end = System.currentTimeMillis();

			if (this.debug) {
				this.loadTimes.add(end - start);
				this.ticks++;
			}

			if (this.positions.isEmpty()) {
				this.onFinish();
				this.cancel();
			}
		}

	}

}
