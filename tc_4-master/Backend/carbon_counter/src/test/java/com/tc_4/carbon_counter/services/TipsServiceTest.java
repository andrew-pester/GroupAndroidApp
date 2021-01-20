package com.tc_4.carbon_counter.services;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;

import com.tc_4.carbon_counter.databases.TipsDatabase;
import com.tc_4.carbon_counter.exceptions.TipNotFoundException;
import com.tc_4.carbon_counter.models.Tip;
import com.tc_4.carbon_counter.models.Tip.Category;
import com.tc_4.carbon_counter.models.Tip.Status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Andrew Pester
 */
@RunWith(SpringRunner.class)
@ContextConfiguration
public class TipsServiceTest {
    
    @TestConfiguration
    static class configuration{

        @Bean
        public TipsService tipsServiceConfig(){
            return new TipsService();
        }
        @Bean
        public TipsDatabase tipsDatabaseConfig(){
            return mock(TipsDatabase.class);
        }
    }

    @Autowired
    private TipsService tipsService;

    @Autowired
    private TipsDatabase tipsDatabase;

    private List<Tip> tipsList;

    @PostConstruct
    public void setupMockObjects(){
        tipsList = new ArrayList<Tip>();

        Tip test1 = new Tip();
        test1.setTitle("test1");
        test1.setCategory(Category.CARBON);
        test1.setBody("the boody");
        tipsList.add(test1);

        //save
        when(tipsDatabase.save((Tip)any(Tip.class)))
        .thenAnswer(x -> {
          Tip u = x.getArgument(0);
          tipsList.add(u);
          return u;
        });

        //delete
        doAnswer((x) -> {
			tipsList.remove(x.getArgument(0));
			return null;
        }).when(tipsDatabase).delete(any());

        //findByTitle
        when(tipsDatabase.findByTitle((String)any(String.class)))
        .thenAnswer(x -> {
            for(Tip u : tipsList){
                if(u.getTitle().equals(x.getArgument(0))){
                    return Optional.of(u);
                }
            }
            return Optional.empty();
        });

        //findByTitleAndStatus
        when(tipsDatabase.findByTitleAndStatus((String)any(String.class),(Status)any(Status.class)))
        .thenAnswer(x -> {
            for(Tip u : tipsList){
                if(u.getTitle().equals(x.getArgument(0))&& u.getStatus().equals(x.getArgument(1))){
                    return Optional.of(u);
                }
            }
            return Optional.empty();
        });

        //findByWorkingTitleAndStatus
        when(tipsDatabase.findByWorkingTitleAndStatus((String)any(String.class),(Status)any(Status.class)))
        .thenAnswer(x -> {
            for(Tip u : tipsList){
                if(u.getWorkingTitle().equals(x.getArgument(0))&& u.getStatus().equals(x.getArgument(1))){
                    return Optional.of(u);
                }
            }
            return Optional.empty();
        });

        //findByWorkingTitle
        when(tipsDatabase.findByWorkingTitle((String)any(String.class)))
        .thenAnswer(x -> {
            for(Tip u : tipsList){
                if(u.getWorkingTitle().equals(x.getArgument(0))){
                    return Optional.of(u);
                }
            }
            return Optional.empty();
        });

        //findByCategoryAndStatus
        when(tipsDatabase.findByCategoryAndStatus((Category)any(Category.class),(Status)any(Status.class)))
        .thenAnswer(x -> {
            List<Tip> results = new ArrayList<>();

            for(Tip s : tipsList){
                if(s.getCategory().equals(x.getArgument(0)) && s.getStatus().equals(x.getArgument(1))){
                    results.add(s);
                }
            }
            return results;
        });

        //findByStatus
        when(tipsDatabase.findByStatus((Status)any(Status.class)))
        .thenAnswer(x -> {
            List<Tip> results = new ArrayList<>();
 
            for(Tip s : tipsList){
                if(s.getStatus().equals(x.getArgument(0))){
                    results.add(s);
                }
            }
             return results;
        });

        //findById
        when(tipsDatabase.findById((Long)any(Long.class)))
        .thenAnswer(x -> {
            for(Tip s : tipsList){
                if(s.getId() == (Long) x.getArgument(0)){
                    return Optional.of(s);
                }
            }
            return Optional.empty();
        });
    }

    @Test
    public void getTipByTitle(){
        tipsService.setStatus("test1", Status.APPROVED);
        tipsList.get(0).setStatus(Status.DENIED);
        assertThrows(TipNotFoundException.class, () -> {tipsService.getTipByTitle("test2");});
        assertThrows(TipNotFoundException.class, () -> {tipsService.getTipByTitle("test1");});
        tipsList.get(0).setStatus(Status.APPROVED);
        assertEquals(tipsList.get(0), tipsService.getTipByTitle("test1"));
    }

    @Test
    public void getTipByWorkingTitle(){
        assertThrows(TipNotFoundException.class, () ->{tipsService.getTipByWorkingTitle("test2");});
        assertEquals(tipsList.get(0), tipsService.getTipByWorkingTitle("test1"));
    }

}
