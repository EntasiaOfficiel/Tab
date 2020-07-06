package fr.entasia.tab.tools;

import fr.entasia.tab.Main;
import fr.entasia.tab.utils.Mode;
import fr.entasia.tab.utils.TabGroup;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

/*
	- les scoreboards se reset au redémarrage (pas de cache ?)
	- scoreboard par défaut : la main (à pas garder !)
 */

public class TestCmd implements CommandExecutor {

	public static HashMap<String, String> teams = new HashMap<>();

	static{
		teams.put("iTrooz_", "0admin");
		teams.put("cqnta", "1joueur");
	}


	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] arg){

		Player p = (Player)sender;

		TabGroup admin = new TabGroup(150, "admin", "§cAdmin");
		TabGroup def = new TabGroup(50, "default", "§8Joueur");

		admin.assignChar('A');
		def.assignChar('B');

		admin.list.add("mortel1211");
		admin.sendPacket(p, Mode.CREATE);

		def.list.add("iTrooz_");
		def.sendPacket(p, Mode.CREATE);


//		def.sendPacketAll(Mode.ADD_PLAYERS);

		return true;
	}
}