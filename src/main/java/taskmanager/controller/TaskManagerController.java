/**
 * 
 */
package taskmanager.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
import taskmanager.entity.Project;
import taskmanager.entity.Task;
import taskmanager.entity.User;
import taskmanager.model.ParentTaskTO;
import taskmanager.model.ProjectTO;
import taskmanager.model.TaskTO;
import taskmanager.model.UserTO;
import taskmanager.service.ParentTaskService;
import taskmanager.service.ProjectService;
import taskmanager.service.TaskService;
import taskmanager.service.UserService;

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
	@Autowired
	@Qualifier("ProjectService")
	ProjectService projectService;

	@Autowired
	@Qualifier("UserService")
	UserService userService;

	/**
	 * @param id
	 * @return
	 */

	/**
	 * @param bookDO
	 * @return
	 */
	private TaskTO convertEntityToModel(Task task) {

		ParentTaskTO pTaskModel = new ParentTaskTO();
		TaskTO taskModel = new TaskTO();
		if (task.getTaskId() != null) {
			taskModel.setTaskId(task.getTaskId());
		}
		taskModel.setTaskName(task.getTask());
		taskModel.setPriority(task.getPriority());
		taskModel.setStartDate(task.getStartDate());
		taskModel.setEndDate(task.getEndDate());
		taskModel.setStatus(task.getStatus());
		if (task.getParentTask() != null) {
			pTaskModel.setParentId(task.getParentTask().getParentId());
			pTaskModel.setParentTask(task.getParentTask().getParentTask());
			;
			taskModel.setParentTaskName(task.getParentTask().getParentTask());
		}
		if (task.getProjectId() != null) {

			Project project = projectService.getProject(task.getProjectId());
			taskModel.setProjectId(project.getProjectId());
			taskModel.setProjectName(project.getProject());
		}
		return taskModel;
	}

	private ParentTaskTO convertpTaskEntityToModel(ParentTask pTask) {

		ParentTaskTO pTaskModel = new ParentTaskTO();
		TaskTO taskModel = new TaskTO();
		List<TaskTO> taskModels = new ArrayList<TaskTO>();

		pTaskModel.setParentId(pTask.getParentId());
		pTaskModel.setParentTask(pTask.getParentTask());

		if (pTask.getTasks() != null) {
			for (Task task : pTask.getTasks()) {
				taskModel.setTaskId(task.getTaskId());
				taskModel.setTaskName(task.getTask());
				taskModel.setPriority(task.getPriority());
				taskModel.setStartDate(task.getStartDate());
				taskModel.setEndDate(task.getEndDate());
				if (task.getProjectId() != null) {

					Project project = projectService.getProject(task.getProjectId());
					taskModel.setProjectId(project.getProjectId());
					taskModel.setProjectName(project.getProject());
				}
				taskModel.setStatus(task.getStatus());
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
	@RequestMapping(value = "/projectmanager/tasks", produces = "application/json", method = RequestMethod.GET)
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
	@RequestMapping(value = "/projectmanager/users", produces = "application/json", method = RequestMethod.GET)
	public List<UserTO> getAllUsers() {

		List<UserTO> userModels = new ArrayList<UserTO>();
		UserTO userModel = new UserTO();
		List<User> userList = userService.getAllUser();
		if (userList != null) {
			for (User user : userList) {
				userModel = convertUserEntityToModel(user);
				userModels.add(userModel);
			}
		}

		return userModels;
	}

	
	@CrossOrigin
	@RequestMapping(value = "/projectmanager/projects", produces = "application/json", method = RequestMethod.GET)
	public List<ProjectTO> getAllProjects() {

		List<ProjectTO> projectTOs = new ArrayList<ProjectTO>();
		ProjectTO projectModel = new ProjectTO();
		List<Project> projectList = projectService.getAllProject();
		if (projectList != null) {
			for (Project project : projectList) {
				projectModel = convertProjectEntityToModel(project);
				projectTOs.add(projectModel);
			}
		}

		return projectTOs;
	}
	
	
	private ProjectTO convertProjectEntityToModel(Project project) {
		// TODO Auto-generated method stub

		Integer taskCnt = null;
		Integer compCnt = null;

		ProjectTO projectModel = new ProjectTO();
		projectModel.setProjectid(project.getProjectId());
		projectModel.setProject(project.getProject());
		projectModel.setPriority(project.getPriority());

		taskCnt= taskService.getProjectTaskCnt(project.getProjectId());
		compCnt = taskService.getCompTaskCnt(project.getProjectId(),"completed");
		projectModel.setTottsk(taskCnt);
		projectModel.setComptsk(compCnt);
		projectModel.setSdate(project.getStartDate().toString());
		projectModel.setEdate(project.getEndDate().toString());
		
		
		
		return projectModel;
	}

	private UserTO convertUserEntityToModel(User user) {

		UserTO userModel = new UserTO();

		userModel.setUserId(user.getUserId());
		userModel.setEmployeeId(user.getEmployeeId());
		userModel.setFirstName(user.getFirstName());
		userModel.setLastName(user.getLastName());

		return userModel;
	}

	@CrossOrigin
	@RequestMapping(value = "/projectmanager/tasks/sort/startdate", produces = "application/json", method = RequestMethod.GET)
	public List<TaskTO> getTasksSortByStartate() {

		List<TaskTO> taskModels = new ArrayList<TaskTO>();
		TaskTO taskModel = new TaskTO();
		List<Task> taskList = taskService.getAllTask();
		if (taskList != null) {
			for (Task task : taskList) {
				taskModel = convertEntityToModel(task);
				taskModels.add(taskModel);
			}
		}

		final List<TaskTO> tasks = taskModels.stream()
				.sorted((o1, o2) -> o1.getStartDate().compareTo(o2.getStartDate())).collect(Collectors.toList());

		return tasks;
	}

	@CrossOrigin
	@RequestMapping(value = "/projectmanager/tasks/sort/enddate", produces = "application/json", method = RequestMethod.GET)
	public List<TaskTO> getTasksSortByEndDate() {

		List<TaskTO> taskModels = new ArrayList<TaskTO>();
		TaskTO taskModel = new TaskTO();
		List<Task> taskList = taskService.getAllTask();
		if (taskList != null) {
			for (Task task : taskList) {
				taskModel = convertEntityToModel(task);
				taskModels.add(taskModel);
			}
		}

		final List<TaskTO> tasks = taskModels.stream().sorted((o1, o2) -> o1.getEndDate().compareTo(o2.getEndDate()))
				.collect(Collectors.toList());

		return tasks;
	}

	@CrossOrigin
	@RequestMapping(value = "/projectmanager/tasks/sort/priority", produces = "application/json", method = RequestMethod.GET)
	public List<TaskTO> getTasksSortByPriority() {

		List<TaskTO> taskModels = new ArrayList<TaskTO>();
		TaskTO taskModel = new TaskTO();
		List<Task> taskList = taskService.getAllTask();
		if (taskList != null) {
			for (Task task : taskList) {
				taskModel = convertEntityToModel(task);
				taskModels.add(taskModel);
			}
		}

		final List<TaskTO> tasks = taskModels.stream().sorted((o1, o2) -> o1.getPriority().compareTo(o2.getPriority()))
				.collect(Collectors.toList());

		return tasks;
	}

	@CrossOrigin
	@RequestMapping(value = "/projectmanager/tasks/sort/status", produces = "application/json", method = RequestMethod.GET)
	public List<TaskTO> getTasksSortByStatus() {

		List<TaskTO> taskModels = new ArrayList<TaskTO>();
		TaskTO taskModel = new TaskTO();
		List<Task> taskList = taskService.getAllTask();
		if (taskList != null) {
			for (Task task : taskList) {
				taskModel = convertEntityToModel(task);
				taskModels.add(taskModel);
			}
		}
		Comparator<TaskTO> taskComparatorNull = Comparator.comparing(TaskTO::getStatus,
				Comparator.nullsLast(String::compareTo));

		Collections.sort(taskModels, taskComparatorNull);

		return taskModels;
	}

	@CrossOrigin
	@RequestMapping(value = "/projectmanager/tasks/{taskname}", produces = "application/json", method = RequestMethod.GET)
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
	@RequestMapping(value = "/projectmanager/pTasks/{taskname}", produces = "application/json", method = RequestMethod.GET)
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

	@CrossOrigin
	@RequestMapping(value = "/projectmanager/tasks/{priority}", produces = "application/json", method = RequestMethod.GET)
	public ResponseEntity getTaskByPriority(@PathVariable("priority") Integer priority) {

		TaskTO taskModel = new TaskTO();
		Task task = taskService.geTaskByPriority(priority);
		taskModel = convertEntityToModel(task);
		if (taskModel != null) {

			return ResponseEntity.ok(taskModel);
		} else {

			return ResponseEntity.noContent().build();
		}
	}

	@CrossOrigin
	@RequestMapping(value = "/projectmanager/tasks/{startDate}", produces = "application/json", method = RequestMethod.GET)
	public ResponseEntity getTaskByStartDate(@PathVariable("startDate") Date startDate) {

		TaskTO taskModel = new TaskTO();
		Task task = taskService.geTaskByStartDate(startDate);
		taskModel = convertEntityToModel(task);
		if (taskModel != null) {

			return ResponseEntity.ok(taskModel);
		} else {

			return ResponseEntity.noContent().build();
		}
	}

	@CrossOrigin
	@RequestMapping(value = "/projectmanager/tasks/{endDate}", produces = "application/json", method = RequestMethod.GET)
	public ResponseEntity getTaskByEndDate(@PathVariable("endDate") Date endDate) {

		TaskTO taskModel = new TaskTO();
		Task task = taskService.geTaskByStartDate(endDate);
		taskModel = convertEntityToModel(task);
		if (taskModel != null) {

			return ResponseEntity.ok(taskModel);
		} else {

			return ResponseEntity.noContent().build();
		}
	}

	/**
	 * @param id
	 * @return
	 */

	
	@CrossOrigin
	@RequestMapping(value = "/projectmanager/project/create", consumes = "application/json", produces = "application/json", method = RequestMethod.POST)
	public ResponseEntity createProject(@RequestBody ProjectTO projectModel) {

		Project project;
		if (projectModel != null) {
			project = convertProjectModelToEntity(projectModel);

			projectService.addProject(project);
			return ResponseEntity.status(HttpStatus.CREATED).build();
		} else {

			return ResponseEntity.badRequest().build();
		}

	}
	
	
	
	private Project convertProjectModelToEntity(ProjectTO projectModel) {
		LocalDate date = LocalDate.parse(projectModel.getSdate());
		java.sql.Date sDate = java.sql.Date.valueOf(date) ;
		
		
		LocalDate date1 = LocalDate.parse(projectModel.getEdate());
		java.sql.Date EDate = java.sql.Date.valueOf(date1) ;
		
		Project project = new Project();
		Project pj = projectService.getProjectByName(projectModel.getProject());
		if(pj!=null && pj.getProjectId()!=null)
		{
		project.setProjectId(projectModel.getProjectid());
		}
		project.setProject(projectModel.getProject());
		project.setPriority(projectModel.getPriority());
		project.setStartDate(sDate);
		project.setEndDate(EDate);
		return project;
	}

	@CrossOrigin
	@RequestMapping(value = "/projectmanager/task/create", consumes = "application/json", produces = "application/json", method = RequestMethod.POST)
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
	
	@CrossOrigin
	@RequestMapping(value = "/projectmanager/ptask/create", consumes = "application/json", produces = "application/json", method = RequestMethod.POST)
	public ResponseEntity createParentTask(@RequestBody TaskTO taskModel) {

		ParentTask pTask;
		if (taskModel!=null && taskModel.getTaskName() != null) {
			pTask = convertModelToPTaskEntity(taskModel);

			pTaskService.addPTask(pTask);
			return ResponseEntity.status(HttpStatus.CREATED).build();
		} else {

			return ResponseEntity.badRequest().build();
		}

	}

	private ParentTask convertModelToPTaskEntity(TaskTO taskModel) {
		// TODO Auto-generated method stub
		ParentTask pTask = new ParentTask();
		pTask.setParentTask(taskModel.getTaskName());
		
		return pTask;
	}

	/**
	 * @param book
	 * @return
	 */
	private Task convertModelToEntity(TaskTO taskModel) {

		ParentTask pTask;
		Project project;
		Task task = new Task();

		if (taskModel.getTaskId() != null) {
			task.setTaskId(taskModel.getTaskId());
		}
		task.setTask(taskModel.getTaskName());
		task.setStartDate(taskModel.getStartDate());
		task.setEndDate(taskModel.getEndDate());
		task.setPriority(taskModel.getPriority());
		if (taskModel.getParentTaskName() != null) {
			pTask = pTaskService.getpTaskByName(taskModel.getParentTaskName());

			if (pTask != null) {
				task.setParentId(pTask.getParentId());
			} else {
				pTask = new ParentTask();
				pTask.setParentTask(taskModel.getParentTaskName());
				pTaskService.addPTask(pTask);
				getTaskParentID(task, taskModel.getParentTaskName());
			}

			if (taskModel.getProjectName() != null) {
				project = projectService.getProjectByName(taskModel.getProjectName());
				if (project != null) {
					task.setProjectId(project.getProjectId());
				}
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
	@RequestMapping(value = "/projectmanager/task/modify", produces = "application/json", method = RequestMethod.PUT)
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

	/**
	 * @param book
	 * @return
	 */
	@CrossOrigin
	@RequestMapping(value = "/projectmanager/user/add", produces = "application/json", method = RequestMethod.POST)
	public ResponseEntity addUpdateUser(@RequestBody UserTO userModel) {
		User entity = new User();

		if (userModel != null) {

			entity = convertUserModelToEntity(userModel);

			userService.addUser(entity);

			return ResponseEntity.ok().build();
		} else {

			return ResponseEntity.badRequest().build();
		}

	}

	private User convertUserModelToEntity(UserTO userModel) {

		User user = new User();
		if (userModel.getUserId() != null) {
			user.setUserId(userModel.getUserId());
		}
		user.setFirstName(userModel.getFirstName());
		user.setLastName(userModel.getLastName());
		user.setEmployeeId(userModel.getEmployeeId());
		if (userModel.getProjectId() != null) {
			user.setProjectId(userModel.getProjectId());
		}
		if (userModel.getTaskId() != null) {
			user.setTaskId(userModel.getTaskId());
		}
		return user;
	}

	@CrossOrigin
	@RequestMapping(value = "/projectmanager/users/sort/firstname", produces = "application/json", method = RequestMethod.GET)
	public List<UserTO> getUsersSortByFirstname() {

		List<UserTO> userModels = new ArrayList<UserTO>();
		UserTO userModel = new UserTO();
		List<User> userList = userService.getAllUser();
		if (userList != null) {
			for (User user : userList) {
				userModel = convertUserEntityToModel(user);
				userModels.add(userModel);
			}
		}
		final List<UserTO> users = userModels.stream()
				.sorted((o1, o2) -> o1.getFirstName().compareTo(o2.getFirstName())).collect(Collectors.toList());

		return users;
	}

	@CrossOrigin
	@RequestMapping(value = "/projectmanager/users/sort/lastname", produces = "application/json", method = RequestMethod.GET)
	public List<UserTO> getUsersSortByLastname() {

		List<UserTO> userModels = new ArrayList<UserTO>();
		UserTO userModel = new UserTO();
		List<User> userList = userService.getAllUser();
		if (userList != null) {
			for (User user : userList) {
				userModel = convertUserEntityToModel(user);
				userModels.add(userModel);
			}
		}
		final List<UserTO> users = userModels.stream().sorted((o1, o2) -> o1.getLastName().compareTo(o2.getLastName()))
				.collect(Collectors.toList());

		return users;
	}

	@CrossOrigin
	@RequestMapping(value = "/projectmanager/users/sort/id", produces = "application/json", method = RequestMethod.GET)
	public List<UserTO> getUsersSortById() {

		List<UserTO> userModels = new ArrayList<UserTO>();
		UserTO userModel = new UserTO();
		List<User> userList = userService.getAllUser();
		if (userList != null) {
			for (User user : userList) {
				userModel = convertUserEntityToModel(user);
				userModels.add(userModel);
			}
		}
		final List<UserTO> users = userModels.stream()
				.sorted((o1, o2) -> o1.getEmployeeId().compareTo(o2.getEmployeeId())).collect(Collectors.toList());

		return users;
	}

	@CrossOrigin
	@RequestMapping(value = "/projectmanager/user/delete/{userId}", method = RequestMethod.DELETE)
	public ResponseEntity deleteUser(@PathVariable("userId") Integer userId) {

		if (userId != null) {

			userService.deleteUser(userId);

			return ResponseEntity.ok().build();
		} else {

			return ResponseEntity.badRequest().build();
		}

	}
	
	
	@CrossOrigin
	@RequestMapping(value = "/projectmanager/projects/sort/sdate", produces = "application/json", method = RequestMethod.GET)
	public List<ProjectTO> getProjBySDate() {

		List<ProjectTO> projModels = getAllProjects();
		
		final List<ProjectTO> projects = projModels.stream()
				.sorted((o1, o2) -> o1.getSdate().compareTo(o2.getSdate())).collect(Collectors.toList());

		return projects;
	}
	
	
	@CrossOrigin
	@RequestMapping(value = "/projectmanager/projects/sort/edate", produces = "application/json", method = RequestMethod.GET)
	public List<ProjectTO> getProjByEDate() {

		List<ProjectTO> projModels = getAllProjects();
		
		final List<ProjectTO> projects = projModels.stream()
				.sorted((o1, o2) -> o1.getEdate().compareTo(o2.getEdate())).collect(Collectors.toList());

		return projects;
	}
	
	@CrossOrigin
	@RequestMapping(value = "/projectmanager/projects/sort/priority", produces = "application/json", method = RequestMethod.GET)
	public List<ProjectTO> getProjByPriority() {

		List<ProjectTO> projModels = getAllProjects();
		
		final List<ProjectTO> projects = projModels.stream()
				.sorted((o1, o2) -> o1.getPriority().compareTo(o2.getPriority())).collect(Collectors.toList());

		return projects;
	}
	

	@CrossOrigin
	@RequestMapping(value = "/projectmanager/projects/sort/status", produces = "application/json", method = RequestMethod.GET)
	public List<ProjectTO> getProjByStatus() {

		List<ProjectTO> projModels = getAllProjects();
		
		Comparator<ProjectTO> projComparatorNull = Comparator.comparing(ProjectTO::getComptsk,
				Comparator.reverseOrder());

		Collections.sort(projModels, projComparatorNull);

		return projModels;
	}

}
