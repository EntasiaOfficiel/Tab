package fr.entasia.tab;

import fr.entasia.tab.commands.TabRlCmd;
import fr.entasia.tab.commands.TestCmd;
import fr.entasia.tab.utils.SBUtils;
import fr.entasia.tab.utils.Utils;
import me.lucko.luckperms.LuckPerms;
import me.lucko.luckperms.api.LuckPermsApi;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class Main extends JavaPlugin {

	public static LuckPermsApi LpAPI;
	public static Chat vault;

	@Override
	public void onEnable(){
		LpAPI = LuckPerms.getApi();
		vault = getServer().getServicesManager().getRegistration(Chat.class).getProvider();
//		SBUtils.scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
//		Utils.scoreboard

		System.out.println("Plugin activé");
		getServer().getPluginManager().registerEvents(new Listeners(), this);
		getCommand("tabreload").setExecutor(new TabRlCmd());
		getCommand("test").setExecutor(new TestCmd());
		SBUtils.loadPriorities();

		new BukkitRunnable() {
			public void run() {
				SBUtils.loadAllUsers();
			}
		}.runTaskTimer(this, 0, 20*60*5);

		new BukkitRunnable() {
			public void run() {
				for(Player p : Bukkit.getOnlinePlayers()) Utils.refreshTab(p);
			}
		}.runTaskTimer(this, 0, 20*10);


	}


}
