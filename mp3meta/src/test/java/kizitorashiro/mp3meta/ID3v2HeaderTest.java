package kizitorashiro.mp3meta;

import static org.junit.Assert.*;

import java.io.FileInputStream;

import org.junit.Test;

public class ID3v2HeaderTest {
	private final String TESTFILE_v22 = "/Users/shizuku/Documents/workspace/ID3Tool/testdata/ESLPod1199.mp3";
	
	@Test
	public void test_create() {
		ID3v2Header header = new ID3v2Header();
		header.create(2, 2, 0);
		byte[] data = header.getRawData();
		assertEquals(0x49, data[0]);
		assertEquals(0x44, data[1]);
		assertEquals(0x33, data[2]);
		assertEquals(0x02, data[3]);
		assertEquals(0x02, data[4]);
		assertEquals(0x00, data[5]);
		assertEquals(0x00, data[6]);
		assertEquals(0x00, data[7]);
		assertEquals(0x00, data[8]);
		assertEquals(0x00, data[9]);
		
	}

	@Test
	public void test_load(){
		try{
			FileInputStream fis = new FileInputStream(TESTFILE_v22);
			ID3v2Header header = new ID3v2Header();
			header.load(fis);
			fis.close();
			byte[] data = header.getRawData();
			assertEquals(0x49, data[0]);
			assertEquals(0x44, data[1]);
			assertEquals(0x33, data[2]);
			
			assertEquals(0x02, data[3]);
			assertEquals(0x00, data[4]);
			
			assertEquals(0x00, data[5]);
			
			assertEquals(0x00, data[6]);
			assertEquals(0x02, data[7]);
			assertEquals(0x0D, data[8]);
			assertEquals(0x6F, data[9]);
						
			
		}catch(Exception e){
			e.printStackTrace();
			fail("exception occurs");
		}
	}
	
	@Test
	public void test_getTagVersion(){
		try{
			FileInputStream fis = new FileInputStream(TESTFILE_v22);
			ID3v2Header header = new ID3v2Header();
			header.load(fis);
			fis.close();
			assertEquals("2", header.getTagVersion());
			
		}catch(Exception e){
			e.printStackTrace();
			fail("exception occurs");
		}
	}
	
	@Test
	public void test_getBodySize(){
		try{
			FileInputStream fis = new FileInputStream(TESTFILE_v22);
			ID3v2Header header = new ID3v2Header();
			header.load(fis);
			fis.close();
			assertEquals(new Integer(34543), header.getBodySize());
		}catch(Exception e){
			fail("exception occurs");
		}
	}
	
	@Test
	public void test_setTagVersion(){
		try{
			FileInputStream fis = new FileInputStream(TESTFILE_v22);
			ID3v2Header header = new ID3v2Header();
			header.load(fis);
			fis.close();
			assertEquals("2", header.getTagVersion());
			header.setTagVersion(3, 0);
			assertEquals("3", header.getTagVersion());
		}catch(Exception e){
			fail("exception occurs");
		}
			
	}
}
