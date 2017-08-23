package me.jannyboy11.livenotes.bukkit.messaging;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import me.jannyboy11.livenotes.common.framework.LiveNote;
import me.jannyboy11.livenotes.common.messaging.LiveNotesPlayer;
import me.jannyboy11.livenotes.common.midi.MidiReceiverBase;

public class LiveNotesPlayerBukkit implements LiveNotesPlayer {

	private Player player;
	
	public LiveNotesPlayerBukkit(Player player) {
		this.player = player;
	}
	
	@Override
	public void playNote(LiveNote note) {
	    //can't be bothered to do a client version check.
		player.playSound(
				player.getLocation(),
				note.getInstrument().getMinecraftName(),
				note.getVolume(),
				note.getPitch());
		player.playSound(
                player.getLocation(),
                note.getInstrument().getNewMinecraftName(),
                note.getVolume(),
                note.getPitch());
	}
	
	public Player getPlayer() {
		return player;
	}

	@Override
	public void kick(String reason) {
		player.kickPlayer(reason);
	}

	@Override
	public String getName() {
		return player.getName();
	}
	
	@Override
	public void displayNote(int midiPitch) {
		final Location location = player.getLocation();
		Vector direction = location.getDirection().setY(0);
		
		Vector distance = direction.clone().multiply(3);
		
		final Location startingPoint = location.clone().add(distance);
		startingPoint.setY(location.getY() + 3);
		
		double multiplier = (midiPitch - MidiReceiverBase.SPLIT_NOTE) * 0.10D;
		
		final Location effectLocation = startingPoint.clone();
		effectLocation.setYaw(location.getYaw() + 90);
		Vector toAdd = effectLocation.getDirection().setY(0).multiply(multiplier);
		effectLocation.add(toAdd);
		
		player.playEffect(effectLocation, Effect.NOTE, 0);
	}

}
