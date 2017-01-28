package com.example.academyreviewandrating;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

/**
 * Created by PC on 19/01/2017.
 */

/**
 * Custom Array adapter of chat participants
 */
public class UserChatArrayAdapter extends ArrayAdapter<String> {
    private  FirebaseStorage mStorage;
    private ArrayList<String> UnreadUser;
    private Context contextThis;

    public UserChatArrayAdapter(Context context, ArrayList<String> users,ArrayList<String> UnreadUsers) {
        super(context,0, users);
        this.UnreadUser = UnreadUsers;
        contextThis = context;
        mStorage = FirebaseStorage.getInstance();
    }

    /**
     * Get view of specific user that include image and textView (user name)
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        String userName = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.user_list_item, parent, false);
        }
        // Lookup view for data population
        TextView USER_NAME = (TextView) convertView.findViewById(R.id.user_item);
        ImageView NewOne = (ImageView) convertView.findViewById(R.id.unreadDetail);
        ImageView Iprofile = (ImageView) convertView.findViewById(R.id.pic);
        USER_NAME.setText(userName);
            if(UnreadUser.contains(userName)){
                NewOne.clearAnimation();
                NewOne.setVisibility(View.VISIBLE);
            }
        else{
                NewOne.clearAnimation();
                NewOne.setVisibility(View.INVISIBLE);
            }
        for (int i = 0;i<NavigationStartActivity.userList.size();i++){
            if (NavigationStartActivity.userList.get(i).getUserName().equals(userName)==true){
                if(NavigationStartActivity.userList.get(i).getImage_exist() == true) {
                    String useIDpic = NavigationStartActivity.mMap.get(userName);
                    StorageReference ref = mStorage.getReference().child("UserImages").child(useIDpic).child("pic.jpeg");
                    Glide.with(contextThis).using(new FirebaseImageLoader()).load(ref).into(Iprofile);
                }
                }
        }


        return convertView;
    }
}




