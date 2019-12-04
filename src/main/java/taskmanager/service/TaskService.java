package taskmanager.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import taskmanager.entity.Task;
import taskmanager.repository.TaskRepository;


@Service("taskService")
public class TaskService {


	@Autowired
	@Qualifier("taskRepository")
	TaskRepository taskRepository;

	@Transactional
	public List getAllTask() {
		List task = new ArrayList<Task>();
		Iterable taskIterable = taskRepository.findAll();
		Iterator taskIterator = taskIterable.iterator();
		while (taskIterator.hasNext()) {
			task.add(taskIterator.next());
		}
		return task;
	}

	@Transactional
	public Task getTask(int id) {
		return taskRepository.findOne(id);
	}
	
	@Transactional
	public void addTask(Task task) {
		taskRepository.save(task);
	}

	@Transactional
	public void updateTask(Task task) {
		taskRepository.save(task);

	}

	@Transactional
	public void deleteTask(int id) {
		taskRepository.delete(id);
	}

	@Transactional
	public Task geTaskByName(String taskName) {
		return taskRepository.findTaskByName(taskName);
	}
	

}
