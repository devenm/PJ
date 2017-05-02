package com.init.panj.activity;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.VideoView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.init.panj.R;
import com.init.panj.adapter.CommentAdap;
import com.init.panj.adapter.SimilerAdopter;
import com.init.panj.clases.DatabaseHandler;
import com.init.panj.clases.ImageLoaderInit;
import com.init.panj.model.CommentBean;
import com.init.panj.model.ItemBean;
import com.init.panj.model.RecomendBean;
import com.init.panj.model.UrlBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PlayVideo extends AppCompatActivity {
VideoView myVideoView;
    MediaController mediaControls;
    ProgressBar prog;
    ArrayList<ItemBean> url;
    ArrayList<CommentBean> commentlist=new ArrayList<>();
    int position,pos=0;
    String title,description,gener,artist;
    SimilerAdopter similerAdopter;
 //  int id;
    String likecount,commentcount;
    ArrayList<ItemBean> list=new ArrayList<>();
    StringRequest stringRequest;
    RecyclerView similer,comment;
    TextView titleview,artistview,generview,descview,likecounttext,dislikecount;
    ImageButton like,post;
    DatabaseHandler db;
    CommentAdap commentAdap;
    String idd;
    EditText commentbox;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video);
     db= new DatabaseHandler(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        final Toolbar toolbar= (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        SharedPreferences sp = ImageLoaderInit.shared;
        SharedPreferences.Editor ed = sp.edit();
        ed.commit();
        comment= (RecyclerView) findViewById(R.id.comment);
        commentAdap=new CommentAdap(PlayVideo.this,commentlist,comment);
       // similerAdopter=new SimilerAdopter(PlayVideo.this,list, singleurl);
        prog= (ProgressBar) findViewById(R.id.progressBar);
        similer= (RecyclerView) findViewById(R.id.similer);
        titleview= (TextView) findViewById(R.id.title);
        artistview= (TextView) findViewById(R.id.artist);
        generview= (TextView) findViewById(R.id.gener);
        descview= (TextView) findViewById(R.id.desc);
        likecounttext= (TextView) findViewById(R.id.likecount);
        dislikecount= (TextView) findViewById(R.id.dislikecount);
        like= (ImageButton) findViewById(R.id.like);
        commentbox= (EditText) findViewById(R.id.writecomment);
        post= (ImageButton) findViewById(R.id.postcomment);
        similer.setLayoutManager(new LinearLayoutManager(PlayVideo.this,LinearLayoutManager.HORIZONTAL,false));
        comment.setLayoutManager(new LinearLayoutManager(PlayVideo.this, LinearLayoutManager.VERTICAL, false));
       // comment.setLayoutManager(new MLinearLayoutManager(PlayVideo.this,LinearLayoutManager.VERTICAL,false));
       // comment.setAutoMeasureEnabled(true);
        comment.setAdapter(commentAdap);
        similer.setAdapter(similerAdopter);
        comment.setNestedScrollingEnabled(false);
        //addComment();
        if (mediaControls == null) {
            mediaControls = new MediaController(this);
        }
        //geting list of url
        UrlBean dw = (UrlBean) getIntent().getSerializableExtra("list");
        url = dw.getUrl();
        position=getIntent().getIntExtra("position", 0);
       // id=getIntent().getIntExtra("id",0);
        idd=getIntent().getStringExtra("id");
        Log.e("urllist", "s" + idd);
        //like.setColorFilter(Color.parseColor("#ffffff"));
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendLike();
            }
        });
post.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        postComment();
    }
});
        myVideoView = (VideoView) findViewById(R.id.videoView);
        switch (ImageLoaderInit.shared.getInt("type", 0)){
            case 0:
                setVideo(url.get(position).BT200);
                break;
            case 1:
                setVideo(url.get(position).BT385);
                break;
            case 2:
                setVideo(url.get(position).BT500);
                break;
            case 3:
                setVideo(url.get(position).BT600);
                break;
            case 4:
                setVideo(url.get(position).BT750);
                break;
            case 5:
                setVideo(url.get(position).BT1000);
                break;
            case 6:
                setVideo(url.get(position).BT1500);
                break;
            default:
                setVideo(url.get(position).BT200);
                break;
        }
//setVideo(url.get(position).BT200);

        myVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                prog.setVisibility(View.GONE);
                myVideoView.seekTo(pos);
                if (pos == 0) {
                    myVideoView.start();
                } else {
                    myVideoView.pause();
                }
            }
        });
        myVideoView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (getSupportActionBar().isShowing())
                    getSupportActionBar().hide();
                else
                getSupportActionBar().show();
                return false;
            }
        });
