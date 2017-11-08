package com.zm.timetask.pojo;

/**
 * ClassName: TimeTaskModel <br/>
 * Function: 任务调度器实体类. <br/>
 * date: Sep 26, 2017 3:20:33 PM <br/>
 * 
 * @author wqy
 * @version
 * @since JDK 1.7
 */
public class TimeTaskModel {

	private Integer id;

	private String jobName;

	private String jobGroup;

	private Integer status;// 0禁用；1启用

	private String cronExpression;//调度时间表达式

	private Integer concurrent;// 0允许并发，1不允许

	private String description;

	private String targetObject;

	private String targetMethod;

	private String createTime;

	private String updateTime;

	private String opt;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String getJobGroup() {
		return jobGroup;
	}

	public void setJobGroup(String jobGroup) {
		this.jobGroup = jobGroup;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getCronExpression() {
		return cronExpression;
	}

	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}

	public Integer getConcurrent() {
		return concurrent;
	}

	public void setConcurrent(Integer concurrent) {
		this.concurrent = concurrent;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTargetObject() {
		return targetObject;
	}

	public void setTargetObject(String targetObject) {
		this.targetObject = targetObject;
	}

	public String getTargetMethod() {
		return targetMethod;
	}

	public void setTargetMethod(String targetMethod) {
		this.targetMethod = targetMethod;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getOpt() {
		return opt;
	}

	public void setOpt(String opt) {
		this.opt = opt;
	}

	@Override
	public String toString() {
		return "TimeTaskModel [id=" + id + ", jobName=" + jobName + ", jobGroup=" + jobGroup + ", status=" + status
				+ ", cronExpression=" + cronExpression + ", concurrent=" + concurrent + ", description=" + description
				+ ", targetObject=" + targetObject + ", targetMethod=" + targetMethod + ", createTime=" + createTime
				+ ", updateTime=" + updateTime + ", opt=" + opt + "]";
	}

}
