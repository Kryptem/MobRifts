package net.shyos.mobrifts.rifts.mobs;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import net.shyos.mobrifts.MobRiftsPlugin;
import net.shyos.mobrifts.utils.Utils;

public class RiftMob {

	private String configName;
	private char nameColor;

	private LivingEntity entity;
	private EntityType entityType;
	private boolean canDrop;
	private double hearts;
	private List<RiftMobItem> loot;
	private ItemStack[] armor;
	private ItemStack weapon;
	private List<RiftMobEffect> damageEffects;

	public RiftMob(String configName) {
		this.configName = configName;
		this.loot = new ArrayList<>();
		this.damageEffects = new ArrayList<>();
	}

	public void spawn(Location loc) {
		entity = (LivingEntity) loc.getWorld().spawnEntity(loc, entityType);
		entity.setMetadata(configName, new FixedMetadataValue(MobRiftsPlugin.getInstance(), "mob"));
		equip();

	}

	private void equip() {
		entity.getEquipment().clear();
		entity.getEquipment().setArmorContents(armor);
		entity.getEquipment().setItemInMainHand(weapon);
		entity.getEquipment().setBootsDropChance(0);
		entity.getEquipment().setLeggingsDropChance(0);
		entity.getEquipment().setChestplateDropChance(0);
		entity.getEquipment().setHelmetDropChance(0);
		entity.getEquipment().setItemInMainHandDropChance(0);
		entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(2 * hearts);
		entity.setHealth(entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
		entity.setCustomName(Utils.color("&" + nameColor + configName));
//		updateName();
		entity.setCustomNameVisible(true);

	}

	public LivingEntity getEntity() {
		return entity;
	}

//	public void updateName() {
//		entity.setCustomName(configName + ChatColor.RED + "| Hearts: " + Math.round(entity.getHealth() * 2) / 2D);
//	}

	public void setArmor(ItemStack[] armor) {
		this.armor = armor;
	}

	public void setHearts(double hearts) {
		this.hearts = hearts <= 0 ? 1 : hearts / 2;
	}

	public double getHearts() {
		return hearts;
	}

	public ItemStack getRandomDrop(int rand) {
		for (RiftMobItem item : loot) {
			if (item.getChance() >= rand)
				return item.getItem();
		}
		return null;
	}

	public void setNameColor(char nameColor) {
		this.nameColor = nameColor;
	}

	public char getNameColor() {
		return nameColor;
	}

	public void setEntityType(EntityType entityType) {
		this.entityType = entityType;
	}

	public EntityType getEntityType() {
		return entityType;
	}

	public void setEntity(LivingEntity entity) {
		this.entity = entity;
	}

	public void setCanDrop(boolean canDrop) {
		this.canDrop = canDrop;
	}

	public boolean canDrop() {
		return canDrop;
	}

	public void setWeapon(ItemStack weapon) {
		this.weapon = weapon;
	}

	public ItemStack getWeapon() {
		return weapon;
	}

	public String getConfigName() {
		return configName;
	}

	public List<RiftMobItem> getLoot() {
		return loot;
	}

	public List<RiftMobEffect> getDamageEffects() {
		return damageEffects;
	}

	public void setDamageEffects(List<RiftMobEffect> damageEffects) {
		this.damageEffects = damageEffects;
	}

}
