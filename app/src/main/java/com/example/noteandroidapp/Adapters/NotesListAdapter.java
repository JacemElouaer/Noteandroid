package com.example.noteandroidapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.noteandroidapp.Models.Notes;
import com.example.noteandroidapp.NotesClickListener;
import com.example.noteandroidapp.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


 //Adapter is a bridge between UI component and data source that helps us to fill data in UI component.
 // It holds the data and send the data to an Adapter view then view can takes the data from the adapter view and shows the data on different views like as ListView,
 // GridView, Spinner etc. For more customization in Views we uses the base adapter or custom adapters.
 // Convert data item into view item so it can be displayed in  ui component .
public class NotesListAdapter extends RecyclerView.Adapter<NoteViewHolder>{
    Context context;
    List<Notes> list;
    NotesClickListener listener;


    public NotesListAdapter(Context context, List<Notes> list, NotesClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NoteViewHolder(LayoutInflater.from(context).inflate(R.layout.notes_list , parent ,false));
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
            holder.textView_title.setText(list.get(position).getTitle());
            holder.textView_title.setSelected(true);

            holder.textview_notes.setText(list.get(position).getNotes());

            holder.textview_Date.setText(list.get(position).getDate());
            holder.textview_Date.setSelected(true);

            if(list.get(position).isPinned()){
                holder.image_pin.setImageResource(R.drawable.ic_pin);
            }
            else{
                holder.image_pin.setImageResource(0);
            }
            int color_code  =  getRandomColor();
            // cardView we use setCardBackgroundColor
            holder.notes_container.setCardBackgroundColor(holder.itemView.getResources().getColor(color_code, null));
            System.out.println(color_code);

            holder.notes_container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onClick(list.get(holder.getAdapterPosition()));
                }
            });
            holder.notes_container.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    listener.onLongClick(list.get(holder.getAdapterPosition()),  holder.notes_container);
                    return true;
                }
            });
    }
    private int getRandomColor(){
        List<Integer> colorCode =  new ArrayList<>();
        colorCode.add(R.color.color1);
        colorCode.add(R.color.color2);
        colorCode.add(R.color.color3);
        colorCode.add(R.color.color4);
        colorCode.add(R.color.color5);

        Random  random =  new Random();
        int random_color = colorCode.get(random.nextInt(colorCode.size()));
        return  random_color;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public  void filterList(List<Notes> filteredList){
        list =  filteredList;
        notifyDataSetChanged();
    }

}

class NoteViewHolder extends RecyclerView.ViewHolder{

    CardView notes_container;
    TextView textView_title,textview_notes, textview_Date;
    ImageView image_pin;
    public NoteViewHolder(@NonNull View itemView) {
        super(itemView);
     notes_container =  itemView.findViewById(R.id.notes_container);
     textView_title =  itemView.findViewById(R.id.textView_title);
     textview_notes =  itemView.findViewById(R.id.textview_notes);
     textview_Date =  itemView.findViewById(R.id.textview_Date);
     image_pin =  itemView.findViewById(R.id.image_pin);

    }
}