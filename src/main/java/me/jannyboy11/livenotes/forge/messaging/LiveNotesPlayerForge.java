package me.jannyboy11.livenotes.forge.messaging;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.S29PacketSoundEffect;
import net.minecraft.network.play.server.S2APacketParticles;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.Vec3;
import net.minecraft.util.Vec3i;
import me.jannyboy11.livenotes.common.framework.LiveNote;
import me.jannyboy11.livenotes.common.helpers.LiveNotesStatics;
import me.jannyboy11.livenotes.common.messaging.LiveNotesPlayer;
import me.jannyboy11.livenotes.common.midi.MidiReceiverBase;

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

	@Override
	public void displayNote(int midiPitch) {
		
		final Vec3 location = player.getPositionVector();
		
		Vec3 direction = player.getLook(1.0F).normalize();
		
		Vec3 distance = new Vec3(3*direction.xCoord, 0, 3*direction.zCoord);
		
		Vec3 startingPoint = location.add(distance);
		startingPoint = startingPoint.addVector(0, 3, 0);
		
		double multiplier = (midiPitch - MidiReceiverBase.SPLIT_NOTE) * 0.10D;
		
		Vec3 effectLocation = startingPoint.addVector(0, 0, 0);
		Vec3 toAdd = direction.rotateYaw((float) (-0.5*Math.PI));
		toAdd = new Vec3(toAdd.xCoord * multiplier, 0, toAdd.zCoord * multiplier);
		effectLocation = effectLocation.add(toAdd);
		
		player.playerNetServerHandler.sendPacket(
					new S2APacketParticles(EnumParticleTypes.NOTE, false,
							(float) effectLocation.xCoord, (float) effectLocation.yCoord, (float) effectLocation.zCoord,
							0, 0, 0,
							0, 1));
		
	}

}
