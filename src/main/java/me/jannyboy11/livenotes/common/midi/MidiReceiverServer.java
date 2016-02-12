package me.jannyboy11.livenotes.common.midi;

import me.jannyboy11.livenotes.common.framework.LiveNote;
import me.jannyboy11.livenotes.common.framework.LiveNoteInstrument;

public class MidiReceiverServer extends MidiReceiverBase {

	public MidiReceiverServer(MidiDeviceManager manager) {
		super(manager);
	}

	@Override
	protected void playNote(LiveNote note) {
		manager.getMod().playOnServer(note);
	}
	
	@Override
	protected int getNoteblockClicks(LiveNoteInstrument instrument, int miditone) {
		switch (instrument.getRange()) {
		case FIS1_FIS3 :
			while (miditone < MidiReceiverBase.LOWEST_NOTE) {
				miditone += 12;
			}
			return miditone - LOWEST_NOTE;
		case FIS3_FIS5 :
			while (miditone > MidiReceiverBase.HIGHEST_NOTE) {
				miditone -= 12;
			}
			return miditone - SPLIT_NOTE;
		}
		return -1;
	}

	@Override
	protected void displayNote(int miditone) {
		manager.getMod().displayOnServer(miditone);
	}			

}
