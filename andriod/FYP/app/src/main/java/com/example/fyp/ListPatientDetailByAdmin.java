package com.example.fyp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class ListPatientDetailByAdmin extends AppCompatActivity {

    TextView txtID;
    TextView txtFirstname;
    TextView txtLastname;
    TextView txtUsername;
    TextView txtPassword;
    TextView txtBedRoomNumber;
    TextView txtAddress;
    TextView txtTel;
    TextView txtEmail;
    TextView txtAge;
    TextView txtDepartment;
    TextView txtPrecautions;
    String id, firstname, lastname, username, password, bedRoomNumber, address, tel, email, age, departmentID, precautions;
    ImageButton btnUpdate, btnStatus;
    String[] departmentName, AllDepartmentID;
    ListDepartment Listdepartment = new ListDepartment();
    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_patient_detail_by_admin);
        txtID = (TextView) findViewById(R.id.id);
        txtFirstname = (TextView) findViewById(R.id.firstname);
        txtLastname = (TextView) findViewById(R.id.lastname);
        txtUsername = (TextView) findViewById(R.id.username);
        txtPassword = (TextView) findViewById(R.id.password);
        txtBedRoomNumber = (TextView) findViewById(R.id.bedRoomNumber);
        txtAddress = (TextView) findViewById(R.id.address);
        txtTel = (TextView) findViewById(R.id.tel);
        txtEmail = (TextView) findViewById(R.id.email);
        txtAge = (TextView) findViewById(R.id.age);
        txtDepartment = (TextView) findViewById(R.id.department);
        txtPrecautions = (TextView) findViewById(R.id.precautions);
        btnUpdate = (ImageButton) findViewById(R.id.ButtonUpdate);
        btnStatus = (ImageButton) findViewById(R.id.Buttonstatus);
        Bundle bundle = getIntent().getExtras();
        Listdepartment.getData();
        departmentName = Listdepartment.getDepartmentName(); // Access to get deapartment name
        AllDepartmentID = Listdepartment.getDepartmentID();
        id = bundle.getString("id");
        firstname = bundle.getString("firstname");
        lastname = bundle.getString("lastname");
        username = bundle.getString("username");
        password = bundle.getString("password");
        bedRoomNumber = bundle.getString("bedRoomNumber");
        address = bundle.getString("address");
        tel = bundle.getString("tel");
        email = bundle.getString("email");
        age = bundle.getString("age");
        departmentID = bundle.getString("departmentID");
        precautions = bundle.getString("precautions");
        txtID.setText("id: " + id);
        txtFirstname.setText("firstname: " + firstname);
        txtLastname.setText("lastname: " + lastname);
        txtUsername.setText("username:" + username);
        txtPassword.setText("password: " + password);
        txtBedRoomNumber.setText("room number: " + bedRoomNumber);
        txtAddress.setText("address: " + address);
        txtTel.setText("tel: " + tel);
        txtEmail.setText("email" + email);
        txtAge.setText("age: " + age);
        for (i = 0; i <= AllDepartmentID.length; i++) {
            if (AllDepartmentID[i].equals(departmentID)) {
                break;
            }
        }
        txtDepartment.setText("department:" + departmentName[i]);
        txtPrecautions.setText("precautions: "+precautions);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(ListPatientDetailByAdmin.this, UpdatePatientDetailByAdmin.class);
                Bundle bundle = new Bundle();
                bundle.putString("id", id);
                bundle.putString("firstname", firstname);
                bundle.putString("lastname", lastname);
                bundle.putString("username", username);
                bundle.putString("password", password);
                bundle.putString("bed_room_number", bedRoomNumber);
                bundle.putString("address", address);
                bundle.putString("tel", tel);
                bundle.putString("email", email);
                bundle.putString("age", age);
                bundle.putString("departmentID", departmentID);
                bundle.putString("precautions", precautions);
                intent.putExtra("Listdepartment", Listdepartment);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        });
        btnStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(ListPatientDetailByAdmin.this, ListHeartbeatAndBodyTemperature.class);
                Bundle bundle = getIntent().getExtras();
                bundle.putString("id", id);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
}
