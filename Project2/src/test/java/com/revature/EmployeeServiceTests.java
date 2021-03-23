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
	
//	@Test
//	void displayInternalDirectory() {
//		Key ky = new Key();
//		User u1 = new User(); User u2 = new User(); User u3 = new User();
//		User u4 = new User(); User u5 = new User(); User u6 = new User();
//		Optional<List<User>> dirEmp = Optional.ofNullable(new ArrayList<User>()); dirEmp.add(u1); dirEmp.add(u3); dirEmp.add(u5);
//		Optional<List<User>> dirAdm = Optional.ofNullable(new ArrayList<User>()); dirEmp.add(u2); dirEmp.add(u4); dirEmp.add(u6);
//		Mockito.doReturn(dirEmp).when(userDAO).findAllByAccesslevel("Employee");
//		Mockito.doReturn(dirAdm).when(userDAO).findAllByAccesslevel("Admin");
//		//dirEmp = Stream.concat(dirEmp.stream(), dirAdm.stream()).collect(Collectors.toList());
//		Assertions.assertEquals(dirEmp, eServ.displayInternalDirectory(ky));
//	}
}
