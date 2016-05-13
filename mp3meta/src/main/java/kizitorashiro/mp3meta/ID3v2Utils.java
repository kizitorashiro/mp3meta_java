package kizitorashiro.mp3meta;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.LinkedList;

public class ID3v2Utils {

	public static int synchsafeToInt(byte[] input){
		int output = 0;
		output |= input[0] << 21;
		output |= input[1] << 14;
		output |= input[2] << 7;
		output |= input[3];
		return output;
	}
	
	public static byte[] intToSynchsafe(int input){
		int mask = 0x0000007F;
		byte output[] = new byte[4];
		int intermediate[] = new int[4];
		
		intermediate[3] = input & mask;
		input = input >> 7;
		intermediate[2] = input & mask;
		input = input >> 7;
		intermediate[1] = input & mask;
		input = input >> 7;
		intermediate[0] = input & mask;
		
		for(int i=0; i<intermediate.length; i++){
			byte[] tmp = ByteBuffer.allocate(4).putInt(intermediate[i]).array();
			output[i] = tmp[3];
		}
		return output;
	}
	
	private static String convertBytesToString(byte[] input, int length, String encode) throws UnsupportedEncodingException{
		byte convertedBytes[] = new byte[length];
		System.arraycopy(input, 0, convertedBytes, 0, convertedBytes.length);
		return new String(convertedBytes, encode);
	}
	
	private static String convertNewLine_ISO88591(byte[] input) throws UnsupportedEncodingException{
		String ISO88591 = "ISO-8859-1";
		byte[] textBytes = new byte[input.length * 2];
		int currentIndex = 0;
		LinkedList<String> lineList = new LinkedList<String>();
		for(int i=0; i<input.length;i++){
			if(input[i] == 0x0A){
				lineList.add(convertBytesToString(textBytes, currentIndex, ISO88591));
				currentIndex = 0;
			}else if(input[i] == 0x0D){
				if(i < (input.length -1) && input[i+1] == 0x0A){
					i = i + 1;
				}
				lineList.add(convertBytesToString(textBytes, currentIndex, ISO88591));
				currentIndex = 0;
			}else{
				textBytes[currentIndex++] = input[i];
			}
		}
		lineList.add(convertBytesToString(textBytes, currentIndex, ISO88591));

		StringBuffer sb = new StringBuffer();
		Iterator<String> i = lineList.iterator();
		while(i.hasNext()){
			sb.append(i.next());
			if(i.hasNext()) {
				sb.append(System.getProperty("line.separator"));
			}
		}
		return sb.toString();
	}
	
	private static String convertNewLine_UTF16(byte[] input) throws UnsupportedEncodingException{
		String UTF16 = "UTF-16";
		byte[] textBytes = new byte[input.length * 2];
		int currentIndex = 0;
		LinkedList<String> lineList = new LinkedList<String>();
		
		if((input[0] & 0xFF) == 0xFF && (input[1] & 0xFF) == 0xFE){ // LE
			for(int i=0; i<input.length; i=i+2){ // LE -> BE
				byte tmp = input[i];
				input[i] = input[i+1];
				input[i+1] = tmp;
			}
		}
		for(int i=0; i<input.length; i=i+2){		
			if(input[i] == 0x00 && input[i+1] == 0x0A){
				lineList.add(convertBytesToString(textBytes, currentIndex, UTF16));
				currentIndex = 0;
				textBytes[currentIndex++] = (byte) 0xFE;
				textBytes[currentIndex++] = (byte) 0xFF;
			}else if(input[i] == 0x00 && input[i+1] == 0x0D){
				if(i < (input.length - 3) && input[i+2] == 0x00 && input[i+3] == 0x0A){
						i = i + 2;
				}
				lineList.add(convertBytesToString(textBytes, currentIndex, UTF16));
				currentIndex = 0;
				textBytes[currentIndex++] = (byte) 0xFE;
				textBytes[currentIndex++] = (byte) 0xFF;
			}else{
				textBytes[currentIndex++] = input[i];
				textBytes[currentIndex++] = input[i+1];
			}
		}
		lineList.add(convertBytesToString(textBytes, currentIndex, UTF16));

		StringBuffer sb = new StringBuffer();
		Iterator<String> i = lineList.iterator();
		while(i.hasNext()){
			sb.append(i.next());
			if(i.hasNext()) {
				sb.append(System.getProperty("line.separator"));
			}
		}
		return sb.toString();
	}
	
