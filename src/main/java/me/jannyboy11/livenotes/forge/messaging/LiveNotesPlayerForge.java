package me.jannyboy11.livenotes.forge.messaging;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.S29PacketSoundEffect;
import me.jannyboy11.livenotes.common.framework.LiveNote;
import me.jannyboy11.livenotes.common.helpers.LiveNotesStatics;
import me.jannyboy11.livenotes.common.messaging.LiveNotesPlayer;

public class LiveNotesPlayerForge implements LiveNotesPlayer {
	
	private EntityPlayerMP player;
	
	public LiveNotesPlayerForge(EntityPlayerMP player) {
		this.player = player;
	}

	@Override
	public void playNote(LiveNote note) {
		player.playerNetServerHandler.sendPacket(
				new S29PacketSoundEffect(
						note.getInstrument().getMinecraftName(),
						player.posX, player.posY, player.posZ,
						note.getVolume(),
						note.getPitch()));
	}
	
	public EntityPlayerMP getPlayer() {
		return player;
	}

	@Override
	public void kick(String reason) {
		player.playerNetServerHandler.kickPlayerFromServer(reason);
	}

	@Override
	public String getName() {
		return player.getName();
	}

}
