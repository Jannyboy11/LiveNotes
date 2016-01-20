package me.jannyboy11.livenotes.bukkit.commands;

import java.io.File;
import java.io.IOException;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;

import me.jannyboy11.livenotes.bukkit.LiveNotesBukkit;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

public class PlayMidiFileCommandExecutor implements CommandExecutor {
	
	private final LiveNotesBukkit liveNotes;
	
	public PlayMidiFileCommandExecutor(LiveNotesBukkit liveNotes) {
		this.liveNotes = liveNotes;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length > 0) {
			String midiFileName = args[0];
			if (!midiFileName.endsWith(".mid")) {
				midiFileName += ".mid";
			}
			File midiFile = new File(liveNotes.getMidiFilesFolder(), midiFileName);
			if (!midiFile.exists()) {
				sender.sendMessage(ChatColor.RED + midiFileName + " was not found in midifiles folder!");
			}
			
			try {
				final Sequence sequence = MidiSystem.getSequence(midiFile);
				final Sequencer sequencer = MidiSystem.getSequencer();
				sequencer.setSequence(sequence);
				liveNotes.getDeviceManager().connectToMidiSequencer(sequencer);				
				
				final long durationMillis = sequence.getMicrosecondLength() / 1000;
				final BukkitScheduler scheduler = liveNotes.getServer().getScheduler();
				scheduler.runTaskAsynchronously(liveNotes, () -> sequencer.start());
				scheduler.runTaskLater(liveNotes, () -> sequencer.stop(), durationMillis / 50);
				sender.sendMessage(ChatColor.GREEN + "Started playing " + ChatColor.WHITE + midiFileName + ChatColor.GREEN + "!");
				
			} catch (InvalidMidiDataException e) {
				sender.sendMessage(ChatColor.RED + midiFileName + " is not a valid midi file!");
			} catch (IOException e) {
				sender.sendMessage(ChatColor.RED + "could not load " + midiFileName);
			} catch (MidiUnavailableException e) {
				sender.sendMessage(ChatColor.RED + "Default MIDI sequencer not found on your OS!");
			}
			
			return true;
		} else {
			return false;
		}
	}

}
