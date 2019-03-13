package net.shyos.mobrifts.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.shyos.mobrifts.MobRiftsPlugin;
import net.shyos.mobrifts.rifts.Rift;
import net.shyos.mobrifts.rifts.RiftTemplate;
import net.shyos.mobrifts.rifts.mobs.RiftMob;
import net.shyos.mobrifts.utils.Utils;

public class RiftMobCommand implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("You may only use this command from inside the game!");
			return true;
		}

		Player p = (Player) sender;

		if (cmd.getName().equalsIgnoreCase("mobrifts")) {

			if (args.length == 0) {
				if (p.hasPermission("mobrifts.help")) {
					sendHelp(p);
				}
				return true;
			}

			if (args.length == 1) {
				if (p.hasPermission("mobrifts.list")) {
					p.sendMessage(Utils.color("&eAvailable &5RiftMobs:"));

					for (RiftMob mob : MobRiftsPlugin.getInstance().getRiftMobManager().getRiftMobs()) {
						p.sendMessage(Utils.color("&e• &a") + mob.getConfigName());
					}
					return true;
				}
			}

			if (args.length == 3) {
				// mr spawn <name> <amount>
				if (args[0].equalsIgnoreCase("spawn")) {
					if (p.hasPermission("mobrifts.spawn")) {

						if (!Utils.isNumber(args[2])) {
							p.sendMessage(Utils.color("&a" + args[2] + " &cis not a number!"));
							return true;
						}

						int amount = Integer.parseInt(args[2]);
						for (RiftMob mob : MobRiftsPlugin.getInstance().getRiftMobManager().getRiftMobs()) {
							if (mob.getConfigName().equalsIgnoreCase(args[1])) {

								for (int i = 0; i < amount; i++)
									mob.spawn(p.getLocation());
								return true;
							}

						}

						p.sendMessage(Utils.color("&a" + args[1] + " &cis not a RiftMob!"));
						return true;
					}
				}

				// mr spawnrift <name> <respawn[true | false]>
				if (args[0].equalsIgnoreCase("spawnrift")) {
					if (p.hasPermission("mobrifts.spawnrift")) {

						for (RiftTemplate template : MobRiftsPlugin.getInstance().getRiftManager().getRiftTemplates()) {

							if (template.getRiftKey().equalsIgnoreCase(args[1])) {

								if (!template.getWorlds().contains(p.getWorld().getUID())) {
									p.sendMessage(Utils
											.color("&cThis rift is disabled in the world &a" + p.getWorld().getName()));
									return true;
								}

								boolean respawning = false;

//								if (!args[2].equalsIgnoreCase("true") || !args[2].equalsIgnoreCase("false")) {
//									p.sendMessage(Utils.color("&a" + args[2] + " &cis not a valid option. Use either &atrue &cor &afalse&c!"));
//									return true;
								// }F

								if (args[2].equalsIgnoreCase("true"))
									respawning = !respawning;

								Rift rift = new Rift(p.getLocation(), template);
								rift.setRespawning(respawning);
								rift.setLocation(p.getLocation());
								rift.setGround();
								rift.runTaskTimer(MobRiftsPlugin.getInstance(), 0, 0);
								MobRiftsPlugin.getInstance().getRiftManager().getActiveRifts().add(rift);
								return true;
							}
						}

						p.sendMessage(Utils.color(MobRiftsPlugin.PREFIX + "&5" + args[1] + " &cis not a valid rift!"));
						return true;
					}
				}
			}

			sendHelp(p);

		}

		return true;
	}

	private void sendHelp(Player p) {
		p.sendMessage(Utils.color("&e== [&5MobRifts &e- &aCommands&e] &e=="));
		p.sendMessage(Utils.color("&e• &5list &e- Lists all available mobs"));
		p.sendMessage(Utils.color("&e• &5spawn <name> <amount> &e- Spawns the desired mob"));
		p.sendMessage(
				Utils.color("&e• &5spawnrift <name> <true|false> &e- Spawns a respawning or non-respawning rift."));
	}

}
