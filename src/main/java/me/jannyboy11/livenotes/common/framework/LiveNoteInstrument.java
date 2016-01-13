package me.jannyboy11.livenotes.common.framework;

public enum LiveNoteInstrument {

	PIANO("note.harp"),
	BASS("note.bass"),
	BASS_GUITAR("note.bassattack"),
	PLING("note.pling"),
	BASS_DRUM("note.bd"),
	SNARE_DRUM("note.snare"),
	STICKS("note.hat");

	private String minecraftName;

	private LiveNoteInstrument(String minecraftName){
		this.minecraftName = minecraftName;
	}
	
	public String getMinecraftName() {
		return minecraftName;
	}
	
}
