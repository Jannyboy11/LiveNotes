package me.jannyboy11.livenotes.common.messaging;

import me.jannyboy11.livenotes.common.framework.LiveNote;

public interface LiveNotesPlayer {
	
	public void playNote(LiveNote note);

	public void kick(String reason);
	
	public String getName();
}
