package me.jannyboy11.livenotes.forge.midi;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;

import me.jannyboy11.livenotes.common.framework.LiveNoteInstrument;
import me.jannyboy11.livenotes.common.framework.LiveNote;
import me.jannyboy11.livenotes.forge.LiveNotesMod;
import me.jannyboy11.livenotes.forge.messaging.LiveNoteMessage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MidiInputReceiver implements Receiver {

	private static final int LOWEST_NOTE = 30;
	private static final int HIGHEST_NOTE = 78;
	private static final int SPLIT_NOTE = 54;
	
	private MidiDeviceManager manager;

	public MidiInputReceiver(MidiDeviceManager manager) {
		this.manager = manager;
	}

	@Override
	public void close() {
	}

	@Override
	public void send(MidiMessage message, long timestamp) {
		if (message instanceof ShortMessage) {
			ShortMessage shortMessage = (ShortMessage) message;
			if (shortMessage.getCommand() == ShortMessage.NOTE_ON) {
				int tone = shortMessage.getData1();
				int velocity = shortMessage.getData2();
				if (velocity > 0) {
					LiveNote note = parseMessage(tone, velocity);
					manager.getMod().getLiveNotesChannel().sendToServer(new LiveNoteMessage(note));
				}			
			}
		}
	}

	private LiveNote parseMessage(int tone, int velocity) {		
		LiveNoteInstrument instrument = tone < SPLIT_NOTE ? LiveNoteInstrument.BASS : LiveNoteInstrument.PIANO;
		float pitch = parsePitch(tone);
		float volume = parseVolume(velocity);
		return new LiveNote(instrument, pitch, volume);
	}

	private float parsePitch(int midiTone) {
		double uses = (midiTone < SPLIT_NOTE ? midiTone + 24 : midiTone) - SPLIT_NOTE;
		return (float) (0.5 * Math.pow(2d, uses / 12d));
	}

	private float parseVolume(int velocity) {
		return (float) (((double) velocity) / 64D + 0.5D);	
	}

}
