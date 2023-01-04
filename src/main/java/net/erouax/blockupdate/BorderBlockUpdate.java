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
	 * Algorithm thanks to http://members.chello.at/~easyfilter/bresenham.html
	 * 
	 * Usage Example: 
	 *    BorderBlockUpdate update = new BorderBlockUpdate();
	 *    update.createCircularBorder(new Location(Bukkit.getWorld("world"), 0, 0, 0), 50, 5);
	 *    update.run();
	 *
	 * @param center - Center location of the border
	 * @param radius - Radius of the border
	 * @param height - Height of the border
	 */
	public void createCircularBorder(Location center, int radius, int height) {
		int x = -radius;
		int z = 0;
		int err = 2-2*radius;
		do {
			addPillar(center.getBlockX() - x, center.getBlockZ() + z, height);
			addPillar(center.getBlockX() - z, center.getBlockZ() - x, height);
			addPillar(center.getBlockX() + x, center.getBlockZ() - z, height);
			addPillar(center.getBlockX() + z, center.getBlockZ() + x, height);
			radius = err;
			if (radius <= z) {
				err += ++z*2+1;
			}
			if (radius > x || err > z) {
				err += ++x*2+1;
			}
		} while (x < 0);
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
				addPillar(x + center.getBlockX(), z + center.getBlockZ(), height);
			}
		}
	}

	/**
	 * Add the blocks and chunks for a given x, z
	 * coordinates up to the specified height starting
	 * from the block returned by World#getHighestBlockAt()
	 *
	 * @param x      - X Coordinate of the pillar
	 * @param z      - Z Coordinate of the pillar
	 * @param height - Height of the pillar
	 */
	public void addPillar(int x, int z, int height) {
		Block highest = this.world.getHighestBlockAt(x, z);
		this.addChunk(highest.getChunk());
		for (int y = highest.getY(); y < highest.getY() + height && y < 256; y++) {
			this.addBlock(x, y, z);
		}
	}
}
