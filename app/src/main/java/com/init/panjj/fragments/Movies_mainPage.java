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
import com.init.panjj.adapter.MoviesAdapter;
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


public class Movies_mainPage extends Fragment implements SwipeRefreshLayout.OnRefreshListener, ServerResult, Communicator {
    ArrayList<ItemBean> latestlist,featuredlist,spinerdatlist;
    ArrayList<ItemBean>latesturl,featuredurl,spinerdaturl;
    MainActivity act;
    String datachange;
    String dtype;
    boolean flagg;
    GridLayoutManager gdd;
    ArrayList<CutomBean> mainlist;
    int moreitem;
    RecyclerView moviecontainer;
    MoviesCommunicator moviesCommunicator;
    MoviesAdapter movies_itemAdap;
    AVLoadingIndicatorView progressBar;
    View rootView;
    StringRequest stringRequest;
    private SwipeRefreshLayout swipeRefreshLayout;
    String tag;
    public MoviesCommunicator getMoviesCommunicator() {
        if (this.moviesCommunicator == null) {
            this.moviesCommunicator = new MoviesCommunicator();
            this.moviesCommunicator.setCommunicator(this);
        }
        return this.moviesCommunicator;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (!this.flagg) {
            this.flagg = true;
            this.mainlist = new ArrayList();
        }
        if (this.rootView != null) {
            return this.rootView;
        }
        this.rootView = inflater.inflate(R.layout.recyclerview, container, false);
        this.act = (MainActivity) getActivity();
        Bundle bundle = getArguments();
        this.tag = MainFragment.tag;
        this.swipeRefreshLayout = (SwipeRefreshLayout) this.rootView.findViewById(R.id.swipe_refresh_layout);
        this.moviecontainer = (RecyclerView) this.rootView.findViewById(R.id.recycler_container);
        this.progressBar = (AVLoadingIndicatorView) this.rootView.findViewById(R.id.avi);
        this.gdd = new GridLayoutManager(getActivity(), 3, 1, false);
        this.moviecontainer.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.movies_itemAdap = new MoviesAdapter((MainActivity) getActivity(), this.mainlist);
        this.moviecontainer.setNestedScrollingEnabled(false);
        this.moviecontainer.setScrollingTouchSlop(0);
        if (getResources().getBoolean(R.bool.istab)) {
            this.dtype = "t";
            Log.e("tablet", "yes");
        } else {
            this.dtype = "m";
            Log.e("phone", "yes");
        }
        this.moviecontainer.setAdapter(this.movies_itemAdap);
        if (this.flagg) {
            videoRequest();
        }
        this.swipeRefreshLayout.setOnRefreshListener(this);
        this.swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
            }
        });
        moviecontainer.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int visibleItemCount = gdd.getChildCount();
                int totalItemCount = gdd.getItemCount();
                int pastVisiblesItems = gdd.findFirstVisibleItemPosition();
                if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {

                    if (stringRequest.hasHadResponseDelivered()) {
                        moreitem++;
                        videoRequest();
                    }
                    Log.e("item","visibleite"+visibleItemCount +" total"+totalItemCount+ " passvible"+pastVisiblesItems);
                }
            }
        });
        return rootView;
    }




    private void videoRequest() {
            this.swipeRefreshLayout.setRefreshing(true);
            this.progressBar.setVisibility(View.VISIBLE);
            StringBuilder stringBuilder = new StringBuilder();
            new ServerUrls().getClass();
            ServerRequest serverRequest = new ServerRequest(this, stringBuilder.append("http://iiscandy.com/panj/SectionPlayList?id=1&").append(this.moreitem).append("stype=").append(this.dtype).toString(), new HashMap(), 1, 0);

    }

    public void setDataFromServer(JSONObject dataFromServer, int requestCode) throws JSONException {
        if (requestCode == 1) {
            getMoviesCommunicator().processdata(dataFromServer);
            this.swipeRefreshLayout.setRefreshing(false);
            this.progressBar.setVisibility(View.GONE);
        }
    }

    public void sendErrorMessage(String message) {
    }

    public void data(Object object) {
        this.mainlist.clear();
        this.mainlist.addAll((Collection) object);
        if (this.movies_itemAdap != null) {
            this.movies_itemAdap.notifyDataSetChanged();
            Log.e("movie", "" + object);
        }
    }

    public void data(ArrayList<ItemBean> arrayList, ArrayList<ItemBean> arrayList2) {
    }

    @Override
    public void onRefresh() {
        videoRequest();
    }
}
