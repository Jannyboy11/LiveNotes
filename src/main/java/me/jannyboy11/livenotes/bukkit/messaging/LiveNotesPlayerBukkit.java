package me.jannyboy11.livenotes.bukkit.messaging;

import org.bukkit.entity.Player;

import me.jannyboy11.livenotes.common.framework.LiveNote;
import me.jannyboy11.livenotes.common.messaging.LiveNotesPlayer;

public class LiveNotesPlayerBukkit implements LiveNotesPlayer {

	private Player player;
	
	public LiveNotesPlayerBukkit(Player player) {
		this.player = player;
	}
	
	@Override
	public void playNote(LiveNote note) {
		player.playSound(
				player.getLocation(),
				note.getInstrument().getMinecraftName(),
				note.getVolume(),
				note.getPitch());
	}
	
	public Player getPlayer() {
		return player;
	}

}
