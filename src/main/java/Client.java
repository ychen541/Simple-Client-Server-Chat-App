import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.function.Consumer;

import java.util.ArrayList;

public class Client extends Thread{


	Socket socketClient;

	ObjectOutputStream out;
	ObjectInputStream in;

	private Consumer<Serializable> callback;

	Client(Consumer<Serializable> call){

		callback = call;
	}

	public void run() {

		try {
			socketClient= new Socket("127.0.0.1",5555);
			out = new ObjectOutputStream(socketClient.getOutputStream());
			in = new ObjectInputStream(socketClient.getInputStream());
			socketClient.setTcpNoDelay(true);
		}
		catch(Exception e) {}

		while(true) {

			try {
				String message = in.readObject().toString();
				callback.accept(message);
			}
			catch(Exception e) {}
		}

	}

	public void send(String data) {

		try {
			out.writeObject(data);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void sendPrivateMessage(String data, int recipient) {
		try {
			out.writeObject("private chat request");
			out.writeObject(data);
			out.writeInt(recipient);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

//	public void sendGroupMessage(String data, ArrayList<Integer> recipients) {
//		try {
//			out.writeObject("group chat request");
//			out.writeObject(data);
//			for (Integer recipient : recipients) {
//				out.writeObject(recipient);
//			}
//			out.flush();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}




}