package com.example.academyreviewandrating;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.academyreviewandrating.Model.ChatMessage;
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
    private DatabaseReference mDatabaReadMesseges;
    private EditText messegeText;
    private FirebaseListAdapter<ChatMessage> adapter;
    //private FirebaseUser senderID;
    private String ReciverName;
    private String ReciverID;
    private String SenderName;
    private  String CurrentUID ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_main);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("Messeges");
        messegeText = (EditText) findViewById(R.id.new_message);
        Intent intentChatRoom = getIntent();
        Bundle bd = intentChatRoom.getExtras();
        CurrentUID = mAuth.getCurrentUser().getUid();
        if(bd != null)
        {
            ReciverID = (String)bd.get("ReciveID");
            ReciverName = (String)bd.get("ReciveName");
        }
        SenderName = LoginActivity.user_ref.getUserName();

        mDatabaReadMesseges = FirebaseDatabase.getInstance().getReference();
        mDatabaseuser =  FirebaseDatabase.getInstance().getReference().child("Messeges").child(CurrentUID).child(ReciverID);
        mDatabaseuser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                displayChatMessage();
                for (DataSnapshot mDataSnapshot : dataSnapshot.getChildren()) {
                        ChatMessage messages = mDataSnapshot.getValue(ChatMessage.class);
                        String keyMessege = mDataSnapshot.getKey();
                        if (messages.getMessageUser().equals(SenderName) == false) {
                            if (messages.getread() == false) {
                                mDatabaReadMesseges.child("Messeges").child(CurrentUID)
                                        .child(ReciverID)
                                        .child(keyMessege)
                                        .child("read").setValue(true);
                            }
                            if(NavigationStartActivity.UsernamesListUnRead.contains(ReciverName) == true){
                                NavigationStartActivity.UsernamesListUnRead.remove(ReciverName);
                            }
                        }

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void sendMessege(View view){
        final String st1 = messegeText.getText().toString();

        mDatabase = FirebaseDatabase.getInstance().getReference("Messeges" );
        mDatabase.child(CurrentUID).child(ReciverID).push().setValue(new ChatMessage(st1,SenderName,true));

        mDatabase = FirebaseDatabase.getInstance().getReference("Messeges" );
        mDatabase.child(ReciverID).child(CurrentUID).push().setValue(new ChatMessage(st1,SenderName,false));

        messegeText.setText("");
        messegeText.requestFocus();
    //    displayChatMessage();
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
