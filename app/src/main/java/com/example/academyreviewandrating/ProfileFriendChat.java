package com.example.academyreviewandrating;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.academyreviewandrating.Model.User;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ProfileFriendChat extends AppCompatActivity {


    private FirebaseStorage mStorage;
    private TextView UserDetail;
    private TextView UserName;
    private User curruser;
    private ImageView Improfile;
    private ImageView backbotton;
    private String UserIDProfile;
    private StorageReference imagesRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_friend_chat);
        Intent intentChatRoom = getIntent();
        mStorage = FirebaseStorage.getInstance();
        imagesRef = mStorage.getReference();
        Bundle bd = intentChatRoom.getExtras();
        if(bd != null) {
            UserIDProfile = (String) bd.get("uid");
            curruser = (User) bd.getSerializable("friend User");
        }
        UserName = (TextView)findViewById(R.id.username) ;
        backbotton = (ImageView)findViewById(R.id.backtochat) ;
        backbotton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        UserDetail = (TextView)findViewById(R.id.userdetail) ;
        Improfile = (ImageView)findViewById(R.id.ImageProfile);
        ShowDetail();

    }







    public void ShowDetail(){
        UserName.setText(curruser.getFullName());
        UserDetail = (TextView)findViewById(R.id.userdetail) ;
        UserDetail.setText("Study in "+curruser.getInstitution());
        UserDetail = (TextView)findViewById(R.id.userdetail1) ;
        UserDetail.setText("Faculty: "+curruser.getFaculty());
        UserDetail = (TextView)findViewById(R.id.userdetail2) ;
        UserDetail.setText("User Name: "+curruser.getUserName());
        UserDetail = (TextView)findViewById(R.id.userdetail3) ;
        UserDetail.setText(curruser.getEmail());
        UserDetail = (TextView)findViewById(R.id.userdetail4) ;
        UserDetail.setText(curruser.getPhone());
        if(curruser.getImage_exist()==true) {
            Improfile.clearAnimation();
            StorageReference ref = mStorage.getReference().child("UserImages").child(UserIDProfile).child("pic.jpeg");
            // Glide.with(getActivity()).using(new FirebaseImageLoader()).load(ref).into(Iprofile);
            Glide.with(this).using(new FirebaseImageLoader()).load(ref).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(Improfile);

        }
    }





}
