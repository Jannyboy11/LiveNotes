package me.jannyboy11.livenotes.framework;

import net.minecraft.server.v1_8_R3.PacketPlayOutNamedSoundEffect;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import lombok.Getter;
import lombok.Setter;

public class Note {
	
	private @Getter @Setter Instrument instrument;
	private @Getter @Setter float pitch;
	private @Getter @Setter float volume;
	
	public Note(Instrument instr, float pitch, float volume) {
		setInstrument(instr);
		setPitch(pitch);
		setVolume(volume);
	}
	
	public void play(Player player) {
		Location loc = player.getEyeLocation();
		PacketPlayOutNamedSoundEffect packet = new PacketPlayOutNamedSoundEffect(instrument.getMinecraftName(), loc.getX(), loc.getY(), loc.getZ(), volume, pitch);
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
	}
	
}
