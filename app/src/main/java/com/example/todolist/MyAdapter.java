package com.example.todolist;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private List<ListItem> listItems;
    private Context context;

    public MyAdapter(List<ListItem> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_row_all,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final ListItem listItem = listItems.get(position);

        holder.textViewNeck.setText(listItem.getNeckL());
        holder.textViewDescription.setText(listItem.getDescriptionL());
        holder.textViewStatus.setText(listItem.getStatusL());

        if (listItem.getStatusL().equals("Uncompleted")){
            holder.textViewStatus.setTextColor(Color.RED);
            holder.linearLayout.setBackgroundColor(Color.rgb(255, 204, 204));
        }else{
            holder.textViewStatus.setTextColor(Color.rgb(0, 153, 0));
            holder.linearLayout.setBackgroundColor(Color.rgb(198, 255, 179));
        }

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listItem.getStatusL().equals("Uncompleted")){
                    Intent intent = new Intent(context,SelectTask.class);
                    intent.putExtra("DesAct", listItem.getDescriptionL());
                    intent.putExtra("DateAct", listItem.getNeckL());
                    intent.putExtra("IDAct", listItem.getKegiatanL());
                    context.startActivity(intent);
                }else{
                    Intent intents = new Intent(context,FinishedTask.class);
                    intents.putExtra("DesAct", listItem.getDescriptionL());
                    intents.putExtra("DateAct", listItem.getNeckL());
                    intents.putExtra("IDAct", listItem.getKegiatanL());
                    context.startActivity(intents);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView textViewNeck;
        public TextView textViewDescription;
        public TextView textViewStatus;
        public LinearLayout linearLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            textViewNeck = (TextView) itemView.findViewById(R.id.textViewNeck);
            textViewDescription = (TextView) itemView.findViewById(R.id.textViewDescription);
            textViewStatus = (TextView) itemView.findViewById(R.id.textViewStatus);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.linearLayout);
        }
    }
}