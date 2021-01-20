package com.tc_4.carbon_counter.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.tc_4.carbon_counter.databases.NewsDatabase;
import com.tc_4.carbon_counter.exceptions.UnauthorizedException;
import com.tc_4.carbon_counter.models.News;
import com.tc_4.carbon_counter.models.User;
import com.tc_4.carbon_counter.models.User.Role;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NewsService {

    @Autowired
    NewsDatabase newsDatabase;

    /**
     * Add news objects to the database.
     * 
     * @param news The news to add
     * @return The News object
     */
    public News addNews(News news) {
        if(!User.checkPermission(Role.CREATOR)){
            throw new UnauthorizedException("You don't have authorization to add news items.");
        }
        if (news.getDate() == null) {
            news.setDate(LocalDate.now());
        }
        return newsDatabase.save(news);
    }

    /**
     * Delete a news item from the database based on its title.
     * @param title The title of the news item to delete
     * @return the news item that was deleted
     */
    public News deleteNews(String title){
        if(!User.checkPermission(Role.CREATOR)){
            throw new UnauthorizedException("You don't have authorization to delete news items.");
        }

        News deletedItem = newsDatabase.findByTitle(title).get();
        newsDatabase.delete(deletedItem);
        return deletedItem;
    }

    /**
     * Retrieve a news object by its title.
     * Returns null if not news object is found.
     * 
     * @param title
     * @return The news object.
     */
    public News getNewsByTitle(String title) {
        Optional<News> item = newsDatabase.findByTitle(title);
        if(item.isPresent()){
            return item.get();
        }else{
            return null;
        }
    }

    /**
     * get a list of news objects that happened after a certain date
     * List is sorted by date past to present.
     * 
     * @param date
     * @return a list of news objects
     */
    public List<News> getNewsAfterDate(LocalDate date) {
        return newsDatabase.findByDateGreaterThanOrderByDateAsc(date);
    }

    /**
     * Get the last n news items. If n is greater than
     * the number of news items in the database, will
     * return less than n items in the list.
     * List is sorted by date past to present.
     * 
     * @param n the number of news items to get
     * @return a list of news items up to n in size
     */
    public List<News> getLastNNews(int n) {
        List<News> all = newsDatabase.findAllByOrderByDateAsc();
        List<News> result = new ArrayList<News>();
        for (int i = Math.max(all.size() - n, 0); i < all.size(); i++) {
            result.add(all.get(i));
        }
        return result;
    }

    /**
     * @return all news objects stored in the database
     * List is sorted by date past to present.
     */
    public List<News> getAllNews(){
        return newsDatabase.findAllByOrderByDateAsc();
    }
}
