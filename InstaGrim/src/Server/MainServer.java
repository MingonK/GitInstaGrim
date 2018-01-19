package Server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class MainServer {

	private ServerSocket server;
	private static final int portNum = 30000;	// 포트번호 수정을 편리하게 하기 위해 상수로 선언
	public static HashMap<String, GameInfo> clientMap;
	public static ArrayList<GameRoom> roomList;
	
	public MainServer() throws Exception{
		server = new ServerSocket(portNum);
		clientMap = new HashMap<String, GameInfo>();
		roomList = new ArrayList<GameRoom>();
		
		Collections.synchronizedMap(clientMap);
		
		while (true) {	// 접속하는 클라이언트들이 많기 때문에 다중 연결 스레드로 각 클라이언트들을 접속 받음
			Socket sock = server.accept();
			System.out.println(sock.getInetAddress() + "님 접속");
			new ConnectionThread(sock).start();
		}
	}


	public static void main(String[] args) throws Exception {
		new MainServer();		
	}

}
