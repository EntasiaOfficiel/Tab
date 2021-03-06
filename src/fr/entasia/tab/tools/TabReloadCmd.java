package fr.entasia.tab.tools;


import fr.entasia.tab.Utils;
import fr.entasia.tab.utils.Mode;
import fr.entasia.tab.utils.TabGroup;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class TabReloadCmd implements CommandExecutor {

	public static void args(CommandSender sender){
		sender.sendMessage("§cArguments :");
		sender.sendMessage("§c- priorities");
		sender.sendMessage("§c- players");
		sender.sendMessage("§c- all");
	}

	public boolean onCommanda(CommandSender sender, Command cmd, String label, String[] arg){

		TabGroup admin = new TabGroup(150, "admin", "§cAdmin");
		admin.assignChar('A');
		admin.list.add(sender.getName());

		admin.sendPacketAll(Mode.CREATE);
		admin.sendPacketAll(Mode.DELETE);
		admin.sendPacketAll(Mode.CREATE);
		admin.sendPacketAll(Mode.DELETE);

		return true;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] arg){
		if(sender.hasPermission("entasia.tabreload")){
			if(arg.length==0){
				sender.sendMessage("§cMet un argument !");
				args(sender);
			}else{
				switch (arg[0]) {
					case "priorities":{
						Utils.loadPriorities();
						sender.sendMessage("§bRôles rechargées !");
						break;
					}
					case "players":{
						Utils.loadAllUsers();
						sender.sendMessage("§bJoueurs rechargés !");
						break;
					}
					case "all":{
						Utils.loadPriorities();
						Utils.loadAllUsers();
						sender.sendMessage("§3Tout à été rechargé !");
						break;
					}
					default:{
						sender.sendMessage("§cArgument incorrect !");
						args(sender);
						break;
					}
				}
			}
		}else sender.sendMessage("§cTu n'as pas accès à cette commande !");
		return true;
	}
}