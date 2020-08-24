package fr.entasia.tab;

import fr.entasia.apis.utils.PlayerUtils;
import fr.entasia.tab.tools.Listeners;
import fr.entasia.tab.tools.TabReloadCmd;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class Main extends JavaPlugin {

	public static Main main;

	@Override
	public void onEnable(){
		try{
			main = this;
//			vaultAPI = getServer().getServicesManager().getRegistration(Chat.class).getProvider();

			getLogger().info("Plugin activé");
			getServer().getPluginManager().registerEvents(new Listeners(), this);
			getCommand("tabreload").setExecutor(new TabReloadCmd());
			Utils.loadPriorities();

			new BukkitRunnable() {
				public void run() {
					Utils.loadPriorities();
					Utils.loadAllUsers();
				}
			}.runTaskTimer(this, 0, 20*60*5);

			new BukkitRunnable() {
				public void run() {
					for(Player p : getServer().getOnlinePlayers()) refreshTab(p);
				}
			}.runTaskTimer(this, 0, 20*10);

		}catch(Throwable e){
			e.printStackTrace();
			getLogger().severe("Une erreur s'est produite ! ARRET DU SERVEUR !");
			getServer().shutdown();
		}
	}


	public static void refreshTab(Player p){
		p.setPlayerListHeaderFooter(new TextComponent("§3Bienvenue sur §bEnta§7sia!\n Ping : §b"+ PlayerUtils.getPing(p, false) +" \n §7§m-------------------§r"),
				new TextComponent("§7§m-------------------§r\n §3Discord: §b/discord"));
	}

}
