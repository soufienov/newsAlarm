package com.freedev.soufienov.allnews;


import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by user on 19/02/2018.
 */

public class Article {

    public String author;
    public String url;
    public String title;
    public String description;
    public String urlToImage;
    public String publishedAt;
    public SourceOf source;
    public String sourceid;
    public String sourcename;
public  Article(SourceOf source,String author,String title,String description,String publishedAt,String url,String urlToImage){

    this.author=author;
    this.description=description;
    this.publishedAt=publishedAt;
    this.source=source;
    this.url=url;
    this.urlToImage=urlToImage;
    this.title=title;
}
    public  Article(String sourceid,String sourcename,String author,String title,String description,String publishedAt,String url,String urlToImage){

        this.author=author;
        this.description=description;
        this.publishedAt=publishedAt;
        this.sourceid=sourceid;
        this.sourcename=sourcename;
        this.url=url;
        this.urlToImage=urlToImage;
        this.title=title;
    }
    public  Article(JSONObject obj) throws JSONException {

        this.author=obj.getString("author");
        this.description=obj.getString("description");
        this.publishedAt=obj.getString("publishedAt");
        this.sourceid=new JSONObject(obj.getString("source")).getString("id");
        this.sourcename=new JSONObject(obj.getString("source")).getString("name");
        this.url=obj.getString("url");
        this.urlToImage=obj.getString("urlToImage");
        this.title=obj.getString("title");
    }
    public  Article(String author,String title,String publishedAt){

        this.author=author;
        this.publishedAt=publishedAt;


        this.title=title;
    }
    public class SourceOf {
        public String id;
        public String name;
    }
}
