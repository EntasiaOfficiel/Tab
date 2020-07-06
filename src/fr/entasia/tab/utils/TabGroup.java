package fr.entasia.tab.utils;

import fr.entasia.apis.utils.ReflectionUtils;
import net.minecraft.server.v1_9_R2.MerchantRecipe;
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

	public void setField(String field, Object value){
		ReflectionUtils.setField(packet, field, value);
	}

	public void sendPacket(Player p){
		ReflectionUtils.sendPacket(p, packet);
	}

	public void sendPacket(){
		for(Player p : Bukkit.getOnlinePlayers()){
			sendPacket(p);
		}
	}



	public static TabGroup getByPriority(int w){
		for(TabGroup tg : Utils.tabGroups){
			if(tg.priority ==w)return tg;
		}
		return null;
	}

}
