package me.jannyboy11.livenotes.common.framework;

import java.io.File;
import java.util.logging.Logger;

import me.jannyboy11.livenotes.common.midi.MidiDeviceManager;

public interface LiveNotesPluginMod {
	
	public Logger getLogger();
	
	public void playOnServer(LiveNote note);
	
	public void playFromClient(LiveNote note);

	public void displayOnServer(int miditone);
	
	public File getMidiFilesFolder();
	
	public MidiDeviceManager getDeviceManager();


}
