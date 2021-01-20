package com.example.carbon_counter_front_end.data.logic;

import android.content.Context;
import android.net.Uri;

import com.example.carbon_counter_front_end.data.model.RequestServerForService;
import com.example.carbon_counter_front_end.data.view.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Logic class to take care of the logic for MainActivity.
 * @author Zachary Current
 */
public class MainActivityLogic {
    private RequestServerForService model;
    private MainActivity view;
    private Context context;
    private ArrayList<JSONObject> myNews;
    private int newsIndex;

    /**
     * MainActivityLogic Constructor - to take care of the mainActivity logic
     * @param view view of mainActivity
     * @param context context of mainActivity
     */
    public MainActivityLogic(MainActivity view, Context context){
        this.view = view;
        this.context = context;
        this.newsIndex = 0;
        this.myNews = new ArrayList<>();
    }

    /**
     * Set mainActivityLogic's model to take care of server contact
     * @param m model to be set of class RequestServerForService
     */
    public void setModel(RequestServerForService m) { this.model = m;}

    /**
     * Method to contact server to get all of the news items on the database
     * OnSuccess will call setMyNews(response)
     */
    public void getNews(){
        String url = "http://10.24.227.38:8080/news/all";

        model.contactServerArray(url);
    }

    /**
     * After successful getNews(), the response is sent back to this logic class.
     * The response is then added to a list to be accessed by mainActivity
     * @param response response from server containing news items, if any
     */
    public void setMyNews(JSONArray response){
        for(int i = 0; i < response.length(); i++){
            try {
                myNews.add((JSONObject) response.get(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        getImage();
    }

    /**
     * Returns news Index, used for testing purposes.
     * @return newsIndex
     */
    public int getNewsIndex(){
        return newsIndex;
    }

    /**
     * Adds a news object to logic's myNews list. Has no effect on server.
     * Used for testing purposes.
     * @param news news to be added
     */
    public void addNews(JSONObject news){
        myNews.add(news);
    }


    private void getImage(){
        try {
            String image = myNews.get(newsIndex).getString("imageTitle");
            String url = "http://10.24.227.38:8080/image/" + image;

            model.contactServerImage(url);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * When next button is pressed. Handles newsIndex and calls getImage() -
     * which contacts the server for the current news object on sends the image back on
     * a successful connection.
     */
    public void setNextImage(){
        if(newsIndex == myNews.size() - 1){
            newsIndex = 0;
        } else {
            newsIndex++;
        }

        getImage();
    }

    /**
     * When prev button is pressed. Handles newsIndex and calls getImage() -
     * which contacts the server for the current news object on sends the image back on
     * a successful connection.
     */
    public void setPrevImage(){
        if(newsIndex == 0){
            newsIndex = myNews.size() - 1;
        } else {
            newsIndex--;
        }

        getImage();
    }

    /**
     * Called when go is pressed, generates URI for the link of the current news object.
     * @return URI for current news object
     */
    public Uri getUri(){
        Uri returnUri = null;
        try {
            returnUri = Uri.parse(myNews.get(newsIndex).getString("link"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return returnUri;
    }

    /**
     * Returns title of news object at newsIndex
     * @return title of current news object
     */
    public String getTitle(){
        String title = "";

        try {
            title = myNews.get(newsIndex).getString("title");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return title;
    }

    public boolean Permissions(String role)
    {
        if(role.equals("ADMIN") || role.equals("DEV"))
        {
            return true;
        }
        else
        {
            return false;
        }

    }
}
