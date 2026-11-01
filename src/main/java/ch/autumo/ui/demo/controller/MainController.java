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
package ch.autumo.ui.demo.controller;

import java.awt.Desktop;
import java.io.File;
import java.net.URI;
import java.net.URL;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.autumo.ui.Constants;
import ch.autumo.ui.demo.model.MainModel;
import ch.autumo.ui.demo.view.MainView;
import ch.autumo.ui.fx.BaseApp;
import ch.autumo.ui.fx.control.Icons;
import ch.autumo.ui.fx.controller.BaseController;
import ch.autumo.ui.fx.controller.DefaultCodeEditorController;
import ch.autumo.ui.fx.view.JsonFormatterView;
import ch.autumo.ui.utils.Helper;
import ch.autumo.ui.utils.OSUtils;
import ch.autumo.ui.utils.UI;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


/**
 * Main controller.
 */
public class MainController extends BaseController {

	private static final Logger LOG = LoggerFactory.getLogger(MainController.class.getName());
	
    @FXML
    private MenuBar menuBar;
    
    @FXML
    private BorderPane toolBar;
    
    @FXML
    private Pane mainPane;
    
    @FXML
    private Pane starPane;

    @FXML
    private Button openButton;

    @FXML
    private MenuItem menuItemJsonFormatter;

    @FXML
    private MenuItem menuItemConfirmAlert;

    @FXML
    private MenuItem menuItemInfoAlert;

    @FXML
    private MenuItem menuItemWarningAlert;

    @FXML
    private MenuItem menuItemErrorAlert;

    @FXML
    private MenuItem menuItemErrorException;
    
    @FXML
    private MenuItem menuItemExampleException;

    @FXML
    private MenuItem menuItemExampleException2;
    
	@FXML
    private Button menuButtonAutumo;

    @FXML
    private Button menuButtonInfo;
    
    @FXML
    private MenuItem menuItemOpen;

    @FXML
    private Menu menuItemRecent;
    
    @FXML
    private MenuItem menuItemQuit;

    @FXML
    private MenuItem menuItemRemove;

    @FXML
    private MenuItem menuItemRegister;
    
    @FXML
    private MenuItem menuItemAbout;

    @FXML
    private Label status0;

    @FXML
    private Label status1;
    
    
    private String path = null;


    /**
     * Main controller. 
     * 
     * @param app app
     * @param stage stage
     * @param model model
     * @param view view
     */
	public MainController(BaseApp app, Stage stage, MainModel model, MainView view) {
		super(app, stage, model, view);
	}
	
	/**
	 * Get the menu bar.
	 */
	@Override
	public MenuBar getMenuBar() {
		// Install macOS menu!
		return this.menuBar;
	}

	/**
	 * Get the toolbar if any.
	 */
	@Override
	public Pane getToolBar() {
		return this.toolBar;
	}

	/**
	 * Initialize the controller.
	 */
	@Override
	public void init(URL location, ResourceBundle resources, double toolBarShift) {
		
		menuItemOpen.setGraphic(Icons.create("folder-open", 16));
		menuItemRecent.setGraphic(Icons.create("clock-counter-clockwise", 16));
		menuItemQuit.setGraphic(Icons.create("sign-out", 16));
		menuItemRemove.setGraphic(Icons.create("trash", 16));
		menuItemRegister.setGraphic(Icons.create("user-plus", 16));
		menuItemAbout.setGraphic(Icons.create("info", 16));

	    // Initialize recent menu
	    super.initializeRecentMenu(menuItemRecent, null);
	    
        // Fill recent menu
        super.rebuildRecentList();
        
        // Open button uses a glyph instead of a bitmap icon.
        openButton.setGraphic(Icons.create("folder-open", 48));

        if (menuItemJsonFormatter != null)
            menuItemJsonFormatter.setGraphic(Icons.create("brackets-curly", 16));
        if (menuItemConfirmAlert != null)
            menuItemConfirmAlert.setGraphic(Icons.create("question", 16));
        if (menuItemInfoAlert != null)
            menuItemInfoAlert.setGraphic(Icons.create("info", 16));
        if (menuItemWarningAlert != null)
            menuItemWarningAlert.setGraphic(Icons.create("warning", 16));
        if (menuItemErrorAlert != null)
            menuItemErrorAlert.setGraphic(Icons.create("x-circle", 16));
        if (menuItemErrorException != null)
        	menuItemErrorException.setGraphic(Icons.create("bug", 16));
        if (menuItemExampleException != null)
            menuItemExampleException.setGraphic(Icons.create("bug", 16));
        if (menuItemExampleException2 != null)
            menuItemExampleException2.setGraphic(Icons.create("bug", 16));

        // Star field
        new StarField(mainPane, starPane, toolBarShift);
	}
	
	/**
	 * Handle key events from the main view.
	 * Per default 'CTRL-SHIFT-l' for example shows the log-file. 
	 */
	@Override
	public void keyPressed(KeyEvent key) {
	}
	
