package kizitorashiro.mp3meta.id3v22;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import kizitorashiro.mp3meta.ID3v2Exception;


public class COM implements ID3v22FrameBody{

	@Override
	public byte[] create(HashMap<String, String> args) throws ID3v2Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getReadableText(byte[] data) {
		byte encordingByte = data[0];
		String encording;
		byte languageBytes[] = { data[1], data[2], data[3] };
		String language;
		try {
			language = new String( languageBytes, "ISO-8859-1");
		} catch (UnsupportedEncodingException e) {
			return null;
		}
		
		switch(encordingByte){
		case 0:
			encording = "ISO-8859-1";
			break;
		case 1:
			encording = "UTF-16";
			break;
		default:
			encording = "ISO-8859-1";
			break;
		}

		int firstDelim = -1;
		int secondDelim = -1;
		
		if(encording.equals("UTF-16")){
			for(int i=4;i<data.length;i=i+2){
				if(firstDelim == -1 && data[i] == 0 && data[i+1] == 0){
					firstDelim = i;
				}else if(firstDelim != -1 && data[i] == 0 && data[i+1] == 0){
					secondDelim = i;
				}
			}
		}else{
			for(int i=4;i<data.length;i++){
				if(firstDelim == -1 && data[i] == 0){
					firstDelim = i;
				}else if(firstDelim != -1 && data[i] == 0){
					secondDelim = i;
				}
			}
		}
		//System.out.println("delim " + firstDelim + " " + secondDelim + " " + data.length);
		byte shortContentDescBytes[] = new byte[firstDelim - 4];
		System.arraycopy(data, 4, shortContentDescBytes, 0, shortContentDescBytes.length); 
		String shortContentDesc = null;
		try {
			shortContentDesc = new String(shortContentDescBytes, encording);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println("short:" + shortContentDesc);
		
		if(encording.equals("UTF-16")){
			firstDelim += 1;
		}
		byte commentBytes[] = new byte[secondDelim - firstDelim - 1];
		System.arraycopy(data, firstDelim + 1, commentBytes, 0, commentBytes.length);
		String comment = null;
		try {
			comment = new String(commentBytes, encording);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//System.out.println("comment:" +comment);
		
		StringBuffer readableText = new StringBuffer();
		readableText.append(language);
		readableText.append(",");
		readableText.append(shortContentDesc);
		readableText.append(",");
		readableText.append(comment);

		return readableText.toString();
	}

	@Override
	public String getOptionDesc() {
		// TODO Auto-generated method stub
		return null;
	}

}
