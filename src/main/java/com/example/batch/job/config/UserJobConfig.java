package com.example.batch.job.config;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.example.batch.job.model.User;
import com.example.batch.job.processor.UserProcessor;

@Configuration
@EnableBatchProcessing
public class UserJobConfig {

	@Autowired
	JobBuilderFactory jobBuilderFactory;

	@Autowired
	StepBuilderFactory stepBuilderFactory;
	
	@Autowired
	private DataSource dataSource;

	@Bean(name="CSV-DB-Job")
	public Job loadToDb() {
		return jobBuilderFactory.get("CSV-DB-Job")
				.incrementer(new RunIdIncrementer())
				.start(step1())
				.build();
	}

	@Bean
	public Step step1() {
		return stepBuilderFactory.get("step1")
				.<User, User>chunk(5)
				.reader(csvReader())
				.processor(csvProcessor())
				.writer(csvWriter())
				.build();
	}

	@Bean
	public ItemReader<User> csvReader() {
		FlatFileItemReader<User> reader = new FlatFileItemReader<>();
		reader.setResource(new ClassPathResource("user.csv"));
//		reader.setLinesToSkip(1);
		reader.setName("CSV-Reader");
		reader.setLineMapper(userLineMapper());
		return reader;
	}

	@Bean
	public LineMapper<User> userLineMapper() {
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
	public ItemWriter<User> csvWriter() {
		JdbcBatchItemWriter<User> jdbcWriter = new JdbcBatchItemWriter<>();
		jdbcWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<User>());
		jdbcWriter.setSql("INSERT into User values (:id,:name)");
		jdbcWriter.setDataSource(dataSource);
		return jdbcWriter;
	}

	@Bean
	public ItemProcessor<User, User> csvProcessor() {
		return new UserProcessor();
	}

}
