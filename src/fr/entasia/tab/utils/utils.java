package fr.entasia.tab.utils;

import fr.entasia.apis.other.Pair;
import fr.entasia.apis.utils.ServerUtils;
import fr.entasia.tab.Main;
import me.lucko.luckperms.api.Contexts;
import me.lucko.luckperms.api.Group;
import me.lucko.luckperms.api.LocalizedNode;
import me.lucko.luckperms.api.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Comparator;

public class utils {


	public static final char[] alfa = "0123456789abcdefghijklmnopqrstuvwxyz".toCharArray();

	public static ArrayList<TabGroup> tabGroups = new ArrayList<>();

	public static Comparator<TabGroup> comparator;

	static{
		comparator = Comparator.comparingInt(o -> o.weight);
		comparator = comparator.reversed();
	}

	public synchronized static void loadPriorities(){
		System.out.println("loading priorities");

		for(TabGroup tg : tabGroups) {
			tg.setField("i", 1);
			tg.sendPacket();
		}
		tabGroups.clear();
		System.out.println("cleared current teams. recreating...");

		// calcul des priorités
		for(Group gr : Main.LpAPI.getGroups()){

			System.out.println("found group "+gr.getName());
			if(gr.getWeight().isPresent()){
				String suffix = Main.vaultAPI.getGroupSuffix(Bukkit.getWorlds().get(0), gr.getName());
				System.out.println("has weight");
				if(!suffix.equals("")){
					System.out.println("added");
					tabGroups.add(new TabGroup(gr.getWeight().getAsInt(), gr.getName(), suffix));
				}
			}
		}
		System.out.println("finish with "+ tabGroups.size()+" groups");
		
		for(Player p : Bukkit.getOnlinePlayers()){
			Pair<String, Integer> pair = LPUtils.getPlayerPriority(p);
			if(pair!=null) tabGroups.add(new TabGroup(pair.value, p.getName(), pair.key));
		}

		tabGroups.sort(comparator);
		int i=0;
		for(TabGroup tg : tabGroups){
			tg.assignChar(alfa[i]);
			i++;
			tg.setField("i", 0);
			tg.sendPacket();
			tg.setField("i", 3);
		}
	}

	public static void loadAllUsers() {
//		System.out.println("players reloading");
		for(Player p : Bukkit.getOnlinePlayers()) loadUser(p);
	}

	public static void loadUser(Player p) {

		p.setCustomNameVisible(true);
		p.setPlayerListName(Main.vaultAPI.getPlayerPrefix(Bukkit.getWorlds().get(0).getName(), p).replace("&", "§") + p.getDisplayName());

		User user = Main.LpAPI.getUserManager().getUser(p.getName());
		if (user == null) error("Luckperms user could not being loaded for " + p.getName());
		else {
			LocalizedNode lnode = LPUtils.getHighestSuffix(user);
			if(lnode==null)error("Luckperms suffix not found for "+p.getName());
			else{
				int priority = lnode.getSuffix().getKey();
				TabGroup tg = getByWeight(priority);
				if (tg == null) {
					loadPriorities();
					tg = getByWeight(priority);
					if(tg == null) {
						error("TabGroup could be loaded for prefix "+lnode.getSuffix().getValue()+" priority "+priority);
						return;
					}
				}
				tg.list.add(p.getName());
				tg.sendPacket();
			}
		}
	}


	public static void error(Exception e){
		ServerUtils.permMsg("log.tab", e.getMessage());
		e.printStackTrace();
	}

	public static void error(String s){
		ServerUtils.permMsg("log.tab", s);
		new Exception(s).printStackTrace();
	}


	public static TabGroup getByWeight(int w){
		System.out.println(" ");
		for(TabGroup tg : tabGroups){
			System.out.println("compare "+tg.weight+" to "+w);
			if(tg.weight==w)return tg;
		}
		return null;
	}

}
