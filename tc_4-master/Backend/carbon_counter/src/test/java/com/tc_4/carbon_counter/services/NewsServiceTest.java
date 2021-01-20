package com.tc_4.carbon_counter.services;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;

import com.tc_4.carbon_counter.databases.NewsDatabase;
import com.tc_4.carbon_counter.databases.UserDatabase;
import com.tc_4.carbon_counter.exceptions.UnauthorizedException;
import com.tc_4.carbon_counter.models.News;
import com.tc_4.carbon_counter.models.User;
import com.tc_4.carbon_counter.models.User.Role;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Colton Glick
 */
@RunWith(SpringRunner.class)
@ContextConfiguration
public class NewsServiceTest {

    @TestConfiguration
    static class configuration{

        //system under test
        @Bean
        public NewsService newsServiceConfig(){
            return new NewsService();
        }

        //mock stats database backed by a list
        @Bean
        public NewsDatabase newsDatabaseConfig(){
            return mock(NewsDatabase.class);
        }

        //needed for authentication to work correctly
        @Bean
        public CarbonUserDetailsService userDetails(){
           return new CarbonUserDetailsService();
        }

        //mock database backed by a list
        @Bean
        public UserDatabase userDatabaseConfig(){
            return mock(UserDatabase.class);
        }
    }

    //system under test
    @Autowired
    private NewsService newsService;

    @Autowired
    private UserDatabase userDatabase;  //mock database
    private List<User> userList;    //list to hold data for mock database

    @Autowired
    private NewsDatabase newsDatabase;  //mock database
    private List<News> newsList;    //list to hold data for mock database

    /**
     * setup mock databases and define how they should behave when called
     * add a test entries to the databases to begin
     * 
     * using postConstruct instead of before so that the mock database
     * is setup before spring boot attempts to find the test user
     * for authentication
     */
    @PostConstruct
    public void setupMockObjects(){
        newsList =  new ArrayList<News>();
        userList =  new ArrayList<User>();

        //add test users
        User testUser = new User();
        testUser.setUsername("testUser");
        testUser.setPassword("password");
        testUser.setRole(Role.USER);
        testUser.setEmail("testEmail");
        userList.add(testUser);

        User testCreator = new User();
        testCreator.setUsername("testCreator");
        testCreator.setPassword("password");
        testCreator.setRole(Role.CREATOR);
        testCreator.setEmail("testEmail");
        userList.add(testCreator);

        User testAdmin = new User();
        testAdmin.setUsername("testAdmin");
        testAdmin.setPassword("password");
        testAdmin.setRole(Role.ADMIN);
        testAdmin.setEmail("testEmail");
        userList.add(testAdmin);

        User testDev = new User();
        testDev.setUsername("testDev");
        testDev.setPassword("password");
        testDev.setRole(Role.DEV);
        testDev.setEmail("testEmail");
        userList.add(testDev);

        //add test news entry
        News testEntry = new News();
        testEntry.setTitle("test_title_1");
        testEntry.setDate(LocalDate.now());
        testEntry.setLink("test_link_1");
        testEntry.setImageTitle("test_image_title_1");
        testEntry.setBody("test_body_1");
        newsList.add(testEntry);

        //second news entry
        News testEntry2 = new News();
        testEntry2.setTitle("test_title_2");
        testEntry2.setDate(LocalDate.now());
        testEntry2.setLink("test_link_2");
        testEntry2.setImageTitle("test_image_title_2");
        testEntry2.setBody("test_body_2");
        newsList.add(testEntry2);

        //news database mock functions-------------------------------------------

        //save
        when(newsDatabase.save((News)any(News.class)))
            .thenAnswer(x -> {
                News s = x.getArgument(0);
                if(s.getId() != 0){
                    for(News stats : newsList){
                        if(stats.getId() == s.getId()){
                            newsList.remove(stats);
                            break;
                        }
                    }
                }
            newsList.add(s);
            return s;
        });

        //delete
        doAnswer((x) -> {
			newsList.remove(x.getArgument(0));
			return null;
		}).when(newsDatabase).delete(any());

        //findAll
        when(newsDatabase.findAll()).thenReturn(newsList);

        //find all order by date ascending
        when(newsDatabase.findAllByOrderByDateAsc())
        .thenAnswer(x -> {
            List<News> result = new ArrayList<>(newsList);

            //should sort into ascending order
            result.sort((a, b) -> a.getDate().compareTo(b.getDate()));

            return result;
        });

        //findByTitle
        when(newsDatabase.findByTitle((String)any(String.class)))
        .thenAnswer(x -> {
            for(News s : newsList){
                if(s.getTitle().equals(x.getArgument(0))){
                    return Optional.of(s);
                }
            }
            return Optional.empty();
        });

        //findById
        when(newsDatabase.findById((Long)any(Long.class)))
        .thenAnswer(x -> {
            for(News s : newsList){
                if(s.getId() == (Long) x.getArgument(0)){
                    return Optional.of(s);
                }
            }
            return Optional.empty();
        });

        //find By Date Greater Than Order By Date Asc
        when(newsDatabase.findByDateGreaterThanOrderByDateAsc((LocalDate)any(LocalDate.class)))
        .thenAnswer(x -> {
            List<News> result = new ArrayList<>();

            for(News s : newsList){
                if(s.getDate().compareTo(x.getArgument(1)) >= 0){
                    result.add(s);
                }
            }

            //should sort into ascending order
            result.sort((a, b) -> a.getDate().compareTo(b.getDate()));

            return result;
        });


        //user database mock functions--------------------------------------------------
        //save
        when(userDatabase.save((User)any(User.class)))
            .thenAnswer(x -> {
              User u = x.getArgument(0);
              userList.add(u);
              return u;
        });

        //delete
        doAnswer((x) -> {
			userList.remove(x.getArgument(0));
			return null;
        }).when(userDatabase).delete(any());

        //findAll
        when(userDatabase.findAll()).thenReturn(userList);

        //findByUsername
        when(userDatabase.findByUsername((String)any(String.class)))
        .thenAnswer(x -> {
            for(User u : userList){
                if(u.getUsername().equals(x.getArgument(0))){
                    return Optional.of(u);
                }
            }
            return Optional.empty();
        });

        //findById
        when(userDatabase.findById((Long)any(Long.class)))
        .thenAnswer(x -> {
            for(User u : userList){
                if(u.getId() == x.getArgument(0)){
                    return Optional.of(u);
                }
            }
            return Optional.empty();
        });

        //existsByUsername
        when(userDatabase.existsByUsername((String)any(String.class)))
        .thenAnswer(x -> {
            return userDatabase.findByUsername(x.getArgument(0)).isPresent();
        });

    }

