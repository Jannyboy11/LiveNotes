package me.jannyboy11.livenotes.common.midi;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;

import me.jannyboy11.livenotes.common.framework.LiveNoteInstrument;
import me.jannyboy11.livenotes.common.framework.LiveNote;
import me.jannyboy11.livenotes.common.helpers.LiveNotesStatics;
import me.jannyboy11.livenotes.forge.LiveNotesForge;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class MidiReceiverBase implements Receiver {

	//the MIDI tone that represents the lowest note (F#1) of the minecraft BASS instrument
	public static final int LOWEST_NOTE = 30;
	//the MIDI tone that represents both the highest note of the minecraft BASS instrument,
	//as well as the lowest note of the minecraft PIANO instrument (F#3)
	public static final int SPLIT_NOTE = 54;
	//the MIDI tone that represents the highest note (F#5) of the minecraft PIANO instrument
	public static final int HIGHEST_NOTE = 78;

	protected MidiDeviceManager manager;

	public MidiReceiverBase(MidiDeviceManager manager) {
		this.manager = manager;
	}

	@Override
	public void close() {
	}

	@Override
	public void send(final MidiMessage message, final long timestamp) {
		if (message instanceof ShortMessage) {
			ShortMessage shortMessage = (ShortMessage) message;
			if (shortMessage.getCommand() == ShortMessage.NOTE_ON) {
				int tone = shortMessage.getData1();
				int velocity = shortMessage.getData2();
				if (velocity > 0) {
					LiveNote note = getNote(tone, velocity);
					playNote(note);
					displayNote(tone);
				}			
			}
		}
	}
	
	protected abstract void playNote(LiveNote note);
	protected abstract void displayNote(int miditone);

	protected LiveNote getNote(final int tone, final int velocity) {		
		LiveNoteInstrument instrument = tone < SPLIT_NOTE ? LiveNoteInstrument.BASS : LiveNoteInstrument.PIANO;
		int noteblockClicks = getNoteblockClicks(instrument, tone);
		float pitch = getPitch(noteblockClicks);
		float volume = getVolume(velocity);
		return new LiveNote(instrument, pitch, volume);
	}

	protected int getNoteblockClicks(final LiveNoteInstrument instrument, final int miditone) {
		switch (instrument.getRange()) {
		case FIS1_FIS3 :
			return miditone - LOWEST_NOTE;
		case FIS3_FIS5 :
			 return miditone - SPLIT_NOTE;
		}
		return -1;
	}

	protected float getPitch(final int noteblockClicks) {
		return (float) (0.5 * Math.pow(2D, noteblockClicks / 12D));
	}

	protected float getVolume(final int velocity) {
		return (float) (((double) velocity) / 64D + 0.5D);	
	}

}
