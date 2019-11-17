package com.bankfinance.management;

import com.bankfinance.management.entities.Role;
import com.bankfinance.management.entities.User;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class ManagementApplicationTests {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	public void testUser() {
		//test add
		User user = new User("admin", "password", "first_name", "last_name", null);
		ResponseEntity<JSONObject> responseEntity = this.restTemplate
				.postForEntity("http://localhost:" + port + "/users", user, JSONObject.class);
		assertEquals(HttpStatus.CREATED.value(), responseEntity.getStatusCodeValue());

		//test unique login
		responseEntity = this.restTemplate
				.postForEntity("http://localhost:" + port + "/users", user, JSONObject.class);
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), responseEntity.getStatusCodeValue());

		// test get and update
		String newFirstName = "newFirstName";
		String newLastName = "newLastName";
		User[] users = restTemplate.getForObject("http://localhost:" + port + "/users", User[].class);
		assertNotNull(users);
		assertTrue(users.length > 0);
		user = users[0];
		user.setFirstName(newFirstName);
		user.setLastName(newLastName);
		restTemplate.put("http://localhost:" + port + "/users", user, User.class);
		users = restTemplate.getForObject("http://localhost:" + port + "/users", User[].class);
		assertNotNull(users);
		assertTrue(users.length > 0);
		user = users[0];
		assertEquals(newFirstName, user.getFirstName());
		assertEquals(newLastName, user.getLastName());

		//test delete
        restTemplate.delete("http://localhost:" + port + "/users/" + user.getId());
        users = restTemplate.getForObject("http://localhost:" + port + "/users", User[].class);
        assertEquals(0, users.length);
	}

	@Test
	public void testRole() {
		//test add
		Role role = new Role("admin");
		ResponseEntity<JSONObject> responseEntity = this.restTemplate
				.postForEntity("http://localhost:" + port + "/roles", role, JSONObject.class);
		assertEquals(HttpStatus.CREATED.value(), responseEntity.getStatusCodeValue());

		//test unique role
		responseEntity = this.restTemplate
				.postForEntity("http://localhost:" + port + "/roles", role, JSONObject.class);
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), responseEntity.getStatusCodeValue());

		//test get and update
		String newAdmin = "newAdmin";
		Role[] roles = restTemplate.getForObject("http://localhost:" + port + "/roles", Role[].class);
		assertNotNull(roles);
		assertTrue(roles.length > 0);
		role = roles[0];
		role.setName(newAdmin);
		restTemplate.put("http://localhost:" + port + "/roles", role, Role.class);
		roles = restTemplate.getForObject("http://localhost:" + port + "/roles", Role[].class);
		assertTrue(roles.length > 0);
		role = roles[0];
		assertEquals(newAdmin, role.getName());

		//test delete
		restTemplate.delete("http://localhost:" + port + "/roles/" + role.getId());
		roles = restTemplate.getForObject("http://localhost:" + port + "/roles", Role[].class);
		assertEquals(0, roles.length);
	}

	@Test
	public void testRoleToUser() {
		//add user
		User user = new User("admin", "password", "first_name", "last_name", null);
		ResponseEntity<JSONObject> responseEntity = this.restTemplate
				.postForEntity("http://localhost:" + port + "/users", user, JSONObject.class);
		assertEquals(HttpStatus.CREATED.value(), responseEntity.getStatusCodeValue());

		//get user
		User[] users = restTemplate.getForObject("http://localhost:" + port + "/users", User[].class);
		assertNotNull(users);
		assertTrue(users.length > 0);
		user = users[0];

		//add role
		Role role = new Role("admin");
		responseEntity = this.restTemplate
				.postForEntity("http://localhost:" + port + "/roles", role, JSONObject.class);
		assertEquals(HttpStatus.CREATED.value(), responseEntity.getStatusCodeValue());

		//get role
		Role[] roles = restTemplate.getForObject("http://localhost:" + port + "/roles", Role[].class);
		assertNotNull(roles);
		assertTrue(roles.length > 0);
		role = roles[0];

		//add role to user
		restTemplate.put("http://localhost:" + port + "/users/" + user.getId() + "/roles/" + role.getId(), JSONObject.class);

		//get user
		users = restTemplate.getForObject("http://localhost:" + port + "/users", User[].class);
		assertNotNull(users);
		assertTrue(users.length > 0);
		user = users[0];
		assertNotNull(user.getRoles());
		assertTrue(user.getRoles().size() > 0);

		//delete user
		restTemplate.delete("http://localhost:" + port + "/users/" + user.getId());
		users = restTemplate.getForObject("http://localhost:" + port + "/users", User[].class);
		assertEquals(0, users.length);

		//delete role
		restTemplate.delete("http://localhost:" + port + "/roles/" + role.getId());
		roles = restTemplate.getForObject("http://localhost:" + port + "/roles", Role[].class);
		assertEquals(0, roles.length);
	}
}
