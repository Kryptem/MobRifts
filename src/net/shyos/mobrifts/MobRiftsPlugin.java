package net.shyos.mobrifts;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import net.shyos.mobrifts.commands.RiftMobCommand;
import net.shyos.mobrifts.listeners.RiftMobListener;
import net.shyos.mobrifts.rifts.RiftManager;
import net.shyos.mobrifts.rifts.mobs.RiftMobManager;
import net.shyos.mobrifts.utils.Utils;

public class MobRiftsPlugin extends JavaPlugin {

	private static MobRiftsPlugin instance;
	public static final String PREFIX = "&e&l<<&5Mob Rifts&e&l>>&r ";
	private final Map<String, FileConfiguration> configurations = new HashMap<>();

	public RiftMobManager riftMobManager;
	public RiftManager riftManager;

	@Override
	public void onEnable() {
		instance = this;

		for (String name : Arrays.asList("config", "messages", "rifts", "activeRifts", "mobs")) {
			File file = new File(getDataFolder(), name + ".yml");

			if (!file.exists()) {
				file.getParentFile().mkdirs();
				saveResource(file.getName(), false);
				debugMessage("Created new default file " + file.getName());
			}

			FileConfiguration config = new YamlConfiguration();
			try {
				config.load(file);
			} catch (IOException | InvalidConfigurationException e) {
				e.printStackTrace();
			}
			configurations.put(name, config);
		}

		riftMobManager = new RiftMobManager();
		riftManager = new RiftManager(this);

		riftManager.loadActiveRifts();
		Bukkit.getPluginManager().registerEvents(new RiftMobListener(), this);

		getCommand("mobrifts").setExecutor(new RiftMobCommand());
	}

	@Override
	public void onDisable() {
		riftManager.saveActiveRifts();
	}

	public static void consoleMessage(String msg) {
		Bukkit.getLogger().info(Utils.color(msg));
	}

	public static void debugMessage(String msg) {
		if (instance.getConfig().getBoolean("debug")) {
			consoleMessage("&b " + msg);
		}
	}

	public Optional<FileConfiguration> getConfiguration(String name) {
		return Optional.ofNullable(configurations.get(name));
	}

	public File getFile(String name) {
		return new File(getDataFolder(), name + ".yml");
	}

	public RiftManager getRiftManager() {
		return riftManager;
	}

	public RiftMobManager getRiftMobManager() {
		return riftMobManager;
	}

	public static MobRiftsPlugin getInstance() {
		return instance;
	}
}
