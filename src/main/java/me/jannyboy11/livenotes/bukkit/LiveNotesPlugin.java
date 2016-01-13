package me.jannyboy11.livenotes.bukkit;

import me.jannyboy11.livenotes.bukkit.messaging.LiveNoteMessageListener;
import me.jannyboy11.livenotes.common.framework.LiveNotesPluginMod;
import me.jannyboy11.livenotes.common.helpers.UnimportantCrap;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListenerRegistration;

public class LiveNotesPlugin extends JavaPlugin implements LiveNotesPluginMod {
	
	@Override
	public void onEnable() {
		getServer().getMessenger().registerIncomingPluginChannel(this, UnimportantCrap.CHANNEL_NOTE, new LiveNoteMessageListener(this));
	}
	
	@Override
	public void onDisable() {
		getServer().getMessenger().unregisterIncomingPluginChannel(this);
	}

}
