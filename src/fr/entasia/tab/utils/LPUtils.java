package fr.entasia.tab.utils;

import fr.entasia.apis.other.Pair;
import fr.entasia.tab.Main;
import me.lucko.luckperms.api.Contexts;
import me.lucko.luckperms.api.LocalizedNode;
import me.lucko.luckperms.api.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.Optional;

public class LPUtils {



	public static Pair<String, Integer> getPlayerPriority(Player p) { // BUT : Get la priorité max du suffix du joueur (SANS GROUPE)
		Pair<String, Integer> pair = new Pair<>();
		pair.key = Main.vaultAPI.getPlayerSuffix(Bukkit.getWorlds().get(0).getName(), p);
		String group = getGroup(p, pair.key);
		if (group == null) {
			Contexts contexts = Main.LpAPI.getContextsForPlayer(p);
			User user = Main.LpAPI.getUserManager().getUser(p.getName());
			if (user == null) return null;
			pair.value = -1;
			for (Map.Entry<Integer, String> e : user.getCachedData().getMetaData(contexts).getSuffixes().entrySet()) {
				if (e.getValue().equals(pair.key)) pair.value = e.getKey();
			}
			if(pair.value==-1)return null;
			else return pair;


		} else return null;
	}

	private static String getGroup(Player p, String suffix){
		for (String i : Main.vaultAPI.getPlayerGroups(p)) {
			if (Main.vaultAPI.getGroupSuffix(Bukkit.getWorlds().get(0).getName(), i).equals(suffix)) {
				return i;
			}
		}
		return null;
	}

	public static LocalizedNode getHighestSuffix(User user){

		LocalizedNode lnode = null;
		Optional<Contexts> contexts = Main.LpAPI.getContextForUser(user);
		if(contexts.isPresent()){
			for (LocalizedNode node : user.resolveInheritances(contexts.get())) {
				if (node.isSuffix()) {
					if (lnode == null || node.getSuffix().getKey() > lnode.getSuffix().getKey()) {
						lnode = node;
					}
				}
			}
		}
		return lnode;
	}
}
