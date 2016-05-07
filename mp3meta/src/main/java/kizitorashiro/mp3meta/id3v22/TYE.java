package kizitorashiro.mp3meta.id3v22;

import java.util.HashMap;

import kizitorashiro.mp3meta.ID3v2Exception;
import kizitorashiro.mp3meta.ID3v2InvalidFormatException;

public class TYE extends TXX{
	public TYE(){
		super();
	}
	
	@Override
	public byte[] create(HashMap<String, String> args) throws ID3v2Exception{
		String value = args.get("value");
		if(isValidValue(value)){
			return super.create(args);
		}else{
			throw new ID3v2InvalidFormatException("TYE MUST be from 1000 to 9999");
		}		
	}
	
	private boolean isValidValue(String value){
		if(value.length() != 4){
			return false;
		}
		
		int year;
		try{
			year = Integer.parseInt(value);
			if(1000 <= year && year <= 9999){
				return true;
			}else{
				return false;
			}
		}catch(Exception e){
			return false;
		}
	}
}
