package com.example.note.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.note.Model.Note;
import com.example.note.NoteClikListener;
import com.example.note.R;

import java.util.List;

public class NoteListAdapter extends RecyclerView.Adapter<NoteViewHolder>{
    Context context;
    List<Note> list;
    NoteClikListener listener;

    public NoteListAdapter(Context context, List<Note> list, NoteClikListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }


    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NoteViewHolder(LayoutInflater.from(context).inflate(R.layout.note_list, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        holder.textView_title.setText(list.get(position).getTitle());
        holder.textView_title.setSelected(true);

        holder.textView_note.setText(list.get(position).getNote());

        holder.textView_date.setText(list.get(position).getDate());
        holder.textView_date.setSelected(true);

        if (list.get(position).isPinned()){
            holder.imageView_pin.setImageResource(0);
        }

        holder.note_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(list.get(holder.getAdapterPosition()));
            }
        });
        
        holder.note_container.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                listener.onLongClick(list.get(holder.getAdapterPosition()), holder.note_container);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void filterList(List<Note> filteredList){
        list = filteredList;
        notifyDataSetChanged();

    }
}
class NoteViewHolder extends RecyclerView.ViewHolder {


    CardView note_container;
    TextView textView_title, textView_note, textView_date;
    ImageView imageView_pin;

    public NoteViewHolder(@NonNull View itemView) {
        super(itemView);
        note_container = itemView.findViewById(R.id.note_container);
        textView_title = itemView.findViewById(R.id.textView_title);
        textView_note = itemView.findViewById(R.id.textView_note);
        textView_date = itemView.findViewById(R.id.textView_date);


    }
}