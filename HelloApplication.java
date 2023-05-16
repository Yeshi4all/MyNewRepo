    package josuerojasruiz_comp228lab5.gameplayer;
    
    import javafx.application.Application;
    import javafx.beans.property.SimpleIntegerProperty;
    import javafx.beans.property.SimpleLongProperty;
    import javafx.beans.property.SimpleStringProperty;
    import javafx.collections.FXCollections;
    import javafx.collections.ObservableList;
    import javafx.event.ActionEvent;
    import javafx.event.EventHandler;
    import javafx.fxml.FXMLLoader;
    import javafx.geometry.Insets;
    import javafx.scene.Group;
    import javafx.scene.Scene;
    import javafx.scene.control.*;
    import javafx.scene.control.cell.PropertyValueFactory;
    import javafx.scene.layout.ColumnConstraints;
    import javafx.scene.layout.GridPane;
    import javafx.scene.layout.VBox;
    import javafx.scene.paint.Color;
    import javafx.scene.text.Font;
    import javafx.stage.Stage;
    
    import java.io.IOException;
    import java.sql.*;
    import java.text.DateFormat;
    import java.text.SimpleDateFormat;
    import java.util.ArrayList;
    import java.util.HashSet;
    import java.util.concurrent.atomic.AtomicReference;
    
    
    
    
    public class HelloApplication extends Application {
    
        String  game_id;
        String player_id;
        String gameplayer_id;
        // private final TableView table = new TableView();
        private final TableView<Gamer> table = new TableView<>();
    
        @Override
        public void start(Stage primaryStage) {
    
    
    
    
            //Declaration Fields
            Group root = new Group();
            Scene scene = new Scene(root, 1080, 575);
            ArrayList<String> aListCourses = new ArrayList<String>();
            ArrayList<String> aListCareers = new ArrayList<String>();
            AtomicReference<String> txtAllCareers = null;
    
            final Label label1 = new Label("List Gamers");
            label1.setFont(new Font("Arial", 20));
    
            /******************************************************************/
    
            Gamer[] obj = new Gamer[20];
            try {
                System.out.println("> Start Program ...");
                Class.forName("oracle.jdbc.driver.OracleDriver");
                System.out.println("> Driver Loaded successfully.");
    
                //Creating a connection between Java program and Oracle database.
    
                Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@ oracle1.centennialcollege.ca:1521:SQLD","COMP228_w23_sy_32", "password");
                //Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@ oracle1.centennialcollege.ca:1521:SQLD","COMP228_w23_sy_32", "password");
    
                System.out.println("Database connected successfully.");
    
                ResultSet rset1;
    
                String query1 =  " SELECT c.game_ID,a.PLAYER_ID,PLAYERANDGAME_ID ,a.FIRST_NAME, a.LAST_NAME, a.ADDRESS \n" +
                        ",a.PROVINCE, a.POSTAL_CODE,a.PHONE_NUMBER  \n" +
                        ",b.GAME_TITLE\n" +
                        ",c.SCORE\n" +
                        ",c.PLAYING_DATE\n" +
                        "FROM player a\n" +
                        "INNER JOIN PLAYERANDGAME c ON c.PLAYER_ID = a.PLAYER_ID\n" +
                        "INNER JOIN game b ON b.game_ID = c.game_ID\n";
    
    //Creating a Statement object to execute SQL statements
                Statement stmt = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
    
    //Executing a SQL INSERT query using executeUpdate()  method of Statement object.
                rset1 = stmt.executeQuery(query1);
                System.out.println(" Get All Values "  );
    
                ResultSetMetaData rsmd = rset1.getMetaData();
                int countrow = 0;
                int columnsNumber = rsmd.getColumnCount();
                while (rset1.next()) {
                    System.out.println(countrow +" ");
                    obj[countrow] = new Gamer(rset1.getInt(2),
                            rset1.getString(4), rset1.getString(5), rset1.getString(6)
                            ,rset1.getString(7), rset1.getString(8), rset1.getString(9)
                            ,rset1.getString(10), rset1.getInt(11), rset1.getString(12).substring(0, 10));
                    for (int i = 1; i <= columnsNumber; i++) {
                        if (i > 1) System.out.print(",  ");
                        String columnValue = rset1.getString(i);
                        System.out.print(columnValue + " " + rsmd.getColumnName(i));
                    }
    
                    countrow += 1;
                }
    
                stmt.close();
    
                System.out.println("Done!!");
    
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
    
    
    
            final ObservableList<Gamer> data =  FXCollections.observableArrayList(obj);
    
    
            table.setEditable(true);
    
    
            TableColumn playerIdCol = new TableColumn("Player ID");
            playerIdCol.setMinWidth(100);
            playerIdCol.setCellValueFactory(new PropertyValueFactory<>("playerId"));
            TableColumn firstNameCol = new TableColumn("First Name");
            firstNameCol.setMinWidth(100);
            firstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));
            TableColumn lastNameCol = new TableColumn("Last Name");
            lastNameCol.setMinWidth(100);
            lastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));
            TableColumn AddressCol = new TableColumn("Address");
            AddressCol.setMinWidth(100);
            AddressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
            TableColumn ProvinceCol = new TableColumn("Province");
            ProvinceCol.setMinWidth(100);
            ProvinceCol.setCellValueFactory(new PropertyValueFactory<>("province"));
            TableColumn PostalCodeCol = new TableColumn("PostalCode");
            PostalCodeCol.setMinWidth(100);
            PostalCodeCol.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
            TableColumn PhoneNumberCol = new TableColumn("Phone Number");
            PhoneNumberCol.setMinWidth(100);
            PhoneNumberCol.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
            TableColumn GameTitleCol = new TableColumn("Game Title");
            GameTitleCol.setMinWidth(100);
            GameTitleCol.setCellValueFactory(new PropertyValueFactory<>("gameTitle"));
            TableColumn GameScoreCol = new TableColumn("Game Score");
            GameScoreCol.setMinWidth(100);
            GameScoreCol.setCellValueFactory(new PropertyValueFactory<>("gameScore"));
            TableColumn DatePlayedCol = new TableColumn("Date Played");
            DatePlayedCol.setMinWidth(100);
            DatePlayedCol.setCellValueFactory(new PropertyValueFactory<>("datePlayed"));
    
    
            table.setItems(data);
            table.getColumns().addAll(playerIdCol,firstNameCol, lastNameCol, AddressCol,ProvinceCol, PostalCodeCol, PhoneNumberCol,GameTitleCol, GameScoreCol, DatePlayedCol);
    
            final VBox vbox = new VBox();
            vbox.setSpacing(5);
            vbox.setPadding(new Insets(310, 0, 0, 30));
            vbox.getChildren().addAll(label1, table);
    
            ((Group) scene.getRoot()).getChildren().addAll(vbox);
            /******************************************************************/
    
            // Create pane
            GridPane pane = new GridPane();
    
            for (int i = 0; i < 6; i++) {
                ColumnConstraints column = new ColumnConstraints();
                column.setPrefWidth(120);
                pane.getColumnConstraints().add(column);
            }
    
    
            //Set its properties
            pane.setPadding(new Insets(10));
            pane.setHgap(5);
            pane.setVgap(5);
            Label notification = new Label();
    
    
            //Text box group
    
            TextField txtFirstName = new TextField();
            TextField txtLastName = new TextField();
            TextField txtAddress = new TextField();
            TextField txtProvince = new TextField();
            TextField txtPostalCode = new TextField();
            TextField txtPhoneNumber = new TextField();
    
            TextField txtMessages = new TextField();
            txtMessages.setEditable(false);
    
            TextField txtUpdatePlayerId = new TextField();
            TextField txtGameTitle = new TextField();
            TextField txtGameScore = new TextField();
            TextField txtDatePlayed = new TextField();
    
            final Label label = new Label("Player Information: ");
            label.setFont(new Font("Arial", 20));
    
            pane.add(label, 0, 0);
            pane.add(new Label("First Name: "), 0, 1);
            pane.add(txtFirstName, 1, 1);
            pane.add(new Label("Last Name: "), 0, 2);
            pane.add(txtLastName, 1, 2);
            pane.add(new Label("Address: "), 0, 3);
            pane.add(txtAddress, 1, 3);
            pane.add(new Label("Province: "), 0, 4);
            pane.add(txtProvince, 1, 4);
            pane.add(new Label("Postal Code: "), 0, 5);
            pane.add(txtPostalCode, 1, 5);
            pane.add(new Label("Phone Number: "), 0, 6);
            pane.add(txtPhoneNumber, 1, 6);
    
            pane.add(new Label("System Messages: "), 0, 9);
            pane.add(txtMessages, 1, 9);
    
            pane.add(new Label("Update Player by ID: "), 2, 1);
            pane.add(txtUpdatePlayerId, 3, 1);
            pane.add(new Label("Game Information: "), 2, 3);
            pane.add(new Label("Game Title: "), 2, 4);
            pane.add(txtGameTitle, 3, 4);
            pane.add(new Label("Game Score: "), 2, 5);
            pane.add(txtGameScore, 3, 5);
            pane.add(new Label("Game Played: "), 2, 6);
            pane.add(txtDatePlayed, 3, 6);
    
            // Add Button to update information from Player ID
            Button bttSearch = new Button(" Search");
            pane.add(bttSearch, 4, 1);
            // Add Button to update information from Player ID
            Button bttUpdate = new Button("Update");
            pane.add(bttUpdate, 4, 2);
    
    
            // Add Button to update information from Player ID
            Button bttCreatePlayer = new Button("Create Player");
            pane.add(bttCreatePlayer, 3, 9);
    
            // Add Button to update information from Player ID
            Button bttDisplayAllPlayers = new Button("Display All Players");
            pane.add(bttDisplayAllPlayers, 4, 9);
    
    
    
            bttSearch.setOnAction(value -> {
    
                try {
                    System.out.println("> Start Program ...");
                    Class.forName("oracle.jdbc.driver.OracleDriver");
                    System.out.println("> Driver Loaded successfully.");
    
                    //Creating a connection between Java program and Oracle database.
    
                  Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@ oracle1.centennialcollege.ca:1521:SQLD","COMP228_w23_sy_32", "password");
                //Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@ 199.212.26.208:1521:SQLD","COMP228_w23_sy_32", "password");
    
                    System.out.println("Database connected successfully.");
    
                    ResultSet rset;
    
                    String query1 =  " SELECT c.game_ID,a.PLAYER_ID,PLAYERANDGAME_ID ,a.FIRST_NAME, a.LAST_NAME, a.ADDRESS \n" +
                            ",a.PROVINCE, a.POSTAL_CODE,a.PHONE_NUMBER  \n" +
                            ",b.GAME_TITLE\n" +
                            ",c.SCORE\n" +
                            ",c.PLAYING_DATE\n" +
                            "FROM player a\n" +
                            "INNER JOIN PLAYERANDGAME c ON c.PLAYER_ID = a.PLAYER_ID\n" +
                            "INNER JOIN game b ON b.game_ID = c.game_ID\n" +
                            "WHERE a.PLAYER_ID = " + txtUpdatePlayerId.getText();
    
    //Creating a Statement object to execute SQL statements
                    Statement stmt = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
    
    //Executing a SQL INSERT query using executeUpdate()  method of Statement object.
                    rset = stmt.executeQuery(query1);
                    System.out.println(" Get Values "  );
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    
                    while (rset.next()) {
                        System.out.println(rset.getInt(1) + " " +
                                rset.getString(2) + " " +
                                rset.getString(3) + " " +
                                rset.getString(4));
                        game_id = rset.getString(1);
                        player_id = rset.getString(2);
                        gameplayer_id = rset.getString(3);
    
                        txtFirstName.setText(rset.getString(4));
                        txtLastName.setText(rset.getString(5));
                        txtAddress.setText(rset.getString(6));
                        txtProvince.setText(rset.getString(7));
                        txtPostalCode.setText(rset.getString(8));
                        txtPhoneNumber.setText(rset.getString(9));
    
                        txtGameTitle.setText(rset.getString(10));
                        txtGameScore.setText(rset.getString(11));
                        txtDatePlayed.setText(rset.getString(12).substring(0, 10));
                    }
    
    
    
    
                    stmt.close();
    
                    txtMessages.setText("Done!!");
    
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
    
            });
    
            bttUpdate.setOnAction(value -> {
    
                try {
                    System.out.println("> Start Program ...");
                    Class.forName("oracle.jdbc.driver.OracleDriver");
                    System.out.println("> Driver Loaded successfully.");
    
                    //Creating a connection between Java program and Oracle database.
    
    
                    Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@ oracle1.centennialcollege.ca:1521:SQLD","COMP228_w23_sy_32", "password");
                  //Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@ 199.212.26.208:1521:SQLD","COMP228_w23_sy_32", "password");
    
                    System.out.println("Database connected successfully.");
    
    
                    String sql = "UPDATE PLAYER " +
                            "SET first_name = ?,last_name = ?,address = ?,postal_code = ?,province = ?,phone_number = ? WHERE player_id = ?";
    
                    PreparedStatement statement = connection.prepareStatement(sql);
                    statement.setString(1, txtFirstName.getText());
                    statement.setString(2, txtLastName.getText());
                    statement.setString(3, txtAddress.getText());
                    statement.setString(4, txtPostalCode.getText());
                    statement.setString(5, txtProvince.getText());
                    statement.setString(6, txtPhoneNumber.getText());
                    statement.setString(7, txtUpdatePlayerId.getText());
    
                    //,playing_date = ?
    //                statement.setString(7, txtDatePlayed.getText());
                    int rowsInserted = statement.executeUpdate();
    
                    if (rowsInserted > 0) {
                        System.out.println("A new user was inserted successfully!");
                    }
    
    
                    statement.close();
                    String query2 =  "UPDATE GAME " +
                            " SET game_title = ? WHERE game_id = (SELECT GAME_ID FROM PLAYERANDGAME  WHERE PLAYER_ID = ?)" ;
    
                    PreparedStatement statement2 = connection.prepareStatement(query2);
                    statement2.setString(1, txtGameTitle.getText());
                    statement2.setString(2, txtUpdatePlayerId.getText());
    
    
                    int rowsInserted2 = statement2.executeUpdate();
    
                    if (rowsInserted2 > 0) {
                        System.out.println("A new user was inserted successfully!");
                    }
    
                    statement2.close();
    
                    String query3 =  "UPDATE PLAYERANDGAME " +
                            " SET playing_date = ?,score = ? WHERE PLAYER_ID = ?";
    
                    //  String query2 =  "UPDATE GAME " +
                    //        " SET game_title = ? WHERE game_id = (SELECT GAME_ID FROM PLAYERANDGAME  WHERE PLAYER_ID = ?)" ;
    
                    PreparedStatement statement3 = connection.prepareStatement(query3);
                    statement3.setString(1, txtDatePlayed.getText());
                    statement3.setString(2, txtGameScore.getText());
                    statement3.setString(3, txtUpdatePlayerId.getText());
    
    
                    int rowsInserted3 = statement3.executeUpdate();
    
                    if (rowsInserted3 > 0) {
                        System.out.println("A new user was inserted successfully!");
                    }
    
    
    
                    connection.commit();
                    statement3.close();
    
                    txtMessages.setText("Update Done!!");
    
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
    
            });
    
            bttCreatePlayer.setOnAction(value -> {
    
                try {
                    System.out.println("> Start Program ...");
                    Class.forName("oracle.jdbc.driver.OracleDriver");
                    System.out.println("> Driver Loaded successfully.");
    
                    //Creating a connection between Java program and Oracle database.
    
                    Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@ oracle1.centennialcollege.ca:1521:SQLD","COMP228_w23_sy_32", "password");
                  //Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@ 199.212.26.208:1521:SQLD","COMP228_w23_sy_32", "password");
    
                    System.out.println("Database connected successfully.");
    
    
    
                    String query1 =  "INSERT INTO PLAYER (player_id,first_name,last_name,address,postal_code,province,phone_number)"
                            + "VALUES (player_seq.NEXTVAL,'" + txtFirstName.getText() + "','" + txtLastName.getText() + "','" + txtAddress.getText() + "','" + txtPostalCode.getText() + "','" + txtProvince.getText() + "','" + txtPhoneNumber.getText() + "')";
    
                    String query2 =  "INSERT INTO GAME (game_id,game_title)"
                            + "VALUES (game_seq.NEXTVAL,'" + txtGameTitle.getText() + "')";
    
                    String query3 =  "INSERT INTO PLAYERANDGAME (PLAYERANDGAME_ID,game_id,player_id,playing_date,score)"
                            + "VALUES (playerandgame_seq.NEXTVAL,game_seq.CURRVAL,player_seq.CURRVAL,'" + txtDatePlayed.getText() + "','" + txtGameScore.getText() + "')";
    
    
    
    //Creating a Statement object to execute SQL statements
                    Statement stmt = connection.createStatement();
    
    //Executing a SQL INSERT query using executeUpdate()  method of Statement object.
                    int count = stmt.executeUpdate(query1);
                    System.out.println("Number of rows updated in database =  " + count);
    
    //Executing next SQL INSERT query using executeUpdate()  method of Statement object.
                    count = stmt.executeUpdate(query2);
                    System.out.println("Number of rows updated in database = " + count);
    
    //Executing next SQL INSERT query using executeUpdate()  method of Statement object.
                    count = stmt.executeUpdate(query3);
                    System.out.println("Number of rows updated in database = " + count);
    
    
                    stmt.close();
    
                    txtMessages.setText("Done!!");
    
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
    
            });
    
            bttDisplayAllPlayers.setOnAction(value ->{
    
                for ( int i = 0; i<table.getItems().size(); i++) {
                    table.getItems().clear();
                }
    
                Gamer[] obj2 = new Gamer[20];
                try {
                    System.out.println("> Start Program ...");
                    Class.forName("oracle.jdbc.driver.OracleDriver");
                    System.out.println("> Driver Loaded successfully.");
    
                    //Creating a connection between Java program and Oracle database.
    
                  Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@ oracle1.centennialcollege.ca:1521:SQLD","COMP228_w23_sy_32", "password");
                //Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@ 199.212.26.208:1521:SQLD","COMP228_w23_sy_32", "password");
    
                    System.out.println("Database connected successfully.");
    
                    ResultSet rset2;
    
                    String query9 =  " SELECT c.game_ID,a.PLAYER_ID,PLAYERANDGAME_ID ,a.FIRST_NAME, a.LAST_NAME, a.ADDRESS \n" +
                            ",a.PROVINCE, a.POSTAL_CODE,a.PHONE_NUMBER  \n" +
                            ",b.GAME_TITLE\n" +
                            ",c.SCORE\n" +
                            ",c.PLAYING_DATE\n" +
                            "FROM player a\n" +
                            "INNER JOIN PLAYERANDGAME c ON c.PLAYER_ID = a.PLAYER_ID\n" +
                            "INNER JOIN game b ON b.game_ID = c.game_ID\n";
    
    //Creating a Statement object to execute SQL statements
                    Statement stmt2 = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
    
    //Executing a SQL INSERT query using executeUpdate()  method of Statement object.
                    rset2 = stmt2.executeQuery(query9);
                    System.out.println(" Get All Values "  );
    
                    ResultSetMetaData rsmd2 = rset2.getMetaData();
                    int countrow2 = 0;
                    int columnsNumber2 = rsmd2.getColumnCount();
                    while (rset2.next()) {
                        System.out.println(countrow2 +" ");
                        obj2[countrow2] = new Gamer(rset2.getInt(2),
                                rset2.getString(4), rset2.getString(5), rset2.getString(6)
                                ,rset2.getString(7), rset2.getString(8), rset2.getString(9)
                                ,rset2.getString(10), rset2.getInt(11), rset2.getString(12).substring(0, 10));
                        for (int i = 1; i <= columnsNumber2; i++) {
                            if (i > 1) System.out.print(",  ");
                            String columnValue = rset2.getString(i);
                            System.out.print(columnValue + " " + rsmd2.getColumnName(i));
                        }
    
                        countrow2 += 1;
                    }
    
                    stmt2.close();
    
    
                    final ObservableList<Gamer> data2 =  FXCollections.observableArrayList(obj2);
                    table.setItems(data2);
    
                    System.out.println("Done!!");
    
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
    
    
            });
    
    
                root.getChildren().add(pane);
            primaryStage.setTitle("Player Registration"); // Set the stage title
            primaryStage.setScene(scene); // Place the scene in the stage
            primaryStage.show(); // Display the stage
    
        }
    
        public static class Gamer {
    
    
            private final SimpleLongProperty  PlayerId;
            private final SimpleStringProperty FirstName;
            private final SimpleStringProperty LastName;
            private final SimpleStringProperty Address;
            private final SimpleStringProperty Province;
            private final SimpleStringProperty PostalCode;
            private final SimpleStringProperty PhoneNumber;
            private final SimpleStringProperty GameTitle;
            private final SimpleLongProperty GameScore;
            private final SimpleStringProperty DatePlayed;
    
            private Gamer(long playerId,String fName, String lName, String address,String province, String postalcode, String phonenumber,String gametitle, long gamescore, String dateplayed) {
                this.PlayerId = new SimpleLongProperty(playerId);
                this.FirstName = new SimpleStringProperty(fName);
                this.LastName = new SimpleStringProperty(lName);
                this.Address = new SimpleStringProperty(address);
                this.Province = new SimpleStringProperty(province);
                this.PostalCode = new SimpleStringProperty(postalcode);
                this.PhoneNumber = new SimpleStringProperty(phonenumber);
                this.GameTitle = new SimpleStringProperty(gametitle);
                this.GameScore = new SimpleLongProperty(gamescore);
                this.DatePlayed = new SimpleStringProperty(dateplayed);
            }
    
    
    
            public Long getPlayerId() {
                return PlayerId.get();
            }
    
            public void setPlayerId(Long playerid) {
                PlayerId.set(playerid);
            }
            public String getFirstName() {
                return FirstName.get();
            }
    
            public void setFirstName(String fName) {
                FirstName.set(fName);
            }
    
            public String getLastName() {
                return LastName.get();
            }
    
            public void setLastName(String lName) {
                LastName.set(lName);
            }
    
            public String getAddress() {
                return Address.get();
            }
    
            public void setAddress(String address) {
                Address.set(address);
            }
    
            public String getProvince() {
                return Province.get();
            }
    
            public void setProvince(String province) {
                Province.set(province);
            }
    
            public String getPostalCode() {
                return PostalCode.get();
            }
    
            public void setPostalCode(String postalcode) {
                PostalCode.set(postalcode);
            }
    
            public String getPhoneNumber() {
                return PhoneNumber.get();
            }
    
            public void setPhoneNumber(String phonenumber) {
                PhoneNumber.set(phonenumber);
            }
    
            public String getGameTitle() {
                return GameTitle.get();
            }
    
            public void setGameTitle(String gametitle) {
                GameTitle.set(gametitle);
            }
    
            public long getGameScore() {
                return GameScore.get();
            }
    
            public void setGameScore(long gamescore) {
                GameScore.set(gamescore);
            }
    
            public String getDatePlayed() {
                return DatePlayed.get();
            }
    
            public void setDatePlayed(String dateplayed) {
                DatePlayed.set(dateplayed);
            }
    
        }
    
        public static void main(String[] args) {
            HelloApplication.launch();
        }
    }