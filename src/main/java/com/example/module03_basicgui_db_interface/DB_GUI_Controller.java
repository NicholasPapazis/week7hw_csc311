package com.example.module03_basicgui_db_interface;

import com.example.module03_basicgui_db_interface.db.ConnDbOps;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class DB_GUI_Controller implements Initializable {

    private final ObservableList<Person> data =
            FXCollections.observableArrayList(


            );


    @FXML
    TextField first_name, last_name, department, major, course;
    @FXML
    private TableView<Person> tv;
    @FXML
    private TableColumn<Person, Integer> tv_id;
    @FXML
    private TableColumn<Person, String> tv_fn, tv_ln, tv_dept, tv_major, tv_course;

    @FXML
    ImageView img_view;

    private static ConnDbOps cdbop = new ConnDbOps();; //database connection object

    //gets called automatically when program runs
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tv_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        tv_fn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        tv_ln.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        tv_dept.setCellValueFactory(new PropertyValueFactory<>("dept"));
        tv_major.setCellValueFactory(new PropertyValueFactory<>("major"));
        tv_course.setCellValueFactory(new PropertyValueFactory<>("course"));

        data.addAll(cdbop.getData());//populates observable list data with the data from the database

        tv.setItems(data); //adds what ever data is in the observable list to the TableView
    }


    //adds new record to gui table
    @FXML
    protected void addNewRecord() {

        int id = data.size()+1;
        String firstName = first_name.getText();
        String lastName = last_name.getText();
        String department_ = department.getText();
        String major_ = major.getText();
        String course_ = course.getText();

        //add data to the observable list
        /*data.add(new Person(
                id,
                firstName,
                lastName,
                department_,
                major_,
                course_
        ));*/

        cdbop.connectToDatabase(); //connect to the database;

        //insert data into mysql database
        cdbop.insertUser(id,
               firstName,
               lastName,
               department_,
               major_,
               course_
        );
        System.out.println("successfully added to database");

        data.clear(); //clears the data before we read the data back in from users2 table
        data.addAll(cdbop.getData()); //populates observable list data with the data from the database

    }

    //clears form on gui
    @FXML
    protected void clearForm() {
        first_name.clear();
        last_name.setText("");
        department.setText("");
        major.setText("");
        course.setText("");

    }


    @FXML
    protected void closeApplication() {
        System.exit(0);
    }


    //edits record in table
    @FXML
    protected void editRecord() {
        Person p= tv.getSelectionModel().getSelectedItem();
        int c=data.indexOf(p);
        Person p2= new Person();
        p2.setId(c+1);
        p2.setFirstName(first_name.getText());
        p2.setLastName(last_name.getText());
        p2.setDept(department.getText());
        p2.setMajor(major.getText());
        p2.setCourse(course.getText());
        data.remove(c);
        data.add(c,p2);
        tv.getSelectionModel().select(c);

        //update the database
        cdbop.updateUser(p2.getId(), p2.getFirstName(), p2.getLastName(), p2.getDept(), p2.getMajor(), p2.getCourse());




    }

    @FXML
    protected void deleteRecord() {
        Person p= tv.getSelectionModel().getSelectedItem();
        data.remove(p);

        cdbop.deleteUser(p.getId()); //delete user from database
        data.clear();
        data.addAll(cdbop.getData()); //re-add data from database to the data observable list after user has been deleted.

        //add code here to clear the input fields after the user has been deleted.


    }



    @FXML
    protected void showImage() {
        File file= (new FileChooser()).showOpenDialog(img_view.getScene().getWindow());
        if(file!=null){
            img_view.setImage(new Image(file.toURI().toString()));

        }
    }


    @FXML
    protected void selectedItemTV(MouseEvent mouseEvent) {
        Person p= tv.getSelectionModel().getSelectedItem();
        first_name.setText(p.getFirstName());
        last_name.setText(p.getLastName());
        department.setText(p.getDept());
        major.setText(p.getMajor());
        course.setText(p.getCourse());


    }
}