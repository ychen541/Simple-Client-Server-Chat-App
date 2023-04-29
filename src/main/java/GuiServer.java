
import java.util.HashMap;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.util.stream.Collectors;

import java.util.Set;

import java.util.HashSet;

public class GuiServer extends Application{


	TextField s1,s2,s3,s4, c1;
	Button serverChoice,clientChoice,b1,privateChatSendButton, groupchat, online, button;
	HashMap<String, Scene> sceneMap;
	GridPane grid;
	HBox buttonBox, clientVbox;
	VBox clientBox;
	Scene startScene;
	BorderPane startPane;

	Server serverConnection;
	Client clientConnection;

	ArrayList<Integer> clientsList = new ArrayList<>();

	ListView<String> listItems, listItems2,listItems3;
	ListView<String> clientListView;

	Set<Integer> clientsSet = new HashSet<>();



	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		primaryStage.setTitle("The Networked Client/Server GUI Example");

		this.serverChoice = new Button("Server");
		this.serverChoice.setStyle("-fx-font-family: 'Arial';" + "-fx-text-fill: #fff8dc;" + "-fx-background-color: #ff8c00;" + "-fx-font-size:20");
		this.serverChoice.setStyle("-fx-pref-width: 300px");
		this.serverChoice.setStyle("-fx-pref-height: 300px");

		this.serverChoice.setOnAction(e->{ primaryStage.setScene(sceneMap.get("server"));
											primaryStage.setTitle("This is the Server");
				serverConnection = new Server(data -> {
					Platform.runLater(()->{
						listItems.getItems().add(data.toString());
					});

				});
		});


		this.clientChoice = new Button("Client");
		this.clientChoice.setStyle("-fx-font-family: 'Arial';" + "-fx-text-fill: #fff8dc;" + "-fx-background-color: #ff8c00;" + "-fx-font-size:20");
		this.clientChoice.setStyle("-fx-pref-width: 300px");
		this.clientChoice.setStyle("-fx-pref-height: 300px");

		this.clientChoice.setOnAction(e-> {primaryStage.setScene(sceneMap.get("client"));
											primaryStage.setTitle("This is a client");
											clientConnection = new Client(data->{
							Platform.runLater(()->{
								listItems2.getItems().add(data.toString());

							   });
							});
											clientConnection.start();
		});

		this.buttonBox = new HBox(400, serverChoice, clientChoice);
		this.buttonBox.setStyle("-fx-font-family: 'Arial';" + "-fx-text-fill: #fff8dc;" + "-fx-background-color: #ff8c00;" + "-fx-font-size:20");


		startPane = new BorderPane();
		startPane.setPadding(new Insets(70));
		startPane.setCenter(buttonBox);

		startScene = new Scene(startPane, 800,800);



		listItems = new ListView<String>();
		listItems.setStyle("-fx-font-family: 'Arial';" + "-fx-text-fill: #fff8dc;" + "-fx-background-color: #ff8c00;" + "-fx-font-size:20");


		//##########################################################################
		listItems2 = new ListView<String>();
		listItems2.setStyle("-fx-font-family: 'Arial';" + "-fx-text-fill: #fff8dc;" + "-fx-background-color: #ff8c00;" + "-fx-font-size:20");

		listItems3 = new ListView<String>();
		listItems3.setStyle("-fx-font-family: 'Arial';" + "-fx-text-fill: #fff8dc;" + "-fx-background-color: #ff8c00;" + "-fx-font-size:20");


		c1 = new TextField();
		c1.setStyle("-fx-font-family: 'Arial';" + "-fx-text-fill: #fff8dc;" + "-fx-background-color: #ff8c00;" + "-fx-font-size:20");


		b1 = new Button("double click to Send");
		b1.setStyle("-fx-font-family: 'Arial';" + "-fx-text-fill: #fff8dc;" + "-fx-background-color: #ff8c00;" + "-fx-font-size:20");
		b1.setOnAction(e -> {
			clientConnection.send(c1.getText());
			c1.clear();
		});

		groupchat = new Button("Group Chat");
		groupchat.setStyle("-fx-font-family: 'Arial';" + "-fx-text-fill: #fff8dc;" + "-fx-background-color: #ff8c00;" + "-fx-font-size:20");
		groupchat.setOnAction(e -> {
		});

