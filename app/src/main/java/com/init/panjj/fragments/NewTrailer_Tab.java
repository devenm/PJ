package com.init.panjj.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.toolbox.StringRequest;
import com.init.panjj.R;
import com.init.panjj.activity.MainActivity;
import com.init.panjj.adapter.TrailerAdapter;
import com.init.panjj.communicate.Communicator;
import com.init.panjj.communicate.MoviesCommunicator;
import com.init.panjj.model.CutomBean;
import com.init.panjj.model.ItemBean;
import com.init.panjj.network.ServerRequest;
import com.init.panjj.network.ServerResult;
import com.init.panjj.network.ServerUrls;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * Created by INIT on 1/30/2017.
 */

public class NewTrailer_Tab extends Fragment implements SwipeRefreshLayout.OnRefreshListener, ServerResult, Communicator {
    MainActivity act;
    AVLoadingIndicatorView avLoadingIndicatorView;
    String dtype;
    boolean flag;
    boolean flagg;
    GridLayoutManager gdd;
    int moreitem;
    View rootView;
    StringRequest stringRequest;
    SwipeRefreshLayout swipeRefreshLayout;
    TrailerAdapter trailerAdap;
    MoviesCommunicator moviesCommunicator;
    RecyclerView trailercontainer;
    ArrayList<ItemBean> trailerlist;
    ArrayList<ItemBean> trailerurl;
    ArrayList<CutomBean> mainlist;
    public MoviesCommunicator getMoviesCommunicator() {
        if (this.moviesCommunicator == null) {
            this.moviesCommunicator = new MoviesCommunicator();
            this.moviesCommunicator.setCommunicator(this);
        }
        return this.moviesCommunicator;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (!this.flagg) {
            this.flagg = true;
            this.trailerlist = new ArrayList();
            this.trailerurl = new ArrayList();
            mainlist=new ArrayList<>();
        }
        if (this.rootView != null) {
            return this.rootView;
        }
        this.rootView = inflater.inflate(R.layout.recyclerview, container, false);
        this.act = (MainActivity) getActivity();
        this.swipeRefreshLayout = (SwipeRefreshLayout) this.rootView.findViewById(R.id.swipe_refresh_layout);
        this.trailercontainer = (RecyclerView) this.rootView.findViewById(R.id.recycler_container);
        this.avLoadingIndicatorView = (AVLoadingIndicatorView) this.rootView.findViewById(R.id.avi);
        this.gdd = new GridLayoutManager(getActivity(), 3);
        this.trailercontainer.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.trailerAdap = new TrailerAdapter((MainActivity) getActivity(), mainlist);
        this.trailercontainer.setAdapter(this.trailerAdap);
        if (getResources().getBoolean(R.bool.istab)) {
            this.dtype = "t";
        } else {
            this.dtype = "m";
        }
        videoRequest();
        //  this.swipeRefreshLayout.setColorSchemeResources(17170459, 17170452, 17170456, 17170454);
        this.swipeRefreshLayout.setOnRefreshListener(this);
        this.swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
            }
        });
        videoRequest();
        trailercontainer.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int visibleItemCount = gdd.getChildCount();
                int totalItemCount = gdd.getItemCount();
                int pastVisiblesItems = gdd.findFirstVisibleItemPosition();
                if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                    {
                        moreitem++;
                        videoRequest();
                    }
                }
            }
        });
        return rootView;
    }

    private void videoRequest() {
        this.swipeRefreshLayout.setRefreshing(true);
        this.avLoadingIndicatorView.show();
        new ServerUrls().getClass();
        new ServerRequest(this, "http://iiscandy.com/panj/SectionPlayList?id=1&"+"&stype=" + dtype, new HashMap(), 3, 0);

    }


    public void data(Object object) {
        this.mainlist.clear();
        this.mainlist.addAll((Collection) object);
        if (trailerAdap != null) {
            this.trailerAdap.notifyDataSetChanged();
            Log.e("trailer", "" + object);
        }
    }

    public void data(ArrayList<ItemBean> latesturllist, ArrayList<ItemBean> latesturl) {
        if (this.trailerAdap != null) {
           // this.trailerAdap.notifyDataSetChanged();
        }
    }

    public void setDataFromServer(JSONObject dataFromServer, int requestCode) throws JSONException {
        if (requestCode == 3) {
            Log.e("js", getMoviesCommunicator() + "hhh");
            getMoviesCommunicator().processdata(dataFromServer);
            this.swipeRefreshLayout.setRefreshing(false);
            this.avLoadingIndicatorView.hide();
        }
    }

    public void sendErrorMessage(String message) {
    }

    @Override
    public void onRefresh() {
        videoRequest();
    }
}
