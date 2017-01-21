package com.example.academyreviewandrating;

import android.app.Fragment;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.academyreviewandrating.Model.User;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.FileNotFoundException;
import java.io.IOException;

public class NavigationFregmentProfile extends Fragment {
    private FirebaseAuth mAuth;
    private static int RESULT_LOAD_IMAGE = 1;
    private static int RESULT_OK = -1;
    private ImageView Iprofile;
    private  FirebaseStorage mStorage;
    private StorageReference imagesRef;
    private TextView UserName;
    private TextView UserDetail;
    private User curruser;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseImages;
    private String UID;
    private int cnt_update = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.activity_user_profie,container,false);

        Log.d("onCreateView ","NavigationRankRev");

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        UID = mAuth.getCurrentUser().getUid();

        mDatabaseImages = FirebaseDatabase.getInstance().getReference("Images");

        return myView;
    }



    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mStorage = FirebaseStorage.getInstance();
        imagesRef = mStorage.getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Iprofile =  (ImageView)getActivity().findViewById(R.id.ImageProfile);
        UserName = (TextView)getActivity().findViewById(R.id.username) ;
        UserDetail = (TextView)getView().findViewById(R.id.userdetail) ;
        Iprofile.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });
if (LoginActivity.user_created == true){
    curruser = LoginActivity.user_ref;
    ShowDetail();
}
    //  else{
    //      cnt_update=1;
    //  }
    //  mDatabase.child("Users").addValueEventListener(new ValueEventListener() {
    //      @Override
    //      public void onDataChange(DataSnapshot dataSnapshot) {
    //          //            for(DataSnapshot mDataSnapshot: dataSnapshot.getChildren()){
    //          //                User us =  mDataSnapshot.getValue(User.class);
    //          //                String KeyUid = mDataSnapshot.getKey();
    //          //                if (KeyUid.equals(UID)){
    //          //                    curruser = us;
    //          //                }
    //          //            }
    //          if (cnt_update > 0) {
    //              cnt_update =1;
    //              ShowDetail();
    //          }
    //      }

    //      @Override
    //      public void onCancelled(DatabaseError databaseError) {

    //      }
    //  });
    }

    private Bitmap getBitmap(Uri selectedimg) throws FileNotFoundException {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 3;
        AssetFileDescriptor fileDescriptor = null;
        fileDescriptor = getActivity().getContentResolver().openAssetFileDescriptor(selectedimg, "r");
        Bitmap original = BitmapFactory.decodeFileDescriptor(fileDescriptor.getFileDescriptor(), null, options);
        return original;
    }
    public void ShowDetail(){
        UserName.setText(curruser.getFullName());
        UserDetail.setText("Study in "+curruser.getInstitution());
        UserDetail = (TextView)getView().findViewById(R.id.userdetail1) ;
        UserDetail.setText("Faculty: "+curruser.getFaculty());
        UserDetail = (TextView)getView().findViewById(R.id.userdetail2) ;
        UserDetail.setText("User Name: "+curruser.getUserName());
        UserDetail = (TextView)getView().findViewById(R.id.userdetail3) ;
        UserDetail.setText(curruser.getEmail());
        UserDetail = (TextView)getView().findViewById(R.id.userdetail4) ;
        UserDetail.setText(curruser.getPhone());
        if(curruser.getImage_exist()==true) {
            StorageReference ref = mStorage.getReference().child("UserImages").child(UID).child("pic.jpeg");
           Glide.with(this).using(new FirebaseImageLoader()).load(ref).into(Iprofile);

        }}


public static int getOrientation(int orient){

    if (orient == 8){
        return -90;
    }
    if (orient == 3){
        return 180;
    }
    if (orient == 6){
        return 90;
    }
    return 0;
}




public Uri Uparam;
    private static final int SELECT_PICTURE = 1;
 @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
     if (resultCode == RESULT_OK) {
         if (requestCode == SELECT_PICTURE) {
             // Get the url from data
             Uri selectedImageUri = data.getData();
             String[] filePathColumn = { MediaStore.Images.Media.DATA };
             if (null != selectedImageUri) {

                 Cursor cursor = getActivity().getContentResolver().query(selectedImageUri,
                         filePathColumn, null, null, null);
                 cursor.moveToFirst();

                 int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                 String picturePath = cursor.getString(columnIndex);
                 cursor.close();



                 int orientation ;
                 Bitmap rotatedBitmap=null;
                 try {
                     ExifInterface f = new ExifInterface(picturePath);
                     orientation = f.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
                     Matrix matrix = new Matrix();
                     matrix.postRotate(getOrientation(orientation));
                     BitmapFactory.Options op = new BitmapFactory.Options();
                     rotatedBitmap = Bitmap.createBitmap(BitmapFactory.decodeFile(picturePath,op), 0, 0, BitmapFactory.decodeFile(picturePath,op).getWidth(), BitmapFactory.decodeFile(picturePath,op).getHeight(), matrix, true);
                 } catch (IOException e) {
                     e.printStackTrace();
                 }

                 Iprofile.setImageBitmap(rotatedBitmap);
               //  Set the image in ImageView
                        imagesRef.child("UserImages").child(UID).child("pic.jpeg").putFile(selectedImageUri);

                         mDatabase.child("Users").child(UID).child("image_exist").setValue(true);



             }}
         }
     }
 }




