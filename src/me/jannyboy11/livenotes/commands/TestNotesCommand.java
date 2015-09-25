package me.jannyboy11.livenotes.commands;

import me.jannyboy11.livenotes.LiveNotesPlugin;
import me.jannyboy11.livenotes.framework.Instrument;
import me.jannyboy11.livenotes.framework.Note;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginIdentifiableCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class TestNotesCommand extends Command implements PluginIdentifiableCommand {
	
	private LiveNotesPlugin plugin;

	public TestNotesCommand(LiveNotesPlugin plugin) {
		super("TestNotes");
		this.plugin = plugin;
		setUsage("/TestNotes <PIANO|BASS|BASS_GUITAR|PLING|BASS_DRUM|SNARE_DRUM|STICKS>");
	}

	@Override
	public boolean execute(CommandSender sender, String alias, String[] args) {
		if (!(sender instanceof Player)) {
			return false;
		}
		Player player = (Player) sender;
		float pitch = -1f;
		
		Instrument instrument = null;
		if (args.length >= 1) {
			try {
				instrument = Instrument.valueOf(args[0].toUpperCase());
			} catch (Exception ignored) {
			}
		}
		if (instrument == null) {
			instrument = Instrument.PIANO;
		}
		
		new NotePlayer(player, instrument, pitch).runTaskLater(plugin, 10L);
		
		return true;
	}

	@Override
	public Plugin getPlugin() {
		return plugin;
	}
	
	private class NotePlayer extends BukkitRunnable {
		
		private final Player player;
		private final Instrument instrument;
		private final float pitch;
		
		NotePlayer(Player player, Instrument instrument, float pitch) {
			this.player = player;
			this.instrument = instrument;
			this.pitch = pitch;
		}
		
		@Override
		public void run() {
			new Note(instrument, pitch, 16).play(player);
			if (pitch < 4f)
				new NotePlayer(player, instrument, pitch + 0.1f).runTaskLater(plugin, 5L);			
		}
	}

}
