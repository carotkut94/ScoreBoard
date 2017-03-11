package net.carotkut.scoreboard;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmRecyclerViewAdapter;

public class AllPlayersAdapter extends RealmRecyclerViewAdapter<PlayersModel, AllPlayersAdapter.MyViewHolder> {

    Realm realm;
    RealmConfiguration realmConfiguration;
    private MainActivity activity;
    public AllPlayersAdapter(RealmConfiguration configuration, MainActivity activity, OrderedRealmCollection<PlayersModel> data) {
        super(activity ,data, true);
        this.activity = activity;
        realmConfiguration = configuration;
        Realm.setDefaultConfiguration(configuration);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.custom_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.player_name.setText(getData().get(position).getName());
        if(getData().get(position).isFav)
        {
            holder.setItFav.setChecked(true);
        }else
        {
            holder.setItFav.setChecked(false);
        }

        holder.setItFav.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                if(isChecked)
                {
                    getData().get(position).setFav(true);
                    Toast.makeText(context, "Added to favourites", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    getData().get(position).setFav(false);
                    Toast.makeText(context, "Removed from favourites", Toast.LENGTH_SHORT).show();
                }
                realm.commitTransaction();
            }
        });

        holder.player_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Choose a desired style.");
                builder.setNegativeButton("View 1", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startDetailView1(context, position);
                    }
                });
                builder.setPositiveButton("View 2", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startDetailView2(context, position);
                    }
                });
                builder.show();
            }
        });

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Choose a desired style.");
                builder.setNegativeButton("View 1", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startDetailView1(context, position);
                    }
                });
                builder.setPositiveButton("View 2", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startDetailView2(context, position);
                    }
                });
                builder.show();
            }
        });
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView player_name;
        public CheckBox setItFav;
        public RelativeLayout relativeLayout;
        public MyViewHolder(View view) {
            super(view);
            this.setIsRecyclable(false);
            player_name = (TextView) view.findViewById(R.id.player_name);
            setItFav = (CheckBox) view.findViewById(R.id.isfave);
            relativeLayout = (RelativeLayout) view.findViewById(R.id.relativeContainer);

        }

    }
    public void startDetailView1(Context context, int position)
    {
        Intent intent = new Intent(context,ShowDetails.class);
        intent.putExtra("name", getData().get(position).getName());
        intent.putExtra("runs", getData().get(position).getTotal_score());
        intent.putExtra("match", getData().get(position).getMatches_played());
        intent.putExtra("desc", getData().get(position).getDescription());
        intent.putExtra("image", getData().get(position).getImage_url());
        intent.putExtra("country", getData().get(position).getCountry());
        intent.putExtra("isFavourite", getData().get(position).isFav());
        context.startActivity(intent);
    }
    public void startDetailView2(Context context, int position)
    {
        Intent intent = new Intent(context,UiVersionSecond.class);
        intent.putExtra("name", getData().get(position).getName());
        intent.putExtra("runs", getData().get(position).getTotal_score());
        intent.putExtra("match", getData().get(position).getMatches_played());
        intent.putExtra("desc", getData().get(position).getDescription());
        intent.putExtra("image", getData().get(position).getImage_url());
        intent.putExtra("country", getData().get(position).getCountry());
        intent.putExtra("isFavourite", getData().get(position).isFav());
        context.startActivity(intent);
    }

}