package me.jannyboy11.livenotes.forge.messaging;

import me.jannyboy11.livenotes.common.framework.LiveNote;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class LiveNoteMessage implements IMessage {
	
	private LiveNote note;
	
	public LiveNoteMessage() {
	}
	
	public LiveNoteMessage(LiveNote note) {
		setNote(note);
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		String utf8 = ByteBufUtils.readUTF8String(buf);
		note = LiveNote.deserialize(utf8);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		String utf8 = note.serialize();
		ByteBufUtils.writeUTF8String(buf, utf8);
	}

	public LiveNote getNote() {
		return note;
	}

	public void setNote(LiveNote note) {
		this.note = note;
	}

}
