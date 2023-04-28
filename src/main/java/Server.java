import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.function.Consumer;

import javafx.application.Platform;
import javafx.scene.control.ListView;
/*
 * Clicker: A: I really get it    B: No idea what you are talking about
 * C: kind of following
 */

public class Server{

	int count = 1;
	ArrayList<ClientThread> clients = new ArrayList<ClientThread>();
	TheServer server;
	private Consumer<Serializable> callback;

	public ArrayList<Integer> clientList = new ArrayList<Integer>() ;


	Server(Consumer<Serializable> call){

		callback = call;
		server = new TheServer();
		server.start();
	}




	public class TheServer extends Thread{

		public void run() {

			try(ServerSocket mysocket = new ServerSocket(5555);){
				System.out.println("Server is waiting for a client!");


				while(true) {

					ClientThread c = new ClientThread(mysocket.accept(), count);
					callback.accept("client has connected to server: " + "client #" + count);
					clients.add(c);
					clientList.add(count);
					c.start();


					count++;

				}
			}//end of try
			catch(Exception e) {
				callback.accept("Server socket did not launch");
			}
		}//end of while
	}


	class ClientThread extends Thread{


		Socket connection;
		int count;
		ObjectInputStream in;
		ObjectOutputStream out;
		ArrayList<String> privateMessages;
		Helper helper = new Helper();


		ClientThread(Socket s, int count){
			this.connection = s;
			this.count = count;
			privateMessages = new ArrayList<>();
		}


		public void updateClients(String message, int recipient) {
			for (int i = 0; i < clients.size(); i++) {
				ClientThread t = clients.get(i);
				if (t.count == recipient || recipient == -1) {
					try {
						t.out.writeObject(message);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}

		public void run(){


			try {
				in = new ObjectInputStream(connection.getInputStream());
				out = new ObjectOutputStream(connection.getOutputStream());
				connection.setTcpNoDelay(true);
			}
			catch(Exception e) {
				System.out.println("Streams not open");
			}

			//1 list 1    1   print welcome, online client1
			//2 list 2    1,2
			//3 list 3  . 1,2,3
			//2 drop list 1,3

			helper.clientListhelper = clientList;
			helper.setClientListhelper(clientList);
			updateClients("Welcome: client #" + count +"!", count);
			updateClients("---------~ current status ~-----------" , -1);
			for(int i=0; i <clientList.size(); i++){
				updateClients("ONLINE(click to chat): client #" + clientList.get(i), -1);
			}

			//updateClients("new client on server, click to chat: client #"+count, -1);

			while (true) {
				try {
					String type = in.readObject().toString();
					if (type.equals("private chat request")) {
						String data = in.readObject().toString();
						int recipient = in.readInt();
						if (recipient == count) {
							privateMessages.add(data);
							updateClients("You are taking to yourself :)", count);
						} else {
							updateClients("[PRIVATE]click to reply"+ ": " + data +" --from client #" + count , recipient);
						}
					} else {
						String data = in.readObject().toString();
						updateClients("[PUBLIC]client #" + count + " said: " + data, -1);
					}
				} catch (Exception e) {
					e.printStackTrace();
					clientList.remove(Integer.valueOf(count));
					helper.clientListhelper = clientList;
					updateClients("---------~ current status ~-----------" , -1);
					updateClients("Client #" + count + " has left the server!", -1);
					for(int i=0; i<clientList.size(); i++){
						updateClients("ONLINE(click to chat): client #" + clientList.get(i), -1);
					}
					//updateClients("Client #" + count + " has left the server!", -1);
					callback.accept("Client #" + count + " has left the server!");
					//updateClients("Client #" + count + " has left the server!", -1);
					clients.remove(this);
					break;
				}


			}

//			for(int k=0; k <clientList.size(); k++){
//				updateClients("ONLINE(click to chat): client #" + clientList.get(k), -1);
//			}
//
//			updateClients("-------------------------------" , -1);


		}//end of run



	}//end of client thread
}