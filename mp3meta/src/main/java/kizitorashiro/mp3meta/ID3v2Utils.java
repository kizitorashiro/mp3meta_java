package kizitorashiro.mp3meta;

import java.nio.ByteBuffer;

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
	
}
