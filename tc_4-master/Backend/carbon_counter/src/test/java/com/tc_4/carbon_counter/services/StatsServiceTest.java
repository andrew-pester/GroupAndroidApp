package com.tc_4.carbon_counter.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.tc_4.carbon_counter.databases.DailyStatsDatabase;
import com.tc_4.carbon_counter.models.DailyStats;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Colton Glick
 */
@RunWith(SpringRunner.class)
@ContextConfiguration
public class StatsServiceTest {

    @TestConfiguration
    static class configuration{

        //system under test
        @Bean
        public StatsService statsServiceConfig(){
            return new StatsService();
        }

        //mock stats database backed by a list
        @Bean
        public DailyStatsDatabase statsDatabaseConfig(){
            return mock(DailyStatsDatabase.class);
        }
    }

    @Autowired
    private StatsService statsService;

    @Autowired
    private DailyStatsDatabase statsDatabase;  //mock database

    private List<DailyStats> statsList;    //list to hold data for mock database

    /**
     * setup mock database and define how it should behave when 
     * it is called
     * add a test entry to the database to begin
     * 
     */
    @Before
    public void setupMockObjects(){
        statsList =  new ArrayList<DailyStats>();

        //add test entry
        DailyStats testEntry = new DailyStats(Long.parseLong("123"));
        testEntry.setUsername("testUsername");
        testEntry.setDate(LocalDate.now());
        testEntry.setWater(10.44);
        testEntry.setPower(23.123);
        testEntry.setMilesDriven(30.2341);
        testEntry.setMeat(200.9887);
        testEntry.setGarbage(504.322);
        statsList.add(testEntry);

        //save
        when(statsDatabase.save((DailyStats)any(DailyStats.class)))
            .thenAnswer(x -> {
                DailyStats s = x.getArgument(0);
                if(s.getId() != null){
                    for(DailyStats stats : statsList){
                        if(stats.getId().equals(s.getId())){
                            statsList.remove(stats);
                            break;
                        }
                    }
                }
            statsList.add(s);
            return s;
        });

        //delete
        doAnswer((x) -> {
			statsList.remove(x.getArgument(0));
			return null;
		}).when(statsDatabase).delete(any());

        //findAll
        when(statsDatabase.findAll()).thenReturn(statsList);

        //findByUsername
        when(statsDatabase.findByUsername((String)any(String.class)))
        .thenAnswer(x -> {
            List<DailyStats> result = new ArrayList<>();

            for(DailyStats s : statsList){
                if(s.getUsername().equals(x.getArgument(0))){
                    result.add(s);
                }
            }
            return result;
        });

        //findById
        when(statsDatabase.findById((Long)any(Long.class)))
        .thenAnswer(x -> {
            for(DailyStats s : statsList){
                if(s.getId() == x.getArgument(0)){
                    return Optional.of(s);
                }
            }
            return Optional.empty();
        });

        //find by username and date
        when(statsDatabase.findTopByUsernameAndDateOrderByIdDesc((String)any(String.class), (LocalDate)any(LocalDate.class)))
        .thenAnswer(x -> {
            for(DailyStats s : statsList){
                if(s.getUsername().equals(x.getArgument(0)) && s.getDate().equals(x.getArgument(1))){
                    return Optional.of(s);
                }
            }
            return Optional.empty();
        });


        //find by user name and greater than date
        when(statsDatabase.findByUsernameAndDateGreaterThanOrderByDateAsc((String)any(String.class), (LocalDate)any(LocalDate.class)))
        .thenAnswer(x -> {
            List<DailyStats> result = new ArrayList<>();

            for(DailyStats s : statsList){
                if(s.getUsername().equals(x.getArgument(0)) && s.getDate().compareTo(x.getArgument(1)) >= 0){
                    result.add(s);
                }
            }

            //should sort into ascending order
            result.sort((a, b) -> a.getDate().compareTo(b.getDate()));

            return result;
        });

    }

    @Test
    public void testGetUserDailyStats(){
        assertEquals(statsList.get(0), statsService.getUserDailyStats("testUsername").get(0));
    }

    @Test
    public void testGetUserDailyStatsByDate(){
        assertEquals(statsList.get(0), statsService.getUserDailyStatsByDate("testUsername", LocalDate.now()).get());
    }

    @Test
    public void testGetLastMonthUserDailyStats(){
        DailyStats lastMonthStats = new DailyStats();
        lastMonthStats.copyFrom(statsList.get(0));
        lastMonthStats.setDate(LocalDate.now().minusMonths(1));
        statsList.add(lastMonthStats);

        List<DailyStats> result = statsService.getLastMonthUserDailyStats("testUsername");
        //reversed because in ascending order
        assertEquals(statsList.get(0), result.get(1));
        assertEquals(statsList.get(1), result.get(0));
    }

    @Test
    public void testAddDailyStats(){
        //test with existing entry
        DailyStats changedEntry = new DailyStats();
        changedEntry.copyFrom(statsList.get(0));
        changedEntry.setMeat(1234);
        changedEntry.setMilesDriven(222);

        statsService.addDailyStats(changedEntry);

        assertEquals(1234, statsList.get(0).getMeat());
        assertEquals(222, statsList.get(0).getMilesDriven());


        //test with new entry
        DailyStats newEntry = new DailyStats();
        newEntry.copyFrom(statsList.get(0));
        newEntry.setMeat(1234);
        newEntry.setDate(LocalDate.now().plusDays(4));

        statsList.remove(statsList.get(0));

        statsService.addDailyStats(newEntry);
        assertEquals(true, newEntry.equals(statsList.get(0)));
    }
}
