package net.shyos.mobrifts.rifts;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import net.shyos.mobrifts.rifts.mobs.RiftMob;

public class RiftTemplate {

	private final int maxPackSize;
	private final int spawnDelay;
	private final int mobSpawnDelay;
	private final int mobLimit;
	private final int playerSpawnRadius;

	private final String riftKey;
	private final String spawnMessage;

	private final Set<UUID> worlds = new HashSet<>();
	private final List<RiftMob> riftMobs = new ArrayList<>();

	public RiftTemplate(String riftKey, int playerSpawnRadius, int spawnDelay, int mobLimit, int mobSpawnDelay,
			String spawnMessage, int maxPackSize) {

		this.riftKey = riftKey;
		this.spawnDelay = spawnDelay;
		this.mobLimit = mobLimit;
		this.mobSpawnDelay = mobSpawnDelay;
		this.spawnMessage = spawnMessage;
		this.maxPackSize = maxPackSize;
		this.playerSpawnRadius = playerSpawnRadius;
	}

	public String getRiftKey() {
		return riftKey;
	}

	public int getPlayerSpawnRadius() {
		return playerSpawnRadius;
	}

	public int getMaxPackSize() {
		return maxPackSize;
	}

	public int getSpawnDelay() {
		return spawnDelay;
	}

	public int getMobSpawnDelay() {
		return mobSpawnDelay;
	}

	public int getMobLimit() {
		return mobLimit;
	}

	public String getSpawnMessage() {
		return spawnMessage;
	}

	public Set<UUID> getWorlds() {
		return worlds;
	}

	public List<RiftMob> getRiftMobs() {
		return riftMobs;
	}

}
