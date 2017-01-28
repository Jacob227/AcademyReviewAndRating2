package com.example.academyreviewandrating;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.academyreviewandrating.Model.User;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * Profile fragment
 * Watch all student/lecturer profile
 * Can edit some details
 */
public class NavigationFregmentProfile extends Fragment {
    private FirebaseAuth mAuth;
    private static int RESULT_LOAD_IMAGE = 1;
    private static int RESULT_OK = -1;
    private ImageView Editprofiles;
    public static ImageView Iprofile;
    private  FirebaseStorage mStorage;
    private StorageReference imagesRef;
    private TextView UserName;
    private TextView UserDetail;
    private User curruser;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseImages;
    private String UID;
    private int cnt_update = 0;
    public static boolean ProfileEdited = false;
    public String UserIDProfile = "";

    /**
     * Init Firebase reference to get an access to DB
     * for getting an image and user information
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.activity_user_profie,container,false);
        Log.d("onCreateView ","NavigationRankRev");
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabaseImages = FirebaseDatabase.getInstance().getReference("Images");
        return myView;
    }


    /**
     * Init all View components
     * get user details from DB
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mStorage = FirebaseStorage.getInstance();
        imagesRef = mStorage.getReference();
        UID = mAuth.getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Iprofile =  (ImageView)getActivity().findViewById(R.id.ImageProfile);
        Editprofiles =  (ImageView)getActivity().findViewById(R.id.Editprofile);
        UserName = (TextView)getActivity().findViewById(R.id.username) ;
        UserDetail = (TextView)getView().findViewById(R.id.userdetail) ;
        Bundle args = getArguments();
         UserIDProfile = args.getString("uid");
        if(UID.equals(UserIDProfile)==true) {
            Editprofiles.setVisibility(ImageView.VISIBLE);
            Editprofiles.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent1 = new Intent(getActivity(), EditProfile.class);
                    startActivity(intent1);
                }
            });
        }


    mDatabase.child("Users").addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if(UID.equals(UserIDProfile)==true) {
            User UserS1 = null;
            if (ProfileEdited == true) {
                for (DataSnapshot mDataSnapshot : dataSnapshot.getChildren()) {
                    if(mDataSnapshot.getKey().equals(UID) == true) {
                        UserS1 = mDataSnapshot.getValue(User.class);
                    }

                }
                curruser = UserS1;
                ShowDetail();
                ProfileEdited = false;
            }
        }}

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    });

        if(ProfileEdited == false) {
            curruser = (User) args.getSerializable("userClass");
            ShowDetail();
        }

    }

    /**
     * Show all DB details on screen
     */
    public void ShowDetail(){
        UserName.setText(curruser.getFullName());
        UserDetail = (TextView)getView().findViewById(R.id.userdetail) ;
        if(curruser.isPrivilage().equals("Student")) {
            UserDetail.setText("Study in " + curruser.getInstitution());
        }
        else{
            UserDetail.setText("Lecturer in " + curruser.getInstitution());
        }
        UserDetail = (TextView)getView().findViewById(R.id.userdetail1) ;
        UserDetail.setText("Faculty: "+curruser.getFaculty());
        UserDetail = (TextView)getView().findViewById(R.id.userdetail2) ;
        UserDetail.setText("User Name: "+curruser.getUserName());
        UserDetail = (TextView)getView().findViewById(R.id.userdetail3) ;
        UserDetail.setText(curruser.getEmail());
        UserDetail = (TextView)getView().findViewById(R.id.userdetail4) ;
        UserDetail.setText(curruser.getPhone());
        if(curruser.getImage_exist()==true) {
            Iprofile.clearAnimation();
            StorageReference ref = mStorage.getReference().child("UserImages").child(UserIDProfile).child("pic.jpeg");
           // Glide.with(getActivity()).using(new FirebaseImageLoader()).load(ref).into(Iprofile);
               Glide.with(getActivity()).using(new FirebaseImageLoader()).load(ref).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(Iprofile);

        }
    }

 }




