package kizitorashiro.mp3meta.id3v23;

import java.util.HashMap;

import kizitorashiro.mp3meta.ID3v2Exception;

public interface ID3v23FrameBody {
	public byte[] create(HashMap<String, String> args) throws ID3v2Exception;
	public String getReadableText(byte[] data);
	public String getOptionDesc();
}
