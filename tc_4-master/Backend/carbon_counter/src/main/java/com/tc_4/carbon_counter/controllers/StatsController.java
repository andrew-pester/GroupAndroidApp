package com.tc_4.carbon_counter.controllers;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.tc_4.carbon_counter.models.DailyStats;
import com.tc_4.carbon_counter.services.StatsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The statistics controller provides an API to handle commands
 * related to adding, removing, and viewing a user's statistics.
 * This controller deals with all mappings beginning with /stats/
 * 
 * @see StatsService
 * 
 * @author Colton Glick
 * @author Andrew Pester
 */
@RestController
public class StatsController {

    @Autowired
    private StatsService statsService;

    /**
     * Returns an array containing the daily stats for the specified user
     * or an empty array if no stats are found for the user
     * 
     * @param username pass as a path variable
     * @return A JSON Array
     */
    @GetMapping("/stats/{username}")
    public List<DailyStats> getUserDailyStats(@PathVariable String username){
       return statsService.getUserDailyStats(username);
    }

     /**
     * Returns the daily stats for the specified user on the specified day
     * 
     * @param username Pass as a path variable
     * @param date Enter date as a path variable in the format "yyyy-MM-dd"
     * @return The most recent Daily stats entry for that day or null if none exists
     */
    @GetMapping("/stats/{date}/{username}")
    public Optional<DailyStats> getUserDailyStatsByDate(@PathVariable String username, @PathVariable String date){
        return statsService.getUserDailyStatsByDate(username, LocalDate.parse(date));
    }

    /**
     * Returns the daily stats that have been entered for today, or 
     * null if no stats have been entered yet.
     * 
     * @param username Provide as a path variable
     * @return a JSON object if a stats entry exists, if not returns null
     */
    @GetMapping("/stats/today/{username}")
    public Optional<DailyStats> getUserDailyStatsToday(@PathVariable String username){
        return statsService.getUserDailyStatsByDate(username, LocalDate.now());
    }


    /**
     * Returns an array of daily stats for dates that are grater than 
     * the current date - 1 month.
     * EX: If today is 2020-10-31, would return everything on 2020-09-30
     * and after
     * 
     * @param username pass as a path variable
     * @return A JSON array of the daily stats from the last month
     */
    @GetMapping("/stats/lastMonth/{username}")
    public List<DailyStats> getLastMonthUserDailyStats(@PathVariable String username){        
        return statsService.getLastMonthUserDailyStats(username);
    }


    /**
     * Add a daily statistic for the specified user in the json body.
     * Required fields: username.
     * If an entry already exists for this user today,
     * it will be overwritten by this new entry.
     * 
     * @param dailyStats Pass the statistics via the JSON body, the only required
     *      field is the username to assign the stats to.
     * @return The daily stats object if it was successfully saved to the database.
     */
    @PostMapping("/stats/addDaily")
    public DailyStats addDailyStats(@RequestBody DailyStats dailyStats){
        return statsService.addDailyStats(dailyStats);
    }

    @RequestMapping("/stats/delete/{username}")
    public Boolean deleteDailyStats(@PathVariable String username){
        return statsService.deleteAllStatsByUser(username);
    }
    
}
