package me.jannyboy11.livenotes.bukkit;

import me.jannyboy11.livenotes.bukkit.messaging.LiveNoteMessageListener;
import me.jannyboy11.livenotes.common.helpers.UnimportantCrap;
import me.jannyboy11.livenotes.forge.messaging.LiveNoteMessageHandler;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListenerRegistration;

public class LiveNotesPlugin extends JavaPlugin {
	
	private PluginMessageListenerRegistration pluginMessageListenerDetails;
	
	@Override
	public void onEnable() {
		pluginMessageListenerDetails = getServer().getMessenger().registerIncomingPluginChannel(this, UnimportantCrap.CHANNEL, new LiveNoteMessageListener(this));
	}

}
