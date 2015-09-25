package me.jannyboy11.livenotes;

import lombok.Getter;
import me.jannyboy11.livenotes.commands.TestNotesCommand;
import me.jannyboy11.livenotes.midi.MidiDeviceManager;

import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.plugin.java.JavaPlugin;

/*	+-------------------------------------------------+
 *  | Livenotes plugin! Play notes on a midi          |
 *  | keyboard and let players hear the music!        |
 *  | TODO later implement midi file support as well  |
 *  +-------------------------------------------------+
 */

public class LiveNotesPlugin extends JavaPlugin {
	
	private @Getter MidiDeviceManager midiManager;
	
	@Override
	public void onEnable() {
		reloadConfig();
		saveConfig();
		
		midiManager = new MidiDeviceManager(this);
		midiManager.connect();
		
		CraftServer server = (CraftServer) getServer();
		server.getCommandMap().register("note", new TestNotesCommand(this));
		
	}
	
	public void onDisable() {
		midiManager.disconnect();
	}

}
