package com.github.chibyhq.store;

import org.junit.Test;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.map.repository.config.EnableMapRepositories;
import org.springframework.test.context.junit4.SpringRunner;
import static org.junit.Assert.*;

import com.github.chibyhq.playar.model.Playlist;
import com.github.chibyhq.playar.model.User;
import com.github.chibyhq.store.model.repositories.PlaylistRepository;
import com.github.chibyhq.store.model.repositories.UserRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = "spring.main.allow-bean-definition-overriding=true")
@EnableMapRepositories
public class StoreApplicationTest {

	@Autowired
	UserRepository userRepo;
	
	@Autowired
	PlaylistRepository playlistRepo;
	
	@Test
	public void contextLoads() {
		assertEquals(1, playlistRepo.count());
		
		User john = userRepo.findOneByNickname("John");
		
		assertTrue(playlistRepo.findOneByNameAndUser("default", john).isPresent());
		
		assertFalse(playlistRepo.findOneByNameAndUser("non-existent", john).isPresent());
	}

}
