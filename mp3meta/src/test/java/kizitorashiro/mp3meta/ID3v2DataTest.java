package kizitorashiro.mp3meta;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;

import org.junit.Test;

public class ID3v2DataTest {
	private final String TESTFILE_V22 = "03_Letting_You(2.2).mp3";
	
	@Test
	public void test_load() throws Exception{
		String originalFilePath = this.getClass().getClassLoader().getResource(TESTFILE_V22).getPath();
		
		ID3v2Data data = new ID3v2Data();
		data.load(originalFilePath);
		String[] frameIDList = data.getFrameIDList();
		for(int i=0;i<frameIDList.length;i++){
			System.out.print(frameIDList[i]);
			System.out.print(":");
			String[] readableText = data.getReadableText(frameIDList[i]);
			for(int j=0;j<readableText.length;j++){
				System.out.println("\t" + readableText[j]);
			}
			
		}
		
		assertEquals("TT2", frameIDList[0]);
		assertEquals("TP1", frameIDList[1]);
		assertEquals("TCM", frameIDList[2]);
		assertEquals("TAL", frameIDList[3]);
		assertEquals("TRK", frameIDList[4]);
		assertEquals("TPA", frameIDList[5]);
		assertEquals("TYE", frameIDList[6]);
		assertEquals("TBP", frameIDList[7]);
		assertEquals("COM", frameIDList[8]);
		assertEquals("TEN", frameIDList[9]);
		assertEquals("PIC", frameIDList[10]);
		assertEquals("ULT", frameIDList[11]);
			
	}

	@Test
	public void test_store() throws Exception{
		String originalFilePath = this.getClass().getClassLoader().getResource(TESTFILE_V22).getPath();
		String testFilePath = originalFilePath + ".copy";

		
		Files.copy(new File(originalFilePath).toPath(), new File(testFilePath).toPath(), StandardCopyOption.REPLACE_EXISTING);
		ID3v2Data data = new ID3v2Data();
		data.load(testFilePath);
		data.store();
		
		FileInputStream original = new FileInputStream(originalFilePath);
		FileInputStream copy = new FileInputStream(testFilePath);
		
		int originalByte = -1;
		int copyByte = -1;
		while( true ){
			byte originalBuff[] = new byte[1024];
			byte copyBuff[] = new byte[1024];
			originalByte = original.read(originalBuff, 0, originalBuff.length);
			copyByte = copy.read(copyBuff, 0, copyBuff.length);
			assertEquals(originalByte, copyByte);
			for(int i=0; i<originalByte; i++){
				if(originalBuff[i] != copyBuff[i]){
					fail("copy is differenct from original");
				}
			}
			if( originalByte == -1 ){
				break;
			}
		}
		original.close();
		copy.close();		
	}
	
	
	@Test
	public void test_update_TT2() throws Exception{
		String originalFilePath = this.getClass().getClassLoader().getResource(TESTFILE_V22).getPath();
		String testFilePath = originalFilePath + ".copy";
	
		Files.copy(new File(originalFilePath).toPath(), new File(testFilePath).toPath(), StandardCopyOption.REPLACE_EXISTING);
		ID3v2Data data = new ID3v2Data();
		data.load(testFilePath);
		HashMap<String, String> value = new HashMap<String,String>();
		value.put("value", "TEST");
		data.updateValue("TT2", value);
		data.store();
		
		ID3v2Data data2 = new ID3v2Data();
		data2.load(testFilePath);
		String[] frameIDList = data2.getFrameIDList();
		for(int i=0;i<frameIDList.length;i++){
			System.out.print(frameIDList[i]);
			System.out.print(":");
			String[] readableText = data2.getReadableText(frameIDList[i]);
			for(int j=0;j<readableText.length;j++){
				System.out.println("\t" + readableText[j]);
			}
		}
		String tt2Text = data2.getReadableText("TT2")[0];
		assertEquals("TEST", tt2Text);	
	}
	
	

}
