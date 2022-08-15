package com.example.quizapp.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizapp.R;
import com.example.quizapp.model.RankModel;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RankAdapter extends RecyclerView.Adapter<RankAdapter.viewHolder> {

    private List<RankModel> userList;

    public RankAdapter(List<RankModel> userList) {
        this.userList = userList;
    }

    @NonNull
    @NotNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rank_item_layout,parent,false);

        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull viewHolder holder, int position) {

         String name = userList.get(position).getName();
         int score = userList.get(position).getScore();
         int rank = userList.get(position).getRank();

         holder.setData(name,score,rank);
    }

    @Override
    public int getItemCount() {
         if(userList.size() > 10){
             return 10;
         }else{
             return userList.size();
         }

    }

    public class viewHolder extends RecyclerView.ViewHolder {

        private TextView nameTV, imgtextTV, scoreTV, rankTV;

        public viewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            nameTV = itemView.findViewById(R.id.frag_ld_name);
            imgtextTV = itemView.findViewById(R.id.frag_ld_img_text);
            scoreTV = itemView.findViewById(R.id.frag_ld_score);
            rankTV = itemView.findViewById(R.id.frag_ld_rank);
        }

        private void setData(String name, int score, int rank){

            nameTV.setText(name);
            scoreTV.setText("Score : "+score);
            rankTV.setText("Rank : " + rank);
            imgtextTV.setText(name.toUpperCase().substring(0,1));

        }
    }
}
