package net.shyos.mobrifts.listeners;

import java.util.Random;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;

import net.shyos.mobrifts.MobRiftsPlugin;
import net.shyos.mobrifts.rifts.Rift;
import net.shyos.mobrifts.rifts.mobs.RiftMob;
import net.shyos.mobrifts.rifts.mobs.RiftMobEffect;

public class RiftMobListener implements Listener {

	@EventHandler
	public void onRiftMobDeath(EntityDeathEvent e) {
		for (RiftMob mob : MobRiftsPlugin.getInstance().getRiftMobManager().getRiftMobs()) {
			if (e.getEntity().hasMetadata(mob.getConfigName())) {

				if (mob.canDrop()) {
					e.getDrops().clear();
					e.getDrops().add(mob.getRandomDrop(new Random().nextInt(100)));

				}

				// Check if the mob is from a rift and subtract mobcount of the rift
				for (Rift rift : MobRiftsPlugin.getInstance().getRiftManager().getActiveRifts()) {
					if (e.getEntity().hasMetadata("rift")) {
						if (e.getEntity().getMetadata("rift").get(0).value().equals(rift)) {
							rift.setMobsLeftToClose(rift.getMobsLeftToClose() - 1);
							rift.getSpawnedMobs().remove(mob);
						}
					}
				}

			}
		}
	}

	@EventHandler
	public void onRiftMobDamage(EntityDamageByEntityEvent e) {
		for (RiftMob mob : MobRiftsPlugin.getInstance().getRiftMobManager().getRiftMobs()) {
			if (e.getEntity().hasMetadata(mob.getConfigName()) || e.getDamager().hasMetadata(mob.getConfigName())) {

				if (mob.getEntity() == null) {
					if (e.getEntity() instanceof LivingEntity) {
						mob.setEntity((LivingEntity) e.getEntity());
					}
				}

//				if (!(e.getEntity() instanceof Player))
//				mob.updateName();

				if (e.getEntity() instanceof Player) {
					Player p = (Player) e.getEntity();

					int rand = new Random().nextInt(100);
					for (RiftMobEffect effect : mob.getDamageEffects()) {
						if (effect.getChance() >= rand)
							p.addPotionEffect(effect.getDamageEffect());
					}
				}

			}
		}

	}

}
