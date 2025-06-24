import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.concurrent.Worker;
import java.util.List;
import java.util.ArrayList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.Image;

public class VortexMiniWebBrowserJava extends Application {
	String paginaAtual = "";
	int indicePagina = 0;

	@Override
	public void start(Stage palco) {
		Button voltar = new Button("<");
		voltar.setAlignment(Pos.CENTER);
		Button proximo = new Button(">");
		proximo.setAlignment(Pos.CENTER);
		TextField campoUrl = new TextField();
		WebView navegador = new WebView();
		WebEngine motor = navegador.getEngine();
		List<String> historico = new ArrayList<>();

		campoUrl.setText("https://www.google.com");
		motor.load(campoUrl.getText());

		motor.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
		    if (newState == Worker.State.SUCCEEDED) {
		    	paginaAtual = motor.getLocation();
		        campoUrl.setText(paginaAtual);
		        if (!historico.contains(paginaAtual)) {
		        	historico.add(paginaAtual);
		        	indicePagina = historico.size() - 1;
		        }
		    }
		});

		// carregar uma página da web quando o usuário pressiona Enter
		campoUrl.setOnAction(evento -> {
			motor.load( formataUrl(campoUrl.getText()) );
		});

		voltar.setOnAction(e -> {
			if (indicePagina > 0) {
				indicePagina -= 1;
				motor.load(historico.get(indicePagina));
			}
		});

		proximo.setOnAction(e -> {
			if (indicePagina < (historico.size() - 1)) {
				indicePagina += 1;
				motor.load(historico.get(indicePagina));
			}
		});

		HBox hbox = new HBox();
		hbox.setSpacing(10);
		hbox.setPadding(new Insets(12));
		VBox vbox = new VBox();
		hbox.getChildren().addAll(voltar, proximo, campoUrl);
		vbox.getChildren().addAll(hbox, navegador); // retorna a lista de camponentes filhos do VBox e adiciona os que colocamos
	
		Scene cena = new Scene(vbox);

		cena.getStylesheets().add("file:/C:/Users/Higor/Documents/vortex/public/assets/styles/styles.css");

		palco.getIcons().add(new Image("file:C:/Users/Higor/Documents/vortex/public/assets/images/vortex.png"));
		palco.setTitle("Vortex Browser");
		palco.setScene(cena);
		palco.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

	// método para inserir http se o usuário não digitar
	public String formataUrl (String url) {
		if (!url.startsWith("http://") && !url.startsWith("https://")) {
			url = "http://" + url;
		}
		return url;
	}
}

// compilação: javac -encoding UTF-8 --module-path "%PATH_TO_FX%" --add-modules javafx.controls,javafx.web VortexMiniWebBrowserJava.java