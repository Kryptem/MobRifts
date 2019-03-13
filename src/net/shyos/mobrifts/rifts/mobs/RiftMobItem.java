package net.shyos.mobrifts.rifts.mobs;

import org.bukkit.inventory.ItemStack;

public class RiftMobItem {

	private ItemStack item;
	private int chance;

	public RiftMobItem(ItemStack item, int chance) {
		this.item = item;
		this.chance = chance;
	}

	public ItemStack getItem() {
		return item;
	}

	public void setItem(ItemStack item) {
		this.item = item;
	}

	public int getChance() {
		return chance;
	}

	public void setChance(int chance) {
		this.chance = chance;
	}

}
