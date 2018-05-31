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

        mFirebaseBtn = (Button) findViewById(R.id.firebase_btn);
        mNameField = (EditText) findViewById(R.id.name_field);
        mEmailField = (EditText) findViewById(R.id.email_field);
        mAgeField = (EditText) findViewById(R.id.age_field);
        mNameView = (TextView) findViewById(R.id.name_view);

        // get database reference
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // click to save user and email to Firebase
        mFirebaseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 1 Create child in root object
                String name = mNameField.getText().toString().trim();
                String email = mEmailField.getText().toString().trim();
                String age = mAgeField.getText().toString().trim();

                HashMap<String, String> dataMap = new HashMap<>();

                dataMap.put("Name", name);
                dataMap.put("Email", email);
                dataMap.put("Age", age);

                mDatabase.push().setValue(dataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            mNameField.setText("");
                            mEmailField.setText("");

                            Toast.makeText(MainActivity.this, "Stored...", Toast.LENGTH_LONG).show();

                        } else {
                            Toast.makeText(MainActivity.this, "Error...", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

        // retrieve data from Database , then show them on textView
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                String email = dataSnapshot.child("Name").child("Email").getValue().toString();
//                String age = dataSnapshot.child("Age").getValue().toString();

                String name="", email="", age="";
                String result = "";

                for(DataSnapshot child: dataSnapshot.getChildren()){
//                        if(child.getKey().equals("Name"))
//                            name = child.getValue().toString();
//                        if(child.getKey().equals("Email"))
//                            email = child.getValue().toString();
//                        if(child.getKey().equals("Age"))
//                            age = child.getValue().toString();
                    result = child.getKey();

//                    result = result + "Email: " + email + ", age: " + age+ "\n";
                }

                mNameView.setText(result);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }
}
