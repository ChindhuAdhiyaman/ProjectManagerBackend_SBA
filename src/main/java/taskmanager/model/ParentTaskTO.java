package taskmanager.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;


/**
 * 
 * 
 */
@JsonInclude(Include.NON_NULL)
public class ParentTaskTO {

	
	private int parentId;

	
	private String parentTask;

	
	private List<TaskTO> tasks;

	public ParentTaskTO() {
	}

	public int getParentId() {
		return this.parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

	public String getParentTask() {
		return this.parentTask;
	}

	public void setParentTask(String parentTask) {
		this.parentTask = parentTask;
	}

	public List<TaskTO> getTasks() {
		return this.tasks;
	}

	public void setTasks(List<TaskTO> tasks) {
		this.tasks = tasks;
	}

	public TaskTO addTask(TaskTO task) {
		getTasks().add(task);
		task.setParentTask(this);

		return task;
	}

	public TaskTO removeTask(TaskTO task) {
		getTasks().remove(task);
		task.setParentTask(null);

		return task;
	}

}