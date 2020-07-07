package fr.entasia.tab.utils;

import com.google.common.reflect.Reflection;
import fr.entasia.apis.utils.ReflectionUtils;
import fr.entasia.tab.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class TabGroup {

	/*
	- Packet de l'équipe

	Pars d'ici tant que tu le peux encore
	 */

	public static Class<?> PacketPlayOutScoreboardTeam;

	static{
		try{
			PacketPlayOutScoreboardTeam = ReflectionUtils.getNMSClass("PacketPlayOutScoreboardTeam");
		}catch(ReflectiveOperationException e){
			e.printStackTrace();
		}
	}

	public String id;

	public Collection<String> list = new ArrayList<>();
	public int priority;
	public String cutName;
	public String prefix;
	public Character letter;

	public TabGroup(int priority, String name, String prefix) {
		this.priority = priority;
		this.id = name;
		if (name.length() > 16) this.cutName = name.substring(0, 15);
		else this.cutName = name;

		if (prefix.length() > 15) Utils.error("Prefix too large : |" + prefix + "|");
		else this.prefix = prefix.replace("&", "§") + " ";
	}

	public void assignChar(Character letter) {
		this.letter = letter;
	}

//	private void setField(String field, Object value){
//		try{
//			ReflectionUtils.setField(packet, field, value);
//		}catch(ReflectiveOperationException e){
//			e.printStackTrace();
//		}
//	}

	private Object createPacket(Mode mode){
		try{
			Object packet = PacketPlayOutScoreboardTeam.newInstance();

			ReflectionUtils.setField(packet,"a", letter+cutName);
			ReflectionUtils.setField(packet,"b", letter+cutName);
			ReflectionUtils.setField(packet,"c", prefix); // prefix
			ReflectionUtils.setField(packet,"e", "always"); // display ?
			ReflectionUtils.setField(packet,"f", "1"); // aucune idée ?
			ReflectionUtils.setField(packet,"h", list); // collection (joueurs dans la team)
			ReflectionUtils.setField(packet,"i", mode.value); // mode  https://wiki.vg/Protocol#Display_Scoreboard
			ReflectionUtils.setField(packet,"j", 1); // friendly fire
			return packet;
		}catch(ReflectiveOperationException e){
			e.printStackTrace();
			return null;
		}
	}

	public synchronized void sendPacket(Player p, Mode mode){
		Object packet = createPacket(mode);
		System.out.println("sent packet for "+cutName+" to "+p.getName()+" (mode "+mode+") (members "+ Arrays.toString(list.toArray())+")");
		ReflectionUtils.sendPacket(p, packet);
	}

	public synchronized void sendPacketAll(Mode mode){
		sendPacketAll(mode, null);
	}

	public synchronized void sendPacketAll(Mode mode, String except){
		Object packet = createPacket(mode);
		System.out.println("sent packet for "+cutName+" to ALL PLAYERS (mode "+mode+") (members "+ Arrays.toString(list.toArray())+")");
		for(Player p : Bukkit.getOnlinePlayers()){
			if(p.getName().equals(except)){
//				System.out.println("except LOOP "+p.getName());
			}else{
//				System.out.println("sent packet for "+cutName+" to LOOP "+p.getName()+" (mode "+mode.value+") (members "+ Arrays.toString(list.toArray())+")");
				ReflectionUtils.sendPacket(p, packet);
			}
		}
	}

	public static TabGroup getByPrio(int w){
		for(TabGroup tg : Utils.tabGroups){
			if(tg.priority==w)return tg;
		}
		return null;
	}

}
