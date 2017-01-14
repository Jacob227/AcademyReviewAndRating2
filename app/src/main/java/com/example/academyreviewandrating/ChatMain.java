package com.example.academyreviewandrating;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.academyreviewandrating.Model.ChatMessage;
import com.example.academyreviewandrating.Model.User;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

    public class ChatMain extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseuser;
    private EditText messegeText;
    private FirebaseListAdapter<ChatMessage> adapter;
    //private FirebaseUser senderID;
    private String ReciverName;
    private String ReciverID;
    private String SenderName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_main);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("Messeges");
        messegeText = (EditText) findViewById(R.id.new_message);
        Intent intentChatRoom = getIntent();
        Bundle bd = intentChatRoom.getExtras();
        if(bd != null)
        {
            ReciverID = (String)bd.get("ReciveID");
            ReciverName = (String)bd.get("ReciveName");
        }

        mDatabaseuser =  FirebaseDatabase.getInstance().getReference();
        mDatabaseuser.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot mDataSnapshot: dataSnapshot.getChildren()){
                    String KeyUid = mDataSnapshot.getKey();
                    if (KeyUid.equals(mAuth.getCurrentUser().getUid())){
                        SenderName =  mDataSnapshot.getValue(User.class).getUserName();
                    }
            }}
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
            displayChatMessage();
    }

    public void sendMessege(View view){
        final String st1 = messegeText.getText().toString();
        String CurrentUID  = mAuth.getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference("Messeges" );
        mDatabase.child(CurrentUID).child(ReciverID).push().setValue(new ChatMessage(st1,SenderName));

        mDatabase = FirebaseDatabase.getInstance().getReference("Messeges" );
        mDatabase.child(ReciverID).child(CurrentUID).push().setValue(new ChatMessage(st1,SenderName));


        messegeText.setText("");
        messegeText.requestFocus();
        displayChatMessage();
    }


 private void displayChatMessage() {

     ListView listOfMessage = (ListView)findViewById(R.id.messages_list);
     mDatabase = FirebaseDatabase.getInstance().getReference("Messeges").child(mAuth.getCurrentUser().getUid()).child(ReciverID);
     adapter = new FirebaseListAdapter<ChatMessage>(this,ChatMessage.class,R.layout.list_item,mDatabase)
     {
         @Override
         protected void populateView(View v, ChatMessage model, int position) {
          //   LinearLayout wrapper = (LinearLayout) v.findViewById(R.id.wrapper);
             LinearLayout wrapper = (LinearLayout) v.findViewById(R.id.wrapper);

             //Get references to the views of list_item.xml
             TextView messageText, messageUser, messageTime;
             messageText = (TextView) v.findViewById(R.id.comment);
            // messageUser = (TextView) v.findViewById(R.id.usernmae);
           //  messageTime = (TextView) v.findViewById(R.id.message_time);

             if(model.getMessageUser().equals(ReciverName)) {
                 wrapper.setGravity(Gravity.RIGHT);
                 messageText.setBackgroundResource(R.drawable.bubble_green);

             }
             else {
                 wrapper.setGravity(Gravity.LEFT);
                 messageText.setBackgroundResource(R.drawable.bubble_yellow);

             }

             messageText.setText(model.getMessageText());
            // messageUser.setText(model.getMessageUser());
     //        messageTime.setText(DateFormat.format("h:mm a", model.getMessageTime()));

         }
     };

     listOfMessage.setAdapter(adapter);
     listOfMessage.setSelection(adapter.getCount() - 1);
 }


}
