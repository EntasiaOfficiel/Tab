package fr.entasia.tab;

import fr.entasia.tab.utils.SBUtils;
import fr.entasia.tab.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scoreboard.Scoreboard;

import java.util.HashMap;
import java.util.UUID;

public class Listeners implements Listener {

	@EventHandler
	public void onJoin(PlayerJoinEvent e){
		Utils.refreshTab(e.getPlayer());
//		SBUtils.loadUser(e.getPlayer());
	}








	public static HashMap<String, Scoreboard> scoreboards = new HashMap<>();

//	@EventHandler(priority = EventPriority.LOWEST) // TODO TEMPORAIRE : IRA DANS LES LIBRARIES
//	public void setupSB(PlayerJoinEvent e){
//		e.getPlayer().setScoreboard(get(e.getPlayer()));
//	}

	public static Scoreboard get(Player p){
		Scoreboard sb = scoreboards.get(p.getName());
		if(sb==null){
			sb = Bukkit.getScoreboardManager().getNewScoreboard();
			scoreboards.put(p.getName(), sb);
		}
		return sb;
	}

}
