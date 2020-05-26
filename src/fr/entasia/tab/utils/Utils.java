package fr.entasia.tab.utils;

import fr.entasia.apis.PlayerUtils;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

import java.util.HashMap;

public class Utils {

	public static HashMap<String, Scoreboard> scoreboards = new HashMap<>();

	public static void refreshTab(Player p){
		p.setPlayerListHeaderFooter(new TextComponent("§3Bienvenue sur §bEnta§7sia!\n Ping : §b"+ PlayerUtils.getPing(p, false) +" \n §7§m-------------------§r"),
				new TextComponent("§7§m-------------------§r\n §3Discord: §b/discord"));
	}

}
