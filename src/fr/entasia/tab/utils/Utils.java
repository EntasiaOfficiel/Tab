package fr.entasia.tab.utils;

import fr.entasia.apis.other.Pair;
import fr.entasia.apis.utils.ServerUtils;
import fr.entasia.errors.EntasiaException;
import fr.entasia.tab.Main;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Comparator;

public class Utils {


	public static final char[] alfa = "0123456789abcdefghijklmnopqrstuvwxyz".toCharArray();

	public static ArrayList<TabGroup> tabGroups = new ArrayList<>();

	public static Comparator<TabGroup> comparator;

	static{
		comparator = Comparator.comparingInt(o -> o.priority);
		comparator = comparator.reversed();
	}

	public synchronized static void loadPriorities(){

		for(TabGroup tg : tabGroups) {
			tg.sendPacketAll(Mode.DELETE);
		}
		tabGroups.clear();

		// calcul des priorités
		for(Group gr : Main.lpAPI.getGroupManager().getLoadedGroups()){

			Pair<String, Integer> pair = LPUtils.getSuffix(gr);
			if(pair!=null){
				tabGroups.add(new TabGroup(pair.value, gr.getName(), pair.key));
			}
		}
		
		for(Player p : Bukkit.getOnlinePlayers()){
			Pair<String, Integer> pair = LPUtils.getPersonnalSuffix(p);
			if(pair!=null) tabGroups.add(new TabGroup(pair.value, p.getName(), pair.key));
		}

		tabGroups.sort(comparator);
		int i=0;
		for(TabGroup tg : tabGroups){
			tg.assignChar(alfa[i]);
			i++;
			tg.sendPacketAll(Mode.CREATE);
		}
	}

	public static void loadAllUsers() {
		for(Player p : Bukkit.getOnlinePlayers()) loadUser(p);
	}


	public static void loadUser(Player p) {

		p.setCustomNameVisible(true);

		User user = Main.lpAPI.getUserManager().getUser(p.getName());
		if (user == null) error("Luckperms user could not being loaded for " + p.getName());
		else {
			Pair<String, Integer> pair = LPUtils.getSuffix(user);
			if(pair==null)error("Luckperms suffix not found for "+p.getName());
			else{
				TabGroup tg = TabGroup.getByName(pair.value);
				if (tg == null) {
					loadPriorities();
					tg = TabGroup.getByName(pair.value);
					if(tg == null) {
						error("TabGroup could be loaded for prefix "+pair.key+" priority "+pair.value);
					}
				}

				assert tg != null;

				p.setPlayerListName(pair.key.replace("&", "§")+" §7"+p.getDisplayName());
				tg.list.add(p.getName());

				for(TabGroup i : tabGroups){
					i.sendPacket(p, Mode.CREATE);
				}
				tg.sendPacketAll(Mode.ADD_PLAYERS, p.getName());
			}
		}
	}


	public static void error(Exception e){
		ServerUtils.permMsg("log.tab", e.getMessage());
		throw new EntasiaException(e);
	}

	public static void error(String s){
		ServerUtils.permMsg("log.tab", s);
		throw new EntasiaException(s);
	}


}
