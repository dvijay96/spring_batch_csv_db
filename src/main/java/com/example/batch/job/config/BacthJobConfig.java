package com.example.batch.job.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.example.batch.job.entity.User;
import com.example.batch.job.processor.UserProcessor;
import com.example.batch.job.writer.UserWriter;

@Configuration
@EnableBatchProcessing
public class BacthJobConfig {

	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Bean
	public Job loadToDb() {
		return jobBuilderFactory.get("CSV-DB-Job")
				.incrementer(new RunIdIncrementer())
				.start(step1())
				.build();
	}

	@Bean
	private Step step1() {
		return stepBuilderFactory.get("step1")
				.<User, User>chunk(5)
				.reader(csvReader())
				.processor(csvProcessor())
				.writer(csvWriter())
				.build();
	}

	@Bean
	private ItemReader<User> csvReader() {
		FlatFileItemReader<User> reader = new FlatFileItemReader<>();
		reader.setResource(new ClassPathResource("user.csv"));
//		reader.setLinesToSkip(1);
		reader.setName("CSV-Reader");
		reader.setLineMapper(userLineMapper());
		return reader;
	}

	@Bean
	private LineMapper<User> userLineMapper() {
		DefaultLineMapper<User> lineMapper=new DefaultLineMapper<>();
		
		DelimitedLineTokenizer tokenizer=new DelimitedLineTokenizer();
		tokenizer.setDelimiter(",");
		tokenizer.setStrict(false);
		tokenizer.setNames(new String[] {"id","name"});
		
		BeanWrapperFieldSetMapper<User> wrapper=new BeanWrapperFieldSetMapper<>();
		wrapper.setTargetType(User.class);
		
		lineMapper.setLineTokenizer(tokenizer);
		lineMapper.setFieldSetMapper(wrapper);
		
		return lineMapper;
	}

	@Bean
	private ItemWriter<User> csvWriter() {
		return new UserWriter();
	}

	@Bean
	private ItemProcessor<User, User> csvProcessor() {
		return new UserProcessor();
	}

}
