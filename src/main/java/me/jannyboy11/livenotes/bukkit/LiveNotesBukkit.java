package me.jannyboy11.livenotes.bukkit;

import java.io.File;

import me.jannyboy11.livenotes.bukkit.commands.PlayMidiFileCommandExecutor;
import me.jannyboy11.livenotes.bukkit.messaging.LiveNoteMessageListenerBukkit;
import me.jannyboy11.livenotes.bukkit.messaging.LiveNotesPlayerBukkit;
import me.jannyboy11.livenotes.common.framework.LiveNote;
import me.jannyboy11.livenotes.common.framework.LiveNotesPluginMod;
import me.jannyboy11.livenotes.common.helpers.LiveNotesStatics;
import me.jannyboy11.livenotes.common.messaging.LiveNotesPlayer;
import me.jannyboy11.livenotes.common.midi.MidiDeviceManager;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListenerRegistration;

public class LiveNotesBukkit extends JavaPlugin implements LiveNotesPluginMod {
	
	private File midiFilesFolder;
	
	private final MidiDeviceManager midiDeviceManager = new MidiDeviceManager(this, false); 
	
	@Override
	public File getMidiFilesFolder() {
		return midiFilesFolder;
	}

	public void onEnable() {
		getServer().getMessenger().registerIncomingPluginChannel(this, LiveNotesStatics.CHANNEL_NOTE, new LiveNoteMessageListenerBukkit(this));
		
		File pluginFolder = getDataFolder();
		midiFilesFolder = new File(pluginFolder, "midifiles");
		if (!midiFilesFolder.exists()) {
			midiFilesFolder.mkdir();
		}
		
		getCommand("playmidi").setExecutor(new PlayMidiFileCommandExecutor(this));
	}
	
	@Override
	public void onDisable() {
		getServer().getMessenger().unregisterIncomingPluginChannel(this);
	}

	@Override
	public void playOnServer(LiveNote note) {
		getServer().getOnlinePlayers().stream().map(LiveNotesPlayerBukkit::new).forEach(liveNotesPlayer -> liveNotesPlayer.playNote(note));
	}

	@Override
	public MidiDeviceManager getDeviceManager() {
		return midiDeviceManager;
	}
	
	@Override
	public void displayOnServer(int miditone) {
		getServer().getOnlinePlayers().stream().map(LiveNotesPlayerBukkit::new).forEach(liveNotesPlayer -> liveNotesPlayer.displayNote(miditone));
	}

	@Override
	public void playFromClient(LiveNote note) {
		throw new UnsupportedOperationException("Did you just mod your client with a bukkit plugin? Congratz!");
	}

}
