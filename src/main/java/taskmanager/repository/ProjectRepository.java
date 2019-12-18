package taskmanager.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import taskmanager.entity.Project;


public interface ProjectRepository extends CrudRepository<Project, Integer> {

	@Query("FROM Project WHERE project = ?1")
	Project findProjectByName(String projectName);
	
}
