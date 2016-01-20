package me.jannyboy11.livenotes.common.messaging;

import java.util.Arrays;

import me.jannyboy11.livenotes.common.framework.LiveNote;
import me.jannyboy11.livenotes.common.framework.LiveNotesPluginMod;
import me.jannyboy11.livenotes.common.helpers.LiveNotesStatics;

public abstract class LiveNoteMessageRecipient<T extends LiveNotesPlayer> {
	
	protected LiveNotesPluginMod pluginMod;
	
	protected LiveNoteMessageRecipient(LiveNotesPluginMod pluginMod) {
		this.pluginMod = pluginMod;
	}
	
	protected void noteReceived(T liveNotesPlayer, LiveNote note) {
		playNoteNearby(liveNotesPlayer, note);
	}
	
	public abstract void playNoteNearby(T liveNotesPlayer, LiveNote note);
	
	protected void handleBadPacket(LiveNotesPlayer player, String channel, byte[] packetdata, Exception e) {
		pluginMod.getLogger().warning("Player " + player.getName() + " tried to send an invalid message!");
		pluginMod.getLogger().warning("channel = " + channel);
		pluginMod.getLogger().warning("message = " + Arrays.toString(packetdata));
		player.kick(LiveNotesStatics.KICK_MESSAGE_PACKET_SPOOF);
	}

}
