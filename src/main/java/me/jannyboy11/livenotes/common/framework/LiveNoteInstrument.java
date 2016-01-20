package me.jannyboy11.livenotes.common.framework;

public enum LiveNoteInstrument {

	BASS("note.bass", LiveNoteInstrumentRange.FIS1_FIS3),
	BASS_GUITAR("note.bassattack", LiveNoteInstrumentRange.FIS1_FIS3),
	PIANO("note.harp", LiveNoteInstrumentRange.FIS3_FIS5),
	PLING("note.pling", LiveNoteInstrumentRange.FIS3_FIS5),
	BASS_DRUM("note.bd", null),
	SNARE_DRUM("note.snare", null),
	STICKS("note.hat", null);

	private String minecraftName;
	private LiveNoteInstrumentRange range;

	private LiveNoteInstrument(String minecraftName, LiveNoteInstrumentRange range){
		this.minecraftName = minecraftName;
		this.range = range;
	}

	public String getMinecraftName() {
		return minecraftName;
	}
	
	public LiveNoteInstrumentRange getRange() {
		return range;
	}

}
