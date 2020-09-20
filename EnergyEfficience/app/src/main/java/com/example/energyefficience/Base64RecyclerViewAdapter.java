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

public class Base64RecyclerViewAdapter extends  RecyclerView.Adapter<Base64RecyclerViewAdapter.SimpleTextViewViewHolder>{

    private final LayoutInflater mInflater;
    private List<String> elementsList;



    class SimpleTextViewViewHolder extends RecyclerView.ViewHolder {
        private TextView stringItemTextView = null;

        private SimpleTextViewViewHolder(View recyclerItemView) {
            super(recyclerItemView);
            stringItemTextView = recyclerItemView.findViewById(R.id.ResultItemTextView);
        }
    }


    public Base64RecyclerViewAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }
    @Override
    public SimpleTextViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View recyclerItemView = mInflater.inflate(R.layout.recyclerview_item, parent, false);
        return new SimpleTextViewViewHolder(recyclerItemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SimpleTextViewViewHolder holder, int position) {
        if(elementsList != null){
            String current = elementsList.get(position);
            holder.stringItemTextView.setText(current);
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
}
