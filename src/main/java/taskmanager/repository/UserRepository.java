package taskmanager.repository;

import org.springframework.data.repository.CrudRepository;

import taskmanager.entity.User;

public interface UserRepository  extends CrudRepository<User, Integer> {

}
