package me.jannyboy11.livenotes.forge;

import java.util.logging.Logger;

import me.jannyboy11.livenotes.common.helpers.UnimportantCrap;
import me.jannyboy11.livenotes.forge.messaging.LiveNoteMessageHandler;
import me.jannyboy11.livenotes.forge.messaging.LiveNoteMessage;
import me.jannyboy11.livenotes.forge.midi.MidiDeviceManager;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod(modid = UnimportantCrap.MOD_ID, name = UnimportantCrap.NAME, version = UnimportantCrap.VERSION)
public class LiveNotesMod {

	private SimpleNetworkWrapper liveNotesChannel;
	private Logger logger = Logger.getLogger("minecraft");
	
	private Configuration config;

	private final MidiDeviceManager midiDeviceManager = new MidiDeviceManager(this);
	
	public SimpleNetworkWrapper getLiveNotesChannel() {
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
		liveNotesChannel = NetworkRegistry.INSTANCE.newSimpleChannel(UnimportantCrap.CHANNEL);
		liveNotesChannel.registerMessage(LiveNoteMessageHandler.class, LiveNoteMessage.class, 0, Side.SERVER);	
		
		setupConfig(event);
    }
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		registerMidiDevice();
	}
	
	private static final String CATEGORY_MIDI = "midi";
	private static final String INPUT_DEVICE = "input-device";
	
	private void registerMidiDevice() {
		String midiInputDevice = config.getString(INPUT_DEVICE, CATEGORY_MIDI, "Put your midi input device here! :)", "Used for sending notes to the server");
		midiDeviceManager.connect(midiInputDevice);
	}

	private void setupConfig(FMLPreInitializationEvent event) {
		config = new Configuration(event.getSuggestedConfigurationFile());
		config.load();
		
		if (!config.hasKey(CATEGORY_MIDI, INPUT_DEVICE)) {
			config.get(CATEGORY_MIDI, INPUT_DEVICE, "Put your midi input device here! :)", "Used for sending notes to the server");
			config.save();
		}
	}
	
}
