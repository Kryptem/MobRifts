package net.shyos.mobrifts.rifts;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import net.shyos.mobrifts.MobRiftsPlugin;
import net.shyos.mobrifts.rifts.mobs.RiftMob;
import net.shyos.mobrifts.utils.Utils;

public class Rift extends BukkitRunnable {

	private final Random rand;
	private BukkitTask bukkitTask;

	private final RiftTemplate template;

	private Location location;
	private boolean respawning;
	private int tempSpawnDelay;
	private int mobsLeftToClose;
	private int id;
	private List<RiftMob> spawnedMobs;

	public Rift(Location location, RiftTemplate template) {
		this.location = location;
		this.template = template;
		spawnedMobs = new ArrayList<>();
		id = nextId(template.getRiftKey());
		rand = new Random();
		mobsLeftToClose = template.getMobLimit();

	}

	public void setGround() {
		int radius = template.getPlayerSpawnRadius();
		for (int x = -radius; x <= radius; x++) {
			for (int z = -radius; z <= radius; z++) {
				location.getBlock().getRelative(x, -1, z).setType(Material.OBSIDIAN);

			}
		}
	}

	public void run() {

		// Rift closed
		if (mobsLeftToClose <= 0) {
			// Remove the remaining RiftMobs of that rift
			for (RiftMob mob : spawnedMobs) {
				System.out.println(mob.getEntity());
				mob.getEntity().remove();
				System.out.println(spawnedMobs.size());
			}

			MobRiftsPlugin.getInstance().getRiftManager().getActiveRifts().remove(this);
			MobRiftsPlugin.getInstance().getRiftManager().removeActiveRiftFromConfig(this);

			location.getWorld().getPlayers().stream().forEach(p -> p.sendMessage(
					Utils.color(MobRiftsPlugin.PREFIX + "&aA &5" + template.getRiftKey() + " &ahas been closed!")));

			this.cancel();
		}

		tempSpawnDelay++;

		if (tempSpawnDelay % 20 == 0) {
			System.out.println(tempSpawnDelay);
			System.out.println(template.getMobSpawnDelay());
		}

		if (tempSpawnDelay >= template.getMobSpawnDelay()) {

			/*
			 * Only spawn mobs when player is within a certain radius. Multiply radius with
			 * 2, since the starting location is the center of the rift
			 */
			int radius = template.getPlayerSpawnRadius();
			int playerRadius = 2 * radius;

			for (Entity e : location.getWorld().getNearbyEntities(location, playerRadius, playerRadius, playerRadius)) {
				if (e instanceof Player) {

					/*
					 * Roll a random number to randomize spawning location within rift diameter
					 */
					int isNegative = rand.nextInt(2);
					int randomX = isNegative == 0 ? rand.nextInt(radius) : -rand.nextInt(radius);

					isNegative = rand.nextInt(2);

					int randomZ = isNegative == 0 ? rand.nextInt(radius) : -rand.nextInt(radius);
					int highestY = location.getWorld().getHighestBlockYAt(randomX, randomZ);

					Location spawnLocation = this.location.getBlock().getRelative(randomX, highestY - 4, randomZ)
							.getLocation();

					RiftMob mob = template.getRiftMobs().get(rand.nextInt(template.getRiftMobs().size()));

					for (int i = rand.nextInt(template.getMaxPackSize()); i < template.getMaxPackSize(); i++) {
						mob.spawn(spawnLocation.clone().add(i * 0.5, 0, 0));
						mob.getEntity().setMetadata("rift", new FixedMetadataValue(MobRiftsPlugin.getInstance(), this));
						// Keep track of spawned mobs so we can remove them all once the rift is closed
						spawnedMobs.add(mob);
					}

				}
			}
			tempSpawnDelay = 0;
		}

	}

	private int nextId(String forRift) {
		int i = 0;

		while (MobRiftsPlugin.getInstance().getConfiguration("activeRifts").get()
				.contains(forRift + "." + String.valueOf(i)))
			i++;
		return i;
	}

	// TODO
	/*
	 * Not sure if I want to make them respawning private void startRespawnPeriod()
	 * {
	 * 
	 * }
	 */

	public int getId() {
		return id;
	}

	public List<RiftMob> getSpawnedMobs() {
		return spawnedMobs;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setMobsLeftToClose(int mobsLeftToClose) {
		this.mobsLeftToClose = mobsLeftToClose;
	}

	public int getMobsLeftToClose() {
		return mobsLeftToClose;
	}

	public RiftTemplate getTemplate() {
		return template;
	}

	public BukkitTask getRunnable() {
		return bukkitTask;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public void setRespawning(boolean respawning) {
		this.respawning = respawning;
	}

	public boolean isRespawning() {
		return respawning;
	}

	public Location getLocation() {
		return location;
	}

}
