package net.shyos.mobrifts.rifts;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import net.shyos.mobrifts.MobRiftsPlugin;
import net.shyos.mobrifts.rifts.mobs.RiftMob;
import net.shyos.mobrifts.utils.Utils;

public class RiftManager {

	private final MobRiftsPlugin instance;

	private FileConfiguration config, activeRiftsConfig;
	private List<RiftTemplate> riftTemplates;
	private List<Rift> activeRifts;

	public RiftManager(MobRiftsPlugin instance) {
		this.instance = instance;
		config = instance.getConfiguration("rifts").get();
		activeRiftsConfig = instance.getConfiguration("activeRifts").get();
		riftTemplates = new ArrayList<>();
		activeRifts = new ArrayList<>();

		initiateRifts();
	}

	private void initiateRifts() {
		ConfigurationSection section = config.getConfigurationSection("");

		for (String riftKey : section.getKeys(false)) {

			int playerSpawnRadius = config.getInt(riftKey + ".player-mobspawn-radius");
			int spawnDelay = config.getInt(riftKey + ".rift-spawn-delay");
			int mobLimit = config.getInt(riftKey + ".mobs.kill-to-close-amount");
			// Times 20 to convert to seconds instead of just ticks
			int mobSpawnDelay = config.getInt(riftKey + ".mobs.spawn-delay-seconds") * 20;
			String spawnMessage = config.getString(riftKey + ".spawn-message");
			int maxPackSize = config.getInt(riftKey + ".mobs.max-mob-pack-size");

			RiftTemplate riftTemplate = new RiftTemplate(riftKey, playerSpawnRadius, spawnDelay, mobLimit,
					mobSpawnDelay, spawnMessage, maxPackSize);

			// Worlds
			for (String worldString : config.getStringList(riftKey + ".worlds")) {
				riftTemplate.getWorlds().add(Bukkit.getWorld(worldString).getUID());
			}

			// RiftMobs
			for (RiftMob mob : instance.getRiftMobManager().getRiftMobs()) {
				for (String mobString : config.getStringList(riftKey + ".rift-mobs")) {
					if (mob.getConfigName().equals(mobString)) {
						riftTemplate.getRiftMobs().add(mob);
					}
				}
			}

			riftTemplates.add(riftTemplate);

		}
	}

	public void loadActiveRifts() {
		for (String riftKey : activeRiftsConfig.getConfigurationSection("").getKeys(false)) {
			for (RiftTemplate template : riftTemplates) {
				if (template.getRiftKey().equals(riftKey)) {
					for (String id : activeRiftsConfig.getConfigurationSection(riftKey + "").getKeys(false)) {

						int idValue = Integer.valueOf(id);

						Location location = Utils
								.stringToLocation(activeRiftsConfig.getString(riftKey + "." + id + ".location"));
						RiftTemplate riftTemplate = getTemplateByName(riftKey);
						int mobsToClose = activeRiftsConfig.getInt(riftKey + "." + id + ".mobs-to-kill");

						Rift rift = new Rift(location, riftTemplate);

						rift.setMobsLeftToClose(mobsToClose);
						rift.setId(idValue);
						rift.runTaskTimer(MobRiftsPlugin.getInstance(), 0, 0);
						activeRifts.add(rift);
					}
				}
			}
		}
	}

	public void saveActiveRifts() {
		for (Rift rift : activeRifts) {
			int mobsToClose = rift.getMobsLeftToClose();
			Location location = rift.getLocation();
			String riftKey = rift.getTemplate().getRiftKey();

			int id = rift.getId();
			activeRiftsConfig.set(riftKey + "." + id + ".mobs-to-kill", mobsToClose);
			activeRiftsConfig.set(riftKey + "." + id + ".location", Utils.locationToString(location));
		}
		try {
			activeRiftsConfig.save(instance.getFile("activeRifts"));

		} catch (IOException e) {
			instance.getLogger().warning("There was a problem trying to save the active rifts!");
			e.printStackTrace();
		}
	}

	public RiftTemplate getTemplateByName(String name) {
		for (RiftTemplate template : riftTemplates) {
			if (template.getRiftKey().equals(name)) {
				return template;
			}
		}

		return null;
	}

	public void removeActiveRiftFromConfig(Rift rift) {
		String riftKey = rift.getTemplate().getRiftKey();

		activeRiftsConfig.set(riftKey + "." + String.valueOf(rift.getId()), null);
	}

	public List<RiftTemplate> getRiftTemplates() {
		return riftTemplates;
	}

	public List<Rift> getActiveRifts() {
		return activeRifts;
	}

}
