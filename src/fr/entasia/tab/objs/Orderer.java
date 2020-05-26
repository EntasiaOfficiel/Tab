package fr.entasia.tab.objs;

import me.lucko.luckperms.api.Group;

public class Orderer {

	public int weight;
	public String suffix;
//	public Group group; // ?!?
	public Character letter;

	public Orderer(int weight){
		this.weight = weight;
	}

	public Orderer(int weight, String suffix){
		this.weight = weight;
		this.suffix = suffix;
	}
}
