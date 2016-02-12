package me.jannyboy11.livenotes.forge;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.io.File;
import java.util.logging.Logger;

import me.jannyboy11.livenotes.common.framework.LiveNote;
import me.jannyboy11.livenotes.common.framework.LiveNotesPluginMod;
import me.jannyboy11.livenotes.common.helpers.LiveNotesStatics;
import me.jannyboy11.livenotes.common.midi.MidiDeviceManager;
import me.jannyboy11.livenotes.forge.commands.PlayMidiFileCommand;
import me.jannyboy11.livenotes.forge.messaging.LiveNotesMessageListenerForge;
import me.jannyboy11.livenotes.forge.messaging.LiveNotesPlayerForge;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.PacketBuffer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import net.minecraftforge.fml.common.network.FMLEventChannel;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod(modid = LiveNotesStatics.ID, name = LiveNotesStatics.NAME, version = LiveNotesStatics.VERSION, acceptableRemoteVersions = "*")
public class LiveNotesForge implements LiveNotesPluginMod {

	private FMLEventChannel liveNotesChannel;
	private LiveNotesMessageListenerForge liveNotesChannelListener;

	private Logger logger = Logger.getLogger("minecraft");

	private Configuration config;

	private final MidiDeviceManager midiDeviceManager = new MidiDeviceManager(this, FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT);

	private final File midiFilesFolder = new File("midifiles");

	public FMLEventChannel getLiveNotesChannel() {
		return liveNotesChannel;
	}

	public Logger getLogger() {
		return logger;
	}

	public Configuration getConfig() {
		return config;
	}	

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {		
		liveNotesChannel = NetworkRegistry.INSTANCE.newEventDrivenChannel(LiveNotesStatics.CHANNEL_NOTE);
		liveNotesChannelListener = new LiveNotesMessageListenerForge(this);
		liveNotesChannel.register(liveNotesChannelListener);

		if (!midiFilesFolder.exists()) {
			midiFilesFolder.mkdir();
		}

		setupConfig(event);
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
			registerMidiDevice();
		}
	}

	@EventHandler
	public void stopping(FMLServerStoppingEvent event) {
		midiDeviceManager.close();
		liveNotesChannel.unregister(liveNotesChannelListener);
	}

	@EventHandler
	public void serverStarting(FMLServerStartingEvent event) {
		event.registerServerCommand(new PlayMidiFileCommand(this));
	}

	private static final String CATEGORY_MIDI = "midi";
	private static final String INPUT_DEVICE = "input-device";

	private void registerMidiDevice() {
		String midiInputDevice = config.getString(INPUT_DEVICE, CATEGORY_MIDI, "Put your midi input device here! :)", "Used for sending notes to the server");
		midiDeviceManager.connectToMidiInputDevice(midiInputDevice);
	}

	private void setupConfig(FMLPreInitializationEvent event) {
		config = new Configuration(event.getSuggestedConfigurationFile());
		config.load();

		if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
			if (!config.hasKey(CATEGORY_MIDI, INPUT_DEVICE)) {
				config.get(CATEGORY_MIDI, INPUT_DEVICE, "Put your midi input device here! :)", "Used for sending notes to the server");
				config.save();
			}
		}
	}

	@Override
	public void playOnServer(LiveNote note) {
		for (WorldServer world : MinecraftServer.getServer().worldServers) {
			for (Object player : world.playerEntities) {
				if (player instanceof EntityPlayerMP) {
					new LiveNotesPlayerForge((EntityPlayerMP) player).playNote(note);
				}
			}
		}
	}

	@Override
	public void playFromClient(LiveNote note) {
		ByteBuf buf = Unpooled.wrappedBuffer(note.getBytes());
		PacketBuffer buffer = new PacketBuffer(buf);
		FMLProxyPacket packet = new FMLProxyPacket(buffer, LiveNotesStatics.CHANNEL_NOTE);
		liveNotesChannel.sendToServer(packet);
	}

	@Override
	public MidiDeviceManager getDeviceManager() {
		return midiDeviceManager;
	}

	@Override
	public File getMidiFilesFolder() {
		return midiFilesFolder;
	}

	@Override
	public void displayOnServer(int miditone) {
		for (WorldServer world : MinecraftServer.getServer().worldServers) {
			for (Object player : world.playerEntities) {
				if (player instanceof EntityPlayerMP) {
					new LiveNotesPlayerForge((EntityPlayerMP) player).displayNote(miditone);
				}
			}
		}
	}

}
