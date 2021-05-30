import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Client {
	private String IP;
	private int port;
	private String request;
	private byte key;
	private Socket connection;
	private Scanner scanner;
	private PrintWriter printWriter;
	private String result;
	
	public Client(String IP, int port) throws UnknownHostException, IOException {
		this.IP = IP;
		this.port =port;
		connect();
	}

	public String getResult() {
		return this.result;
	}
	
	public void connect() throws UnknownHostException, IOException {
		connection = new Socket(this.IP, this.port);
		scanner = new Scanner(this.connection.getInputStream());
		printWriter = new PrintWriter(this.connection.getOutputStream());
	}
	
	public void requestKey() {
		this.sendRequestToServer("key");
		String response = this.scanner.nextLine();
		this.key = Byte.parseByte(response); 
		System.out.println("out key"+ this.key);
		
	}
	
	private String encrypt(String message) {
		String result="";
		for(char c: message.toCharArray()) {
			System.out.println("check key!"+ this.key);
			result=String.format("%s%c", result, c+this.key);
		}
		return result;
	}
	private String decrypt(String cypher) {
		String result="";
		for(char c: cypher.toCharArray()) {
			result = String.format("%s%c", result, c-this.key);
		}
		return result;
	}
	public void closeConnection() {
		String request =this.encrypt("STFMP/1.0##close##");
		this.sendRequestToServer(request);
		
	}
	
	private  void sendRequestToServer( String request) {
		printWriter.write(request);
		printWriter.write("\r\n");
		printWriter.flush();
		
	}
	
	public void createFile(String fileName, String content) {
		request ="";
		request = this.encrypt(String.format("STFMP/1.0##write##%s.txt#%s!",fileName, content));
		this.sendRequestToServer(request);
		
		
	}
	
	public  void readResponseFromServer() {
		try {
			String response =this.decrypt( scanner.nextLine());
		
			String body = getResponseBody(response);
			String[] parts = body.split("##");
			System.out.println("Result:");
			this.result="";
			for(int i=0;i< parts.length;i++) {
				if(i==0){
				this.result= String.format("%s",parts[i]);
				}else {
					this.request = String.format("%s\n", result, parts[i]);
				}
				
			}
		}
		catch(NoSuchElementException e) {
			System.out.println("Client No");
			e.printStackTrace();
		}
	}
	
	private String getResponseBody(String response) {
		String[] parts = response.split("##");
		if(parts.length != 3) {
			return null;
		} else {
			return parts[2];
		}
	}
		
	
}
