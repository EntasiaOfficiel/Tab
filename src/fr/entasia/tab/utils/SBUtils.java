package fr.entasia.tab.utils;

import com.google.common.collect.Maps;
import fr.entasia.apis.ServerUtils;
import fr.entasia.tab.Listeners;
import fr.entasia.tab.Main;
import fr.entasia.tab.objs.Orderer;
import me.lucko.luckperms.api.Contexts;
import me.lucko.luckperms.api.LocalizedNode;
import me.lucko.luckperms.api.Node;
import me.lucko.luckperms.api.User;
import me.lucko.luckperms.api.caching.MetaData;
import me.lucko.luckperms.common.model.PermissionHolder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class SBUtils {


	public static final char[] alfa = "0123456789abcdefghijklmnopqrstuvwxyz".toCharArray();

	private static Map<Integer, String> prioToWeight = new HashMap<>();
	public static Map<String, Team> sb_ranks = new HashMap<>();
	public static ArrayList<Orderer> orderers = new ArrayList<>();

	public static Comparator<Orderer> comparator;

	static{
		comparator = Comparator.comparingInt(o -> o.weight);
		comparator = comparator.reversed();
	}

	public static void loadPriorities(){
		// calcul des priorités
		sb_ranks.clear();
		prioToWeight.clear();
		Main.LpAPI.getGroups().forEach(gr -> {
			System.out.println("found group "+gr.getName());
			if(gr.getWeight().isPresent()){
				String suffix = Main.vault.getGroupSuffix(Bukkit.getWorlds().get(0), gr.getName());
				System.out.println("has weight");
				if(!suffix.equals("")){
					System.out.println("added");
					orderers.add(new Orderer(gr.getWeight().getAsInt(), suffix));
				}
			}
		});
		System.out.println("finish with "+orderers.size());
		
		for(Player p : Bukkit.getOnlinePlayers()){
			int a = getPPriority(p);
			if(a!=-1) orderers.add(new Orderer(a));
		}

		orderers.sort(comparator);
		int i=0;
		for(Orderer o : orderers){
			System.out.println("looping");
			prioToWeight.put(o.weight, String.valueOf(alfa[i]));
			i++;
		}

//		System.out.println("--------------------------");
//		for(Orderer o : weights){
//			if(o.group!=null){
//				String s = o.group.getName();
//				System.out.println("loading "+s);
//
//				String s2;
//				if(s.length()>14)s2=s.substring(0, 14);
//				else s2=s;
//
//				Team t = scoreboard.registerNewTeam(weightToPrio.get(o.weight)+"G"+s2);
//				t.setPrefix(o.suffix.replace("&", "§")+" ");
//				sb_ranks.put("G"+s, t);
//				System.out.println("prefix set to "+t.getPrefix());
//			}
//		}
	}


	public static void loadAllUsers() {
//		System.out.println("players reloading");
		for(Player p : Bukkit.getOnlinePlayers()) loadUser(p);
	}

	public static void loadUser(Player p) {

		p.setCustomNameVisible(true);
		p.setPlayerListName(Main.vault.getPlayerPrefix(Bukkit.getWorlds().get(0).getName(), p).replace("&", "§") + p.getDisplayName());

		p.getScoreboard().getTeams().forEach(Team::unregister);

		User user = Main.LpAPI.getUserManager().getUser(p.getName());
		if (user == null) error("Luckperms user could not being loaded for " + p.getName());
		else {
			LocalizedNode lnode = null;
			Contexts contexts = Main.LpAPI.getContextsForPlayer(p);
			for (LocalizedNode node : user.resolveInheritances(contexts)) {
				if (node.isSuffix()) {
					if (lnode == null || node.getSuffix().getKey() > lnode.getSuffix().getKey()) {
						lnode = node;
					}
				}
			}
			if(lnode==null)error("Luckperms suffix not found for "+p.getName());
			else{
				int priority = lnode.getSuffix().getKey();
				String s = prioToWeight.get(priority);
				if (s == null) {
					loadPriorities();
					s = prioToWeight.get(priority);
					if (s == null) {
						error("Sb Role priority could be loaded for role "+lnode.getLocation()+" priority "+priority);
						return;
					}
				}
				String name = lnode.getLocation();
				if(name.length()==36) { // UUID
					name = "ZZZZZZ";
				}else if(name.length()>14)name = name.substring(0, 14);
				Team t = p.getScoreboard().registerNewTeam(s+name);
				t.setPrefix(lnode.getSuffix().getValue().replace("&", "§") + " ");
				t.addPlayer(p);
			}
		}
	}

	public static int getPPriority(Player p) { // BUT : Get la priorité max du suffix du joueur (SANS GROUPE)
		String suffix = Main.vault.getPlayerSuffix(Bukkit.getWorlds().get(0).getName(), p);
		String group = getGroup(p, suffix);
		if (group == null) {
			Contexts contexts = Main.LpAPI.getContextsForPlayer(p);
			User user = Main.LpAPI.getUserManager().getUser(p.getName());
			int gprio = -1;
			if (user == null) return -1;
			Map<Integer, String> test2 = user.getCachedData().getMetaData(contexts).getSuffixes();
			for (int i : test2.keySet()) {
				if (test2.get(i).equals(suffix)) gprio = i;
			}
			return gprio;


		} else return -1;
	}

	public static String getGroup(Player p, String suffix){
		for (String i : Main.vault.getPlayerGroups(p)) {
			if (Main.vault.getGroupSuffix(Bukkit.getWorlds().get(0).getName(), i).equals(suffix)) {
				return i;
			}
		}
		return null;
	}


	public static void error(String s){
		ServerUtils.permMsg("logs.tab", s);
		new Exception(s).printStackTrace();

	}


}
