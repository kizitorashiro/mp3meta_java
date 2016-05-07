package kizitorashiro.mp3meta.id3v22;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import kizitorashiro.mp3meta.ID3v2Exception;
import kizitorashiro.mp3meta.ID3v2InvalidFormatException;

public class TPA extends TXX{
	public TPA(){
		super();
	}
	
	@Override
	public byte[] create(HashMap<String, String> args) throws ID3v2Exception{
		String value = args.get("value").trim();
		if(isValidValue(value)){
			return super.create(args);
		}else{
			throw new ID3v2InvalidFormatException("TPA SHOULD be <numeric>/<numeric>");
		}
	}
	
	private boolean isValidValue(String value){
		String regex = "^[0-9]+/[0-9]+$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(value);
		return matcher.find();
	}
	
}
