package com.example.academyreviewandrating;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

public class ListViewCourseDetails extends Activity {

    private ListView listView;
    private Toolbar myActionBar;
    private TextView textViewTitle;
    private String[] intendMes;
    private HashMap<String, String> hashMap_rating;
    String[] item_desc= {"Watch Reviews", "Rank", "Course information", "SignUp to Course",
    "Course Participants"};

    Integer[] imageId = {R.drawable.rank_icon,R.drawable.star_icon, R.drawable.info_icon,R.drawable.sign_up_icon,
    R.drawable.review_icon};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view_course_details);

        Intent intent = getIntent();
        intendMes = intent.getStringArrayExtra("values");
        hashMap_rating = (HashMap<String, String>)intent.getSerializableExtra("RatingMap");

        textViewTitle = (TextView) findViewById(R.id.title_text) ;
        textViewTitle.setText(intendMes[2] + ", " + intendMes[3] );

        myActionBar = (Toolbar) findViewById(R.id.toolbar2);
        myActionBar.setNavigationIcon(getResources().getDrawable(R.drawable.back_arrow_icon));
        myActionBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        CustomListAdapter adapter = new CustomListAdapter(this,item_desc,imageId);
        listView = (ListView)findViewById(R.id.list_view);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0){ //Watch reviews
                    Toast.makeText(getApplicationContext(),"0",Toast.LENGTH_SHORT).show();
                    //insert the hashmap with all the specific rank!!!!
                } else if (position == 1){

                } else if (position == 2) {

                } else if (position == 3) {

                } else if (position == 4){

                }
            }
        });
    }
}
