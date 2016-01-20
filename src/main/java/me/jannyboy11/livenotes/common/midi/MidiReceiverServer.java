package me.jannyboy11.livenotes.common.midi;

import me.jannyboy11.livenotes.common.framework.LiveNote;

public class MidiReceiverServer extends MidiReceiverBase {

	public MidiReceiverServer(MidiDeviceManager manager) {
		super(manager);
	}

	@Override
	protected void playNote(LiveNote note) {
		manager.getMod().playOnServer(note);
	}

}
