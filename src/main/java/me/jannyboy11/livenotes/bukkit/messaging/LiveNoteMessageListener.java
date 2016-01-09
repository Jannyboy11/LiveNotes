package me.jannyboy11.livenotes.bukkit.messaging;

import me.jannyboy11.livenotes.bukkit.LiveNotesPlugin;
import me.jannyboy11.livenotes.common.framework.LiveNote;
import me.jannyboy11.livenotes.common.messaging.LiveNoteMessageRecipient;
import me.jannyboy11.livenotes.common.messaging.LiveNotesPlayer;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import com.google.common.base.Charsets;

public class LiveNoteMessageListener extends LiveNoteMessageRecipient<LiveNotesPlayerBukkit> implements PluginMessageListener {

	private Plugin plugin;
	
	public LiveNoteMessageListener(Plugin plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] message) {
		LiveNotesPlayerBukkit lnPlayer = new LiveNotesPlayerBukkit(player);
		String json = new String(message, Charsets.UTF_8);
		json = "{" + StringUtils.substringAfter(json, "{");
		LiveNote note = LiveNote.deserialize(json);
		noteReceived(lnPlayer, note);
	}

	@Override
	public void playNoteNearby(LiveNotesPlayerBukkit lnPlayer, LiveNote note) {
		lnPlayer.getPlayer().getWorld().getPlayers().forEach(online -> 
		new LiveNotesPlayerBukkit(online).playNote(note));
	}

}
