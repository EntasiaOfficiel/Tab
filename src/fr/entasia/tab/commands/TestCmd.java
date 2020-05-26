package fr.entasia.tab.commands;

import fr.entasia.tab.Listeners;
import fr.entasia.tab.Main;
import fr.entasia.tab.utils.Utils;
import me.lucko.luckperms.api.Contexts;
import me.lucko.luckperms.api.LocalizedNode;
import me.lucko.luckperms.api.User;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

//		Contexts contexts = Main.LpAPI.getContextsForPlayer(p);
//		User user = Main.LpAPI.getUserManager().getUser(p.getName());
//		ArrayList<LocalizedNode> nodes = new ArrayList<>();
//		for (LocalizedNode node : user.resolveInheritances()) {
//			if(node.isSuffix()){
//				System.out.println(node.getSuffix().getValue()+" with "+node.getSuffix().getKey()+" in "+node.getLocation());
//				if(node.isGroupNode()){
//					System.out.println("group : "+node.getGroupName());
//				}
//				System.out.println(" ");
//				System.out.println(" ");
////				nodes.add(node);
//			}
//		}


		Scoreboard mainSB = Bukkit.getScoreboardManager().getMainScoreboard();


//		Bukkit.getScoreboardManager().getMainScoreboard().getTeams().forEach(Team::unregister);

		for(Player lp : Bukkit.getOnlinePlayers()){

			Scoreboard sb = Listeners.get(lp);

			try{
				Objective o = sb.registerNewObjective( "test", "dummy");
				o.setDisplaySlot(DisplaySlot.SIDEBAR);
				o.setDisplayName("§aTest");
				o.getScore("§3Pseudo : "+lp.getName()).setScore(5);
			}catch(Exception ignore){

			}

			for(Map.Entry<String, String> e : teams.entrySet()){
				Team t = sb.registerNewTeam(e.getValue());
				if(e.getKey().equals(lp.getName())){
					t.addPlayer(lp);
				}

			}
			lp.setScoreboard(sb);
		}

//		CraftScoreboardManager a = (CraftScoreboardManager) Bukkit.getScoreboardManager();
//		a.
//
//		for(Player lp : Bukkit.getOnlinePlayers()){
//			Bukkit.broadcastMessage(lp.getName());
//			Scoreboard sb = Utils.get(lp);
//			for(Team lt : mainSB.getTeams()){
//				Bukkit.broadcastMessage(lt.getName()+" of "+lt.getScoreboard());
//			}


//		}
//
//		Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
//		Objective objective = scoreboard.registerNewObjective("test", "dummy");
//		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
//		objective.setDisplayName("§aTest");
//		objective.getScore("§3Pseudo §7: "+p.getName()).setScore(5);
//		p.setScoreboard(scoreboard);
		return true;
	}
}
