/**
 * Copyright (c) 2026 autumo Ltd. Switzerland
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ch.autumo.ui.demo;

import java.util.Properties;

import ch.autumo.ui.Constants;
import ch.autumo.ui.demo.controller.MainController;
import ch.autumo.ui.demo.model.MainModel;
import ch.autumo.ui.demo.view.MainView;
import ch.autumo.ui.fx.BaseApp;
import ch.autumo.ui.fx.view.SplashScreen;
import ch.autumo.ui.license.LicensedApplication;
import ch.autumo.ui.utils.UI;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;


/**
 * Java FX autumo UI App.
 */
public class App extends BaseApp {
	
	@Override
    protected Pane preInitialize(Stage primaryStage, Properties configuration) {
        // Main view
        final MainView mainView = new MainView(this, primaryStage);
        // MVC setup
    	final MainModel model = new MainModel(mainView);
    	mainView.setModel(model);
    	final MainController controller = new MainController(this, primaryStage, model, mainView);
    	mainView.setController(controller);        
        // Setup UI, must be made after the main view!
        UI.setup(this, this.getBaseResourcePath(), mainView, "main.fxml");
        return mainView;
    }
	
	@Override
	protected void initialize(Stage primaryStage, Properties config) {
        primaryStage.setResizable(false);
		primaryStage.setMinHeight(720);
		primaryStage.setMinWidth(1080);
        // Show primary stage
        primaryStage.show();
	}

    @Override
	protected boolean isSplashScreenEnabled() {
		return true;
	}

    @Override
    protected SplashScreen createSplashScreen() {
        final SplashScreen splash = new SplashScreen()
                .setDuration(Duration.seconds(3));
        return splash;
    }
    
	@Override
	protected String getManufacturer() {
		return "autumo";
	}

	@Override
	public String getBaseResourcePath() {
		return "/ch/autumo/ui/demo/";
	}
	
	@Override
	public String getAppVersion() {
		return "1.2.0"; // Must correspond with the online license and registration!
	}

	@Override
	public String getAppTitle() {
		return "autumo UI FX Demo"; // Must correspond with the online license and registration!
	}
	
	@Override
	public String getInternalAppName() {
		return "autumoUIFXDemo";
	}
	
	@Override
	public String getInMemoryConfigName() {
		return "autumoUIFXDemo";
	}
	
	@Override
	public String getConfigTemplateFile() {
		return "/ch/autumo/ui/demo/" + Constants.FILE_INTERNAL_CONF;
	}
	
	@Override
	public void finish(Stage stage) {
	}

	@Override
	public String getProductKey() {
		return "autumo-ui-fx-demo";
	}

	@Override
	public Properties getLicenseProperties() {
		final Properties p = new Properties();
    	p.setProperty(LicensedApplication.REG_ENTRY_FNAME, "Neon");
    	p.setProperty(LicensedApplication.REG_ENTRY_LNAME, "Hawkes");
    	p.setProperty(LicensedApplication.REG_ENTRY_CITY, "Derendingen");
    	p.setProperty(LicensedApplication.REG_ENTRY_COMP, "autumo Ltd.");
    	p.setProperty(LicensedApplication.REG_ENTRY_EMAIL, "neon.hawkes@autumo.ch");
    	p.setProperty(LicensedApplication.REG_ENTRY_COUNTRY, "Switzerland");
        return p;
	}

	@Override
	public String getLicenseKey() {
		return "MyLicenseKey-123456";
	}

	@Override
	public boolean validateLicenseKey(String firstName, String lastNamme, String city, String company, String country,
			String email, String licKey) {
		return true;
	}
	
}
