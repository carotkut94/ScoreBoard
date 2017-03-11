package net.carotkut.scoreboard;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmQuery;
import io.realm.Sort;

/**
 * Created by deathcode on 11/03/17.
 */

public class FavPlayers extends Fragment {

    Realm realm;
    RealmConfiguration realmConfiguration;
    RecyclerView recyclerView;
    LinearLayout layout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.all_players, container, false);
        Realm.init(getActivity());
        realmConfiguration = new RealmConfiguration.Builder().name("ALLPLAYERS").build();

        return rootview;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Realm.setDefaultConfiguration(realmConfiguration);
        setHasOptionsMenu(true);
        recyclerView = (RecyclerView) view.findViewById(R.id.playersContainer);

        layout = (LinearLayout) view.findViewById(R.id.error);
        Realm.setDefaultConfiguration(realmConfiguration);
        realm = Realm.getDefaultInstance();

        if(realm.where(PlayersModel.class).equalTo("isFav", true).findAll().isEmpty())
        {
            Toast.makeText(getActivity(),"Nothing added yet!", Toast.LENGTH_LONG).show();
        }
        if(UtilsMethods.isInternetAvailable(getActivity())) {
            layout.setVisibility(View.INVISIBLE);
            loadView();
        }
        else if(!UtilsMethods.isInternetAvailable(getActivity()) && realm.isEmpty()) {
            recyclerView.setVisibility(View.INVISIBLE);
            layout.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==R.id.sortAlpha){
            loadSortedAlphaData();
            Toast.makeText(getActivity(),"Sorting names", Toast.LENGTH_LONG).show();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fav, menu);
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public void loadView() {
        Realm.setDefaultConfiguration(realmConfiguration);
        realm = Realm.getDefaultInstance();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(new AllPlayersAdapter(realmConfiguration,(MainActivity)getActivity(), realm.where(PlayersModel.class).equalTo("isFav",true).findAllAsync()));
    }
    public void loadSortedAlphaData() {
        Realm.setDefaultConfiguration(realmConfiguration);
        realm = Realm.getDefaultInstance();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(new AllPlayersAdapter(realmConfiguration,(MainActivity)getActivity(), realm.where(PlayersModel.class).equalTo("isFav",true).findAllSortedAsync("name", Sort.ASCENDING)));
    }


}
