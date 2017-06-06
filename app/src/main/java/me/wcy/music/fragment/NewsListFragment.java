package me.wcy.music.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.wcy.music.R;
import me.wcy.music.adapter.NewListAdapter;
import me.wcy.music.application.MusicApplication;
import me.wcy.music.model.GetMess;
import me.wcy.music.model.ReceiveMess;
import okhttp3.Call;
import okhttp3.MediaType;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

/**
 * 通知
 */
public class NewsListFragment extends android.app.Fragment implements SwipeRefreshLayout.OnRefreshListener{

    @Bind(R.id.swipe_refresh_layout_news)
    SwipeRefreshLayout swipe_refresh_layout;
    private RecyclerView.LayoutManager mLayoutManager;
    @Bind(R.id.cardList_news)
    RecyclerView mRecyclerView;
    NewListAdapter newsadapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_news,container,false);
        ButterKnife.bind(this, view);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        swipe_refresh_layout.setOnRefreshListener(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.HORIZONTAL));
        newsadapter=new NewListAdapter(getActivity());
        mRecyclerView.setAdapter(newsadapter);//设置Adapter
        return view;
    }

    //重绘时刷新
    @Override
    public void onResume() {
        super.onResume();
        onRefresh();
    }


    @Override
    public void onRefresh() {
        newsadapter.notifyChange(addTest());
    }

    public ArrayList<ReceiveMess> GetNewInfo(){
        final ArrayList<ReceiveMess> info = new ArrayList<>();
        OkHttpUtils
                .postString()
                .url(MusicApplication.ip + "enchant/login.action")
                .content(new Gson().toJson(new GetMess("33")))//local user`s id
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.d(TAG, "onError: " + e);
                    }
                    @Override
                    public void onResponse(String response, int id) {
                        Log.d(TAG, "onResponse: " + response);
                        Type type = new TypeToken<ArrayList<ReceiveMess>>() {}.getType();
                        ArrayList<ReceiveMess> jsonObjects = new Gson().fromJson(response, type);
                        for (ReceiveMess infoitem : jsonObjects)
                        {
                            info.add(0,infoitem);
                        }
                    }
                });
        swipe_refresh_layout.setRefreshing(false);
       return info;
    }


    public ArrayList<ReceiveMess> addTest(){
        ArrayList<ReceiveMess> info = new ArrayList<>();
        for(int i = 1; i <10 ;i++){
            info.add(new ReceiveMess("1","1","Title","2016"));
        }
        return  info;
    }
}