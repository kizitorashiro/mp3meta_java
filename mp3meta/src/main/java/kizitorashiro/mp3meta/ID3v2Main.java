package kizitorashiro.mp3meta;

import java.util.HashMap;

/**
 * Command Line tools to edit ID3v2 meta tag for MP3
 * 
 * @author shizuku
 *
 */
public class ID3v2Main {
	public static void main(String args[]){
		process(args);
	}
	public static void process_read(String filename, String args[]){
		try{
			ID3v2Data data = new ID3v2Data();
			data.load(filename);
			if(args.length == 3){
				String frameID = args[2];
				String text[] = data.getReadableText(frameID);
				for(int i=0; i<text.length; i++){
					System.out.println(text[i]);
				}
			}else if(args.length == 2){
				String[] frameIDList = data.getFrameIDList();
				for(int i=0;i<frameIDList.length;i++){
					System.out.print(frameIDList[i]);
					System.out.print(":");
					String[] readableText = data.getReadableText(frameIDList[i]);
					for(int j=0;j<readableText.length;j++){
						System.out.println("\t" + readableText[j]);
					}	
				}
			}else{
				usage();
			}
		}catch(Exception e){
			System.out.println(e.getMessage());
			usage();
		}
	}
	
	
	
	public static void process_update(String filename, String args[]){
		try{
			ID3v2Data data = new ID3v2Data();
			String frameID = args[2];
			if(args.length == 3){
				// usage for the specified framd-id
				data.load(filename);
				String optionDesc = data.getOptionDesc(frameID);
				System.out.println("java -jar mp3meta.jar update " + frameID + " " + optionDesc);
			}else{				
				String value = args[3];
				HashMap<String, String> updateArgs = new HashMap<String, String>();
				updateArgs.put("value", value);
				for(int i=4; i<args.length; i=i+2){
					updateArgs.put(args[i], args[i+1]);
				}
				data.load(filename);
				data.updateValue(frameID, updateArgs);
				data.store();				
			}
		}catch(Exception e){
			System.out.println(e.getMessage());
			usage();
		}
	}
	
	public static void process(String args[]){
		if(args.length < 2){
			usage();
			return;
		}
		String command = args[0];
		String filename = args[1];
		if(command.equals("read")){
			process_read(filename, args);
		}else if(command.equals("update")){
			process_update(filename, args);
		}else{
			usage();
		}
		return;
	}
	
	protected static void usage(){
		StringBuffer sb = new StringBuffer();
		sb.append("Usage:\tjava -jar mp3meta.jar read   <filename> [ <framd-id> ]\n");
		sb.append("\tjava -jar mp3meta.jar update <filename> <frame-id> <frame-value> [ <options> ]\n");
		System.out.println(sb.toString());
	}
}
//id3v2 <command> <file-name> <tag-name> <tag-
//id3v2 update <file-name> <tag-name> <tag-value> [ <tag-specific-options> ]
//id3v2 read <file-name> [ <tag-name> ]
//id3v2 update sample.mp3 TT2 "HELLO" -encode UTF-8
//id3v2 read sample.mp3 TT2