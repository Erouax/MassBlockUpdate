package net.erouax.blockupdate;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.plugin.Plugin;

/**
 * Example implementation of {@link BasicMassBlockUpdate} for creating a border.
 */
public class BorderBlockUpdate extends BasicMassBlockUpdate {

	/**
	 * Constructor to create a new instance of {@link BasicMassBlockUpdate}
	 * Contains simple methods to make borders of different shapes
	 *
	 * @param material  - Border block material
	 * @param world     - World in which the border is
	 * @param plugin    - Plugin to run tasks on
	 * @param rateLimit - Maximum blocks changed per tick
	 */
	public BorderBlockUpdate(Material material, World world, Plugin plugin, int rateLimit) {
		super(material, world, plugin, rateLimit);
	}

	/**
	 * Create a circular border at the specified location
	 * with the specified radius and height.
	 *
	 * @param center - Center location of the border
	 * @param radius - Radius of the border
	 * @param height - Height of the border
	 */
	public void createCircularBorder(Location center, int radius, int height) {
		int points = radius + 1;

		for (double d = 0; d < Math.PI; d += Math.PI / points) {
			double x = Math.cos(d) * radius;
			double z = Math.sin(d) * radius;

			Location location = new Location(this.world, x, 0, z);

			Block highest = this.world.getHighestBlockAt(location);

			this.addChunk(highest.getChunk());

			for (int y = highest.getY(); y < highest.getY() + height && y < 256; y++) {
				this.addBlock(x + center.getBlockX(), y, z + center.getBlockZ());
			}
		}
	}

	/**
	 * Create a square border at the specified location
	 * with the specified radius and height.
	 *
	 * @param center - Center location of the border
	 * @param radius - Radius of the border
	 * @param height - Height of the border
	 */
	public void createSquareBorder(Location center, int radius, int height) {
		for (int x = -radius; x <= radius; x++) {
			for (int z = -radius; z <= radius; z++) {
				if (Math.abs(x) != radius && Math.abs(z) != radius) {
					continue;
				}

				Block highest = this.world.getHighestBlockAt(x, z);

				this.addChunk(highest.getChunk());

				for (int y = highest.getY(); y < highest.getY() + height && y < 256; y++) {
					this.addBlock(x + center.getBlockX(), y, z + center.getBlockZ());
				}
			}
		}
	}

}
