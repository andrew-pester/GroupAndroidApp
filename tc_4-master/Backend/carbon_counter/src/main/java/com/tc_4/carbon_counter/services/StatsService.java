package com.tc_4.carbon_counter.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.tc_4.carbon_counter.databases.DailyStatsDatabase;
import com.tc_4.carbon_counter.exceptions.UserNotFoundException;
import com.tc_4.carbon_counter.models.DailyStats;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service to handle the logic when dealing with
 * statistics objects. Is made to be called from 
 * controllers.
 */
@Service
public class StatsService {
    
    @Autowired
    private DailyStatsDatabase dailyStatsDatabase;

    /**
     * Return the all daily statistics for the given user name.
     * 
     * @param username user name of the user to find stats for.
     * @return  a list of DailyStats object
     * @throws UserNotFoundException
     */
    public List<DailyStats> getUserDailyStats(String username){
        return dailyStatsDatabase.findByUsername(username);
    }

    /**
     * Return the user's stats that correspond to the date given
     * 
     * @param username user name of user to find stats for.
     * @param date the date the stats were submitted 
     * @return returns 0 or 1 dailyStats entry
     */
    public Optional<DailyStats> getUserDailyStatsByDate(String username, LocalDate date){
        return dailyStatsDatabase.findTopByUsernameAndDateOrderByIdDesc(username, date);
    }

    /**
     * Returns a list of all daily sta  ts form the last month
     * for the given user.
     * 
     * @param username user name of user to find stats for.
     * @return A list of DailyStats object
     */
    public List<DailyStats> getLastMonthUserDailyStats(String username){
        LocalDate oneMonthAgo = LocalDate.now().minusMonths(1);
        return dailyStatsDatabase.findByUsernameAndDateGreaterThanOrderByDateAsc(username, oneMonthAgo);
    }

    /**
     * Add stats to the database.
     * Required fields: username
     * If an entry already exists for this user today,
     * it will be overwritten by this new entry.
     * 
     * @param stats The new stats to add
     * @return the resulting statistics after adding to the database
     * may contain additional details.
     */
    public DailyStats addDailyStats(DailyStats stats){        
        //check if an entry already exists for this user today
        Optional<DailyStats> oldEntry = getUserDailyStatsByDate(stats.getUsername(), LocalDate.now());
        if(oldEntry.isEmpty()){
            dailyStatsDatabase.save(stats);
            return stats;
        }else{
            DailyStats oldEntryStats = oldEntry.get();
            oldEntryStats.copyFrom(stats);
            dailyStatsDatabase.save(oldEntryStats);
            return oldEntryStats;
        }
    }
    /**
     * 
     * @param username the username of the user 
     * @return true if it deletes all stats for that user otherwise throws usernotfoundexception if the user can't be found
     */
    public boolean deleteAllStatsByUser(String username){
        List<DailyStats> list = dailyStatsDatabase.findByUsername(username);
        if(list.size() == 0){
            throw new UserNotFoundException(username);
        }
        for(int i = 0; i<list.size();i++){
            dailyStatsDatabase.delete(list.get(i));
        }
        return true;
    }

}
