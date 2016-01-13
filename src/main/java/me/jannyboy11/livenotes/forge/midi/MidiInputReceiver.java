package me.jannyboy11.livenotes.forge.midi;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;

import me.jannyboy11.livenotes.common.framework.LiveNoteInstrument;
import me.jannyboy11.livenotes.common.framework.LiveNote;
import me.jannyboy11.livenotes.common.helpers.UnimportantCrap;
import me.jannyboy11.livenotes.forge.LiveNotesMod;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MidiInputReceiver implements Receiver {

	//the MIDI tone that represents the lowest note (F#) of the minecraft BASS instrument
	private static final int LOWEST_NOTE = 30;
	//the MIDI tone that represents both the highest note of the minecraft BASS instrument,
	//as well as the lowest note of the minecraft PIANO instrument (F#)
	private static final int SPLIT_NOTE = 54;
	//the MIDI tone that represents the highest note (F#) of the minecraft PIANO instrument
	private static final int HIGHEST_NOTE = 78;

	private MidiDeviceManager manager;

	public MidiInputReceiver(MidiDeviceManager manager) {
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
					
					ByteBuf buf = Unpooled.wrappedBuffer(note.getBytes());
					PacketBuffer buffer = new PacketBuffer(buf);
					FMLProxyPacket packet = new FMLProxyPacket(buffer, UnimportantCrap.CHANNEL_NOTE);
					manager.getMod().getLiveNotesChannel().sendToServer(packet);
				}			
			}
		}
	}

	private LiveNote getNote(final int tone, final int velocity) {		
		LiveNoteInstrument instrument = tone < SPLIT_NOTE ? LiveNoteInstrument.BASS : LiveNoteInstrument.PIANO;
		int noteblockClicks = getNoteblockClicks(instrument, tone);
		float pitch = getPitch(noteblockClicks);
		float volume = getVolume(velocity);
		return new LiveNote(instrument, pitch, volume);
	}

	private int getNoteblockClicks(final LiveNoteInstrument instrument, final int miditone) {
		//#noteblockClicks = miditone - lowest possible tone for that instrument
		switch (instrument) {
		case BASS:
		case BASS_DRUM: 
		case BASS_GUITAR:
			return miditone - LOWEST_NOTE;
		case PIANO:
		case PLING:
		case SNARE_DRUM:
		case STICKS:
			 return miditone - SPLIT_NOTE;
		}
		return -1;
	}

	private float getPitch(final int noteblockClicks) {
		return (float) (0.5 * Math.pow(2D, noteblockClicks / 12D));
	}

	private float getVolume(final int velocity) {
		return (float) (((double) velocity) / 64D + 0.5D);	
	}

}
