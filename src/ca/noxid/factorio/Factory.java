package ca.noxid.factorio;

/**
 * Created by noxid on 08/08/17.
 */
public class Factory {

	public final String id;
	public final String name;
	public final String group;
	public final float speed;
	public final int moduleSlots;
	public final float powerUse;
	public final float pollution;

	public Factory(String id, String name, String group, float speed, int mods, float power, float pollution) {
		this.id = id;
		this.name = name;
		this.group = group;
		this.speed = speed;
		this.moduleSlots = mods;
		this.powerUse = power;
		this.pollution = pollution;
	}

}
