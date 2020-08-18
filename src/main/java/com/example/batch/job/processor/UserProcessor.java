package com.example.batch.job.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.example.batch.job.entity.User;

@Component
public class UserProcessor implements ItemProcessor<User, User> {

	private static final Logger log=LoggerFactory.getLogger(User.class);
	
	@Override
	public User process(User user) throws Exception {
		String userNameUpperCase = user.getName().toUpperCase();
		user.setName(userNameUpperCase);
		
		log.info("DATA PROCESSED => USER NAME  LowerC TO UpperC");
		return user;
	}

}
