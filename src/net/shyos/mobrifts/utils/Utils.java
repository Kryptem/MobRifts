package net.shyos.mobrifts.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;

public class Utils {

	public static String color(String s) {
		return ChatColor.translateAlternateColorCodes('&', s);
	}

	public static boolean isNumber(String s) {
		try {
			Integer.parseInt(s);
			return true;

		} catch (NumberFormatException e) {
			return false;
		}
	}

	public static String locationToString(Location location) {
		double x = location.getX();
		double y = location.getY();
		double z = location.getZ();
		String world = location.getWorld().getName();

		if ((location.getYaw() == 0.0F) && (location.getPitch() == 0.0F)) {
			return world + ";" + x + ";" + y + ";" + z;
		}

		float yaw = location.getYaw();
		float pitch = location.getPitch();

		return world + ";" + x + ";" + y + ";" + z + ";" + yaw + ";" + pitch;
	}

	public static Location stringToLocation(String s) {
		String[] split = s.split(";");

		World world = Bukkit.getWorld(split[0]);
		double x = Double.parseDouble(split[1]);
		double y = Double.parseDouble(split[2]);
		double z = Double.parseDouble(split[3]);

		if (s.length() == 6) {
			float yaw = Float.parseFloat(split[4]);
			float pitch = Float.parseFloat(split[5]);

			return new Location(world, x, y, z, yaw, pitch);
		}

		return new Location(world, x, y, z);

	}

}
