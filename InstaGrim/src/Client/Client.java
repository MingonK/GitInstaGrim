package Client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class Client {
	private static Socket sock; // 소켓 생성을 위한 변수
	private static String ipNum; // ip번호를 상수로 설정해서 여기만 수정하면 되도록 설정
	private static final int portNum = 30000; // 포트번호를 상수로 설정해서 여기만 수정하면 되도록 설정
	private static DataOutputStream dos; // DataOutputStream을 static변수로 선언해서 사용하기 편하게 함
	private static DataInputStream dis; // DataInputStream을 static변수로 선언해서 사용하기 편하게 함

	public Client() {
		try {
			ipNum = InetAddress.getLocalHost().getHostAddress();
			sock = new Socket(ipNum, portNum); // 서버와 연결설정
			dos = new DataOutputStream(sock.getOutputStream());
			dis = new DataInputStream(sock.getInputStream());

		} catch (Exception e) {
			System.out.println("서버와 연결 실패 ip, port번호 확인바람");
		}
	}

	// getDos()가 static메소드이므로 해당 패키지 내의 다른 클래스에서 중복된 인스턴스 생성없이 사용할 수 있도록 하기 위해
	// getter생성
	public static DataOutputStream getDos() {
		return dos;
	}

	public static DataInputStream getDis() {
		return dis;
	}

}
