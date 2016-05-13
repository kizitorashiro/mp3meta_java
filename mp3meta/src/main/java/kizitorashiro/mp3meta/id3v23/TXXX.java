package kizitorashiro.mp3meta.id3v23;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

import kizitorashiro.mp3meta.ID3v2Exception;
import kizitorashiro.mp3meta.ID3v2SystemException;
import kizitorashiro.mp3meta.ID3v2UnSupportedDataException;
import kizitorashiro.mp3meta.ID3v2Utils;

public class TXXX implements ID3v23FrameBody{
	
	@Override
	public byte[] create(HashMap<String, String> args) throws ID3v2Exception {
		String tagValue = args.get("value");
		byte[] terminalSymbol;
		
		String encording = "ISO-8859-1";
		byte encordingByte = 0x00;
		if(args.containsKey("encode")){
			encording = args.get("encode");
		}
		if(encording.equals("ISO-8859-1")){
			encordingByte = 0x00;
			terminalSymbol = new byte[1];
			terminalSymbol[0] = 0x00;
		}else if(encording.equals("UTF-16")){
			encordingByte = 0x01;
			terminalSymbol = new byte[2];
			terminalSymbol[0] = 0x00;
			terminalSymbol[1] = 0x00;
		}else{
			throw new ID3v2UnSupportedDataException("UnSupported encode [" + encording + "]");
		}
		
		byte tagValueBytes[];
		try {
			tagValueBytes = tagValue.getBytes(encording);
		} catch (UnsupportedEncodingException e) {
			throw new ID3v2SystemException("UnsupportedEncoding [" + encording + "]", e);
		}
		
		byte frameBodyBytes[] = new byte[ 1 + tagValueBytes.length + terminalSymbol.length];
		frameBodyBytes[0] = encordingByte;
		for(int i=0; i<tagValueBytes.length; i++){
			frameBodyBytes[i+1] = tagValueBytes[i];
		}
		for(int i=0;i<terminalSymbol.length;i++){
			frameBodyBytes[1 + tagValueBytes.length + i] = terminalSymbol[i];
		}
		return frameBodyBytes;
	}

	@Override
	public String getReadableText(byte[] data) {
		String encode = "ISO-8859-1";
		LinkedList<Byte> textstringBytes = new LinkedList<Byte>();
		
		if(data[0] == 0x00){
			encode = "ISO-8859-1";
			for(int i=1; i<data.length; i++){
				if(data[i] == 0x00){
					break;
				}else{
					textstringBytes.add(new Byte(data[i]));
				}
			}
		}else if(data[0] == 0x01){
			encode = "UTF-16";
			for(int i=0; i<data.length; i=i+2){
				if(data[i] == 0x00 && data[i] == 0x00){
					break;
				}else{
					textstringBytes.add(new Byte(data[i]));
					textstringBytes.add(new Byte(data[i+1]));
				}
			}
		}
		
		byte[] tagValueBytes = new byte[textstringBytes.size()];
		for(int i=0; i<tagValueBytes.length; i++){
			tagValueBytes[i] = textstringBytes.get(i).byteValue();
		}
		String text = null;
		try {
			text = ID3v2Utils.convertNewline(tagValueBytes, encode);
		} catch (Exception e) {
			return null;
		}
		return text;
	}

	@Override
	public String getOptionDesc() {
		return "<textstring>";
	}

}
