package net.shyos.mobrifts.rifts.mobs;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.shyos.mobrifts.MobRiftsPlugin;

public class RiftMobManager {

	private List<RiftMob> riftMobs;
	private FileConfiguration config;

	public RiftMobManager() {
		riftMobs = new ArrayList<>();
		config = MobRiftsPlugin.getInstance().getConfiguration("mobs").get();

		initiateRiftMobs();
	}

	// Load the mobs from config into cache
	private void initiateRiftMobs() {

		for (String mobKey : config.getConfigurationSection("").getKeys(false)) {
			RiftMob mob = new RiftMob(mobKey);

			// Get the armor content
			List<ItemStack> armor = new ArrayList<>();
			for (String gear : config.getConfigurationSection(mobKey + ".gear").getKeys(false)) {
				String matValue = config.getString(mobKey + ".gear." + gear);

				Material m = matValue.equalsIgnoreCase("none") ? Material.AIR : Material.valueOf(matValue);
				armor.add(new ItemStack(m));
			}

			ItemStack[] armorArr = new ItemStack[armor.size()];
			armorArr = armor.toArray(armorArr);

			mob.setNameColor(config.getString(mobKey + ".name-color").charAt(1));
			mob.setArmor(armorArr);
			mob.setEntityType(EntityType.valueOf(config.getString(mobKey + ".entity-type")));
			mob.setCanDrop(config.getBoolean(mobKey + ".drops-loot"));
			mob.setHearts(config.getDouble(mobKey + ".skills.hearts"));

			String weaponString = config.getString(mobKey + ".weapon");
			Material weaponMat = weaponString.equalsIgnoreCase("none") ? Material.AIR : Material.valueOf(weaponString);
			mob.setWeapon(new ItemStack(weaponMat));

			// Get the loot
			for (String items : config.getConfigurationSection(mobKey + ".drops").getKeys(false)) {

				Material m = Material.valueOf(config.getString(mobKey + ".drops." + items + ".item"));
				int chance = config.getInt(mobKey + ".drops." + items + ".chance");
				int amount = config.getInt(mobKey + ".drops." + items + ".amount");
				mob.getLoot().add(new RiftMobItem(new ItemStack(m, amount), chance));
			}

			// Get the effects
			for (String effect : config.getStringList(mobKey + ".skills.effects-on-damage-player")) {

				String[] effectsArry = effect.split(";");
				PotionEffectType effectType = PotionEffectType.getByName(effectsArry[0]);
				int duration = Integer.valueOf(effectsArry[1]) * 20;
				int amplitude = Integer.valueOf(effectsArry[2]);
				int chance = Integer.valueOf(effectsArry[3]);

				mob.getDamageEffects()
						.add(new RiftMobEffect(new PotionEffect(effectType, duration, amplitude), chance));

			}

			riftMobs.add(mob);
		}

	}

	public List<RiftMob> getRiftMobs() {
		return riftMobs;
	}

}
