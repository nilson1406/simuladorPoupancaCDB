package investimento;

import java.util.ArrayList;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class MainApp extends Application {

	// instanciando stage
	public static Stage stage;

	// instanciando os scenes
	public static Scene sceneRoot;
	public static Scene sceneInvestimento;

	private static String screen = "";

	@Override
	public void start(Stage primaryStage) throws Exception {
		stage = primaryStage;
		// definindo o titulo da janela
		stage.setTitle("Simulador Renda Fixa");

		// FXML da tela inicial
		Parent root = FXMLLoader.load(getClass().getResource("view/RootLayout.fxml"));
		sceneRoot = new Scene(root);

		// FXML da tela de simulação
		Parent single = FXMLLoader.load(getClass().getResource("view/Investimento.fxml"));
		sceneInvestimento = new Scene(single);

		stage.setScene(sceneRoot);
		stage.setResizable(false);
		stage.show();

		// metodo executado ao fechar as janelas
		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent t) {
				t.consume();

				switch (screen) {

				case "Simulador Poupança":
				case "Simulador CDB":
				case "Comparativo Poupança vs CDB":

					screen = "Root";

					stage.setTitle("Simulador Renda Fixa");
					stage.setScene(sceneRoot);
					notifyAllListeners(screen, null);
					break;

				default:
					stage.close();
					Platform.exit();
					System.exit(0);
				}

			}
		});

	}

	public static void main(String[] args) {
		launch(args);
	}

	// Metodos para a navegação entre as janelas
	public static void changeScreen(String scr) {
		changeScreen(scr, null);
	}

	public static void changeScreen(String scr, Object userData) {

		stage.setScene(sceneInvestimento);

		screen = scr;

		stage.setTitle(scr);
		notifyAllListeners(scr, userData);

	}

	// Obeserver utilizado para que seja possivel enviar dados entre as janelas
	public static ArrayList<OnChangeScreen> listeners = new ArrayList<>();

	public static interface OnChangeScreen {
		void onScreenChanged(String newScreen, Object userData);
	}

	public static void addOnChangeScreenListener(OnChangeScreen newListener) {
		listeners.add(newListener);
	}

	public static void notifyAllListeners(String newScreen, Object userData) {
		for (OnChangeScreen l : listeners)
			l.onScreenChanged(newScreen, userData);
	}

}
