package taskmanager.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import taskmanager.entity.ParentTask;
import taskmanager.repository.ParentTaskRepository;

@Service("parentTaskService")
public class ParentTaskService  {

	@Autowired
	@Qualifier("parentTaskRepository")
	ParentTaskRepository pTaskRepository;

	@Transactional
	public List getAllPTask() {
		List pTask = new ArrayList<ParentTask>();
		Iterable pTaskIterable = pTaskRepository.findAll();
		Iterator pTaskIterator = pTaskIterable.iterator();
		while (pTaskIterator.hasNext()) {
			pTask.add(pTaskIterator.next());
		}
		return pTask;
	}

	@Transactional
	public ParentTask getpTask(int id) {
		return pTaskRepository.findOne(id);
	}
	
	@Transactional
	public void addPTask(ParentTask pTask) {
		pTaskRepository.save(pTask);
	}

	@Transactional
	public void updatePTask(ParentTask pTask) {
		pTaskRepository.save(pTask);

	}

	@Transactional
	public void deletePTask(int id) {
		pTaskRepository.delete(id);
	}
	
	@Transactional
	public ParentTask getpTaskByName(String taskName) {
		return pTaskRepository.findpTaskByName(taskName);
	}

	
}
