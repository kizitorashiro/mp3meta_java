package kizitorashiro.mp3meta;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;

public class ID3v2Data {
	private final int PADDING_AREA_MIN = 1024 * 8;
	private String version;
	private String filename;
	private ID3v2Header header;
	private LinkedList<ID3v2Frame> frames;
	
	public void load(String filename) throws IOException, ID3v2Exception{
		this.filename = filename;
		FileInputStream fis = new FileInputStream(filename);
		load(fis);
		fis.close();
	}
	
	protected void load(InputStream is) throws IOException, ID3v2Exception{
		header = new ID3v2Header();
		header.load(is);
		this.version = header.getTagVersion();
		
		frames = null;
		frames = new LinkedList<ID3v2Frame>();
		ID3v2Frame frame = null;
		int bodySize = header.getBodySize();
		int readLen = 0;
		while( readLen < bodySize ){
			frame = getSupportClass(this.version);
			readLen += frame.load(is);
			if(frame.getFrameID().length() > 0){
				frames.add(frame);
			}else{
				break;
			}
		}
		return;
	}
	
	public String[] getFrameIDList(){
		Iterator<ID3v2Frame> ite = this.frames.iterator();
		LinkedHashSet<String> frameIDSet = new LinkedHashSet<String>();
		while(ite.hasNext()){
			ID3v2Frame frame = ite.next();
			String frameID = frame.getFrameID();
			if(frameID != null && !frameIDSet.contains(frameID)){
				frameIDSet.add(frameID);
			}
		}
		
		return frameIDSet.toArray(new String[0]);
		 
	}
	
	public String[] getReadableText(String frameID){
		Iterator<ID3v2Frame> ite = this.frames.iterator();
		ArrayList<String> textList = new ArrayList<String>();
		while(ite.hasNext()){
			ID3v2Frame frame = ite.next();
			if(frameID.equals(frame.getFrameID())){
				try {
					String readableText = frame.getFrameValueText();
					if(readableText != null){
						textList.add(readableText);
					}else{
						textList.add("(binary or unsupported frame)");
					}
				} catch (ID3v2Exception e) {
					continue;
				}
			}
		}
		return textList.toArray(new String[0]);
	}
	
	public void store() throws IOException{
		int oldSize = this.header.getBodySize();
		int newSize = this.updateSize();
		
		// backup original file
		String tmpDir = System.getProperty("java.io.tmpdir");
		//System.out.println("tmpdir " + tmpDir);
		File original = new File(filename);
		File tmpFile = File.createTempFile(original.getName(), ".org");
		tmpFile.deleteOnExit();
		Files.copy(original.toPath(), tmpFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
		//System.out.println("tmpfile " + tmpFile.getName());
		
		// write ID3v2 tag
		int writeSize = 0;
		RandomAccessFile output = new RandomAccessFile(this.filename, "rw");
		this.header.store(output);
		Iterator<ID3v2Frame> i = this.frames.iterator();
		while(i.hasNext()){
			ID3v2Frame frame = i.next();
			writeSize += frame.store(output);
		}
		while(writeSize < newSize){
			output.write(0);
			writeSize++;
		}
		
		// if size is changed, write mp3 data
		if(newSize > oldSize){	
			RandomAccessFile input = new RandomAccessFile(tmpFile, "r");
			input.seek(writeSize);
			int readByte = -1;
			while((readByte = input.read()) != -1){
				output.write(readByte);
			}
			input.close();
		}
		output.close();
	}
	
	
	public void updateValue(String frameID, HashMap<String, String> value) throws ID3v2Exception{
		updateValue(frameID, value, 0);
	}
	
	public void updateValue(String frameID, HashMap<String, String> value, int index) throws ID3v2Exception {
		this.version = header.getTagVersion();
		ID3v2Frame newFrame;
		try {
			newFrame = getSupportClass(this.version);
		} catch (Exception e) {
			throw new ID3v2SystemException("not support ID3v2." + this.version + " frame", e);
		}
		newFrame.create(frameID, value);
		
		ID3v2Frame oldFrame = null;
		int currentIndex = 0;
		Iterator<ID3v2Frame> i = this.frames.iterator();
		while(i.hasNext()){
			ID3v2Frame frame = i.next();
			if(frame.getFrameID().equals(frameID)){
				if(currentIndex == index){
					oldFrame = frame;
					break;
				}else{
					currentIndex++;
				}
			}
		}
		
		
		if(oldFrame != null){
			int oldFrameIndex = this.frames.indexOf(oldFrame);
			this.frames.remove(oldFrame);
			this.frames.add(oldFrameIndex, newFrame);
		}else{
			this.frames.add(newFrame);
		}
		
		//this.updateSize();
	}
	
	private int updateSize(){
		int frameSize = 0;
		
		// if new size become larger than current size that includes padding area, then update size.
		if(this.header.getBodySize() < frameSize){
			this.header.setBodySize(frameSize + PADDING_AREA_MIN);
		}
		return this.header.getBodySize();
	}
	
	protected ID3v2Frame getSupportClass(String version) throws ID3v2SystemException{
		try{
			String supportClassName = String.format("kizitorashiro.mp3meta.id3v2%s.ID3v2%sFrame", version, version);
			Class<?> supportClass = Class.forName(supportClassName);
			ID3v2Frame frame = (ID3v2Frame) supportClass.newInstance();
			return frame;
		}catch(Exception e){
			throw new ID3v2SystemException("not support ID3v2." + this.version + " frame", e);
		}
	}
	
	public String getOptionDesc(String frameID) throws ID3v2SystemException{
		ID3v2Frame frame = getSupportClass(this.version);
		return frame.getOptionDesc(frameID);
	}
	
}
