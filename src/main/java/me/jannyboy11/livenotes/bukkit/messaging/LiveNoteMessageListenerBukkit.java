package me.jannyboy11.livenotes.bukkit.messaging;

import me.jannyboy11.livenotes.bukkit.LiveNotesBukkit;
import me.jannyboy11.livenotes.common.framework.LiveNote;
import me.jannyboy11.livenotes.common.messaging.LiveNoteMessageRecipient;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

public class LiveNoteMessageListenerBukkit extends LiveNoteMessageRecipient<LiveNotesPlayerBukkit> implements PluginMessageListener {

	public LiveNoteMessageListenerBukkit(LiveNotesBukkit plugin) {
		super(plugin);
	}

	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] message) {
		LiveNotesPlayerBukkit lnPlayer = new LiveNotesPlayerBukkit(player);
		if (player.hasPermission("livenotes.playpiano")) {
			try {
				LiveNote note = LiveNote.fromBytes(message);
				noteReceived(lnPlayer, note);
			} catch (Exception e) {
				handleBadPacket(lnPlayer, channel, message, e);
			}
		}
	}

	@Override
	public void playNoteNearby(LiveNotesPlayerBukkit lnPlayer, LiveNote note) {
		lnPlayer.getPlayer().getWorld().getPlayers().forEach(online -> 
		new LiveNotesPlayerBukkit(online).playNote(note));
	}

}
