package net.carotkut.scoreboard;

import android.app.Fragment;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmQuery;
import io.realm.Sort;

import static net.carotkut.scoreboard.R.id.error;

/**
 * Created by deathcode on 11/03/17.
 */

public class AllPlayers extends Fragment {

    Realm realm;
    RealmConfiguration realmConfiguration;
    RecyclerView recyclerView;
    LinearLayout layout;
    android.support.v7.widget.SearchView searchView;
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
        layout = (LinearLayout) view.findViewById(error);
        Realm.setDefaultConfiguration(realmConfiguration);
        realm = Realm.getDefaultInstance();
        if(UtilsMethods.isInternetAvailable(getActivity())) {
            layout.setVisibility(View.INVISIBLE);
            getJSONResponse(UtilsMethods.getBaseUrl("list_player"));
            long count = realm.where(PlayersModel.class).count();
            Toast.makeText(getActivity(), "Total Players: "+count,Toast.LENGTH_LONG).show();
            loadView();
            gethitCounts(UtilsMethods.getBaseUrl("api_hits"));

        }
        else if(!UtilsMethods.isInternetAvailable(getActivity()) && realm.isEmpty()){
            recyclerView.setVisibility(View.INVISIBLE);
            layout.setVisibility(View.VISIBLE);
        }
    }

    public boolean checkIfFashionExists(String id) {

        Realm.setDefaultConfiguration(realmConfiguration);
        realm = Realm.getDefaultInstance();
        RealmQuery<PlayersModel> query = realm.where(PlayersModel.class)
                .equalTo("id", id);
        return query.count() == 0 ? false : true;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        // write logic here b'z it is called when fragment is visible to user
    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.players, menu);
        searchView = (android.support.v7.widget.SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                loadSearchedData(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.sortMatches:
                loadSortedMatchData();
                Toast.makeText(getActivity(),"Sorting by matches played", Toast.LENGTH_LONG).show();
                break;
            case R.id.sortRuns:
                loadSortedRunData();
                Toast.makeText(getActivity(),"Sorting by runs made", Toast.LENGTH_LONG).show();
                break;
            case R.id.portfolio:
                startActivity(new Intent(getActivity(), Portfolio.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void getJSONResponse(String url) {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.e("RES", response.toString());
                try {
                    JSONArray array = response.getJSONArray("records");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject tempData = array.getJSONObject(i);

                        String id = tempData.getString("id");
                        String name = tempData.getString("name");
                        String image_url = tempData.getString("image");
                        int score = tempData.getInt("total_score");
                        String desc = tempData.getString("description");
                        int matches_played = tempData.getInt("matches_played");
                        String country = tempData.getString("country");
                        if (!checkIfFashionExists(id)) {
                            Realm realm = Realm.getDefaultInstance();
                            realm.beginTransaction();
                            PlayersModel playersModel = realm.createObject(PlayersModel.class);
                            playersModel.setId(id);
                            playersModel.setName(name);
                            playersModel.setImage_url(image_url);
                            playersModel.setDescription(desc);
                            playersModel.setMatches_played(matches_played);
                            playersModel.setTotal_score(score);
                            playersModel.setCountry(country);
                            playersModel.setFav(false);
                            realm.commitTransaction();
                            Log.e("SUCCESS", "DATA INSERTED");
                        } else {
                            Log.e("FAILED", "DATA ALREADY THERE");
                        }
                        Log.e("DATA", id + name + image_url + score + desc + matches_played + country);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    recyclerView.setVisibility(View.INVISIBLE);
                    layout.setVisibility(View.VISIBLE);
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("TAG", "Error: " + error.getMessage());
                Toast.makeText(getActivity(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
                recyclerView.setVisibility(View.INVISIBLE);
                layout.setVisibility(View.VISIBLE);

            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }

    private void gethitCounts(String url) {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.e("RES", response.toString());
                try {
                    Toast.makeText(getActivity(),"API Hits: "+response.getString("api_hits"),Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("TAG", "Error: " + error.getMessage());
                Toast.makeText(getActivity(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }

    public void loadView() {
        Realm.setDefaultConfiguration(realmConfiguration);
        realm = Realm.getDefaultInstance();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(new AllPlayersAdapter(realmConfiguration,(MainActivity)getActivity(), realm.where(PlayersModel.class).findAllAsync()));
    }

    public void loadSortedRunData() {
        Realm.setDefaultConfiguration(realmConfiguration);
        realm = Realm.getDefaultInstance();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(new AllPlayersAdapter(realmConfiguration,(MainActivity)getActivity(), realm.where(PlayersModel.class).findAllSortedAsync("total_score", Sort.DESCENDING)));
    }
    public void loadSortedMatchData() {
        Realm.setDefaultConfiguration(realmConfiguration);
        realm = Realm.getDefaultInstance();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(new AllPlayersAdapter(realmConfiguration,(MainActivity)getActivity(), realm.where(PlayersModel.class).findAllSortedAsync("matches_played", Sort.DESCENDING)));
    }

    public void loadSearchedData(String query) {
        Log.e("Q", query);
        Realm.setDefaultConfiguration(realmConfiguration);
        realm = Realm.getDefaultInstance();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(new AllPlayersAdapter(realmConfiguration,(MainActivity)getActivity(), realm.where(PlayersModel.class).equalTo("country",query).or().equalTo("name", query).findAllAsync()));
    }

}
