package com.github.iamtakagi.commandblocker;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandBlocker extends JavaPlugin implements Listener {

	private List<String> blockedCommands = new ArrayList<String>();
	private String denyMessage = ChatColor.RESET + "Unknown command. Type \"/help\" for help.";

	@Override
	public void onEnable() {
		this.getServer().getPluginManager().registerEvents(this, this);
		this.saveDefaultConfig();
		this.blockedCommands = this.getConfig().getStringList("settings.blocked_commands");
		this.denyMessage = ChatColor.RESET
				+ ChatColor.translateAlternateColorCodes('&', this.getConfig().getString("settings.deny_message"));
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onCommandProcess(PlayerCommandPreprocessEvent event) {
		Player player = event.getPlayer();
		if (player.isOp() || player.hasPermission("commandblocker.ignore"))
			return;

		String message = (event.getMessage().startsWith("/") ? "" : "/") + event.getMessage();

		for (String blockedCommand : blockedCommands) {
			if (message.startsWith(blockedCommand)) {
				player.sendMessage(this.denyMessage);
				event.setCancelled(true);
				break;
			}
		}
	}
}