package fr.entasia.tab.utils;

import fr.entasia.apis.other.Pair;
import fr.entasia.tab.Main;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.context.ImmutableContextSet;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Iterator;
import java.util.Map;
import java.util.Optional;

public class LPUtils {



	public static Pair<String, Integer> getPersonnalSuffix(Player p) { // BUT : Get la priorité max du suffix du joueur (SANS GROUPE)
		Pair<String, Integer> pair = new Pair<>();
		pair.key = Main.vaultAPI.getPlayerSuffix(Bukkit.getWorlds().get(0).getName(), p);
		String group = getGroup(p, pair.key);
		if (group == null) {
			User user = Main.lpAPI.getUserManager().getUser(p.getName());
			if (user == null) return null;
			Optional<ImmutableContextSet> contexts = Main.lpAPI.getContextManager().getContext(user);
			if (contexts.isPresent()){
				pair.value = -1;
//				CachedMetaData metaData = user.getCachedData().getMetaData(QueryOptions.contextual(contexts.get()));
				CachedMetaData metaData = user.getCachedData().getMetaData();
				for (Map.Entry<Integer, String> e : metaData.getSuffixes().entrySet()) {
					if (e.getValue().equals(pair.key)) pair.value = e.getKey();
				}
				if(pair.value==-1)return null;
				else return pair;
			}


		}
		return null;
	}

	private static String getGroup(Player p, String suffix){
		for (String i : Main.vaultAPI.getPlayerGroups(p)) {
			if (Main.vaultAPI.getGroupSuffix(Bukkit.getWorlds().get(0).getName(), i).equals(suffix)) {
				return i;
			}
		}
		return null;
	}


//	public static Pair<String, Integer> getSuffix(User user){
//		CachedMetaData meta = user.getCachedData().getMetaData();
//		Iterator<Map.Entry<Integer, String>> ite = meta.getSuffixes().entrySet().iterator();
//		if(ite.hasNext()){
//			Map.Entry<Integer, String> a = ite.next();
//			return new Pair<>(a.getValue(), a.getKey());
//		}else return null;
//	}

	public static Pair<String, Integer> getSuffix(Group gr){

		CachedMetaData meta = gr.getCachedData().getMetaData();
		Iterator<Map.Entry<Integer, String>> ite = meta.getSuffixes().entrySet().iterator();
		if(ite.hasNext()){
			Map.Entry<Integer, String> a = ite.next();
			return new Pair<>(a.getValue(), a.getKey());
		}else return null;
	}
}
