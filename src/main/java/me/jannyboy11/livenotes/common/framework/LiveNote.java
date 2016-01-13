package me.jannyboy11.livenotes.common.framework;

import com.google.common.base.Charsets;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class LiveNote {
	
	public LiveNote() {
	}
	
	public LiveNote(LiveNoteInstrument instrument, float pitch, float volume) {
		setInstrument(instrument);
		setPitch(pitch);
		setVolume(volume);
	}
	
	private LiveNoteInstrument instrument;
	private float pitch;
	private float volume;
	
	public String serialize() {
		JsonObject json = new JsonObject();
		json.addProperty("instrument", instrument.toString());
		json.addProperty("pitch", pitch);
		json.addProperty("volume", volume);
		return json.toString();
	}
	
	public static LiveNote deserialize(String data) {
		JsonObject json = (JsonObject) new JsonParser().parse(data);
		LiveNote note = new LiveNote();
		note.setInstrument(LiveNoteInstrument.valueOf(json.get("instrument").getAsString()));
		note.setPitch(json.get("pitch").getAsFloat());
		note.setVolume(json.get("volume").getAsFloat());
		return note;
	}
	
	public byte[] getBytes() {
		return serialize().getBytes(Charsets.UTF_8);
	}
	
	public static LiveNote fromBytes(byte[] bytes) {
		return deserialize(new String(bytes, Charsets.UTF_8));
	}
	
	public LiveNoteInstrument getInstrument() {
		return instrument;
	}

	public void setInstrument(LiveNoteInstrument instrument) {
		this.instrument = instrument;
	}

	public float getPitch() {
		return pitch;
	}

	public void setPitch(float pitch) {
		this.pitch = pitch;
	}

	public float getVolume() {
		return volume;
	}

	public void setVolume(float volume) {
		this.volume = volume;
	}
	
}
