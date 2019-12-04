package taskmanager.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import taskmanager.entity.ParentTask;


@Repository("parentTaskRepository")
public interface ParentTaskRepository extends CrudRepository<ParentTask, Integer> {

	@Query("FROM ParentTask WHERE parentTask = ?1")
	ParentTask findpTaskByName(String taskName);
	
}
