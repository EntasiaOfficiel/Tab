package fr.entasia.tab.utils;

public enum Mode {
	CREATE(0),
	DELETE(1),
	ADD_PLAYERS(3),
	;

	public int value;

	Mode(int mode) {
		this.value = mode;
	}
}