	/**
	 * from CR or LF or CRLF to machine specific NewLine code 
	 * @param input
	 * @return
	 * @throws UnsupportedEncodingException 
	 * @throws ID3v2UnSupportedDataException 
	 */
	public static String convertNewline(byte[] input, String encode) throws UnsupportedEncodingException, ID3v2UnSupportedDataException{
		/*
		byte[] resultBytes = new byte[input.length * 2];
		LinkedList<String> result = new LinkedList<String>();
		int currentIndex = 0;
		*/
		if(encode.equals("ISO-8859-1")){
			return convertNewLine_ISO88591(input);
			
			/*
			for(int i=0;i<input.length;i++){
				if(input[i] == 0x0A){
					result.add(convertBytesToString(resultBytes, currentIndex, encode));
					resultBytes = new byte[input.length * 2];
					currentIndex = 0;
				}else if(input[i] == 0x0D){
					if(i < (input.length - 1)){
						if(input[i+1] == 0x0A){
							i = i + 1;
						}
					}
					result.add(convertBytesToString(resultBytes, currentIndex, encode));
					resultBytes = new byte[input.length * 2];
					currentIndex = 0;					
				}else{
					resultBytes[currentIndex++] = input[i];
				}
			}
			*/
		}else if(encode.equals("UTF-16")){
			return convertNewLine_UTF16(input);
			
			/*
			boolean le = false;
			if((input[0] & 0xFF) == 0xFF && (input[1] & 0xFF) == 0xFE){
				le = true;
			}
			for(int i=0; i<input.length; i=i+2){
				if(!le){
					if(input[i] == 0x00 && input[i+1] == 0x0A){
						result.add(convertBytesToString(resultBytes, currentIndex, encode));
						resultBytes = new byte[input.length * 2];
						currentIndex = 0;
						resultBytes[currentIndex++] = (byte) 0xFE;
						resultBytes[currentIndex++] = (byte) 0xFE;
					}else if(input[i] == 0x00 && input[i+1] == 0x0D){
						if(i < (input.length - 3)){
							if(input[i+2] == 0x00 && input[i+3] == 0x0A){
								i = i + 2;
							}
						}
						result.add(convertBytesToString(resultBytes, currentIndex, encode));
						resultBytes = new byte[input.length * 2];
						currentIndex = 0;
						resultBytes[currentIndex++] = (byte) 0xFE;
						resultBytes[currentIndex++] = (byte) 0xFE;
					}else{
						resultBytes[currentIndex++] = input[i];
						resultBytes[currentIndex++] = input[i+1];
					}
				}else{
					if(input[i] == 0x0A && input[i+1] == 0x00){
						result.add(convertBytesToString(resultBytes, currentIndex, encode));
						resultBytes = new byte[input.length * 2];
						currentIndex = 0;
						resultBytes[currentIndex++] = (byte) 0xFF;
						resultBytes[currentIndex++] = (byte) 0xFE;
					}else if(input[i] == 0x0D && input[i+1] == 0x00){
						if(i < (input.length - 3)){
							if(input[i+2] == 0x0A && input[i+3] == 0x00){
								i = i + 2;
							}
						}
						result.add(convertBytesToString(resultBytes, currentIndex, encode));
						resultBytes = new byte[input.length * 2];
						currentIndex = 0;
						resultBytes[currentIndex++] = (byte) 0xFF;
						resultBytes[currentIndex++] = (byte) 0xFE;
					}else{
						resultBytes[currentIndex++] = input[i];
						resultBytes[currentIndex++] = input[i+1];
					}
				}
			}
		}
		
		result.add(convertBytesToString(resultBytes, currentIndex, encode));
		
		StringBuffer sb = new StringBuffer();
		Iterator<String> i = result.iterator();
		while(i.hasNext()){
			sb.append(i.next());
			if(i.hasNext()) sb.append(System.getProperty("line.separator"));
		}
		return sb.toString();
		*/
		}else{
			throw new ID3v2UnSupportedDataException("unsupported encode [" + encode + "]");
		}
	}
	
}
