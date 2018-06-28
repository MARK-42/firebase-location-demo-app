package com.projects.rtk154.firebasedemoapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {
    FirebaseAuth mFirebaseAuth;
    DatabaseReference databaseReference,databaseReference2;
    FirebaseUser user,user2;
    ArrayList<UserInfo> userDataList =new ArrayList<>();
//    ArrayList<UserInfo> nuller=new ArrayList<>();
    ListView mListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_profile);
        TextView tv=(TextView)findViewById(R.id.HelloTv);
        mFirebaseAuth=FirebaseAuth.getInstance();
        //if user is not logged in then take it to login page;
        if(mFirebaseAuth.getCurrentUser()==null) {
            startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
            finish();
        }
//        nuller.add(new UserInfo("",""));
        mFirebaseAuth=FirebaseAuth.getInstance();
        user=FirebaseAuth.getInstance().getCurrentUser();
        if(user.getDisplayName()!=null)
            tv.setText(" "+user.getDisplayName());
        else
            tv.setText("User-ID :- "+user.getEmail());
        user=FirebaseAuth.getInstance().getCurrentUser();
        databaseReference= FirebaseDatabase.getInstance().getReference("Users");
//        databaseReference2=FirebaseDatabase.getInstance().getReference(user.getEmail());
        final EditText name=(EditText)findViewById(R.id.Name);
        final EditText address=(EditText)findViewById(R.id.Address);
        Button submit=(Button)findViewById(R.id.SubmitButton);
        Button logoutButton=(Button)findViewById(R.id.LogoutButton);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addInfo(name,address);
                String nameEntered= name.getText().toString().trim();
                String addressEntered= address.getText().toString().trim();
                if(TextUtils.isEmpty(nameEntered)||TextUtils.isEmpty(addressEntered)) {
                    //
                }
                else
                    showInfo();
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFirebaseAuth.signOut();
                finish();
                startActivity(new Intent(ProfileActivity.this,LoginActivity.class));
            }
        });
    }

    private void addInfo(EditText name, EditText address) {
        String nameEntered= name.getText().toString().trim();
        String addressEntered= address.getText().toString().trim();
        if(TextUtils.isEmpty(nameEntered)||TextUtils.isEmpty(addressEntered)) {
            Toast.makeText(ProfileActivity.this,"Fill the Details Before Submitting",Toast.LENGTH_LONG).show();
            showInfo();
            return;
        }
        UserInfo information = new UserInfo(nameEntered,addressEntered);
        user=FirebaseAuth.getInstance().getCurrentUser();
        databaseReference.child(user.getUid()).setValue(information);
//        databaseReference2.child(user.getEmail()).setValue(information);
        Toast.makeText(ProfileActivity.this,"Information Saved ",Toast.LENGTH_LONG).show();
    }
    private void showInfo() {
//        userDataAdapter userDataSet=new userDataAdapter(ProfileActivity.this, nuller);
//        mListView.setAdapter(userDataSet);
        final ProgressBar progressBar=(ProgressBar)findViewById(R.id.progressProfile);
        progressBar.setVisibility(View.VISIBLE);
        userDataList.clear();
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference usersdRef = rootRef.child("Users");
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {

                    UserInfo name = ds.getValue(UserInfo.class);
                    Log.d("TAG", name.getmAddress());
                    userDataList.add(name);
                }
                progressBar.setVisibility(View.GONE);
//                Toast.makeText(ProfileActivity.this,"hello",Toast.LENGTH_LONG).show();

                userDataAdapter userDataSet=new userDataAdapter(ProfileActivity.this, userDataList);

                mListView=(ListView)findViewById(R.id.ListView);
                mListView.setAdapter(userDataSet);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        usersdRef.addListenerForSingleValueEvent(eventListener);








    }

    @Override
    protected void onStart() {
        super.onStart();
//        showInfo();
    }
}
