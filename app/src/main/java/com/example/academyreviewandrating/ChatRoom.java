package com.example.academyreviewandrating;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Show all the app users with option to filter them
 */
public class ChatRoom extends Fragment {
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private EditText search_user;
    private ArrayAdapter<String> adapter;
    private ListView listOfUsers;
    //private FirebaseListAdapter<User> adapter;
    static String ReciverID;
    public String Suser  = "";
    private Activity my_activity;
   // static Map<String, String> mMap = new HashMap<String, String>();
    private final Object lock = new Object();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.activity_chat_room,container,false);

        Log.d("onCreateView ","NavigationRankRev");
        return myView;
    }

    /**
     * Initialize components parameters
     * implementation of setOnItemClickListener of list of users
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        search_user = (EditText) getView().findViewById(R.id.SearchUser);
        listOfUsers = (ListView)getView().findViewById(R.id.users_list);
        my_activity = getActivity();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Messeges").child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            FilterNameAndDisplay();
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    });


        //final Activity my_activity = getActivity();
        listOfUsers.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ReciverID = adapter.getItem(position);
                Intent intent1 = new Intent(my_activity, ChatMain.class);
                intent1.putExtra("ReciveID",NavigationStartActivity.mMap.get(ReciverID));
                intent1.putExtra("ReciveName",ReciverID);
                for(int I =0;I < NavigationStartActivity.userList.size();I++){
                    if(NavigationStartActivity.userList.get(I).getUserName().equals(ReciverID)){
                            intent1.putExtra("Image_exist",NavigationStartActivity.userList.get(I).getImage_exist());
                        }

                }
                startActivity(intent1);

            }
        });


        search_user.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                Suser = search_user.getText().toString();
                    FilterNameAndDisplay();
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }

    /**
     * Filter the names of users
     */
    private void FilterNameAndDisplay()
    {

        String CurrUID =  mAuth.getCurrentUser().getUid();
        Set set = NavigationStartActivity.mMap.entrySet();
        Iterator i = set.iterator();
        UserChatArrayAdapter mArrayAdapter;
        ArrayList<String> ListFilter = new ArrayList<String>();
        // Display elements
        while(i.hasNext()) {
            Map.Entry me = (Map.Entry)i.next();
            String preFix = (String)me.getKey();
            if(Suser.isEmpty()==true)
            {
                if( CurrUID.equals(NavigationStartActivity.mMap.get(preFix)) == false) {
                    ListFilter.add(preFix);
                }
            }
            else if(preFix.startsWith(Suser) == true){
                if ( CurrUID.equals(NavigationStartActivity.mMap.get(preFix)) == false)
                      ListFilter.add(preFix);
            }

        }
       // mArrayAdapter = new ArrayAdapter<String>(my_activity,R.layout.user_list_item,R.id.user_item,ListFilter);
        mArrayAdapter = new UserChatArrayAdapter(my_activity,ListFilter,NavigationStartActivity.UsernamesListUnRead);
        listOfUsers.setAdapter(mArrayAdapter);
        adapter = mArrayAdapter;
    }
}
