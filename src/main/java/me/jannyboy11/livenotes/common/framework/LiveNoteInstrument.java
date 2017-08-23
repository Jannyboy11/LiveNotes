package me.jannyboy11.livenotes.common.framework;

import me.jannyboy11.livenotes.common.framework.LiveNoteInstrumentRange;

public enum LiveNoteInstrument {
    BASS("note.bass", "block.note.bass", LiveNoteInstrumentRange.FIS1_FIS3),
    BASS_GUITAR("note.bassattack", "block.note.bassattack", LiveNoteInstrumentRange.FIS1_FIS3),
    PIANO("note.harp", "block.note.harp", LiveNoteInstrumentRange.FIS3_FIS5),
    PLING("note.pling", "block.note.pling", LiveNoteInstrumentRange.FIS3_FIS5),
    BASS_DRUM("note.bd", "block.note.basedrum", null),
    SNARE_DRUM("note.snare", "block.note.snare", null),
    STICKS("note.hat", "block.note.hat", null);
    
    private String legacyMinecraftName;
    private String newMinecraftName;
    private LiveNoteInstrumentRange range;

    private LiveNoteInstrument(String legacyMinecraftName, String newMinecraftName, LiveNoteInstrumentRange range) {
        this.legacyMinecraftName = legacyMinecraftName;
        this.newMinecraftName = newMinecraftName;
        this.range = range;
    }

    @Deprecated
    public String getMinecraftName() {
        return this.legacyMinecraftName;
    }

    public String getNewMinecraftName() {
        return this.newMinecraftName;
    }

    public LiveNoteInstrumentRange getRange() {
        return this.range;
    }
}