    @Test
    public void testGetNewsByTitle(){
        assertEquals(newsList.get(0), newsService.getNewsByTitle("test_title_1"));
        assertEquals(newsList.get(1), newsService.getNewsByTitle("test_title_2"));
    }


    @Test
    @WithUserDetails("testUser")
    public void testAddNewsByUser(){
        News n = new News();
        n.setTitle("new_news_title_1");
        n.setDate(LocalDate.now());
        n.setLink("new_news_link_1");
        n.setImageTitle("new_news_image_title_1");
        n.setBody("new_news_body_1");

        //users are not authorized to add news entries
        assertThrows(UnauthorizedException.class, () -> {newsService.addNews(n);});
    }

    @Test
    @WithUserDetails("testCreator")
    public void testAddNewsByCreator(){
        News n = new News();
        n.setTitle("new_news_title_1");
        n.setDate(LocalDate.now());
        n.setLink("new_news_link_1");
        n.setImageTitle("new_news_image_title_1");
        n.setBody("new_news_body_1");
        newsService.addNews(n);

        assertEquals(n, newsList.get(2));
    }

    @Test
    @WithUserDetails("testAdmin")
    public void testAddNewsByAdmin(){
        News n = new News();
        n.setTitle("new_news_title_1");
        n.setDate(LocalDate.now());
        n.setLink("new_news_link_1");
        n.setImageTitle("new_news_image_title_1");
        n.setBody("new_news_body_1");
        newsService.addNews(n);

        assertEquals(n, newsList.get(2));
    }

    @Test
    @WithUserDetails("testDev")
    public void testAddNewsByDev(){
        News n = new News();
        n.setTitle("new_news_title_1");
        n.setDate(LocalDate.now());
        n.setLink("new_news_link_1");
        n.setImageTitle("new_news_image_title_1");
        n.setBody("new_news_body_1");
        newsService.addNews(n);

        assertEquals(n, newsList.get(2));
    }

    @Test
    @WithUserDetails("testUser")
    public void testDeleteNewsByUser(){
        //users are not authorized to delete news entries
        assertThrows(UnauthorizedException.class, () -> {newsService.deleteNews("test_title_1");});
    }

    @Test
    @WithUserDetails("testCreator")
    public void testDeleteNewsByCreator(){
        newsService.deleteNews("test_title_1");

        assertEquals(1, newsList.size());
        assertEquals("test_title_2", newsList.get(0).getTitle());
    }
    
    @Test
    @WithUserDetails("testAdmin")
    public void testDeleteNewsByAdmin(){
        newsService.deleteNews("test_title_1");

        assertEquals(1, newsList.size());
        assertEquals("test_title_2", newsList.get(0).getTitle());
    }

    @Test
    @WithUserDetails("testDev")
    public void testDeleteNewsByDev(){
        newsService.deleteNews("test_title_1");

        assertEquals(1, newsList.size());
        assertEquals("test_title_2", newsList.get(0).getTitle());
    }

    @Test
    @WithUserDetails("testUser")
    public void testGetLastNNews(){
        News n = new News();
        n.setTitle("new_news_title_1");
        n.setDate(LocalDate.now());
        n.setLink("new_news_link_1");
        n.setImageTitle("new_news_image_title_1");
        n.setBody("new_news_body_1");
        newsList.add(n);

        assertEquals(1, newsService.getLastNNews(1).size());
        assertEquals(2, newsService.getLastNNews(2).size());
        assertEquals(3, newsService.getLastNNews(3).size());
        assertEquals(3, newsService.getLastNNews(4).size());
        assertEquals(3, newsService.getLastNNews(100).size());
        assertEquals(0, newsService.getLastNNews(0).size());
        assertEquals(0, newsService.getLastNNews(-1).size());
        assertEquals(0, newsService.getLastNNews(-100).size());
    }

    @Test
    @WithUserDetails("testUser")
    public void testGetAllNews(){
        assertEquals(newsList, newsService.getAllNews());
    }
}
