package fr.entasia.tab.utils;

import fr.entasia.apis.utils.ReflectionUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
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
	public int weight;
	public String cutName;
	public String prefix;
//	public Group group; // ?!?
	public Character letter;

	public TabGroup(int weight, String name, String prefix){
		this.weight = weight;
		if(name.length()>16) this.cutName = name.substring(0, 15);
		else this.cutName = name;
		this.prefix = prefix;

		try{
			packet = PacketPlayOutScoreboardTeam.newInstance();
		}catch(Exception e){
			utils.error(e);
		}
		if(prefix!=null){
			setField("c", prefix);
		}
		setField("h", list); // collection (joueurs dans la team)

		setField("e", "always");
		setField("f", "1");
		setField("i", 0); // join quit
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

	public void sendPacket(){
		for(Player p : Bukkit.getOnlinePlayers()){
			ReflectionUtils.sendPacket(p, packet);
		}
	}

}
