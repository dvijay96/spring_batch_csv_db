package com.example.batch.job.main;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.example")
public class SpringBatchCsvExampleApplication implements CommandLineRunner {

	@Autowired
	Job job;

	@Autowired
	JobLauncher jobLauncher;

	public static void main(String[] args) {
		SpringApplication.run(SpringBatchCsvExampleApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		Map<String, JobParameter> maps = new HashMap<>();
		maps.put("time", new JobParameter(new Date()));

		JobParameters jobParameters = new JobParameters(maps);

		JobExecution jobExecution = jobLauncher.run(job, jobParameters);

		System.out.println("JOB is " + jobExecution.getStatus());

	}

}
