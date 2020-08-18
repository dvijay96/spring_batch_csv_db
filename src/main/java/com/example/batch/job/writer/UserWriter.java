package com.example.batch.job.writer;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.batch.job.entity.User;
import com.example.batch.job.repos.UserRepository;

@Component
public class UserWriter implements ItemWriter<User> {

	@Autowired
	UserRepository userRepository;
	
	private static final Logger log=LoggerFactory.getLogger(User.class);
	
	@Override
	public void write(List<? extends User> users) throws Exception {
		userRepository.saveAll(users);
		log.info("DATA SAVED IN THE DB");
	}

}