//        ctlr.setMediaPlayer(video);
//        Uri video = Uri.parse(url);
//        video.setMediaController(mc);
//        video.setVideoURI(video);
//        video.start();
        addItem();
    }

    private void postComment() {
      String com=commentbox.getText().toString();
        stringRequest = new StringRequest(Request.Method.POST, "http://www.iiscandy.com/panj/InsertComment?vid="+idd+"&msisdn="+941+"&comment="+com, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response != null) {
                    try {
                        setCommmentCount(new JSONObject(response));
                        Log.e("likeresult", "" + response);
                    } catch (JSONException e) {
                        Log.e("error due to", e.getMessage());
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Baba error hai", "Error: " + error.getMessage());
            }
        }

        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> stringStringHashMap = new HashMap<>();
                return stringStringHashMap;
            }
        };
        ImageLoaderInit.getInstance().addToRequestQueue(stringRequest);
    }

    private void setCommmentCount(JSONObject jsonObject) {
        try {
            String s=jsonObject.getString("Video");
            Log.e("liked", s);
//            dislikecount.setText(Integer.parseInt(commentcount)+1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void sendLike() {
        stringRequest = new StringRequest(Request.Method.POST, "http://iiscandy.com/panj/insertlikes?vid="+idd+"&msisdn="+94103, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response != null) {
                    try {
                        setLikeCount(new JSONObject(response));
                        Log.e("likeresult", "" + response);
                    } catch (JSONException e) {
                        Log.e("error due to", e.getMessage());
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Baba error hai", "Error: " + error.getMessage());
            }
        }

        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> stringStringHashMap = new HashMap<>();
                return stringStringHashMap;
            }
        };
        ImageLoaderInit.getInstance().addToRequestQueue(stringRequest);
    }

    private void setLikeCount(JSONObject jsonObject) {
        try {
            if (jsonObject.getString("Like").equals("1")) {
                like.setColorFilter(Color.parseColor("#B92436"));
                int i= Integer.parseInt(likecount)+1;
                likecounttext.setText(""+i);
                Log.e("lk", "" + i);
                likecount= String.valueOf(i);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try{
            if (jsonObject.getString("Unlike").equals("0")) {
              //  like.setColorFilter(Color.parseColor("#FFFFFF"));
                int i= Integer.parseInt(likecount)-1;
                likecounttext.setText(""+i);

                likecount= String.valueOf(i);
                Log.e("ulk", "" +likecount);
            }
        }
        catch (Exception e){

        }

    }

    private void addComment() {
        String name[]={"Deven","Raj","Nav"};
        String profile[]={"http://staticori.iiscandy.com/Music_Videos/IISCandy_Images/Punjabi/Odeon_Music/Fukri/Fukri_Teaser/v_medium.jpg","http://staticori.iiscandy.com/Music_Videos/IISCandy_Images/Punjabi/Odeon_Music/Fukri/Fukri_Teaser/v_medium.jpg","http://staticori.iiscandy.com/Music_Videos/IISCandy_Images/Punjabi/Odeon_Music/Fukri/Fukri_Teaser/v_medium.jpg"};
        String comment[]={"nice nice nicevvnicenice nice nice nice nicenice vnice ","nice","nice"};
    for (int i=0;i<name.length;i++){
        CommentBean commentBean=new CommentBean();
        commentBean.username=name[i];
        commentBean.profilepic=profile[i];
        commentBean.comment=comment[i];
        commentlist.add(commentBean);
    }
        commentAdap.notifyDataSetChanged();
    }

    private void addItem() {

        stringRequest = new StringRequest(Request.Method.POST, "http://iiscandy.com/panj/SingleVideo?id="+idd, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response != null) {
                    try {
                        setValue(new JSONObject(response));
                        Log.e("simi", "" + response);
                    } catch (JSONException e) {
                        Log.e("error due to", e.getMessage());
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Baba error hai", "Error: " + error.getMessage());
            }
        }

        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> stringStringHashMap = new HashMap<>();
                return stringStringHashMap;
            }
        };
        ImageLoaderInit.getInstance().addToRequestQueue(stringRequest);

    }

    private void setValue(JSONObject jsonObject) {
        try {
            JSONObject jsonOb = jsonObject.getJSONObject("Video");
            try {
                JSONArray comnt = jsonOb.getJSONArray("Comments");
                for (int i = 0; i < comnt.length(); i++) {
                    JSONObject comment = comnt.getJSONObject(i);
                    CommentBean commentBean = new CommentBean();
                    commentBean.username = comment.getString("UserName");
                    // commentBean.profilepic=comment.getString("");
                    commentBean.comment = comment.getString("Comment");
                    commentBean.date = comment.getString("DateTime");
                    commentlist.add(commentBean);
                }
            }
            catch (Exception e){
                Log.e("comment error",e.toString());
            }
            try {
                JSONArray jsonArray = jsonOb.getJSONArray("SimilarVideos");
                for (int k = 0; k < jsonArray.length(); k++) {
                    JSONObject similerobject = jsonArray.getJSONObject(k);
                    ItemBean items = new ItemBean();
                    items.tredcover = similerobject.getString("Default");
                    items.tredname = similerobject.getString("videotitle");
                    list.add(items);
                    Log.e("simi", "" + items.tredname);
                }
            }
            catch (Exception e){

            }
            for (int j = 0; j < jsonOb.length(); j++) {

                title = jsonOb.getString("Albumname");
                artist=jsonOb.getString("Artist");
                gener=jsonOb.getString("genre");
                description=jsonOb.getString("Description");
                likecount=jsonOb.getString("likescount");
                commentcount=jsonOb.getString("commentscount");

            }
            setInfo(title,artist,gener,description,likecount,commentcount);
            similerAdopter.notifyDataSetChanged();
            commentAdap.notifyDataSetChanged();
        } catch (Exception e) {
            Log.e("VideoError", e.toString());
        }
    }

    private void setInfo(String title, String artist, String gener, String description, String likecount, String commentcount) {
        if (title.equals("null")){
            titleview.setText("Not Found");
            artistview.setText("Not Found");
            generview.setText("Not Found");
            descview.setText("Not Found");
        }
        else {
        titleview.setText(title+" "+artist+" "+gener+" "+description);
//        artistview.setText(artist);
//        generview.setText(gener);
//        descview.setText(description);
            likecounttext.setText(likecount);
            dislikecount.setText(commentcount);

        }
        Log.d("Insert: ", "Inserting ..");
        db.addRecommend(new RecomendBean(gener,"123456",artist,title));
//        db.addRecommend(new RecomendBean("genre2", "91000000002", "artist2", "albumname2"));
//        db.addRecommend(new RecomendBean("genre3", "91000000003", "artist3", "albumname3"));
    }


    private void setVideo(String url) {
        Log.e("urllll", "z" + url);
        url="http://globalpunjabcloud.purplestream.in/globalpunjab/globalpunjab3-live.smil/playlist.m3u8";
        prog.setVisibility(View.VISIBLE);
        try {
            Uri video = Uri.parse(url);
            myVideoView.setVideoURI(video);
            myVideoView.setMediaController(mediaControls);

        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        myVideoView.requestFocus();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.play, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.bt200) {
            dialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt("Position", myVideoView.getCurrentPosition());
        myVideoView.pause();
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        pos = savedInstanceState.getInt("Position");
        myVideoView.seekTo(pos);
    }
    public void dialog(){
        final Dialog dialog=new Dialog(PlayVideo.this,android.R.style.Theme_DeviceDefault_Light_Dialog);
        dialog.setContentView(R.layout.dialog);
        dialog.setCancelable(true);
        dialog.setTitle("Quality");
        Button bt= (Button) dialog.findViewById(R.id.cancle);
        int posi;
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        final RadioGroup radioGroup= (RadioGroup) dialog.findViewById(R.id.qualitygroup);
        Log.e("checkid", "" + ImageLoaderInit.shared.getInt("posi", R.id.p200));
RadioButton radioButton= (RadioButton) dialog.findViewById(ImageLoaderInit.shared.getInt("posi", R.id.p200));
//        radioButton.setChecked(true);
       radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
           @Override
           public void onCheckedChanged(RadioGroup group, int checkedId) {
//
//               RadioButton radioButton = (RadioButton) dialog.findViewById(checkedId);
//               radioButton.setChecked(true);

               // dialog.dismiss();
               switch (checkedId) {
                   case R.id.p200:
                       // TODO Something
                       Log.e("id", "" + radioGroup.getChildAt(0));
                       setVideo(url.get(position).BT200);
                       saveChildPosition(checkedId,0);
                       break;
                   case R.id.p385:
                       saveChildPosition(checkedId,1);
                        setVideo(url.get(position).BT385);
                       break;
                   case R.id.p500:
                       saveChildPosition(checkedId,2);
                        setVideo(url.get(position).BT500);
                       break;
                   case R.id.p600:
                       saveChildPosition(checkedId,3);
                       setVideo(url.get(position).BT600);
                       break;
                   case R.id.p750:
                       saveChildPosition(checkedId,4);
                        setVideo(url.get(position).BT750);
                       break;
                   case R.id.p1000:
                       saveChildPosition(checkedId,5);
                        setVideo(url.get(position).BT1000);
                       break;
                   case R.id.p1500:
                       saveChildPosition(checkedId,6);
                       setVideo(url.get(position).BT1500);
                       break;
               }
           }

           private void saveChildPosition(int i,int k) {
               SharedPreferences sp = ImageLoaderInit.shared;
               SharedPreferences.Editor ed = sp.edit();
               ed.putInt("posi", i);
               ed.putInt("type",k);
               ed.commit();
               dialog.dismiss();
           }
       });
        dialog.show();
    }
}
