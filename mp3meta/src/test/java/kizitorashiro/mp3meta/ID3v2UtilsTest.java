package kizitorashiro.mp3meta;
import static org.junit.Assert.*;

import org.junit.Test;


public class ID3v2UtilsTest {

	@Test
	public void test_intToSynchsafe_1() {
		byte[] syncsafe = ID3v2Utils.intToSynchsafe(1);
		assertEquals((byte) 0, syncsafe[0]);
		assertEquals((byte) 0, syncsafe[1]);
		assertEquals((byte) 0, syncsafe[2]);
		assertEquals((byte) 1, syncsafe[3]);
	}

	@Test
	public void test_intToSynchsafe_128() {
		byte[] syncsafe = ID3v2Utils.intToSynchsafe(128);
		assertEquals((byte) 0, syncsafe[0]);
		assertEquals((byte) 0, syncsafe[1]);
		assertEquals((byte) 1, syncsafe[2]);
		assertEquals((byte) 0, syncsafe[3]);
	}

	@Test
	public void test_intToSynchsafe_16384() {
		byte[] syncsafe = ID3v2Utils.intToSynchsafe(16384);
		assertEquals((byte) 0, syncsafe[0]);
		assertEquals((byte) 1, syncsafe[1]);
		assertEquals((byte) 0, syncsafe[2]);
		assertEquals((byte) 0, syncsafe[3]);
	}

	
	
	@Test
	public void test_intToSynchsafe_2097152() {
		byte[] syncsafe = ID3v2Utils.intToSynchsafe(2097152);
		assertEquals((byte) 1, syncsafe[0]);
		assertEquals((byte) 0, syncsafe[1]);
		assertEquals((byte) 0, syncsafe[2]);
		assertEquals((byte) 0, syncsafe[3]);
	}

	@Test
	public void test_synchsafeToInt_1() {
		byte[] synchsafe = {0,0,0,1};
		int output = ID3v2Utils.synchsafeToInt(synchsafe);
		assertEquals(1, output);
	}

	@Test
	public void test_syncsafeToInt_128() {
		byte[] synchsafe = {0,0,1,0};
		int output = ID3v2Utils.synchsafeToInt(synchsafe);
		assertEquals(128, output);
	}

	@Test
	public void test_syncsafeToInt_16384() {
		byte[] synchsafe = {0,1,0,0};
		int output = ID3v2Utils.synchsafeToInt(synchsafe);
		assertEquals(16384, output);
	}

	@Test
	public void test_syncsafeToInt_2097152() {
		byte[] synchsafe = {1,0,0,0};
		int output = ID3v2Utils.synchsafeToInt(synchsafe);
		assertEquals(2097152, 16384, output);
	}

	
	@Test
	public void test_synchsafe(){
		int max = 0x0FFFFFFF;
		int numOfTrials = 1000;
		int count = 0;
		while ( numOfTrials > count ){
			int testvalue = (int) (Math.random() * max);
			//System.out.println(testvalue);
			byte[] syncsafe = ID3v2Utils.intToSynchsafe(testvalue);
			int output = ID3v2Utils.synchsafeToInt(syncsafe);
			assertEquals(testvalue, output);
			count++;
		}
	}

}
