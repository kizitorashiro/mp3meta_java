package kizitorashiro.mp3meta;

import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.HashMap;

public interface ID3v2Frame {
	public int load(InputStream is) throws IOException, ID3v2Exception;
	public int store(RandomAccessFile raf) throws IOException;
	public void create(String frameID, HashMap<String,String> args) throws ID3v2Exception;
	
	//public void setFrameID(String frameID) throws ID3v2UnSupportedDataException;
	//public void setFrameValueText(String frameID, String frameValue);
	// public void setFrameValueText(String frameID, String frameValue, String encoding);
	//public void setFrameValueBinary(String frameID, byte[] frameValue);
	//public void setFrameValueFile(String frameID, String filepath);
	public String getFrameID();
	public int getFrameSize();
	public String getFrameValueText() throws ID3v2Exception;
	public byte[] getFrameValueBinary() throws ID3v2Exception;
	public String getOptionDesc(String frameID);
	
}
