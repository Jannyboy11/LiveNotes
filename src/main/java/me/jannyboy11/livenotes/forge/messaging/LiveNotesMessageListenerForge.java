package me.jannyboy11.livenotes.forge.messaging;

import scala.actors.threadpool.Arrays;
import me.jannyboy11.livenotes.bukkit.messaging.LiveNotesPlayerBukkit;
import me.jannyboy11.livenotes.common.framework.LiveNote;
import me.jannyboy11.livenotes.common.helpers.UnimportantCrap;
import me.jannyboy11.livenotes.common.messaging.LiveNoteMessageRecipient;
import me.jannyboy11.livenotes.forge.LiveNotesMod;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.relauncher.Side;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;

public class LiveNotesMessageListenerForge extends LiveNoteMessageRecipient<LiveNotesPlayerForge>{
	
	public LiveNotesMessageListenerForge(LiveNotesMod mod) {
		super(mod);
	}
	
	@SubscribeEvent
	public void onCustomPacket(FMLNetworkEvent.ServerCustomPacketEvent event) {
		INetHandler netHandler = event.packet.getDispatcher().manager.getNetHandler();
		if (netHandler instanceof NetHandlerPlayServer) {
			NetHandlerPlayServer serverHandler = (NetHandlerPlayServer) netHandler;
			EntityPlayerMP player = serverHandler.playerEntity;

			ByteBuf buf = event.packet.payload();
			byte[] message = new byte[buf.readableBytes()];			
			buf.readBytes(message);
			
			LiveNotesPlayerForge lnPlayer = new LiveNotesPlayerForge(player);
			try {
				LiveNote note = LiveNote.fromBytes(message);
				noteReceived(lnPlayer, note);
			} catch (Exception e) {
				handleBadPacket(lnPlayer, event.packet.channel(), message, e);
			}
		}
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
