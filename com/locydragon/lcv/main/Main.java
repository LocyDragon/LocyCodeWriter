package com.locydragon.lcv.main;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin{
	/**
	 * @author LocyDragon
	 * @version 1.0.0
	 */
@Override
public void onEnable() {
	Bukkit.getLogger().info("CodeWriter����Ѿ�������");
	Bukkit.getLogger().info("����: "+this.getDescription().getAuthors().toString());
	if (Version.version.equals(this.getDescription().getVersion())) {
		Bukkit.getLogger().info("�汾��: "+Version.version);
	} else {
		Bukkit.getLogger().info("δ֪�İ汾");
	}
}
}
