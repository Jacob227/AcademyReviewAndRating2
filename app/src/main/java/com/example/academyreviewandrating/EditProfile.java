package com.example.academyreviewandrating;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EditProfile extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseInsert;
    private Spinner faculty_spinner, academy_spinner,semester_spinner,lec_spinner;
    private boolean flag_academy,flag_faculty,ImageChanged=false;
    private DatabaseReference mDatabaseFac;
    private DatabaseReference mDatabaseLec;
    private ImageView Iprofile;
    private String UID;
    private FirebaseAuth mAuth;
    private StorageReference imagesRef;
    private String textInstitution,textFaculty,textPhone,textFullName,textSemester;
    private  Uri selectedImageUri;
    private  FirebaseStorage mStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        mDatabase = FirebaseDatabase.getInstance().getReference("Academy");
        mStorage = FirebaseStorage.getInstance();
        imagesRef = mStorage.getReference();
        academy_spinner = (Spinner) findViewById(R.id.editTextRegInst);
        faculty_spinner = (Spinner) findViewById(R.id.editTextRegFacl);
        mAuth = FirebaseAuth.getInstance();
        UID = mAuth.getCurrentUser().getUid();
        semester_spinner = (Spinner) findViewById(R.id.editTextSemester);
        final EditProfile ref_activity = this;
        flag_academy = flag_faculty = false;
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> List_inst = new ArrayList<String>();
                for (DataSnapshot child: dataSnapshot.getChildren()){
                    List_inst.add(child.getKey());
                    Log.d("In OnData Register", child.getKey().toString());
                }
                String[] inst_str = new String[List_inst.size()];
                inst_str = List_inst.toArray(inst_str);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(ref_activity, R.layout.my_spinner_item, inst_str );
                academy_spinner.setAdapter(adapter);
                flag_academy = true;

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        academy_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                final AdapterView<?> mparent = parent;
                mDatabaseFac = FirebaseDatabase.getInstance().getReference("Academy");//+ parent.getSelectedItem().toString() + "/Faculty");
                mDatabaseFac.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //mparent.getSelectedItem().toString();
                        List<String> List_inst = new ArrayList<String>();
                        List<String> List_Semester = new ArrayList<String>();
                        DataSnapshot mDataSnap = dataSnapshot.child(mparent.getSelectedItem().toString()).child("/Faculty");
                        for (DataSnapshot child: mDataSnap.getChildren()){
                            List_inst.add(child.getKey());
                        }

                        DataSnapshot mDataSnapSemester = dataSnapshot.child(mparent.getSelectedItem().toString()).child("/Semeters");
                        for (DataSnapshot child: mDataSnapSemester.getChildren()){
                            List_Semester.add(child.getKey());
                        }
                        String[] semester_str = new String[List_Semester.size()];
                        semester_str = List_Semester.toArray(semester_str);
                        ArrayAdapter<String> adapterSem = new ArrayAdapter<String>(ref_activity, R.layout.my_spinner_item, semester_str );
                        semester_spinner.setAdapter(adapterSem);

                        String[] inst_str = new String[List_inst.size()];
                        inst_str = List_inst.toArray(inst_str);
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(ref_activity, R.layout.my_spinner_item, inst_str );
                        faculty_spinner.setAdapter(adapter);
                        flag_faculty = true;
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        Iprofile =  (ImageView)findViewById(R.id.ImageProfile1);
        Iprofile.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });
    }



    private static int RESULT_LOAD_IMAGE = 1;

    public void SaveDetail(View view){
        int somethingChange = 0;
        mDatabaseInsert = FirebaseDatabase.getInstance().getReference("Users").child(UID);
        EditText editText;
        if (flag_academy && flag_faculty) {
            textInstitution = academy_spinner.getSelectedItem().toString();
            textFaculty = faculty_spinner.getSelectedItem().toString();

            mDatabaseInsert.child("faculty").setValue(textFaculty);
            mDatabaseInsert.child("institution").setValue(textInstitution);
            somethingChange++;
        }

        editText = (EditText)findViewById(R.id.editTextRegFullName);
        textFullName = editText.getText().toString().trim();
        if(textFullName.isEmpty() == false){
            mDatabaseInsert.child("fullName").setValue(textFullName);
            somethingChange++;
        }


        editText = (EditText)findViewById(R.id.editTextRegPhone);
        textPhone = editText.getText().toString().trim();
        if(textPhone.isEmpty() == false){
            mDatabaseInsert.child("phone").setValue(textPhone);
            somethingChange++;
        }

        textSemester =semester_spinner.getSelectedItem().toString();
        if(textSemester.isEmpty() == false){
            mDatabaseInsert.child("started_semester").setValue(textSemester);
            somethingChange++;
        }
        if(ImageChanged == true) {
            imagesRef.child("UserImages").child(UID).child("pic.jpeg").putFile(selectedImageUri);
            mDatabase.child("Users").child(UID).child("image_exist").setValue(true);
            NavigationFregmentProfile.Iprofile.setImageBitmap(SS);
            somethingChange++;
        }
        if (somethingChange > 0){
           NavigationFregmentProfile.ProfileEdited = true;
        }
        finish();
    }



    public void CancelDetail(View view)
    {
        finish();
    }

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


   public Bitmap SS;
    private static final int SELECT_PICTURE = 1;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                // Get the url from data
              ///  Uri selectedImageUri = data.getData();
               selectedImageUri = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };
                if (null != selectedImageUri) {

                    Cursor cursor = getContentResolver().query(selectedImageUri,
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
                    Iprofile.clearAnimation();
                    Iprofile.setImageBitmap(rotatedBitmap);
                    //  Set the image in ImageView
                    SS = rotatedBitmap;
                   // ImageChosen = selectedImageUri;
                    ImageChanged = true;




                }}
        }
    }




}
