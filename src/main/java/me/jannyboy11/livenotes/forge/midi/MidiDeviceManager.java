package me.jannyboy11.livenotes.forge.midi;

import java.util.Arrays;
import java.util.stream.Stream;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiDevice.Info;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.spi.MidiDeviceProvider;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import me.jannyboy11.livenotes.forge.LiveNotesMod;

public class MidiDeviceManager {
	
	private LiveNotesMod mod;
	private MidiInputReceiver receiver;
	private MidiDevice keyboard;
	
	public MidiDeviceManager(LiveNotesMod mod) {
		this.mod = mod;
		receiver = new MidiInputReceiver(this);		
	}

	public LiveNotesMod getMod() {
		return mod;
	}
	
	public void connect(String deviceInfo) {
		
		for (Info midiDeviceInfo : MidiSystem.getMidiDeviceInfo()) {
			if (deviceInfo.equals(midiDeviceInfo.toString())) {
				try {
					MidiDevice device = MidiSystem.getMidiDevice(midiDeviceInfo);
					if (isMidiInDevice(device)) {
						device.getTransmitters().forEach(transmitter -> transmitter.setReceiver(receiver));
						device.getTransmitter().setReceiver(receiver);
						device.open();
						keyboard = device;
					} else {
						mod.getLogger().warning("Your device is not a midi input device!");
					}
				} catch (MidiUnavailableException e) {
					e.printStackTrace();
				}
				return;
			}
		}
		
		if (keyboard == null) {
			String available = Arrays.toString(Stream.of(MidiSystem.getMidiDeviceInfo()).map(info -> info.toString()).toArray());
			mod.getLogger().warning("Midi Input Device " + deviceInfo + " not found!");
			mod.getLogger().warning("Available devices: " + available);
			mod.getLogger().warning("LiveNotes will not be able to propagate your midi messages to the server!");
		}
	}	
	
	public void disconnect() {
		if (keyboard != null) {
			keyboard.close();
		}
	}
	
	private boolean isMidiInDevice(MidiDevice device) {
		try {
			return Class.forName("com.sun.media.sound.MidiInDevice").isInstance(device);
		} catch (ClassNotFoundException e) {
			mod.getLogger().severe("Could not find the MidiInDevice class! Are you sure your installed java library supports midi?");
			return false;
		}
	}
}