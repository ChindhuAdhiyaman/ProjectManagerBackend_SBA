package taskmanager.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import taskmanager.entity.Task;


@Repository("taskRepository")
public interface TaskRepository extends CrudRepository<Task, Integer> {

	@Query("FROM Task WHERE task = ?1")
	Task findTaskByName(String taskName);
	

}
