package fr.entasia.tab.utils;

import fr.entasia.apis.other.Pair;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;

import java.util.Iterator;
import java.util.Map;

public class LPUtils {


	public static Pair<String, Integer> getHighestPref(User u) {
		CachedMetaData meta = u.getCachedData().getMetaData();
		Iterator<Map.Entry<Integer, String>> ite = meta.getPrefixes().entrySet().iterator();
		if(ite.hasNext()){
			Map.Entry<Integer, String> a = ite.next();
			return new Pair<>(a.getValue(), a.getKey());
		}else return null;
	}
	public static Pair<String, Integer> getHighestSuff(User u) {
		CachedMetaData meta = u.getCachedData().getMetaData();
		Iterator<Map.Entry<Integer, String>> ite = meta.getSuffixes().entrySet().iterator();
		if(ite.hasNext()){
			Map.Entry<Integer, String> a = ite.next();
			return new Pair<>(a.getValue(), a.getKey());
		}else return null;
	}

	public static Pair<String, Integer> getHighestSuff(Group gr){

		CachedMetaData meta = gr.getCachedData().getMetaData();
		Iterator<Map.Entry<Integer, String>> ite = meta.getSuffixes().entrySet().iterator();
		if(ite.hasNext()){
			Map.Entry<Integer, String> a = ite.next();
			return new Pair<>(a.getValue(), a.getKey());
		}else return null;
	}
}
