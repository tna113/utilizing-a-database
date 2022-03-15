/**
 * @author Thea Arias
 * I got everything to work besides what we talked about on email. This program works
 * only if you delte the datebase first, and start fresh. Not sure why it doesn't add
 * records or populate the table with new records added when EventDateabse already
 * exists/created beforehand.
 */


//Note: This program does not create any tables. I used another file DBReady.java to do the
//initial table setup.  Ideally, this will only ever be run once.

package sample;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Popup;
import javafx.stage.Stage;

import java.sql.*;

public class Main extends Application {
    //TableView is a JavaFX class that displays data in columns and rows
    private final TableView<Event> table = new TableView<>();

    //ObservableList is a type of list that listens for changes and will update itself
    //It gets created using the observableArrayList() static method from FXCollections
    //Stores Event objects --> See Event.java template class
    private final ObservableList<Event> eventData = FXCollections.observableArrayList();

    //Using the init() method to create the database and table if not already created
    @Override
    public void init() throws Exception {
        try {
            Connection connect = DriverManager.getConnection("jdbc:derby:EventDatabase; create = true");
            System.out.println("Connected to database.");
            Statement state = connect.createStatement();
            System.out.println("Statement created.");
            DatabaseMetaData dbm = connect.getMetaData();
            System.out.println("MetaData created.");
            ResultSet result = dbm.getTables(null, null, "tblEVENT", null);
            System.out.println("ResultSet created.");
            if (result.next()) {
                System.out.println("tblEVENT exists.");
            } else {
                state.execute("create table tblEVENT(EventID varchar(50), Description varchar(200), EventDate varchar(100), Theme varchar(100), LocationID varchar(50), Daytime boolean)");
                System.out.println("tblEVENT created.");
            }
            DriverManager.getConnection("jdbc:derby:EventDatabase; shutdown=true");
        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    //GUI components and event handlers for communicating with database
    @Override
    public void start(Stage primaryStage) throws Exception{
        //Header Label and settings
        Label title = createLabel("Event Information Form");
        title.setPrefWidth(850);
        title.setAlignment(Pos.CENTER);
        title.setPadding(new Insets(20, 0, 40, 0));

        //Form Labels, TextFields, RadioButtons
        Label lbEventID = createLabel("Enter EventID");
        TextField tfEventID = new TextField();
        Label lbDesc = createLabel("Event Description");
        TextField tfDesc = new TextField();
        Label lbEventDate = createLabel("Event Date");
        TextField tfEventDate = new TextField();
        Label lbTheme = createLabel("Event Theme");
        TextField tfTheme = new TextField();
        Label lbLocationID = createLabel("Location ID");
        TextField tfLocationID = new TextField();
        Label lbDaytime = createLabel("Daytime?");

        ToggleGroup tgDaytime = new ToggleGroup();
        RadioButton rbYes = createRB("yes",tgDaytime);
        RadioButton rbNo = createRB("no",tgDaytime);

        Button submit = createButton("Submit Record");
        Button view = createButton("View Table");
        Button dates = createButton("Need Dates");


        //Form layout
        GridPane gp = createGP();
        gp.add(lbEventID, 0, 0);
        gp.add(tfEventID, 1, 0,2,1);
        gp.add(lbDesc, 0, 1);
        gp.add(tfDesc, 1, 1,2,1);
        gp.add(lbEventDate, 0, 2);
        gp.add(tfEventDate, 1, 2,2,1);
        gp.add(lbTheme, 0, 3);
        gp.add(tfTheme, 1,3,2,1);
        gp.add(lbLocationID, 0,4);
        gp.add(tfLocationID, 1,4,2,1);
        gp.add(lbDaytime,0,5);
        gp.add(rbYes, 1, 5);
        gp.add(rbNo, 2, 5);
        gp.add(submit, 0, 6);
        gp.add(view, 1, 6);
        gp.add(dates, 2, 6);

        //Main layout for header label and gridpane
        VBox mainLayout = new VBox(title, gp);
        mainLayout.setPadding(new Insets(20));

        //The label that will appear in a confirmation pop-up window
        //after record successfully submitted
        Label lbConfirm = new Label("Record Added.");
        lbConfirm.setFont(Font.font("Verdana", 28));
        lbConfirm.setStyle("-fx-background-color: white;");
        lbConfirm.setPrefWidth(300);
        lbConfirm.setPrefHeight(100);
        lbConfirm.setAlignment(Pos.CENTER);
        StackPane sPane = new StackPane(lbConfirm);
        //Add pane to its own scene for use later in the View event handler
        Scene confirmScene = new Scene(sPane);

        //Configure tableview that will be displayed when user hits View button
        //Create each column with header text in quotes
        TableColumn<Event, String> colEventID = new TableColumn<>("EventID");
        TableColumn<Event, String> colDesc = new TableColumn<>("Description");
        TableColumn<Event, String> colEventDate = new TableColumn<>("EventDate");
        TableColumn<Event, String> colTheme = new TableColumn<>("Theme");
        TableColumn<Event, String> colLocationID = new TableColumn<>("LocationID");
        TableColumn<Event, Boolean> colDaytime = new TableColumn<>("Daytime?");

        //Add columns to the javafx tableview
        table.getColumns().add(colEventID);
        table.getColumns().add(colDesc);
        table.getColumns().add(colEventDate);
        table.getColumns().add(colTheme);
        table.getColumns().add(colLocationID);
        table.getColumns().add(colDaytime);

        //Set styling for javafx table view and table columns - so pretty
        table.setStyle("-fx-font-size: 30px; -fx-pref-width: 880px");
        colEventID.setStyle("-fx-font-size: 30px; -fx-pref-width: 220px");
        colDesc.setStyle("-fx-font-size: 30px; -fx-pref-width: 220px");
        colEventDate.setStyle("-fx-font-size: 30px; -fx-pref-width: 220px");
        colTheme.setStyle("-fx-font-size: 30px; -fx-pref-width: 220px");
        colLocationID.setStyle("-fx-font-size: 30px; -fx-pref-width: 220px");
        colDaytime.setStyle("-fx-font-size: 30px; -fx-pref-width: 220px");

        //Add table to its own scene to be used later in view event handler and pop-up stage
        Scene viewScene = new Scene(table);

        //Event handler for Submit button
        submit.setOnAction(e -> {
            try {
                //Connect to database and create PreparedStatement for adding records
                Connection connect = DriverManager.getConnection("jdbc:derby:EventDatabase");
                System.out.println("Connected to database.");
                PreparedStatement addRecord = connect.prepareStatement("INSERT INTO tblEVENT VALUES(?, ?, ?, ?, ?, ?)");
                System.out.println("Prepared Statement created.");

                //Grab data from textfields and radiobuttons, store in variables
                String valEventID = tfEventID.getText();
                String valDesc = tfDesc.getText();
                String valEventDate = tfEventDate.getText();
                String valTheme = tfTheme.getText();
                String valLocationID = tfLocationID.getText();
                boolean valDaytime = rbYes.isSelected(); //isSelected will return true or false

                System.out.println("Data retrieved from controls.");

                //Put data into prepared statement and execute update to add to database table
                addRecord.setString(1, valEventID);
                addRecord.setString(2,valDesc);
                addRecord.setString(3,valEventDate);
                addRecord.setString(4,valTheme);
                addRecord.setString(5,valLocationID);
                addRecord.setBoolean(6, valDaytime);
                addRecord.executeUpdate();
                System.out.println("Added record to table");

                //Create new stage to be displayed and pass in scene for confirmation
                Stage smallStage = new Stage();
                smallStage.setScene(confirmScene);
                smallStage.setTitle("Record Confirmation");
                smallStage.show();

                DriverManager.getConnection("jdbc:derby:EventDatabase; shutdown=true");
            } catch(SQLException ex){
                System.out.println(ex.getMessage());
            }
        });

        //Event handler for View button to see entire table
        view.setOnAction(e->{
            //Clear the javafx tableview each time so that new views don't keep
            //getting appended to old views
            table.getItems().clear();

            try {
                //Connect to database and create statement for executing queries
                Connection connect = DriverManager.getConnection("jdbc:derby:EventDatabase");
                System.out.println("Connected to database.");
                Statement state = connect.createStatement();
                System.out.println("Statement created.");
                //Using * will select all fields from the table
                ResultSet result = state.executeQuery("SELECT * FROM tblEVENT");
                System.out.println("ResultSet created.");
                //Loop through the results from the query and grab each value
                while (result.next()) {
                    String valEventID = result.getString("EventID");
                    String valDesc = result.getString("Description");
                    String valEventDate = result.getString("EventDate");
                    String valTheme = result.getString("Theme");
                    String valLocationID = result.getString("LocationID");
                    boolean valDaytime = result.getBoolean("Daytime");

                    //Store values in a Event object --> See Event.java
                    //This is necessary for using observable list, which is necessary
                    //for adding values to your javafx tableview
                    Event currentEvent = new Event(valEventID, valDesc, valEventDate, valTheme, valLocationID, valDaytime);

                    //Add Cat object to observable list
                    eventData.add(currentEvent);

                    //tell each column which Event data member to connect to --> yes, it's weird.
                    //notice that eventId, description, eventDate, theme, locationID and daytime are my private data member variables from Event
                    colEventID.setCellValueFactory(new PropertyValueFactory<>("eventID"));
                    colDesc.setCellValueFactory(new PropertyValueFactory<>("description"));
                    colEventDate.setCellValueFactory(new PropertyValueFactory<>("eventDate"));
                    colTheme.setCellValueFactory(new PropertyValueFactory<>("theme"));
                    colLocationID.setCellValueFactory(new PropertyValueFactory<>("locationID"));
                    colDaytime.setCellValueFactory(new PropertyValueFactory<>("daytime"));

                    //Add catData to the table
                    table.setItems(eventData);
                }

                System.out.println("Table populated.");
                //A new stage so a new window pops up
                Stage smallStage = new Stage();
                //Add earlier scene to it that holds the table
                smallStage.setScene(viewScene);
                System.out.println("Scene set to stage.");
                smallStage.setTitle("All Records");
                smallStage.show();

                DriverManager.getConnection("jdbc:derby:EventDatabase; shutdown=true");
            } catch(SQLException ex){
                System.out.println(ex.getMessage());
            }
        });

        //Another event handler must be added for third button option
        dates.setOnAction(e-> {
            Label lbDates = createLabel("Date values needed for EventID:");
            TextArea taEmptyDates = new TextArea();
            taEmptyDates.setFont(new Font("Verdana", 28));

            VBox datesVB = new VBox(lbDates,taEmptyDates);
            datesVB.setAlignment(Pos.CENTER);
            datesVB.setPadding(new Insets(20));
            datesVB.setSpacing(10);

            try {
                //Connect to database and create statement for executing queries
                Connection connect = DriverManager.getConnection("jdbc:derby:EventDatabase");
                System.out.println("Connected to database.");
                Statement state = connect.createStatement();
                System.out.println("Statement created.");
                //Using * will select all fields from the table
                ResultSet result = state.executeQuery("SELECT * FROM tblEVENT");
                System.out.println("ResultSet created.");
                //Loop through the results from the query and grab each value
                while (result.next()) {
                    String valEventID = result.getString("EventID");
                    String valDesc = result.getString("Description");
                    String valEventDate = result.getString("EventDate");
                    String valTheme = result.getString("Theme");
                    String valLocationID = result.getString("LocationID");
                    boolean valDaytime = result.getBoolean("Daytime");

                    //Store values in a Event object --> See Event.java
                    //This is necessary for using observable list, which is necessary
                    //for adding values to your javafx tableview
                    Event currentEvent = new Event(valEventID, valDesc, valEventDate, valTheme, valLocationID, valDaytime);

                    if (currentEvent.getEventDate().equals("")) {
                        taEmptyDates.appendText(currentEvent.getEventID());
                        System.out.println();
                    }
                }
                DriverManager.getConnection("jdbc:derby:EventDatabase; shutdown=true");
            } catch(SQLException ex){
                    System.out.println(ex.getMessage());
                }


            //display
            Stage smallStage = new Stage();
            Scene smallScene = new Scene(datesVB, 500, 500);
            smallStage.setScene(smallScene);
            smallStage.show();
        });


        //Main scene and stage
        primaryStage.setTitle("Event Information Entry Form");
        Scene scene = new Scene(mainLayout, 700, 650);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        try{
            DriverManager.getConnection("jdbc:derby:EventDatabase; shutdown=true");
        } catch(SQLException ex){
            System.out.println(ex.getMessage());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static Label createLabel(String str) {
        Label temp = new Label(str);
        temp.setFont(new Font("Verdana", 24));
        return temp;
    }
    public static RadioButton createRB(String str, ToggleGroup tg) {
        RadioButton temp = new RadioButton(str);
        temp.setFont(new Font("Verdana", 24));
        temp.setToggleGroup(tg);
        return temp;
    }
    public static Button createButton(String str){
        Button temp = new Button(str);
        temp.setFont(new Font("Verdana", 24));
        return temp;
    }
    public static GridPane createGP(){
        GridPane temp = new GridPane();
        temp.setVgap(30);
        temp.setHgap(20);
        return temp;
    }
}
