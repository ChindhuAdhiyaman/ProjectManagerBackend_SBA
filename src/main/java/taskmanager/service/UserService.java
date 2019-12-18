package taskmanager.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import taskmanager.entity.User;
import taskmanager.repository.UserRepository;

@Service("userService")
public class UserService {

	@Autowired
	@Qualifier("userRepository")
	UserRepository userRepository;
	
	@Transactional
	public List getAllUser() {
		List User = new ArrayList<User>();
		Iterable UserIterable = userRepository.findAll();
		Iterator UserIterator = UserIterable.iterator();
		while (UserIterator.hasNext()) {
			User.add(UserIterator.next());
		}
		return User;
	}

	@Transactional
	public User getUser(int id) {
		return userRepository.findOne(id);
	}
	
	@Transactional
	public void addUser(User User) {
		userRepository.save(User);
	}

	@Transactional
	public void updateUser(User User) {
		userRepository.save(User);

	}

	@Transactional
	public void deleteUser(int id) {
		userRepository.delete(id);
	}

	
}
