package fr.entasia.tab.tools;

import fr.entasia.tab.Main;
import fr.entasia.tab.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class Listeners implements Listener {

	@EventHandler
	public void onJoin(PlayerJoinEvent e){
		Main.refreshTab(e.getPlayer());
		Utils.loadUser(e.getPlayer());


//		Player p = e.getPlayer();
//		if(p.getName().equals("iTrooz_")){
//			TabGroup admin = new TabGroup(150, "admin", "§cAdmin");
//			TabGroup def = new TabGroup(50, "default", "§8Joueur");
//
//			admin.assignChar('A');
//			def.assignChar('B');
//
//			admin.list.add("mortel1211");
//			admin.sendPacket(p, Mode.CREATE);
//
//			def.list.add("iTrooz_");
//			def.sendPacket(p, Mode.CREATE);
//		}
	}
}
