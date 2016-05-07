package kizitorashiro.mp3meta;

import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.Arrays;


public class ID3v2Header {
	private byte rawData[] = new byte[10];
	private int majorVersion;
	private int minorVersion;
	private int bodySize = 0;
	
	public void load(InputStream is)throws IOException, ID3v2Exception {
		int readLen = is.read(this.rawData, 0, rawData.length);
		if(readLen != rawData.length){
			throw new ID3v2InvalidFormatException("ID3v2 header size is invalid [" + readLen + "]");
		}
		
		if(rawData[0] != 0x49 || rawData[1] != 0x44 || rawData[2] != 0x33){
			throw new ID3v2InvalidFormatException("ID3v2 header MUST begin with 'ID3'");
		}
		
		majorVersion = (int) rawData[3];
		minorVersion = (int) rawData[4];
		
		byte sizeBytes[] = Arrays.copyOfRange(rawData, 6, 10);
		bodySize = ID3v2Utils.synchsafeToInt(sizeBytes);
		
		return;
		
	}
	
	public void store(RandomAccessFile os) throws IOException{
		os.write(rawData, 0, rawData.length);
	}
	
	public void create(int majorVersion, int minorVersion, int bodySize){
		for(int i=0; i<rawData.length; i++){
			rawData[i] = 0;
		}
		
		// MagicNumber 'ID3'
		rawData[0] = 0x49;
		rawData[1] = 0x44;
		rawData[2] = 0x33;
		
		setTagVersion(majorVersion, minorVersion);
		setBodySize(bodySize);
		
	}
	
	/*
	public ID3v2Header(byte rawData[]) throws ID3v2InvalidFormatException{
		if(rawData.length != 10){
			throw new ID3v2InvalidFormatException("ID3v2 header size MUST be 10.");
		}
		if(rawData[0] != 0x49 || rawData[1] != 0x44 || rawData[2] != 0x33){
			throw new ID3v2InvalidFormatException("ID3v2 header MUST begin with 'ID3'");
		}
		for(int i=0; i<rawData.length;i++){
			this.rawData[i] = rawData[i];
		}
	}
	*/
	
	public void setTagVersion(int majorVersion, int minorVersion){
		this.majorVersion = majorVersion;
		this.minorVersion = minorVersion;
		byte[] tmp = ByteBuffer.allocate(4).putInt(this.majorVersion).array();
		rawData[3] = tmp[3];
		tmp = ByteBuffer.allocate(4).putInt(this.minorVersion).array();
		rawData[4] = tmp[3];
	}
	
	public void setBodySize(int bodySize){
		this.bodySize = bodySize;
		byte[] synchsafe = ID3v2Utils.intToSynchsafe(bodySize);
		rawData[6] = synchsafe[0];
		rawData[7] = synchsafe[1];
		rawData[8] = synchsafe[2];
		rawData[9] = synchsafe[3];
	}
		
	public Integer getBodySize(){
		return this.bodySize;
	}
	
	public String getTagVersion(){
		String major = String.valueOf(this.majorVersion);
		return major;
		
		/*
		String minor = String.valueOf(this.minorVersion);
		StringBuffer version = new StringBuffer();
		version.append(major).append(".").append(minor);
		return version.toString();
		*/
	}
	
	public byte[] getRawData(){
		return rawData.clone();
	}
	
	
}
