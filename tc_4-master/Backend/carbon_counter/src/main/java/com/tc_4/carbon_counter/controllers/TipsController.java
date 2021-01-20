package com.tc_4.carbon_counter.controllers;

import java.util.List;

import com.tc_4.carbon_counter.models.Tip;
import com.tc_4.carbon_counter.models.Tip.Category;
import com.tc_4.carbon_counter.models.Tip.Status;
import com.tc_4.carbon_counter.services.TipsService;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Andrew Pester
 */
@RestController
public class TipsController {

    @Autowired
    private TipsService tipsService;

    /**
     * 
     * @param title the title of the tip
     * @return  the tip with that title
     */
    @GetMapping("/tip/{title}")
    public Tip getTipByTitle(@PathVariable String title){
        //DONE
        return tipsService.getTipByTitle(title);
    }
    /**
     * this is for admins and requires authentication 
     * @param title the working title of the tip
     * @return the tip with that working title
     */
    @GetMapping("/tip/{title}/admin")
    public Tip getTipByWorkingTitle(@PathVariable String title){
        //DONE
        return tipsService.getTipByWorkingTitle(title);
    }
    /**
     * 
     * @param category the category 
     * @return all the approved tips in that category
     */
    @GetMapping("/tips/{category}")
    public List<Tip> getTipsByCategory(@PathVariable Category category){
        //DONE
        return tipsService.getTipsByCategory(category);
    }
    /**
     * 
     * @param newTip must be in a JSON format and send a title, body and category that are all non-null and the title must be unique
     * @return the new tip
     */
    @RequestMapping("/tip/addTip")
    public Tip addTip(@RequestBody String newTip){
        //DONE
        return tipsService.addTip(newTip);
    }
    /**
     * this is for admins and requires authentication 
     * @param title the title of the tip to be edited
     * @param edit the edited version of the tip in JSON format
     * @return the edited tip
     */
    @RequestMapping("/tip/editTip/{title}")
    public Tip editTip(@PathVariable String title, @RequestBody String edit)
    {       
        //DONE
        return tipsService.editTip(title, edit);
    }
    /**
     * this is for admins and requires authentication 
     * @param title the working title of the tip
     * @param newStatus the new status of the tip either APPROVED, PENDING, DENIED or EDITING
     * @return the tip
     */
    @RequestMapping("/tip/setStatus/{title}")
    public String setStatus(@PathVariable String title, @RequestParam Status newStatus){
        //DONE
        return tipsService.setStatus(title, newStatus);
    }
    /**
     * this is for admins and requires authentication 
     * @return a list of all tips that are not strictly approved
     */
    @GetMapping("/tips/all/admin")
    public List<Tip> allUnapprovedTips(){
        //DONE
        //returns all unapproved tips in List
        return tipsService.allTips();
    }
    /**
     * 
     * @return the list of all approved tips
     */
    @GetMapping("/tips/all")
    public List<Tip> allApprovedTips(){
        //DONE
        //returns all approved tips in List
        return tipsService.allTipsApproved();
    }
    /**
     * this is for admins and requires authentication 
     * @param title the working title of the tip to be deleted
     * @return true if it is deleted else returns false
     */
    @RequestMapping("/tip/delete/{title}")
    public String deleteTip(@PathVariable String title){
        //DONE
        return tipsService.deleteTipByWorkingTitle(title);
    }    
}
