package me.jannyboy11.livenotes.midi;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;

import org.bukkit.Bukkit;

import me.jannyboy11.livenotes.framework.Instrument;
import me.jannyboy11.livenotes.framework.Note;

public class MidiInputReceiver implements Receiver {

	private static final int LOWEST_NOTE = 30;
	private static final int HIGHEST_NOTE = 78;
	private static final int SPLIT_NOTE = 54;

	public MidiInputReceiver() {
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
					Note note = parseMessage(tone, velocity);
					Bukkit.getOnlinePlayers().forEach(player -> note.play(player));
				}			
			}
		}
	}

	private Note parseMessage(int tone, int velocity) {		
		Instrument instrument = tone < SPLIT_NOTE ? Instrument.BASS : Instrument.PIANO;
		float pitch = parsePitch(tone);
		float volume = parseVolume(velocity);
		return new Note(instrument, pitch, volume);
	}

	private float parsePitch(int midiTone) {
		double uses = (midiTone < SPLIT_NOTE ? midiTone + 24 : midiTone) - SPLIT_NOTE;
		return (float) (0.5 * Math.pow(2d, uses / 12d));
	}

	private float parseVolume(int velocity) {
		return (float) (((double) velocity) / 64D + 0.5D);	
	}

}
