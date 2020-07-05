package fr.entasia.tab.tools;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/*
	- les scoreboards se reset au redémarrage (pas de cache ?)
	- scoreboard par défaut : la main (à pas garder !)
 */

public class TestCmd implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] arg){

		Player p = (Player)sender;
		return true;
	}
}