	/**
	 * Open a file.
	 */
    @FXML
    protected void open() {
    	FileChooser chooser = new FileChooser();
    	if (path == null)
    		chooser.setInitialDirectory(new File(OSUtils.USER_HOME));
    	else
    		chooser.setInitialDirectory(new File(path));
    	chooser.setTitle("Chose any file");
    	chooser.getExtensionFilters().addAll(
    	        new FileChooser.ExtensionFilter("All files", "*.*")
    	);
    	
    	final File file = chooser.showOpenDialog(this.stage.getOwner());
    	if (file == null)
    		return;
    	
    	if (file.isDirectory())
    		return;
    	
    	this.innerOpen(file);
    }
    
    /**
     * Called by choosing a file from the recent list if there is any.
     */
    @Override
	protected void innerOpen(File file) {
    	path = file.getParent();
    	if (path == null)
    		path = OSUtils.USER_HOME;
    	
		status1.setText(file.getAbsolutePath());
		
		// Here you would something with the file, e.g.
		// open it in an editor view, etc.
    	
    	app.addRecentFile(file);
    	this.rebuildRecentList();
    }

    /**
     * Unused, called when the recent sub-menu is opened.
     */
	@FXML
    protected void recent() {
    	//nada
    }
    
	/**
	 * End application.
	 */
    @FXML
    protected void quit() {
        app.storeProps();
        app.finish(super.getStage());
    	System.exit(0);
    }
    
    /**
     * Show remove dialog to remove all application data.
     */
    @FXML
    protected void removeApp() {
    	UI.showRemovePane(app);
    }
   
    /**
     * Show registration form.
     */
    @FXML
    protected void register() {
    	UI.showRegistrationPane(app);    
    }
    
    /**
     * Open the documentation URL as configured in 'cfg/app.cfg'.
     */
    @FXML
    protected void openDocWebsite() {
    	final String url = Constants.DEF_CONFIG_ENTRY_URL_DOC;    	
        if (Desktop.isDesktopSupported()) {
            try {
                final URI uri = new URI(url);
                Desktop.getDesktop().browse(uri);
            } catch (Exception e) {
            	LOG.error("Couldn't open URL '{}'.", url);
            }
        }
    }
    
    /**
     * Shows the about dialog with the content '/ch/autumo/ui/demo/html/about_LANG.html'
     * respectively '/RESOURCE_PACKAGE_PATH/html/about_LANG.html'.
     */
    @FXML
    protected void about() {
    	UI.showAboutWithLicense(app);
    }
    /**
     * Open the JSON formatter tool with example data.
     */
    @FXML
    protected void openJsonFormatter() {
        final DefaultCodeEditorController controller = UI.showJsonFormatter(app);
        final JsonFormatterView view = (JsonFormatterView) controller.getView();
        view.setText("{\n  \"application\": \"autumo UI Demo\",\n  \"version\": \"1.2.0\",\n  \"framework\": \"autumo UI FX\",\n  \"features\": [\n    \"application shell\",\n    \"MVC structure\",\n    \"theming\",\n    \"internationalisation\",\n    \"installer\",\n    \"registration\",\n    \"update checks\",\n    \"recent files\",\n    \"configuration handling\",\n    \"dialog panes\",\n    \"JSON formatter\",\n    \"code editor\"\n  ],\n  \"settings\": {\n    \"language\": \"en\",\n    \"theme\": \"dark\",\n    \"window\": { \"width\": 1080, \"height\": 720, \"resizable\": false },\n    \"logging\": { \"level\": \"INFO\", \"file\": \"autumo-ui-demo.log\" }\n  },\n  \"modules\": [\n    { \"name\": \"autumo-commons\", \"version\": \"1.2.0\" },\n    { \"name\": \"autumo-ui\", \"version\": \"1.2.0\" },\n    { \"name\": \"autumo-registration\", \"version\": \"1.2.0\" }\n  ]\n}");
    }

    @FXML
    protected void openConfirmAlert() {
        UI.showFineConfirmationDialog(app, Helper.getTranslatedText(app, "Alert.confirmation.text"));
    }

    @FXML
    protected void openInfoAlert() {
        UI.showFineInformationDialog(app, Helper.getTranslatedText(app, "Alert.information.text"));
    }

    @FXML
    protected void openWarningAlert() {
        UI.showFineWarningDialog(app, Helper.getTranslatedText(app, "Alert.warning.text"));
    }

    @FXML
    protected void openErrorAlert() {
        UI.showFineErrorDialog(app, Helper.getTranslatedText(app, "Alert.error.text"));
    }

    /**
     * Show an example error exception dialog.
     */
    @FXML
    protected void openErrorException() {
    	final RuntimeException rtex = new RuntimeException(Helper.getTranslatedText(app, "Alert.exception.text"));
    	LOG.error("Test Excpetion.", rtex);
        UI.showError(app, rtex);
    }
    
    /**
     * Show an example exception dialog.
     */
    @FXML
    protected void openExampleException() {
        UI.showExceptionDialog(new RuntimeException(Helper.getTranslatedText(app, "Alert.exception.text")));
    }
    
    /**
     * Show another example exception dialog.
     */
    @FXML
    protected void openExampleException2() {
        UI.showExceptionDialogExtended(new IllegalStateException(Helper.getTranslatedText(app, "Alert.exception.text")), Helper.getTranslatedText(app, "Alert.exception.title"));
    }
    
}
