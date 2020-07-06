package fr.entasia.tab.tools;

import fr.entasia.tab.Main;
import fr.entasia.tab.utils.Utils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class Listeners implements Listener {

	@EventHandler
	public void onJoin(PlayerJoinEvent e){
		Main.refreshTab(e.getPlayer());
		Utils.loadUser(e.getPlayer());
	}
}
