package fr.entasia.tab;

import fr.entasia.apis.utils.PlayerUtils;
import fr.entasia.tab.tools.Listeners;
import fr.entasia.tab.tools.TabRlCmd;
import fr.entasia.tab.tools.TestCmd;
import fr.entasia.tab.utils.utils;
import me.lucko.luckperms.LuckPerms;
import me.lucko.luckperms.api.LuckPermsApi;
import net.md_5.bungee.api.chat.TextComponent;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class Main extends JavaPlugin {

	public static LuckPermsApi LpAPI;
	public static Chat vaultAPI;

	@Override
	public void onEnable(){
		LpAPI = LuckPerms.getApi();
		vaultAPI = getServer().getServicesManager().getRegistration(Chat.class).getProvider();
//		SBUtils.scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
//		Utils.scoreboard

		System.out.println("Plugin activé");
		getServer().getPluginManager().registerEvents(new Listeners(), this);
		getCommand("tabreload").setExecutor(new TabRlCmd());
		getCommand("test").setExecutor(new TestCmd());
		utils.loadPriorities();

		new BukkitRunnable() {
			public void run() {
				utils.loadAllUsers();
			}
		}.runTaskTimer(this, 0, 20*60*5);

		new BukkitRunnable() {
			public void run() {
				for(Player p : Bukkit.getOnlinePlayers()) refreshTab(p);
			}
		}.runTaskTimer(this, 0, 20*10);
	}


	public static void refreshTab(Player p){
		p.setPlayerListHeaderFooter(new TextComponent("§3Bienvenue sur §bEnta§7sia!\n Ping : §b"+ PlayerUtils.getPing(p, false) +" \n §7§m-------------------§r"),
				new TextComponent("§7§m-------------------§r\n §3Discord: §b/discord"));
	}

}
