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
	Bukkit.getLogger().info("CodeWriter插件已经启动！");
	Bukkit.getLogger().info("作者: "+this.getDescription().getAuthors().toString());
	if (Version.version.equals(this.getDescription().getVersion())) {
		Bukkit.getLogger().info("版本号: "+Version.version);
	} else {
		Bukkit.getLogger().info("未知的版本");
	}
}
}
