package uk.ac.ed.notify.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SecondaryTable;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Entity
@Table(name="qrtz_triggers")
@SecondaryTable(name="qrtz_simple_triggers", pkJoinColumns = @PrimaryKeyJoinColumn(name = "trigger_name"))
public class QuartzTrigger {
	
	@JsonIgnore
	@Id
	@Column(name="trigger_name")
	public String triggerName;
	
	@Column(name="job_name")
	public String jobName;
	
	@JsonSerialize(using = MillisecondsDateSerializer.class)
	@Column(name="prev_fire_time")
	public long prevRun;
	
	@JsonSerialize(using = MillisecondsDateSerializer.class)
	@Column(name="next_fire_time")
	public long nextRun;
	
	@Column(name="trigger_state")
	public String triggerState;
	
	@Column(name="times_triggered", table="qrtz_simple_triggers")
	public int timesExecuted;

	@Column(name="repeat_interval", table="qrtz_simple_triggers")
	public int repeatInterval;

	public String getTriggerName() {
		return triggerName;
	}

	public void setTriggerName(String triggerName) {
		this.triggerName = triggerName;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public long getPrevRun() {
		return prevRun;
	}

	public void setPrevRun(long prevRun) {
		this.prevRun = prevRun;
	}

	public long getNextRun() {
		return nextRun;
	}

	public void setNextRun(long nextRun) {
		this.nextRun = nextRun;
	}

	public String getTriggerState() {
		return triggerState;
	}

	public void setTriggerState(String triggerState) {
		this.triggerState = triggerState;
	}

	public int getTimesExecuted() {
		return timesExecuted;
	}

	public void setTimesExecuted(int timesExecuted) {
		this.timesExecuted = timesExecuted;
	}

	public int getRepeatInterval() {
		return repeatInterval;
	}

	public void setRepeatInterval(int repeatInterval) {
		this.repeatInterval = repeatInterval;
	}
}