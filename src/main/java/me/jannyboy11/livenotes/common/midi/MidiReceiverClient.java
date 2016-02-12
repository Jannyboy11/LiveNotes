package me.jannyboy11.livenotes.common.midi;

import me.jannyboy11.livenotes.common.framework.LiveNote;

public class MidiReceiverClient extends MidiReceiverBase {

	public MidiReceiverClient(MidiDeviceManager manager) {
		super(manager);
	}

	@Override
	protected void playNote(LiveNote note) {
		manager.getMod().playFromClient(note);
	}

	@Override
	protected void displayNote(int miditone) {
		// TODO Auto-generated method stub
		
	}

}
