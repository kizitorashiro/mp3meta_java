package kizitorashiro.mp3meta.id3v22;

import java.util.HashMap;

import kizitorashiro.mp3meta.ID3v2Exception;
import kizitorashiro.mp3meta.ID3v2InvalidFormatException;

public class TLA extends TXX{
	public TLA(){
		super();
	}
	
	@Override
	public byte[] create(HashMap<String, String> args) throws ID3v2Exception{
		String value = args.get("value");
		if(value == null || value.length() != 3){
			throw new ID3v2InvalidFormatException("TLA MUST be three characters");
		}
		return super.create(args);
	}
	
}
