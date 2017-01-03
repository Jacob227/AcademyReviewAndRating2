package com.example.academyreviewandrating;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class ListViewCourseDetails extends Activity {

    private ListView listView;
    String[] item_desc= {"Watch Reviews", "Rank", "Course information", "SignUp to Course",
    "Course Participants"};

    Integer[] imageId = {R.drawable.rank_icon,R.drawable.star_icon, R.drawable.info_icon,R.drawable.sign_up_icon,
    R.drawable.review_icon};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view_course_details);

        CustomListAdapter adapter = new CustomListAdapter(this,item_desc,imageId);
        listView = (ListView)findViewById(R.id.list_view);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0){
                    Toast.makeText(getApplicationContext(),"0",Toast.LENGTH_SHORT).show();
                } else if (position == 1){

                } else if (position == 2) {

                } else if (position == 3) {

                } else if (position == 4){

                }
            }
        });
    }
}
