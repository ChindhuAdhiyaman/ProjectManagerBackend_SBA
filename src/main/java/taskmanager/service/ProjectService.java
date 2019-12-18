package taskmanager.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import taskmanager.entity.Project;
import taskmanager.repository.ProjectRepository;



@Service("projectService")
public class ProjectService {

	@Autowired
	@Qualifier("projectRepository")
	ProjectRepository projectRepository;
	
	@Transactional
	public List getAllProject() {
		List project = new ArrayList<Project>();
		Iterable projectIterable = projectRepository.findAll();
		Iterator projectIterator = projectIterable.iterator();
		while (projectIterator.hasNext()) {
			project.add(projectIterator.next());
		}
		return project;
	}

	@Transactional
	public Project getProject(int id) {
		return projectRepository.findOne(id);
	}
	
	@Transactional
	public void addProject(Project project) {
		projectRepository.save(project);
	}

	@Transactional
	public void updateProject(Project project) {
		projectRepository.save(project);

	}

	@Transactional
	public void deleteProject(int id) {
		projectRepository.delete(id);
	}

	@Transactional
	public Project getProjectByName(String projectName) {
		return projectRepository.findProjectByName(projectName);
	}
	
	
	
}
