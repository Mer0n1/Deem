package com.example.deem.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.deem.MainActivity;
import com.example.deem.R;
import com.example.deem.utils.Toolbar;
import com.example.deem.adapters.NewsListRecycleAdapter;
import com.example.deem.fragments.InfoFragments.ListGroupsFragment;
import com.example.deem.fragments.InfoFragments.ListTopsFragment;
import com.example.deem.fragments.InfoFragments.ListUsersFragment;
import com.example.restful.api.APIManager;
import com.example.restful.models.News;
import com.example.restful.models.StandardCallback;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import androidx.lifecycle.MutableLiveData;


/**
 * Фрагмент информации: новости групп, список топов (людей и групп), список участников
 */
public class InfoFragment extends Fragment {

    private FrameLayout main_layout;
    private MainActivity this_activity;

    private ListUsersFragment listUsersFragment;
    private ListGroupsFragment listGroupsFragment;
    private ListTopsFragment listTopsFragment;

    private NewsListRecycleAdapter newsListRecycleAdapter;
    private RecyclerView recyclerView;

    private List<News> listNews;

    private static boolean lock = false;
    /** Если отправляем запрос на обновление данных а информация не приходит или
     * приходит null то isSendUpdate блокирует повторные запросы*/
    private boolean isSendUpdate = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        main_layout = (FrameLayout)inflater.inflate(R.layout.fragment_news, container, false);
        this_activity = ((MainActivity)(getActivity()));

        init();

        return main_layout;
    }

    public void init() {
        initToolbar();

        if (!APIManager.getManager().statusInfo.isNewsListGot() &&
            !APIManager.statusCacheInfo.isListNewsLoaded())
            return;

        //init objects
        listNews = APIManager.getManager().getListNews().getValue();
        listUsersFragment = new ListUsersFragment();
        listGroupsFragment = new ListGroupsFragment();
        listTopsFragment = new ListTopsFragment();

        initListAndRecycle();
    }


    public void initToolbar() {
        Toolbar.getInstance().reset();
        Toolbar.getInstance().setTitle("Новости");

        //--Добавление иконок
        ImageView imgListTops   = Toolbar.getInstance().loadIcon(R.drawable.icon_tops);
        ImageView imgListGroups = Toolbar.getInstance().loadIcon(R.drawable.icon_list_groups);
        ImageView imgListUsers  = Toolbar.getInstance().loadIcon(R.drawable.icon_list_users);

        //--Изменение титла
        Toolbar.getInstance().setTitle("Новости");

        //Листенеры
        View.OnClickListener onClickListPersons = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                this_activity.OpenFragment(listUsersFragment, R.id.fragment_main, true);
            }
        };

        View.OnClickListener onClickListGroups = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                this_activity.OpenFragment(listGroupsFragment, R.id.fragment_main, true);
            }
        };

        View.OnClickListener onClickTop = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                this_activity.OpenFragment(listTopsFragment, R.id.fragment_main, true);
            }
        };

        imgListUsers.setOnClickListener(onClickListPersons);
        imgListGroups.setOnClickListener(onClickListGroups);
        imgListTops.setOnClickListener(onClickTop);
    }

    public void initListAndRecycle() {
        if (listNews != null) {
            main_layout.findViewById(R.id.progressBar).setVisibility(View.GONE);
            main_layout.findViewById(R.id.news_feed).setVisibility(View.VISIBLE);
        } else
            return;


        //Recycle
        recyclerView = main_layout.findViewById(R.id.news_feed);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        newsListRecycleAdapter = new NewsListRecycleAdapter(listNews, this);
        recyclerView.setAdapter(newsListRecycleAdapter);

        //Как только достигаем конца списка то вызываем обновление
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(1)) {

                    if (!lock && !isSendUpdate) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.US);
                        //String dateStr = sdf.format(listNews.get(listNews.size() - 1).getDate());

                        APIManager.getManager().updateNewsFeed(new StandardCallback() {
                                    @Override
                                    public void call() {
                                        newsListRecycleAdapter.notifyDataSetChanged();
                                        lock = false;
                                        isSendUpdate = false;
                                    }
                                });
                        isSendUpdate = true;
                    }
                    lock = true;
                }
            }
        });

        //Observer
        APIManager.getManager().getListNews().observe(getViewLifecycleOwner(), new Observer<List<News>>() {
            @Override
            public void onChanged(List<News> newsList) {
                newsListRecycleAdapter.notifyDataSetChanged();
            }
        });
    }

}
