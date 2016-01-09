package me.jannyboy11.livenotes.common.messaging;

import me.jannyboy11.livenotes.common.framework.LiveNote;

public abstract class LiveNoteMessageRecipient<T extends LiveNotesPlayer> {
	
	protected void noteReceived(T liveNotesPlayer, LiveNote note) {
		playNoteNearby(liveNotesPlayer, note);
	}
	
	public abstract void playNoteNearby(T liveNotesPlayer, LiveNote note);

}
