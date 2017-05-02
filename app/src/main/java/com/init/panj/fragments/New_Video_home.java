package com.init.panj.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.facebook.appevents.AppEventsLogger;
import com.init.panj.R;
import com.init.panj.activity.MainActivity;
import com.init.panj.activity.SubtitlePlayer;
import com.init.panj.adapter.MainContentAdoptor1;
import com.init.panj.adapter.MainContentAdoptor3;
import com.init.panj.adapter.TrailerAdap;
import com.init.panj.adapter.TrendingAdaptor;
import com.init.panj.clases.Download;
import com.init.panj.clases.ImageLoaderInit;
import com.init.panj.clases.SearchDialogArtist;
import com.init.panj.model.CommentBean;
import com.init.panj.model.Data;
import com.init.panj.model.ItemBean;
import com.init.panj.radioplayer.Controls;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import at.blogc.android.views.ExpandableTextView;
import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;


public class New_Video_home extends Fragment {
    Dialog dialog;
    RecyclerView relatedcontainer;
    public static ArrayList<ItemBean> relatedurl;
    ArrayList<ItemBean> relatedlist;
    MainActivity act;
    ArrayList<ItemBean> url, singlalleurl, url2, url3, url4, url5, trecomendurl, singleurl;
    StringRequest stringRequest;
    LinearLayout recomondedtra, recomendvideo;
    View rootView;
    TextView info, download, share, views;
    String shareurl = "", dlinks;
    int cn = 0;
    ArrayList<ItemBean> list = new ArrayList<>();
    ImageView banmner_image;
    TrailerAdap similerAdopter;
    String videourl, cover;
    TextView similardata;
    String information;
    String id;
    String likecount, viewcount;
    int pos;
    public static ViewHolder viewHolder;
    private ArrayList<Data> al;
    String tag;
    public static ArrayList<ItemBean> recommendvideolist, vrecommendurl;
    String infoartist, infoproducer, infodirector, infomusicdirector, infotitle = "null", infostarimg;
    ACProgressFlower progressDialog;
    ExpandableTextView video_desc;
    String bannerImage = "";
    LinearLayout downloadlayout;
    ItemBean itemBean;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView != null)
            return rootView;
        rootView = inflater.inflate(R.layout.new_video_home, container, false);
        act = (MainActivity) getActivity();
        progressDialog = new ACProgressFlower.Builder(getActivity())
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor(Color.WHITE)
                .text("Loading...")
                .build();
        progressDialog.setCancelable(false);

      if (itemBean!=null) {
          id = itemBean.id;
          videourl = itemBean.m3u8;
          Log.e("vid","vid="+id);
      }
        else {
          Bundle bundle = getArguments();
          cover = bundle.getString("cat");
          information = bundle.getString("info");
          tag = bundle.getString("tag");
          pos = bundle.getInt("pos");
          id=bundle.getString("id");
      }
        /*pos = bundle.getInt("pos");
        */
        relatedcontainer = (RecyclerView) rootView.findViewById(R.id.similar);
        banmner_image = (ImageView) rootView.findViewById(R.id.banner_image);
        info = (TextView) rootView.findViewById(R.id.info);
        video_desc = (ExpandableTextView) rootView.findViewById(R.id.video_desc);
        download = (TextView) rootView.findViewById(R.id.download);
        views = (TextView) rootView.findViewById(R.id.views);
        share = (TextView) rootView.findViewById(R.id.share);
        similardata = (TextView) rootView.findViewById(R.id.similardata);
        downloadlayout = (LinearLayout) rootView.findViewById(R.id.downlodayout);
        similardata.setText("Related Videos");
        recomendvideo = (LinearLayout) rootView.findViewById(R.id.recomendvideolayout);
        recomondedtra = (LinearLayout) rootView.findViewById(R.id.recomendedlayout);
        GridLayoutManager gdd = new GridLayoutManager(getActivity(), 3);
        relatedcontainer.setLayoutManager(gdd);
        relatedlist = new ArrayList<>();
        relatedurl = new ArrayList<>();
        url = new ArrayList<>();
        singleurl = new ArrayList<>();
        url2 = new ArrayList<>();
        url3 = new ArrayList<>();
        url4 = new ArrayList<>();
        url5 = new ArrayList<>();
        singleurl = new ArrayList<>();
        trecomendurl = new ArrayList<>();
        recommendvideolist = new ArrayList<>();
        vrecommendurl = new ArrayList<>();
        similerAdopter = new TrailerAdap((MainActivity) getActivity(), list, singleurl, "Videos");
        relatedcontainer.setAdapter(similerAdopter);
        relatedcontainer.setNestedScrollingEnabled(false);
        banmner_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Controls.pauseControl(getActivity());
                MainActivity.allurl = singleurl;
                Intent it = new Intent(act, SubtitlePlayer.class);
                it.putExtra("url", videourl);
                it.putExtra("pos", 0);
                it.putExtra("id", id);
                startActivity(it);
            }
        });

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogDown();
                //  new Download(getActivity(),singleurl.get(0).dlink,information).execute();
            }
        });
       /* LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0,0, (int) getResources().getDimension(R.dimen.nagetive),0);
        downloadlayout.setLayoutParams(layoutParams);*/
        // video_desc.setVisibility(View.GONE);

        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (video_desc.isExpanded()) {
                    // video_desc.setVisibility(View.VISIBLE);
                    video_desc.collapse();

                } else {
                    video_desc.expand();
                    //  video_desc.setVisibility(View.GONE);

                }


                //  dialogs();
                        /*    if (true){
                Animation animation=AnimationUtils.loadAnimation(getActivity(),android.R.anim.fade_in);
                    video_desc.setTextSize(18f);
                    animation.setDuration(1000);
                video_desc.setAnimation(animation);

                }
                else
                {
                    Animation animation=AnimationUtils.loadAnimation(getActivity(),android.R.anim.fade_out);
                    animation.setDuration(1000);
                   video_desc.startAnimation(animation);

                    animation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            video_desc.setTextSize(1f);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });


                }*/
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (infotitle.equals("null")) {
                    Toast.makeText(getActivity(), "please wait", Toast.LENGTH_LONG).show();
                } else {
                    Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    //    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, infotitle + " Artist:- " + infoartist + " for more videos/movies download panj app  " + "https://play.google.com/store/apps/details?id=" + getActivity().getPackageName());
                    sharingIntent.putExtra(Intent.EXTRA_TEXT, infotitle + "   http://iiscandy.com/panj/vstream?id=" + id);
                    startActivity(Intent.createChooser(sharingIntent, "Share With these app"));
                }
            }
        });
        addSimiler();
        recommendRequest();
        al = new ArrayList<>();
        al.add(new Data("http://i.ytimg.com/vi/PnxsTxV8y3g/maxresdefault.jpg", "But I must explain to you how all this mistaken idea of denouncing pleasure and praising pain was born and I will give you a complete account of the system, and expound the actual teachings of the great explorer of the truth, the master-builder of human happiness."));
        al.add(new Data("http://switchboard.nrdc.org/blogs/dlashof/mission_impossible_4-1.jpg", "But I must explain to you how all this mistaken idea of denouncing pleasure and praising pain was born and I will give you a complete account of the system, and expound the actual teachings of the great explorer of the truth, the master-builder of human happiness."));
        al.add(new Data("http://i.ytimg.com/vi/PnxsTxV8y3g/maxresdefault.jpg", "But I must explain to you how all this mistaken idea of denouncing pleasure and praising pain was born and I will give you a complete account of the system, and expound the actual teachings of the great explorer of the truth, the master-builder of human happiness."));
        al.add(new Data("http://switchboard.nrdc.org/blogs/dlashof/mission_impossible_4-1.jpg", "But I must explain to you how all this mistaken idea of denouncing pleasure and praising pain was born and I will give you a complete account of the system, and expound the actual teachings of the great explorer of the truth, the master-builder of human happiness."));
        al.add(new Data("http://i.ytimg.com/vi/PnxsTxV8y3g/maxresdefault.jpg", "But I must explain to you how all this mistaken idea of denouncing pleasure and praising pain was born and I will give you a complete account of the system, and expound the actual teachings of the great explorer of the truth, the master-builder of human happiness."));
        return rootView;
    }

    private void dialogDown() {
        TextView title;
        RadioGroup radioGroup;
        dialog = new Dialog(getActivity(), android.R.style.Theme_DeviceDefault_Light_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.download_dialog);
        dialog.setCancelable(true);
        title = (TextView) dialog.findViewById(R.id.title);
        title.setText("Select Download quality");
        Button bt = (Button) dialog.findViewById(R.id.cancle);
        radioGroup = (RadioGroup) dialog.findViewById(R.id.qualitygroup);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
//
//               RadioButton radioButton = (RadioButton) dialog.findViewById(checkedId);
//               radioButton.setChecked(true);

                // dialog.dismiss();
                switch (checkedId) {
                    case R.id.p200:
                        Log.e("durl", "" + shareurl.replace("600", "200"));
                        new Download(getActivity(), shareurl.replace("600", "200"), infotitle).execute();
                        dialog.dismiss();
                        break;
                    case R.id.p385:
                        new Download(getActivity(), dlinks.replace("600", "385"), infotitle).execute();
                        dialog.dismiss();
                        break;
                    case R.id.p500:
                        new Download(getActivity(), dlinks.replace("600", "500"), infotitle).execute();
                        dialog.dismiss();
                        break;
                    case R.id.p600:
                        new Download(getActivity(), dlinks.replace("600", "600"), infotitle).execute();
                        dialog.dismiss();
                        break;
                    case R.id.p750:
                        new Download(getActivity(), dlinks.replace("600", "750"), infotitle).execute();
                        dialog.dismiss();
                        break;
                    case R.id.p1000:
                        new Download(getActivity(), dlinks.replace("600", "1000"), infotitle).execute();
                        dialog.dismiss();
                        break;
                    case R.id.p1500:
                        new Download(getActivity(), dlinks.replace("600", "1500"), infotitle).execute();
                        dialog.dismiss();
                        break;
                }
            }
        });
        dialog.show();
    }


    private void addSimiler() {

        stringRequest = new StringRequest(Request.Method.POST, "http://iiscandy.com/panj/SingleVideo?id=" + id, new Response.Listener<String>() {
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
        stringRequest.setRetryPolicy(new DefaultRetryPolicy((int) TimeUnit.SECONDS.toMillis(50), 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
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
                    JSONObject js = jsonOb.getJSONObject("Urls");
                    ItemBean itemBean = new ItemBean();
                    itemBean.BT200 = js.getString("BT200");
                    itemBean.BT385 = js.getString("BT385");
                    itemBean.BT500 = js.getString("BT500");
                    itemBean.BT600 = js.getString("BT600");
                    itemBean.BT750 = js.getString("BT750");
                    itemBean.BT1000 = js.getString("BT1000");
                    itemBean.BT1500 = js.getString("BT1500");
                    itemBean.m3u8 = js.getString("m3u8");
                    itemBean.dlink = js.getString("dlink");
                    dlinks = js.getString("dlink");
                    shareurl = js.getString("BT600");
                    singlalleurl.add(itemBean);
                }
                MainActivity.allurl = singlalleurl;
            } catch (Exception e) {
                Log.e("comment error", e.toString());
                relatedcontainer.setVisibility(View.GONE);
                similardata.setVisibility(View.GONE);
            }

            try {

                JSONArray jsonArray = jsonOb.getJSONArray("SimilarVideos");
                if (jsonArray.length() == 0) {
                    relatedcontainer.setVisibility(View.GONE);
                    similardata.setVisibility(View.GONE);
                }
                for (int k = 0; k < jsonArray.length(); k++) {
                    JSONObject similerobject = jsonArray.getJSONObject(k);
                    ItemBean items = new ItemBean();
                    items.tredcover = similerobject.getString("medium");
                    items.tredname = similerobject.getString("videotitle");
                    items.id = similerobject.getString("videoId");
                    list.add(items);
                    Log.e("simi", "" + items.tredcover);
                }
            } catch (Exception e) {
                relatedcontainer.setVisibility(View.GONE);
                similardata.setVisibility(View.GONE);
            }
            for (int j = 0; j < jsonOb.length(); j++) {
                likecount = jsonOb.getString("likescount");
                viewcount = jsonOb.getString("ViewCount");
                information = jsonOb.getString("title") + "  " + jsonOb.getString("Artist") + " " + jsonOb.getString("genre") + " " + jsonOb.getString("Description");
                infotitle = jsonOb.isNull("title") ? " " : "  " + jsonOb.getString("title");
                infoartist = jsonOb.isNull("Artist") ? " " : " " + jsonOb.getString("Artist");
                infoproducer = jsonOb.isNull("Producer") ? " " : " " + jsonOb.getString("Producer");
                infodirector = jsonOb.isNull("Director") ? " " : " " + jsonOb.getString("Director");
                infomusicdirector = jsonOb.isNull("MusicDirector") ? "" : " " + jsonOb.getString("MusicDirector");
                infostarimg = jsonOb.isNull("StarImage") ? "" : jsonOb.getString("StarImage");
                bannerImage = jsonOb.isNull("medium") ? "" : jsonOb.getString("medium");
            }
            similerAdopter.notifyDataSetChanged();
            views.setText("Views : " + viewcount);
            Picasso.with(getActivity()).load(bannerImage).into(banmner_image);
            video_desc.setText(Html.fromHtml(infotitle + infoartist + infomusicdirector + "<br><font color='#c7c5c5'>" + "Producer : " + infoproducer + "<br> Director : " + infodirector));

            try {
                for (int j = 0; j < jsonOb.length(); j++) {
                    JSONObject js = jsonOb.getJSONObject("Urls");
                    ItemBean itemBean = new ItemBean();
                    itemBean.BT200 = js.getString("BT200");
                    itemBean.BT385 = js.getString("BT385");
                    itemBean.BT500 = js.getString("BT500");
                    itemBean.BT600 = js.getString("BT600");
                    itemBean.BT750 = js.getString("BT750");
                    itemBean.BT1000 = js.getString("BT1000");
                    itemBean.BT1500 = js.getString("BT1500");
                    itemBean.m3u8 = js.getString("m3u8");
                    itemBean.dlink = js.getString("dlink");
                    dlinks = js.getString("dlink");
                    shareurl = js.getString("BT600");
                    singleurl.add(itemBean);
                }
            } catch (Exception e) {

            }
        } catch (Exception e) {
            relatedcontainer.setVisibility(View.GONE);
            similardata.setVisibility(View.GONE);
            Log.e("VideoError", e.toString());
        }

    }


    private void dialogs() {
        TextView songename, artistname, produsername, direcname, musicdirector;
        Button ok;
        ImageView img;
        dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.information_dialog);
        songename = (TextView) dialog.findViewById(R.id.songname);
        artistname = (TextView) dialog.findViewById(R.id.artistname);
        produsername = (TextView) dialog.findViewById(R.id.producername);
        direcname = (TextView) dialog.findViewById(R.id.directorname);
        musicdirector = (TextView) dialog.findViewById(R.id.musicdirector);
        img = (ImageView) dialog.findViewById(R.id.img);
        ok = (Button) dialog.findViewById(R.id.ok);
        songename.setText("" + infotitle);
        artistname.setText(Html.fromHtml("Artist :- <font color='#B92436'>" + infoartist + "</font>"));
        produsername.setText("Producer :-" + infoproducer);
        direcname.setText("Director :-" + infodirector);
        musicdirector.setText("Music Director :-" + infomusicdirector);
        Picasso.with(getContext()).load(infostarimg).placeholder(R.mipmap.panjicon).into(img);
        artistname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                act.dialogshow(new SearchDialogArtist(), infoartist, "sd");

            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

      /*  Log.e("data",""+information);
        AlertDialog.Builder alert=new AlertDialog.Builder(getActivity());
        alert.setMessage(""+information);
        alert.setIcon(R.mipmap.panjicon);
        alert.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alert.show();*/

    }


    private void recommendRequest() {
        progressDialog.show();
        stringRequest = new StringRequest(Request.Method.POST, "http://iiscandy.com/panj/RecommendedVideos?msisdn=0", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response != null) {

                    try {
                        setRecommendValue(new JSONObject(response));
                        Log.e("value", "" + response);
                    } catch (JSONException e) {
                        Log.e("error due to", e.getMessage());
                    }
                }

            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("oho Error ", "Error: " + error.toString());
            }
        }

        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> stringStringHashMap = new HashMap<>();
                stringStringHashMap.put("itemToFetch", recommendvideolist.size() + "");
                stringStringHashMap.put("itemToFetcht", recommendvideolist.size() + "");
                return stringStringHashMap;
            }
        };
        ImageLoaderInit.getInstance().addToRequestQueue(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy((int) TimeUnit.SECONDS.toMillis(50), 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }

    //set recommended  item
    private void setRecommendValue(JSONObject jsonObject) {
        try {
            JSONArray trending = jsonObject.getJSONArray("Recommended");
            for (int j = 0; j < trending.length(); j++) {
                JSONObject jsonObject2 = trending.getJSONObject(j);
                JSONObject js = jsonObject2.getJSONObject("Urls");
                ItemBean itemBean = new ItemBean();
                itemBean.BT200 = js.getString("BT200");
                itemBean.BT385 = js.getString("BT385");
                itemBean.BT500 = js.getString("BT500");
                itemBean.BT600 = js.getString("BT600");
                itemBean.BT750 = js.getString("BT750");
                itemBean.BT1000 = js.getString("BT1000");
                itemBean.BT1500 = js.getString("BT1500");
                itemBean.m3u8 = js.getString("m3u8");
                vrecommendurl.add(itemBean);
                ItemBean items = new ItemBean();
                items.tredname = jsonObject2.getString("Albumname");
                if (MainActivity.isConnectionFast(ConnectivityManager.TYPE_MOBILE, 0) && MainActivity.isConnectionFast(ConnectivityManager.TYPE_MOBILE, 1) && MainActivity.isConnectionFast(ConnectivityManager.TYPE_MOBILE, 2) && MainActivity.isConnectionFast(ConnectivityManager.TYPE_MOBILE, 3) && MainActivity.isConnectionFast(ConnectivityManager.TYPE_MOBILE, 4)) {
                    items.tredcover = jsonObject2.getString("medium");
                } else
                    items.tredcover = jsonObject2.getString("medium");
                items.treddesp = jsonObject2.getString("Description");
                items.id = jsonObject2.getString("id");
                recommendvideolist.add(items);
                progressDialog.dismiss();
            }

        } catch (Exception e) {
            Log.e("er", "" + e.toString());


        }
    }

    @Override
    public void onResume() {
        super.onResume();
        AppEventsLogger.activateApp(getActivity());
    }

    @Override
    public void onPause() {
        super.onPause();
        AppEventsLogger.deactivateApp(getActivity());
    }

    public static New_Video_home getInstance(ItemBean itemBean) {
        New_Video_home new_video_home = new New_Video_home();
        new_video_home.itemBean = itemBean;
        return new_video_home;
    }

    public class MyAppAdapter extends BaseAdapter {


        public List<Data> parkingList;
        public Context context;

        private MyAppAdapter(List<Data> apps, Context context) {
            this.parkingList = apps;
            this.context = context;
        }

        @Override
        public int getCount() {
            return parkingList.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            View rowView = convertView;


            if (rowView == null) {
                LayoutInflater inflater = getActivity().getLayoutInflater();
                rowView = inflater.inflate(R.layout.item, parent, false);
                // configure view holder
                viewHolder = new ViewHolder();
                viewHolder.DataText = (TextView) rowView.findViewById(R.id.bookText);
                viewHolder.background = (FrameLayout) rowView.findViewById(R.id.background);
                viewHolder.cardImage = (ImageView) rowView.findViewById(R.id.cardImage);
                rowView.setTag(viewHolder);

            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.DataText.setText(parkingList.get(position).getDescription() + "");

            //  Glide.with(MainActivity.this).load(parkingList.get(position).getImagePath()).into(viewHolder.cardImage);
            ImageLoader.getInstance().loadImage(parkingList.get(position).getImagePath(), new SimpleImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {
                    //viewHolder.cardImage.setImageResource(R.mipmap.musicload);
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

                    super.onLoadingComplete(imageUri, view, loadedImage);
                    viewHolder.cardImage.setImageBitmap(loadedImage);
                }
            });
            return rowView;
        }
    }

    public static class ViewHolder {
        public static FrameLayout background;
        public TextView DataText;
        public ImageView cardImage;
    }
}
