package net.shyos.mobrifts.rifts.mobs;

import org.bukkit.potion.PotionEffect;

public class RiftMobEffect {

	private PotionEffect damageEffect;
	private int chance;

	public RiftMobEffect(PotionEffect effect, int chance) {
		this.damageEffect = effect;
		this.chance = chance;
	}

	public PotionEffect getDamageEffect() {
		return damageEffect;
	}

	public void setDamageEffect(PotionEffect damageEffect) {
		this.damageEffect = damageEffect;
	}

	public int getChance() {
		return chance;
	}

	public void setChance(int chance) {
		this.chance = chance;
	}

}
