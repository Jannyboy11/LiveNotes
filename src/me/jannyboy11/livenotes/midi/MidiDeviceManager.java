package me.jannyboy11.livenotes.midi;

import java.util.Arrays;
import java.util.stream.Stream;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiDevice.Info;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;

import org.bukkit.configuration.file.FileConfiguration;

import lombok.Getter;
import me.jannyboy11.livenotes.LiveNotesPlugin;

public class MidiDeviceManager {
	
	private @Getter LiveNotesPlugin plugin;
	private MidiInputReceiver receiver;
	private MidiDevice keyboard;
	
	public MidiDeviceManager(LiveNotesPlugin plugin) {
		this.plugin = plugin;
		receiver = new MidiInputReceiver();		
	}

	public void connect() {
		FileConfiguration config = plugin.getConfig();
		String deviceInfo = config.getString("midi-in-device-info");
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
						
					}
				} catch (MidiUnavailableException e) {
					e.printStackTrace();
				}				
			}
		}
		
		if (keyboard == null) {
			String available = Arrays.toString(Stream.of(MidiSystem.getMidiDeviceInfo()).map(info -> info.toString()).toArray());
			plugin.getLogger().severe("Midi Input Device not found!");
			plugin.getLogger().severe("Available devices: " + available);
			plugin.getLogger().severe("LiveNotes will now be disabled!");
			plugin.getServer().getPluginManager().disablePlugin(plugin);
		}
	}	
	
	public void disconnect() {
		keyboard.close();
	}
	
	private boolean isMidiInDevice(MidiDevice device) {
		try {
			return Class.forName("com.sun.media.sound.MidiInDevice").isInstance(device);
		} catch (ClassNotFoundException e) {
			plugin.getLogger().severe("Could not find the MidiInDevice class! Are you sure your installed java library supports midi?");
			return false;
		}
	}
}
