package com.example.energyefficience;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.List;

public class MergeSortRecyclerViewAdapter extends RecyclerView.Adapter<MergeSortRecyclerViewAdapter.MergeSortViewHolder> {
    private final LayoutInflater mInflater;
    private List<String> elementsList;

    public MergeSortRecyclerViewAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public MergeSortViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View recyclerItemView = mInflater.inflate(R.layout.recyclerview_sortitem, parent, false);
        return new MergeSortViewHolder(recyclerItemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MergeSortViewHolder holder, int position) {
        if(elementsList != null){
            String current = elementsList.get(position);
            holder.elementTextView.setText(current);
            holder.elementIDTextView.setText(String.valueOf(position));
        }else{
            //data not ready yet
        }
    }

    @Override
    public int getItemCount() {
        if(elementsList != null){
            return elementsList.size();
        }else
            return 0;
    }
    void setElementsList(List<String> elementsList){
        this.elementsList = elementsList;
        notifyDataSetChanged();
    }

    class MergeSortViewHolder extends RecyclerView.ViewHolder {
        private TextView elementTextView = null;
        private TextView elementIDTextView = null;

        private MergeSortViewHolder(View recyclerItemView) {
            super(recyclerItemView);
            elementIDTextView = recyclerItemView.findViewById(R.id.SortItemIDTextView);
            elementTextView = recyclerItemView.findViewById(R.id.SortItemTextView);
        }
    }
}
