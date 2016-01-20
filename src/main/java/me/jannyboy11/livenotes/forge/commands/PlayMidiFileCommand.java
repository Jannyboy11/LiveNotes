package me.jannyboy11.livenotes.forge.commands;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;

import me.jannyboy11.livenotes.forge.LiveNotesForge;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;

public class PlayMidiFileCommand extends CommandBase {

	private final LiveNotesForge liveNotes;

	public PlayMidiFileCommand(LiveNotesForge liveNotesForge) {
		liveNotes = liveNotesForge;
	}

	public int getRequiredPermissionLevel() {
		return 3;
	}

	@Override
	public String getName() {
		return "playmidi";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "/playmidi <midifile>";
	}

	@Override
	public void execute(ICommandSender sender, String[] args) throws CommandException {
		if (args.length > 0) {
			String midiFileName = args[0];
			if (!midiFileName.endsWith(".mid")) {
				midiFileName += ".mid";
			}
			File midiFile = new File(liveNotes.getMidiFilesFolder(), midiFileName);
			if (!midiFile.exists()) {
				sender.addChatMessage(new ChatComponentText(midiFileName + " was not found in midifiles folder!"));
			}

			try {
				final Sequence sequence = MidiSystem.getSequence(midiFile);
				final Sequencer sequencer = MidiSystem.getSequencer();
				sequencer.setSequence(sequence);
				liveNotes.getDeviceManager().connectToMidiSequencer(sequencer);				

				final long durationMillis = sequence.getMicrosecondLength() / 1000;
				new Thread(() -> {
					sequencer.start();
				}).start();
				new Thread(() -> {
					try {
						Thread.sleep(durationMillis);
					} catch (Exception ignored) {
					}
					sequencer.stop();
				}).start();
				sender.addChatMessage(new ChatComponentText("Started playing " + midiFileName + "!"));
			} catch (InvalidMidiDataException e) {
				sender.addChatMessage(new ChatComponentText(midiFileName + " is not a valid midi file!"));
			} catch (IOException e) {
				sender.addChatMessage(new ChatComponentText("could not load " + midiFileName));
			} catch (MidiUnavailableException e) {
				sender.addChatMessage(new ChatComponentText("Default MIDI sequencer not found on your OS!"));
			}
		} else {
			sender.addChatMessage(new ChatComponentText("usage: " + getCommandUsage(sender)));
		}
	}
}