		online = new Button("Online for Group Chat");
		online.setStyle("-fx-font-family: 'Arial';" + "-fx-text-fill: #fff8dc;" + "-fx-background-color: #ff8c00;" + "-fx-font-size:20");
		online.setOnAction(e -> {
			clientConnection.checkOnline();
			Stage clientListStage = new Stage();
			clientListStage.setTitle("Clients List for Group chat");

			TextField textField = new TextField();
			textField.setPromptText("Select Available Clients");
			textField.setStyle("-fx-font-family: 'Arial';");
			button = new Button("Group Chat");
			button.setStyle("-fx-font-family: 'Arial';");

			//------------------------
			button.setOnAction(eg->{
				String message = textField.getText();
				for(int i =0; i<clientsList.size();i++) {
					clientConnection.sendGroupMessage(message, clientsList.get(i));
				}
				textField.clear();
				clientListStage.close();

			});

			//------------------------

			VBox clientListBox = new VBox();
			clientListBox.setStyle("-fx-background-color: white");
			Scene clientListScene = new Scene(clientListBox, 300, 300);

			clientListView = new ListView<String>();
			clientListView.setStyle("-fx-font-family: 'Arial';");

			clientListBox.getChildren().addAll(clientListView,textField, button);

			clientListStage.setScene(clientListScene);
			clientListStage.show();
		});


		//#####################################################
		sceneMap = new HashMap<String, Scene>();

		sceneMap.put("server",  createServerGui());
		sceneMap.put("client",  createClientGui());
		//######################################################


		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                Platform.exit();
                System.exit(0);
            }
        });



		primaryStage.setScene(startScene);
		primaryStage.show();

	}

	public Scene createServerGui() {

		BorderPane pane = new BorderPane();
		pane.setPadding(new Insets(70));
		pane.setStyle("-fx-background-color: coral");

		pane.setCenter(listItems);

		return new Scene(pane, 500, 400);


	}

	public Scene createClientGui() {

		clientVbox = new HBox( 10,b1,online);
		clientBox = new VBox(10, c1,clientVbox,listItems2);
		clientBox.setStyle("-fx-background-color: blue");


		listItems2.setOnMouseClicked(event -> {

			String selectedItem = listItems2.getSelectionModel().getSelectedItem();
			if (selectedItem != null && selectedItem.contains("ONLINE") ||selectedItem != null && selectedItem.contains("PRIVATE") ) {
				String clientName = selectedItem.substring(selectedItem.indexOf("#")+1);

				// create a new stage for the private chat window
				Stage privateChatStage = new Stage();
				privateChatStage.setTitle("Private Chat with " + clientName);

				// create a new client gui for the private chat window
				VBox privateChatBox = new VBox();
				privateChatBox.setStyle("-fx-background-color: green");
				Scene privateChatScene = new Scene(privateChatBox, 300, 100);

				TextField privateChatTextField = new TextField();
				privateChatTextField.setStyle("-fx-font-family: 'Arial';");
				privateChatSendButton = new Button("Send");
				privateChatSendButton.setStyle("-fx-font-family: 'Arial';");

				privateChatSendButton.setOnAction(e -> {
					String message = privateChatTextField.getText();
					// send the private message to the server
					clientConnection.sendPrivateMessage(message,Integer.parseInt(clientName));
					privateChatTextField.clear();
					privateChatStage.close();
				});

				privateChatBox.getChildren().addAll(privateChatTextField, privateChatSendButton);

				// add the scene to the new stage and show it
				privateChatStage.setScene(privateChatScene);
				privateChatStage.show();
			}else if(selectedItem != null && selectedItem.contains("Available")){
				String clientName = selectedItem.substring(selectedItem.indexOf("#")+1);
				if(clientsList.contains(Integer.parseInt(clientName))){
					clientsList.remove(Integer.valueOf(clientName));
				}else{
					clientsList.add(Integer.parseInt(clientName));
				}
				System.out.println(clientsList.size());

				updateClientsListView(clientListView, clientsList);

			}

		});

		return new Scene(clientBox, 600, 300);

	}

	private void updateClientsListView(ListView<String> clientListView, ArrayList<Integer> clientsList) {
		ArrayList<String> clientList = clientsList.stream().map(client -> "Client #" + client).collect(Collectors.toCollection(ArrayList::new));
		clientListView.getItems().setAll(clientList);
	}








}
