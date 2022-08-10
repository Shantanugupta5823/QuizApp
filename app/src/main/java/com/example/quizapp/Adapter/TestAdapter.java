package com.example.quizapp.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizapp.R;
import com.example.quizapp.StartTestActivity;
import com.example.quizapp.DataBase.dbQuery;
import com.example.quizapp.model.TestModel;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TestAdapter extends RecyclerView.Adapter<TestAdapter.ViewHolder> {

   private List<TestModel> testList;
    public TestAdapter(List<TestModel> testList){
        this.testList = testList;
    }
    @NonNull
    @NotNull
    @Override
    public TestAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.test_item_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull TestAdapter.ViewHolder holder, int position) {
        int progress = testList.get(position).getTopScore();
        holder.setData(position,progress);
    }

    @Override
    public int getItemCount() {
        return testList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView testNumber, TopScore;
        private ProgressBar progressBar;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            testNumber = itemView.findViewById(R.id.test_number);
            TopScore = itemView.findViewById(R.id.test_score);
            progressBar = itemView.findViewById(R.id.test_progresBar);


        }

        private void setData(int position , int progress) {
                testNumber.setText("Test No. " + String.valueOf(position+1));
                TopScore.setText(String.valueOf(progress)+" %");
                progressBar.setProgress(progress);
            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(itemView.getContext(), StartTestActivity.class);
                    dbQuery.g_SelectedTestIndex = position;
                    itemView.getContext().startActivity(intent);
                }
            });
        }
    }
}
