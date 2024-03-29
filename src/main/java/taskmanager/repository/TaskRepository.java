package taskmanager.repository;

import java.util.Date;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import taskmanager.entity.Task;


@Repository("taskRepository")
public interface TaskRepository extends CrudRepository<Task, Integer> {

	@Query("FROM Task WHERE task = ?1")
	Task findTaskByName(String taskName);
	
	@Query("FROM Task WHERE priority = ?1")
	Task findTaskByPriority(Integer priority);
	
	@Query("FROM Task WHERE startDate = ?1")
	Task findTaskByStartDate(Date startDate);
	
	@Query("FROM Task WHERE endDate = ?1")
	Task findTaskByEndDate(Date endDate);
	
	@Query("SELECT COUNT(*) as taskCnt FROM Task WHERE projectId = ?1")
	Integer findProjectTaskCount(Integer projectId);
	
	@Query("SELECT COUNT(*) as CmpCnt FROM Task WHERE projectId= ?1 and status =?2")
	Integer findCompletedTaskCount(Integer projectId, String status);
	
}
