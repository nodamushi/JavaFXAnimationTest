import java.net.*;
import java.nio.file.*;

import javafx.application.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.stage.*;

public class Main extends Application{
	public static void main(String[] args) {
		//launchに全部任せます。
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		Path fxmlfile = Paths.get("test.fxml");
		URL url = fxmlfile.toUri().toURL();
		
		Parent parent = (Parent)FXMLLoader.load(url);
		
		Scene scene = new Scene(parent);
		stage.setScene(scene);
		stage.show();
	}
}
