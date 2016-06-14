package uk.ac.ed.notify.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import uk.ac.ed.notify.entity.QuartzTrigger;
import uk.ac.ed.notify.repository.QuartzTriggerRepository;

@Controller
public class SupportController {
	
	@Autowired
	public QuartzTriggerRepository quartzTriggerRepository;
	
	@RequestMapping(value="/scheduled-tasks", method = RequestMethod.GET)
	@ResponseBody
	public List<QuartzTrigger> listScheduledTasks(){
		
		return quartzTriggerRepository.findAll();
	}

}
