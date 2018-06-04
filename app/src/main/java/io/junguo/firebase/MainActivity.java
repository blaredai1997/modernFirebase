package io.junguo.firebase;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private Button mFirebaseBtn;
    private EditText mNameField,mEmailField,mAgeField;
    private DatabaseReference mDatabase;
    private TextView mNameView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get Views by Id
        mFirebaseBtn = (Button) findViewById(R.id.firebase_btn);
        mNameField = (EditText) findViewById(R.id.name_field);
        mEmailField = (EditText) findViewById(R.id.email_field);
        mAgeField = (EditText) findViewById(R.id.age_field);
        mNameView = (TextView) findViewById(R.id.name_view);

        // get database reference, here we use subtable named "users"
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users");

        // Saved info to Firebase after clicked the button
        mFirebaseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = mNameField.getText().toString().trim();
                String email = mEmailField.getText().toString().trim();
                String age = mAgeField.getText().toString().trim();

                //use hashmap to store info
                HashMap<String, String> dataMap = new HashMap<>();

                dataMap.put("Name", name);
                dataMap.put("Email", email);
                dataMap.put("Age", age);

                // .push() will push dataMap to database, OnCompleteListener will effect after
                mDatabase.push().setValue(dataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // clear fields when submit successfully
                        if (task.isSuccessful()) {
                            mNameField.setText("");
                            mEmailField.setText("");
                            mAgeField.setText("");

                            //jump up a toast to notify "Stored"
                            Toast.makeText(MainActivity.this, "Stored...", Toast.LENGTH_LONG).show();

                        } else {
                            Toast.makeText(MainActivity.this, "Error...", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

        // retrieve data from Database, then show them on textView
        mDatabase.addValueEventListener(new ValueEventListener() {

            // 'onDataChange' method reloads data while changes happened in database
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String name="", email="", age="";
                String result = "";

                for(DataSnapshot child: dataSnapshot.getChildren()){
                    name = child.child("Name").getValue().toString();
                    email = child.child("Email").getValue().toString();
                    age = child.child("Age").getValue().toString();

                    result = result + "Name: "+name + " - Email: " + email + " - age: " + age+ "\n";
                }

                //display result on NameView
                mNameView.setText(result);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }
}
