/**
 * 
 */
package taskmanager.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import taskmanager.entity.ParentTask;
import taskmanager.entity.Task;
import taskmanager.model.ParentTaskTO;
import taskmanager.model.TaskTO;
import taskmanager.service.ParentTaskService;
import taskmanager.service.TaskService;

/**
 * @author
 *
 */
@RestController
public class TaskManagerController {

	@Autowired
	@Qualifier("parentTaskService")
	ParentTaskService pTaskService;

	@Autowired
	@Qualifier("TaskService")
	TaskService taskService;

	/**
	 * @param id
	 * @return
	 */
//	@RequestMapping(value = "taskmanager/task/{id}", produces = "application/json", method = RequestMethod.GET)
//	public ResponseEntity getBookById(@PathVariable("id") int id) {
//
//		Book book = null;
//		BookDO bookDO = bookService.findById(id);
//		if (bookDO != null) {
//			book = convertEntityToModel(bookDO);
//			return ResponseEntity.ok(book);
//		} else {
//
//			return ResponseEntity.noContent().build();
//		}
//
//	}
//
	/**
	 * @param bookDO
	 * @return
	 */
	private TaskTO convertEntityToModel(Task task) {

		ParentTaskTO pTaskModel = new ParentTaskTO();
		TaskTO taskModel = new TaskTO();
		if(task.getTaskId()!=null){
		taskModel.setTaskId(task.getTaskId());
		}
		taskModel.setTaskName(task.getTask());
		taskModel.setPriority(task.getPriority());
		taskModel.setStartDate(task.getStartDate());
		taskModel.setEndDate(task.getEndDate());
		if (task.getParentTask() != null) {
			pTaskModel.setParentId(task.getParentTask().getParentId());
			pTaskModel.setParentTask(task.getParentTask().getParentTask());
			;
			taskModel.setParentTask(pTaskModel);
		}
		return taskModel;
	}
	
	
	private ParentTaskTO convertpTaskEntityToModel(ParentTask pTask) {

		ParentTaskTO pTaskModel = new ParentTaskTO();
		TaskTO taskModel = new TaskTO();
		List<TaskTO> taskModels = new ArrayList<TaskTO>();
		
		pTaskModel.setParentId(pTask.getParentId());
		pTaskModel.setParentTask(pTask.getParentTask());
		
		if(pTask.getTasks()!=null) {
		for(Task task :pTask.getTasks()) {
			taskModel.setTaskId(task.getTaskId());
			taskModel.setTaskName(task.getTask());
			taskModel.setPriority(task.getPriority());
			taskModel.setStartDate(task.getStartDate());
			taskModel.setEndDate(task.getEndDate());
			taskModels.add(taskModel);		
			
		}
		pTaskModel.setTasks(taskModels);
		}
		
		
	
		return pTaskModel;
	}

	/**
	 * @return
	 */
	@CrossOrigin
	@RequestMapping(value = "/taskmanager/tasks", produces = "application/json", method = RequestMethod.GET)
	public List<TaskTO> getTasks() {

		List<TaskTO> taskModels = new ArrayList<TaskTO>();
		TaskTO taskModel = new TaskTO();
		List<Task> taskList = taskService.getAllTask();
		if (taskList != null) {
			for (Task task : taskList) {
				taskModel = convertEntityToModel(task);
				taskModels.add(taskModel);
			}
		}
		return taskModels;
	}
	
	@CrossOrigin
	@RequestMapping(value = "/taskmanager/tasks/{taskname}", produces = "application/json", method = RequestMethod.GET)
	public ResponseEntity getTaskByName(@PathVariable("taskname") String taskName) {

		TaskTO taskModel = new TaskTO();
		Task task = taskService.geTaskByName(taskName);

		taskModel = convertEntityToModel(task);
		if (taskModel != null) {

			return ResponseEntity.ok(taskModel);
		} else {

			return ResponseEntity.noContent().build();
		}
	}
	
	@CrossOrigin
	@RequestMapping(value = "/taskmanager/pTasks/{taskname}", produces = "application/json", method = RequestMethod.GET)
	public ResponseEntity getpTaskByName(@PathVariable("taskname") String taskName) {

		ParentTaskTO pTaskModel = null;
		ParentTask pTask = pTaskService.getpTaskByName(taskName);
		pTaskModel = convertpTaskEntityToModel(pTask);
		if (pTaskModel != null) {

			return ResponseEntity.ok(pTaskModel);
		} else {

			return ResponseEntity.noContent().build();
		}
	}

	/**
	 * @param id
	 * @return
	 */

	@CrossOrigin
	@RequestMapping(value = "/taskmanager/task/create", consumes = "application/json", produces = "application/json", method = RequestMethod.POST)
	public ResponseEntity createTask(@RequestBody TaskTO taskModel) {

		Task task;
		if (taskModel != null) {
			task = convertModelToEntity(taskModel);

			taskService.addTask(task);
			return ResponseEntity.status(HttpStatus.CREATED).build();
		} else {

			return ResponseEntity.badRequest().build();
		}

	}

	/**
	 * @param book
	 * @return
	 */
	private Task convertModelToEntity(TaskTO taskModel) {

		ParentTask pTask;
		Task task = new Task();
		
		if (taskModel.getTaskId() != null) {
			task.setTaskId(taskModel.getTaskId());
		}
		task.setTask(taskModel.getTaskName());
		task.setStartDate(taskModel.getStartDate());
		task.setEndDate(taskModel.getEndDate());
		task.setPriority(taskModel.getPriority());
		if (taskModel.getParentTask() != null) {
			pTask = pTaskService.getpTaskByName(taskModel.getParentTask().getParentTask());

			if (pTask != null) {
				task.setParentId(pTask.getParentId());
			} else {
				pTask = new ParentTask();
				pTask.setParentTask(taskModel.getParentTask().getParentTask());
				pTaskService.addPTask(pTask);
				getTaskParentID(task, taskModel.getParentTask().getParentTask());
			}

		}

		return task;

	}

	private Task getTaskParentID(Task task, String pTaskName) {

		ParentTask pTask = pTaskService.getpTaskByName(pTaskName);

		if (pTask != null) {
			task.setParentId(pTask.getParentId());
		}

		return task;
	}

	/**
	 * @param book
	 * @return
	 */
	@CrossOrigin
	@RequestMapping(value = "taskmanager/task/modify", produces = "application/json", method = RequestMethod.PUT)
	public ResponseEntity updateTask(@RequestBody TaskTO taskModel) {
		Task entity = new Task();

		if (taskModel != null) {
			entity = taskService.geTaskByName(taskModel.getTaskName());
			taskModel.setTaskId(entity.getTaskId());

			entity = convertModelToEntity(taskModel);

			taskService.updateTask(entity);

			return ResponseEntity.ok().build();
		} else {

			return ResponseEntity.badRequest().build();
		}

	}

}
