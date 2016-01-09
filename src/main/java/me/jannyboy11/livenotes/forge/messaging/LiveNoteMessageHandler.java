package me.jannyboy11.livenotes.forge.messaging;

import me.jannyboy11.livenotes.common.framework.LiveNote;
import me.jannyboy11.livenotes.common.messaging.LiveNoteMessageRecipient;
import me.jannyboy11.livenotes.common.messaging.LiveNotesPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

public class LiveNoteMessageHandler extends LiveNoteMessageRecipient<LiveNotesPlayerForge> implements IMessageHandler<LiveNoteMessage, IMessage> {

	@Override
	public IMessage onMessage(LiveNoteMessage message, MessageContext ctx) {
		EntityPlayerMP player = ctx.getServerHandler().playerEntity; 
		LiveNotesPlayerForge lnPlayer = new LiveNotesPlayerForge(player);
		noteReceived(lnPlayer, message.getNote());
		return null;
	}

	@Override
	public void playNoteNearby(LiveNotesPlayerForge lnPlayer, LiveNote note) {
		lnPlayer.getPlayer().worldObj.playerEntities.forEach(online -> {			
			if (online instanceof EntityPlayerMP) {
				new LiveNotesPlayerForge((EntityPlayerMP) online).playNote(note);
			}
		});
	}

}
