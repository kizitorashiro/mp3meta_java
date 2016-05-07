package kizitorashiro.mp3meta;

import static org.junit.Assert.*;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import org.junit.Test;

public class ID3v2MainTest {


	@Test
	public void test_process_size0(){
		String[] args = new String[0];
		System.out.println("$ java -jar mp3meta.jar ");
		try{
			ID3v2Main.process(args);
		}catch(Exception e){
			fail("exception occurs");
		}
	}
	
	@Test
	public void test_process_size1(){
		String[] args = {"read"};
		System.out.println("$ java -jar mp3meta.jar " + args[0]);
		
		try{
			ID3v2Main.process(args);
		}catch(Exception e){
			e.printStackTrace();
			fail("exception occurs");
		}
	}
	
	@Test
	public void test_process_read_size2(){
		String originalFilePath = this.getClass().getClassLoader().getResource("03_Letting_You(2.2).mp3").getPath();
		String[] args = {"read", originalFilePath};
		System.out.println("$ java -jar mp3meta.jar " + args[0] + " " + args[1]);
		
		try{
			ID3v2Main.process(args);
		}catch(Exception e){
			e.printStackTrace();
			fail("exception occurs");
		}
	}

	@Test
	public void test_process_read_TT2(){
		String originalFilePath = this.getClass().getClassLoader().getResource("03_Letting_You(2.2).mp3").getPath();
		String[] args = {"read", originalFilePath, "TT2"};
		System.out.println("$ java -jar mp3meta.jar " + args[0] + " " + args[1] + " " + args[2]);
		try{
			ID3v2Main.process(args);
		}catch(Exception e){
			e.printStackTrace();
			fail("exception occurs");
		}
	}

	@Test
	public void test_process_read_TP1(){
		String originalFilePath = this.getClass().getClassLoader().getResource("03_Letting_You(2.2).mp3").getPath();
		String[] args = {"read", originalFilePath, "TP1"};
		System.out.println("$ java -jar mp3meta.jar " + args[0] + " " + args[1] + " " + args[2]);
		try{
			ID3v2Main.process(args);
		}catch(Exception e){
			e.printStackTrace();
			fail("exception occurs");
		}
	}

	@Test
	public void test_process_read_COM(){
		String originalFilePath = this.getClass().getClassLoader().getResource("03_Letting_You(2.2).mp3").getPath();
		String[] args = {"read", originalFilePath, "COM"};
		System.out.println("$ java -jar mp3meta.jar " + args[0] + " " + args[1] + " " + args[2]);
		try{
			ID3v2Main.process(args);
		}catch(Exception e){
			e.printStackTrace();
			fail("exception occurs");
		}
	}
	
	@Test
	public void test_process_read_NO_EXIST_ID_1(){
		String originalFilePath = this.getClass().getClassLoader().getResource("03_Letting_You(2.2).mp3").getPath();
		String[] args = {"read", originalFilePath, "TP2"};
		System.out.println("$ java -jar mp3meta.jar " + args[0] + " " + args[1] + " " + args[2]);
		try{
			ID3v2Main.process(args);
		}catch(Exception e){
			e.printStackTrace();
			fail("exception occurs");
		}
	}
	
	@Test
	public void test_process_read_NO_EXIST_ID_2(){
		String originalFilePath = this.getClass().getClassLoader().getResource("03_Letting_You(2.2).mp3").getPath();
		String[] args = {"read", originalFilePath, "NNN"};
		System.out.println("$ java -jar mp3meta.jar " + args[0] + " " + args[1] + " " + args[2]);
		try{
			ID3v2Main.process(args);
		}catch(Exception e){
			e.printStackTrace();
			fail("exception occurs");
		}
	}

	@Test
	public void test_process_read_PIC(){
		String originalFilePath = this.getClass().getClassLoader().getResource("03_Letting_You(2.2).mp3").getPath();
		String[] args = {"read", originalFilePath, "PIC"};
		System.out.println("$ java -jar mp3meta.jar " + args[0] + " " + args[1] + " " + args[2]);
		try{
			ID3v2Main.process(args);
		}catch(Exception e){
			e.printStackTrace();
			fail("exception occurs");
		}
	}

	
	@Test
	public void test_process_read_INVALID_FORMAT(){
		String originalFilePath = this.getClass().getClassLoader().getResource("03_Letting_You(2.2).mp3").getPath();
		String[] args = {"read", originalFilePath, "TT22"};
		System.out.println("$ java -jar mp3meta.jar " + args[0] + " " + args[1] + " " + args[2]);
		try{
			ID3v2Main.process(args);
		}catch(Exception e){
			e.printStackTrace();
			fail("exception occurs");
		}
	}
	
	@Test
	public void test_process_update_size3() throws Exception{
		String originalFilePath = this.getClass().getClassLoader().getResource("03_Letting_You(2.2).mp3").getPath();
		String testFilePath = this.getClass().getClassLoader().getResource("03_Letting_You(2.2).mp3.copy").getPath();
		Files.copy(new File(originalFilePath).toPath(), new File(testFilePath).toPath(), StandardCopyOption.REPLACE_EXISTING);
		
		String[] args = {"update", testFilePath, "TT2"};
		System.out.println("$ java -jar mp3meta.jar " + args[0] + " " + args[1] + " " + args[2]);
		try{
			ID3v2Main.process(args);
		}catch(Exception e){
			e.printStackTrace();
			fail("exception occurs");
		}
		
	}


	@Test
	public void test_process_update_TP2() throws Exception{
		String originalFilePath = this.getClass().getClassLoader().getResource("03_Letting_You(2.2).mp3").getPath();
		String testFilePath = this.getClass().getClassLoader().getResource("03_Letting_You(2.2).mp3.copy").getPath();
		Files.copy(new File(originalFilePath).toPath(), new File(testFilePath).toPath(), StandardCopyOption.REPLACE_EXISTING);
		
		String[] args = {"update", testFilePath, "TT2", "TEST"};
		System.out.println("$ java -jar mp3meta.jar " + args[0] + " " + args[1] + " " + args[2] + " " + args[3]);
		try{
			ID3v2Main.process(args);
		}catch(Exception e){
			e.printStackTrace();
			fail("exception occurs");
		}
		
		String[] readArgs = {"read", testFilePath};
		System.out.println("$ java -jar mp3meta.jar " + readArgs[0] + " " + readArgs[1]);
		try{
			ID3v2Main.process(readArgs);
		}catch(Exception e){
			e.printStackTrace();
			fail("exception occurs");
		}
	}

	
}
