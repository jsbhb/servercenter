package com.zm.timetask.bussiness.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.zm.timetask.bussiness.service.TimeTaskService;
import com.zm.timetask.constants.Constants;
import com.zm.timetask.pojo.ResultModel;
import com.zm.timetask.pojo.TimeTaskModel;

/**
 * ClassName: TimeTaskController <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * date: Sep 27, 2017 11:08:55 AM <br/>
 * 
 * @author wqy
 * @version
 * @since JDK 1.7
 */
@RestController
public class TimeTaskController {

	@Resource
	TimeTaskService timeTaskService;

	@RequestMapping(value = "{version}/timetask/start", method = RequestMethod.GET)
	public ResultModel startTimeTask(@PathVariable("version") Double version, Integer id) {

		if (Constants.FIRST_VERSION.equals(version)) {

			timeTaskService.startTimeTask(id);
			return new ResultModel(true);
		}

		return null;
	}

	@RequestMapping(value = "{version}/timetask/stop", method = RequestMethod.GET)
	public ResultModel stopTimeTask(@PathVariable("version") Double version, Integer id) {

		if (Constants.FIRST_VERSION.equals(version)) {
			timeTaskService.stopTimeTask(id);
			return new ResultModel(true);
		}

		return null;
	}

	@RequestMapping(value = "{version}/timetask/update", method = RequestMethod.PUT)
	public ResultModel updateTimeTask(@PathVariable("version") Double version, TimeTaskModel model) {

		if (Constants.FIRST_VERSION.equals(version)) {
			timeTaskService.updateTimeTask(model);
			return new ResultModel(true);
		}
		return null;

	}

	@RequestMapping(value = "{version}/timetask/save", method = RequestMethod.POST)
	public ResultModel saveTimeTask(@PathVariable("version") Double version, @RequestBody TimeTaskModel model) {

		if (Constants.FIRST_VERSION.equals(version)) {
			timeTaskService.saveTimeTask(model);
			return new ResultModel(true);
		}
		return null;
	}

	@RequestMapping(value = "{version}/timetask/createActive/{centerId}/{activeId}/{startTime}/{endTime}", method = RequestMethod.POST)
	public ResultModel createActive(@PathVariable("version") Double version, @PathVariable("centerId") Integer centerId,
			@PathVariable("activeId") Integer activeId, @PathVariable("startTime") String startTime,
			@PathVariable("endTime") String endTime) {

		if (Constants.FIRST_VERSION.equals(version)) {
			timeTaskService.createActive(centerId, activeId, startTime, endTime);
			return new ResultModel(true);
		}
		return null;
	}
}
