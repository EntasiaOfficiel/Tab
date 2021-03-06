package fr.entasia.tab;

import fr.entasia.apis.other.Pair;
import fr.entasia.apis.utils.LPUtils;
import fr.entasia.apis.utils.ServerUtils;
import fr.entasia.errors.EntasiaException;
import fr.entasia.tab.utils.Mode;
import fr.entasia.tab.utils.TabGroup;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.model.PermissionHolder;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;

public class Utils {


	public static final char[] alfa = "0123456789abcdefghijklmnopqrstuvwxyz".toCharArray();

	public static ArrayList<TabGroup> tabGroups = new ArrayList<>();

	public static Comparator<TabGroup> comparator;

	static{
		comparator = Comparator.comparingInt(o -> o.priority);
		comparator = comparator.reversed();
	}

//	public synchronized static void loadPrioritiesa() {
//		System.out.println("loading prios");
//		tabGroups.clear();
//		admin = new TabGroup(150, "admin", "§cAdmin");
//		def = new TabGroup(50, "default", "§8Joueur");
//
//		admin.assignChar('A');
//		def.assignChar('B');
//
//		admin.list.add("mortel1211");
//		def.list.add("iTrooz_");
//
//		tabGroups.add(admin);
//		tabGroups.add(def);
//	}

	public static Pair<String, Integer> getPrefix(PermissionHolder holder) {
		CachedMetaData meta = holder.getCachedData().getMetaData();
		Iterator<Map.Entry<Integer, String>> ite = meta.getPrefixes().entrySet().iterator();
		if(ite.hasNext()){
			Map.Entry<Integer, String> a = ite.next();
			return new Pair<>(a.getValue().replace("&", "§"), a.getKey());
		}else return null;
	}

	public static Pair<String, Integer> getSuffix(PermissionHolder holder) {
		CachedMetaData meta = holder.getCachedData().getMetaData();
		Iterator<Map.Entry<Integer, String>> ite = meta.getSuffixes().entrySet().iterator();
		if(ite.hasNext()){
			Map.Entry<Integer, String> a = ite.next();
			return new Pair<>(a.getValue().replace("&", "§"), a.getKey());
		}else return null;
	}

	public synchronized static void loadPriorities(){

//		System.out.println("loading prios");
		for(TabGroup tg : tabGroups) {
//			System.out.println("del "+tg);
			tg.sendPacketAll(Mode.DELETE);
		}
		tabGroups.clear();

		// calcul des priorités
		for(Group gr : LPUtils.lpAPI.getGroupManager().getLoadedGroups()){

			Pair<String, Integer> pair = getSuffix(gr);
			if(pair!=null){
				tabGroups.add(new TabGroup(pair.value, gr.getName(), pair.key));
			}
		}

		for(Player p : Bukkit.getOnlinePlayers()){
			Pair<String, Integer> pair = LPUtils.getSuffix(p);
			if(pair==null)continue;
			for(TabGroup tg : tabGroups){
				if(tg.priority==pair.value&&tg.suffix.equals(pair.key)){
					break;
				}
			}
			tabGroups.add(new TabGroup(pair.value, p.getName(), pair.key));
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
		for(Player p : Bukkit.getOnlinePlayers()) loadUser(p, false);
	}


	public static void loadUser(Player p, boolean join) {

		p.setCustomNameVisible(true);

		User user = LPUtils.getUser(p);
		if (user == null) error("Luckperms user could not being loaded for " + p.getName());
		else {

			Pair<String, Integer> pair = getSuffix(user);
			if(pair!=null) {
				TabGroup tg = TabGroup.getByPrio(pair.value);
				if (tg == null) {
					loadPriorities();
					tg = TabGroup.getByPrio(pair.value);
					if (tg == null) {
						error("TabGroup could be loaded for prefix " + pair.key + " priority " + pair.value);
					}
				}

				assert tg != null;
				String prefix = pair.key;
				pair = getPrefix(user);
				if(pair!=null)prefix = pair.key;
				p.setPlayerListName(prefix + " §7" + p.getDisplayName());
				tg.list.add(p.getName());

				if(join){
					for (TabGroup i : tabGroups) {
						i.sendPacket(p, Mode.CREATE);
					}
				}
				tg.sendPacketAll(Mode.ADD_PLAYERS, p.getName());
			}else error("Luckperms suffix not found for "+p.getName());
		}
	}


	public static void error(String s){
		ServerUtils.permMsg("log.tab", "§4Tab : "+s);
		throw new EntasiaException(s);
	}

}
