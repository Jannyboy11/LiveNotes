package me.jannyboy11.livenotes.common.midi;

import java.util.Arrays;
import java.util.stream.Stream;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiDevice.Info;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Sequencer;
import javax.sound.midi.Transmitter;
import javax.sound.midi.spi.MidiDeviceProvider;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import me.jannyboy11.livenotes.common.framework.LiveNotesPluginMod;

public class MidiDeviceManager {
	
	private LiveNotesPluginMod pluginMod;
	private Receiver receiver;
	private MidiDevice device;
	
	public MidiDeviceManager(LiveNotesPluginMod mod, boolean clientside) {
		this.pluginMod = mod;
		receiver = clientside ? new MidiReceiverClient(this) : new MidiReceiverServer(this);		
	}

	public LiveNotesPluginMod getMod() {
		return pluginMod;
	}
	
	public void connectToMidiSequencer(Sequencer sequencer) {
		try {
			sequencer.getTransmitters().forEach(transmitter -> transmitter.setReceiver(receiver));
			sequencer.getTransmitter().setReceiver(receiver);
			sequencer.open();
		} catch (MidiUnavailableException e) {
			pluginMod.getLogger().warning("Midi transmitter not available!");
		}
	}
	
	public void connectToMidiInputDevice(String deviceInfo) {		
		for (Info midiDeviceInfo : MidiSystem.getMidiDeviceInfo()) {
			if (deviceInfo.equals(midiDeviceInfo.toString())) {
				try {
					MidiDevice device = MidiSystem.getMidiDevice(midiDeviceInfo);
					if (isMidiInDevice(device)) {
						device.getTransmitters().forEach(transmitter -> transmitter.setReceiver(receiver));
						device.getTransmitter().setReceiver(receiver);
						device.open();
						this.device = device;
					} else {
						pluginMod.getLogger().warning("Your device is not a midi input device!");
					}
				} catch (MidiUnavailableException e) {
					e.printStackTrace();
				}
				return;
			}
		}		
		if (device == null) {
			String available = Arrays.toString(Stream.of(MidiSystem.getMidiDeviceInfo()).map(info -> info.toString()).toArray());
			pluginMod.getLogger().warning("Midi Input Device " + deviceInfo + " not found!");
			pluginMod.getLogger().warning("Available devices: " + available);
			pluginMod.getLogger().warning("LiveNotes will not be able to propagate your midi messages to the server!");
		}
	}	
	
	public void close() {
		if (device != null) {
			device.close();
		}
	}
	
	private boolean isMidiInDevice(MidiDevice device) {
		try {
			return Class.forName("com.sun.media.sound.MidiInDevice").isInstance(device);
		} catch (ClassNotFoundException e) {
			pluginMod.getLogger().severe("Could not find the MidiInDevice class! Are you sure your installed java library supports midi?");
			return false;
		}
	}
}