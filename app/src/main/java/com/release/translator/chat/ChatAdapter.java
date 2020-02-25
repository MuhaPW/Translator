package com.release.translator.chat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.release.translator.R;

import java.util.ArrayList;

/**
 * Класс адаптера чата. Тут создается Layout для элементов
 * RecyclerView. Так же инициализируются элементы
 */
public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    ArrayList<MessageBlog> messageBlogs;

    public ChatAdapter(ArrayList<MessageBlog> messageBlogs) {
        this.messageBlogs = messageBlogs;
    }

    @Override
    public int getItemViewType(int position) {
        if (messageBlogs.get(position).isLeft)
            return R.layout.adapter_translator_left_item;
        else
            return R.layout.adapter_translator_right_item;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int layoutResId) {
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutResId, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setFromText(messageBlogs.get(position).getFrom_text());
        holder.setToText(messageBlogs.get(position).getTo_text());
    }

    @Override
    public int getItemCount() {
        return messageBlogs.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void setFromText(String text){
            ((TextView) itemView.findViewById(R.id.from_text)).setText(text);
        }

        public void setToText(String text){
            ((TextView) itemView.findViewById(R.id.to_text)).setText(text);
        }
    }
}
