package com.example.academyreviewandrating;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.academyreviewandrating.Model.CourseDetailsModel;
import com.example.academyreviewandrating.Model.CourseParticipanceModel;
import com.example.academyreviewandrating.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Communicate with other students that participants on course.
 */
public class Courseparticipants extends AppCompatActivity {

    private Toolbar myActionBar;
    private DatabaseReference databaseReference;
    private AlphaAnimation buttonClick = new AlphaAnimation(Animation.ZORDER_BOTTOM,Animation.ZORDER_NORMAL);
    private FirebaseUser firebaseUser;
    private HashMap<String,CourseParticipanceModel> mListParticipant;
    private String[] mIntendData;
    private EditText inputSearch;
    private ParticipantsAdapter adapter;
    private CheckBox mCheckBox;
    private ListView listView;
    private ArrayList<String> userNames;
    private ArrayList<String> userNamesOrig;
    private Boolean[] ParticipantsBool;
    private Activity mActRef = this;
    private Activity refAct;

    /**
     * Init View components
     * Init Firebase instance
     * Get user participants from DB
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courseparticipants);
        mListParticipant = new HashMap<String,CourseParticipanceModel>();
        userNames = new ArrayList<String>();
        userNamesOrig = new ArrayList<String>();
        Intent intent = getIntent();
        mIntendData = intent.getStringArrayExtra("values");
        adapter = null;
        refAct = this;
        inputSearch = (EditText) findViewById(R.id.EditTextViewFindUser);
        mCheckBox = (CheckBox) findViewById(R.id.checkBoxPartner);
        myActionBar = (Toolbar) findViewById(R.id.toolbar_coursePart);
        myActionBar.setNavigationIcon(getResources().getDrawable(R.drawable.back_arrow_icon));
        myActionBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        listView = (ListView) findViewById(R.id.list_view_search_part);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Academy/" + mIntendData[1] + "/Faculty/" + mIntendData[0]
                + "/Course/" + mIntendData[2] + "/" + mIntendData[4] + "/Course Participants").
                addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for( DataSnapshot mData: dataSnapshot.getChildren()){
                    CourseParticipanceModel mUserPart = mData.getValue(CourseParticipanceModel.class);
                    if (!mUserPart.getUserName().equals(LoginActivity.user_ref.getUserName()))
                        mListParticipant.put(mUserPart.getUserName(),mUserPart);
                }
                ParticipantsBool = new Boolean[mListParticipant.size()];
                int i = 0;
                for (String key: mListParticipant.keySet() ){
                    userNames.add(i,key);
                    userNamesOrig.add(i,key);
                    ParticipantsBool[i] = mListParticipant.get(key).getCourse_part();
                    i++;
                }
                adapter = new ParticipantsAdapter(mActRef,userNames,ParticipantsBool);
                listView.setAdapter(adapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        CourseParticipanceModel send;
                        String user_pos = null;
                       try {
                           user_pos = userNames.get(position);
                            boolean flagImage = true;
                           for (String key: mListParticipant.keySet() ){
                                if (user_pos.equals(mListParticipant.get(key).getUserName())){
                                    Intent mIntent = new Intent(refAct, ChatMain.class);
                                    mIntent.putExtra("ReciveID", mListParticipant.get(key).getUser_id());
                                    mIntent.putExtra("ReciveName", mListParticipant.get(key).getUserName());
                                    for(int I =0;I < NavigationStartActivity.userList.size();I++){
                                        if(NavigationStartActivity.userList.get(I).getUserName().equals(mListParticipant.get(key).getUserName())){
                                            mIntent.putExtra("Image_exist",NavigationStartActivity.userList.get(I).getImage_exist());
                                            flagImage = false;
                                        }

                                    }
                                    if (flagImage){
                                        mIntent.putExtra("Image_exist",true);
                                    }
                                    startActivity(mIntent);
                                    finish();
                                }
                           }

                        } catch ( IndexOutOfBoundsException e ) {
                           Toast.makeText(refAct, "can't send a messege to this user", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (adapter != null){
                    adapter.getFilter().filter(s);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    if (adapter != null){
                        //String[] tempPart = new String[userNames.length];
                        Boolean[] tempBool = new Boolean[userNames.size()];
                        //adapter.setClicked_sort(true);
                        adapter.clear();
                        int k = 0;
                        for (int i = 0; i < ParticipantsBool.length; i++){
                            if (ParticipantsBool[i]) { //insert only the looking for partner
                                tempBool[k] = true;
                                adapter.add(userNamesOrig.get(i));
                                k++;
                            }
                        }
                        tempBool = Arrays.copyOf(tempBool,k);
                        Log.d("hii", String.valueOf(adapter.getCount()));
                        //adapter.setOrig();
                        adapter.setBool(tempBool);
                        adapter.notifyDataSetChanged();
                    }
                }
                else {
                    adapter.clear();
                   // adapter.setClicked_sort(false);
                    adapter.addAll(userNamesOrig);
                    //adapter.setOrig();
                    adapter.setBool(ParticipantsBool);
                    adapter.notifyDataSetChanged();
                }
            }
        });

    }

    public void StartChatOnClick(View view){
        view.startAnimation(buttonClick);
    }
    }
