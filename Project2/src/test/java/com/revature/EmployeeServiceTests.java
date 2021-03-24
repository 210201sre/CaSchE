package com.revature;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import com.revature.exceptions.UserNotFoundException;
import com.revature.models.Key;
import com.revature.models.User;
import com.revature.repositories.UserDAO;
import com.revature.services.EmployeeService;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTests {

	@Mock
	UserDAO userDAO;
	
	@InjectMocks
	EmployeeService eServ;
	
	
	
	@Test
	void displayInternalDirectory() {
		Key ky = new Key();
		User u1 = new User(); User u2 = new User(); User u3 = new User();
		User u4 = new User(); User u5 = new User(); User u6 = new User();
		List<User> dirEmp = new ArrayList<User>(); dirEmp.add(u1); dirEmp.add(u3); dirEmp.add(u5);
		List<User> dirAdm = new ArrayList<User>(); dirAdm.add(u2); dirAdm.add(u4); dirAdm.add(u6);		
		Optional<List<User>> dirEmpO; Optional<List<User>> dirAdmO;
		dirEmpO= Optional.of(dirEmp); dirAdmO= Optional.of(dirAdm);	
		Mockito.when(userDAO.findAllByAccesslevel("Employee")).thenReturn(dirEmpO);
		Mockito.when(userDAO.findAllByAccesslevel("Admin")).thenReturn(dirAdmO);
		List<User> dir1 = Stream.concat(dirEmp.stream(), dirAdm.stream()).collect(Collectors.toList());
		List<User> dir2 = new ArrayList<>();
		for (User u : dir1) {
			u.setPswrd(null);
			u.setUname(null);
			dir2.add(u);
		}
		Assertions.assertEquals(dir2, eServ.displayInternalDirectory(ky));
	}
}
