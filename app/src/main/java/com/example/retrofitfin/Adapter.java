package com.example.retrofitfin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private List<Post> posts;
    private List<Post> filtred;
    onAdapterListener onClickItem;
    int sort;

    public interface onAdapterListener {
        void onItemClick(int pos, List<Post> posts);
    }

    public Adapter(List<Post> posts, onAdapterListener onClickItem, int sort) {
        this.posts = posts;
        this.onClickItem = onClickItem;
        this.sort = sort;

        filtred = new ArrayList<>();
        filtred.addAll(posts);

        sortit();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutIdForListItem = R.layout.list_item_app_info;

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(layoutIdForListItem, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return filtred.size();
    }


    public void sortit(){

        filtred.clear();

        for(Post pst : posts){
            if(sort == 0){
                filtred.add(pst);
            }
            else if(pst.getStatus().equals("open") && sort == 1){
                filtred.add(pst);
            }
            else if(pst.getStatus().equals("closed") && sort == 2){
                filtred.add(pst);
            }
            else if(pst.getStatus().equals("in_progress") && sort == 3){
                filtred.add(pst);
            }
        }

    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView time;
        TextView location;
        TextView status;

        private ViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickItem.onItemClick(getAdapterPosition(), filtred);
                }
            });
            title = itemView.findViewById(R.id.tvTitle);
            time = itemView.findViewById(R.id.tvTime);
            location = itemView.findViewById(R.id.tvLocation);
            status = itemView.findViewById(R.id.tvStatus);

        }

        void bind(int listIndex) {
            Post pst;
            pst = filtred.get(listIndex);

            title.setText(pst.getTitle());

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy  HH:mm");
            time.setText(dateFormat.format(new Date((pst.getActualTime() * 1000))));

            location.setText(pst.getLocation());
            switch (pst.getStatus()) {
                case "open":
                    status.setText(R.string.text_status_open);
                    break;
                case "closed":
                    status.setText(R.string.text_status_closed);
                    break;
                case "in_progress":
                    status.setText(R.string.text_status_in_progress);
                    break;
            }
        }
    }

}
