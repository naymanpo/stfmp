import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ResponseThread implements Runnable{
	private byte key;
	private Socket connection;
	private Scanner scanner;
	public ResponseThread(Socket connection) {
		this.connection = connection;
		InputStream inputStream;
		try {
			inputStream = connection.getInputStream();
			 scanner = new Scanner(inputStream);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		this.scanner = scanner;
		this.keyGenerator();
	}
	
	private void keyGenerator() {
		this.key=(byte)Math.floor(Math.random()*(9-0+1)+0);
	}
	
	private void sendResponse(String data) {
		try {
			PrintWriter printWriter = new PrintWriter(connection.getOutputStream());
			System.out.println("Before send to Client:"+ data);
			printWriter.write(data + "\r\n");
			printWriter.flush();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
	private String encrypt(String message) {
		String result="";
		for(char c: message.toCharArray()) {
			System.out.println("check key!"+ key);
			result=String.format("%s%c", result, c+key);
		}
		return result;
	}
	private String decrypt(String cypher) {
		String result="";
		for(char c: cypher.toCharArray()) {
			result = String.format("%s%c", result, c-key);
		}
		return result;
	}
	
	private void processRequest(String request) throws IOException {
		System.out.println(String.format("Receive original data %s", request));
		if(request.trim() == "key") {
			sendResponse(String.format("%d", this.key));
		}
		String[] parts = request.split("##");
		if(parts.length < 2 || parts.length > 3 ) {
			sendResponse("STFMP/1.0##invalid##Invalid request.");
			return;
		}
		
		String operation = parts[1];
		if(operation.equals("write")) {
			String[] data = parts[2].split("#");
			if(data.length==2) {
				FileWriter writer = new FileWriter(String.format("./docs/%s",data[0]));
	            BufferedWriter bufferedWriter = new BufferedWriter(writer);
	 
	            bufferedWriter.write(data[1]);
	            bufferedWriter.close();
	            sendResponse("SFTMP/1.0##ok##The file has been written.");
			}
			
		}
		
		if(operation.equals("list")) {
			String data = parts[2];
			File folder = new File("./docs");
			String response ="SFTMP/1.0##ok##";
			File[] listOfFiles = folder.listFiles();
			for (int i = 0; i < listOfFiles.length; i++) {
				  if (listOfFiles[i].isFile()) {
					  if(i==0)
						  response = String.format("%s%s", response,listOfFiles[i].getName());
					  else
						  response = String.format("%s,%s", response, listOfFiles[i].getName());
				    System.out.println("File " + listOfFiles[i].getName());
				  }
				}
			sendResponse(response);
		}
		else if(operation.equals("view")) {
			
			//SFTMP/1.0##ok##Hello world!
			try {
				FileReader fileReader = new FileReader(String.format("./docs/%s", parts[2]));
				BufferedReader bufferReader = new BufferedReader(fileReader);
				
				
				//SFTMP/1.0##ok##Hello world!
				String response = String.format("SFTMP/1.0##ok##%s",  bufferReader.readLine());
				bufferReader.close();
				sendResponse(response);
			}
			catch(FileNotFoundException e) {
				String response = String.format("SFTMP/1.0##not_found##%s","File not found.");
				sendResponse(response);
				
			}
		}
		else if(operation.equals("close")) {
			connection.close();
			
		}	
	}
	
	@Override
	public void run() {
		
		while(!connection.isClosed()) {
			String data = scanner.nextLine();
			System.out.println("data before process"+ data);
			try {
				processRequest(data);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		this.scanner.close();
	}
}
