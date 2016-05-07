package kizitorashiro.mp3meta.id3v22;

import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.HashMap;

import kizitorashiro.mp3meta.ID3v2Exception;
import kizitorashiro.mp3meta.ID3v2Frame;
import kizitorashiro.mp3meta.ID3v2InvalidFormatException;
import kizitorashiro.mp3meta.ID3v2SystemException;
import kizitorashiro.mp3meta.ID3v2UnSupportedDataException;



public class ID3v22Frame implements ID3v2Frame{
	private byte[] headerRawData = new byte[6];
	private byte[] bodyRawData = null;
	private String frameID;
	private int frameSize;
	
	@Override
	public int load(InputStream is) throws IOException, ID3v2Exception {
		int headerReadLen = is.read(headerRawData, 0, headerRawData.length);
		if(headerReadLen == 0){
			return 0;
		}
		
		if(headerReadLen != headerRawData.length){
			throw new ID3v2InvalidFormatException("frame header size is invalid [" + headerReadLen + "]");
		}
		
		byte[] frameIDBytes = { headerRawData[0], headerRawData[1], headerRawData[2] };
		
		this.frameID = new String(frameIDBytes, "ISO-8859-1");
		this.frameID = this.frameID.trim();
		
		int[] frameSizeBytes = { headerRawData[3] & 0xFF, headerRawData[4] & 0xFF, headerRawData[5] & 0xFF };
		int frameSize = ( (frameSizeBytes[0] << 16) | ( frameSizeBytes[1] << 8) | frameSizeBytes[2]);
		
		// TODO if body size is large, then load data to tmpfile, not RAM
		this.bodyRawData = new byte[frameSize];
		
		int bodyReadLen = is.read(bodyRawData, 0, bodyRawData.length);
		
		if(bodyReadLen != bodyRawData.length){
			throw new ID3v2InvalidFormatException("body size define[" + frameSize + "]" + " entity[" + bodyReadLen + "]");
		}
		
		return headerReadLen + bodyReadLen;
		
	}

	@Override
	public int store(RandomAccessFile raf) throws IOException{
		raf.write(this.headerRawData, 0, this.headerRawData.length);
		raf.write(this.bodyRawData, 0, this.bodyRawData.length);
		return this.headerRawData.length + this.bodyRawData.length;
	}

	@Override
	public void create(String frameID, HashMap<String, String> args) throws ID3v2Exception {
		setFrameID(frameID);
		ID3v22FrameBody frameBody = getSupportClass(frameID, true);
		if(frameBody != null){
			bodyRawData = frameBody.create(args);
			setFrameSize(bodyRawData.length);
		}else{
			throw new ID3v2UnSupportedDataException("Unsupported frame-id [" + frameID + "]");
		}
		return;
	}

	@Override
	public String getFrameID() {
		return this.frameID;
		
	}

	@Override
	public int getFrameSize() {
		return this.frameSize;
	}

	@Override
	public String getFrameValueText() throws ID3v2Exception {
		ID3v22FrameBody frameBody = null;
		frameBody = getSupportClass(frameID, false);
		if(frameBody != null){
			return frameBody.getReadableText(this.bodyRawData);
		}else{
			return null;
		}
	}

	@Override
	public byte[] getFrameValueBinary() {
		return this.bodyRawData.clone();
	}

	private void setFrameID(String frameID) throws ID3v2Exception {
		if(frameID.length() == 3 || getSupportClass(frameID, true) != null){
			this.frameID = frameID;
			byte[] frameIDBytes;
			try {
				frameIDBytes = frameID.getBytes("ISO-8859-1");
			} catch (UnsupportedEncodingException e) {
				throw new ID3v2SystemException("UnSupportedEncoding ISO-8859-1", e);
			}
			this.headerRawData[0] = frameIDBytes[0];
			this.headerRawData[1] = frameIDBytes[1];
			this.headerRawData[2] = frameIDBytes[2];
		}else{
			throw new ID3v2UnSupportedDataException("Unsupported Frame ID [" + frameID + "]");
		}
		return;
	}
	
	private void setFrameSize(int size){
		this.frameSize = size;
		byte sizeBytes[] = ByteBuffer.allocate(4).putInt(this.frameSize).array();
		this.headerRawData[3] = sizeBytes[1];
		this.headerRawData[4] = sizeBytes[2];
		this.headerRawData[5] = sizeBytes[3];
		return;
	}
	

	@Override
	public String getOptionDesc(String frameID) {
		ID3v22FrameBody frameBody = getSupportClass(frameID, true);
		if(frameBody != null){
			return frameBody.getOptionDesc();
		}else{
			return null;
		}
	}
	
	protected ID3v22FrameBody getSupportClass(String frameID, boolean forUpdate) {
		if(frameID.length() != 3){
			return null;
		}
		
		String supportClassName = String.format("kizitorashiro.mp3meta.id3v22.%s", frameID);
		Class<?> supportClass = null;
		try {
			supportClass = Class.forName(supportClassName);
		}catch(ClassNotFoundException e){
			if(!forUpdate && frameID.startsWith("T")){
				supportClass = kizitorashiro.mp3meta.id3v22.TXX.class;
			}else{
				return null;
			}
		}
		ID3v22FrameBody frameBody = null;
		try{
			frameBody = (ID3v22FrameBody) supportClass.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			return null;
		}
		return frameBody;
	}

}
