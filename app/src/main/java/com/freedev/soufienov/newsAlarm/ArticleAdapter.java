package com.freedev.soufienov.newsAlarm;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by user on 19/02/2018.
 */

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.MyViewHolder> {
    private List<Article> articlesList;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, author, publishedAt,link;

        public MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.title);
            author = view.findViewById(R.id.author);
            link = view.findViewById(R.id.link);
            publishedAt = view.findViewById(R.id.publishedAt);
        }
    }

    public ArticleAdapter(List<Article> articlesList) {
        this.articlesList = articlesList;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.article, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Article article = articlesList.get(position);
        holder.title.setText(article.title);
        holder.link.setText(article.url);
        holder.author.setText(article.sourcename);
        holder.publishedAt.setText(article.publishedAt);
    }

    @Override
    public int getItemCount() {
        return articlesList.size();
    }
}
