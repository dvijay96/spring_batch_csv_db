package com.example.batch.job.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.batch.job.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

}
