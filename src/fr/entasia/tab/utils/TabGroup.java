package fr.entasia.tab.utils;

import fr.entasia.apis.utils.ReflectionUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class TabGroup {

	/*
	- Packet de l'équipe
	 */

	public static Class<?> PacketPlayOutScoreboardTeam;

	static{
		try{
			PacketPlayOutScoreboardTeam = ReflectionUtils.getNMSClass("PacketPlayOutScoreboardTeam");
		}catch(ReflectiveOperationException e){
			e.printStackTrace();
		}
	}

	public Object packet;
	public Collection<String> list = new ArrayList<>();
	public int priority;
	public String cutName;
//	public Group group; // ?!?
	public Character letter;

	public TabGroup(int priority, String name, String prefix){
		this.priority = priority;
		if(name.length()>16) this.cutName = name.substring(0, 15);
		if(prefix.length()>15) Utils.error("Prefix too large : |"+prefix+"|");
		else this.cutName = name;

		try{
			packet = PacketPlayOutScoreboardTeam.newInstance();
		}catch(Exception e){
			Utils.error(e);
		}

		setField("c", prefix.replace("&", "§")+" "); // prefix
		setField("h", list); // collection (joueurs dans la team)

		setField("e", "always");
		setField("f", "1");
		setField("i", 0); // create mode  https://wiki.vg/Protocol#Display_Scoreboard
		setField("j", 1); // friendly fire
	}
	public void assignChar(Character letter) {
		this.letter = letter;
		setField("a", letter+cutName);
		setField("b", letter+cutName);
	}

	private void setField(String field, Object value){
		try{
			ReflectionUtils.setField(packet, field, value);
		}catch(ReflectiveOperationException e){
			e.printStackTrace();
		}
	}

	public synchronized void sendPacket(Player p, Mode mode){
		setField("i", mode.value);
		System.out.println("sent packet for "+cutName+" to "+p.getName()+" (mode "+mode.value+") (members "+ Arrays.toString(list.toArray())+")");
		ReflectionUtils.sendPacket(p, packet);
	}

	public synchronized void sendPacketAll(Mode mode){
		sendPacketAll(mode, null);
	}

	public synchronized void sendPacketAll(Mode mode, String except){
		setField("i", mode.value);
//		System.out.println("sent packet for "+cutName+" to ALL PLAYERS (mode "+mode.value+") (members "+ Arrays.toString(list.toArray())+")");
		for(Player p : Bukkit.getOnlinePlayers()){
			if(p.getName().equals(except))continue;
			System.out.println("sent packet for "+cutName+" to () "+p.getName()+" (mode "+mode.value+") (members "+ Arrays.toString(list.toArray())+")");
			ReflectionUtils.sendPacket(p, packet);
		}
	}

	public static TabGroup getByName(int w){
		for(TabGroup tg : Utils.tabGroups){
			if(tg.priority ==w)return tg;
		}
		return null;
	}

}
