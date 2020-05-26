package fr.entasia.tab.commands;


import fr.entasia.tab.utils.SBUtils;
import fr.entasia.tab.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TabRlCmd implements CommandExecutor {


	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] arg){
		if(sender.hasPermission("plugin.tab")){
			if(arg.length==0) sender.sendMessage("§cArguments : players/ranks/all");
			else{
				switch (arg[0]) {
					case "roles":{
						SBUtils.loadPriorities();
						sender.sendMessage("§bRôles rechargées !");
						break;
					}
					case "players":{
						SBUtils.loadAllUsers();
						sender.sendMessage("§bJoueurs rechargés !");
						break;
					}
					case "tab":{
						for (Player p : Bukkit.getOnlinePlayers()) Utils.refreshTab(p);
						sender.sendMessage("§bTab rechargé !");
						break;
					}
					case "all":{
						for (Player p : Bukkit.getOnlinePlayers()) Utils.refreshTab(p);
						SBUtils.loadPriorities();
						SBUtils.loadAllUsers();
						sender.sendMessage("§3Tout à été rechargé !");
						break;
					}
					default:{
						sender.sendMessage("§cArgument incorrect !\nArguments : players/ranks/all");
						break;
					}
				}
			}
		}else sender.sendMessage("§cTu n'as pas accès à cette commande !");
		return true;
	}
}