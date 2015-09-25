package me.jannyboy11.livenotes.framework;

import lombok.Getter;

public enum Instrument {
	
	PIANO("note.harp"), BASS("note.bass"), BASS_GUITAR("note.bassattack"), PLING("note.pling"), BASS_DRUM("note.bd"), SNARE_DRUM("note.snare"), STICKS("note.hat");
	
	private @Getter String minecraftName;
	
	Instrument(String minecraftName){
		this.minecraftName = minecraftName;
	}

}
