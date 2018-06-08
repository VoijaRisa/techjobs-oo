package org.launchcode.controllers;

import org.launchcode.models.*;
import org.launchcode.models.forms.JobForm;
import org.launchcode.models.data.JobData;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

/**
 * Created by LaunchCode
 */
@Controller
@RequestMapping(value = "job")
public class JobController {

    private JobData jobData = JobData.getInstance();

    // The detail display for a given Job at URLs like /job?id=17
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(Model model, int id) {

        model.addAttribute("currentJob", jobData.findById(id));

        return "job-detail";
    }

    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String add(Model model) {
        model.addAttribute(new JobForm());
        return "new-job";
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String add(@ModelAttribute @Valid JobForm jobForm, Errors errors, Model model) {

        if (errors.hasErrors()) {
            model.addAttribute(jobForm);
            return "new-job";
        }

        //fields in jobForm are ints maybe?

        Job newJob = new Job();

        //name//
        newJob.setName(jobForm.getName());

        //Employer//
        Job currentJob = jobData.findbyEmployerId(jobForm.getEmployerId());
        Employer employer = currentJob.getEmployer();
        newJob.setEmployer(employer);

        //Location//
        currentJob = jobData.findbyLocationId(jobForm.getLocationId());
        Location location = currentJob.getLocation();
        newJob.setLocation(location);

        //PositionType//
        currentJob = jobData.findbyPositionTypeId(jobForm.getPositionTypeId());
        PositionType type = currentJob.getPositionType();
        newJob.setPositionType(type);

        //CoreCompetencies//
        currentJob = jobData.findbyCoreCompetencyId(jobForm.getCoreCompetenciesId());
        CoreCompetency skill = currentJob.getCoreCompetency();
        newJob.setCoreCompetency(skill);

        jobData.add(newJob);

        return "redirect:/job?id=" + newJob.getId(); //needs to go to newly added job

    }
}
