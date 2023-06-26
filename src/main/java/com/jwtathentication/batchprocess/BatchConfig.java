package com.jwtathentication.batchprocess;

import java.io.IOException;

import org.hibernate.validator.internal.util.stereotypes.Lazy;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.jwtathentication.entity.UserData;
import com.jwtathentication.repository.BatchProcessRepository;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

	@Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    @Lazy
    public BatchProcessRepository batchProcessRepository;
    
    // reader read data from source
    @Bean
    public FlatFileItemReader<UserData> reader() {
        return new FlatFileItemReaderBuilder<UserData>()
        .name("userDataReader")
        .resource(new ClassPathResource("user.csv"))
        .linesToSkip(1)
        .lineMapper(getLineMapper())
        .build();
        
    }
    
    
    private LineMapper<UserData> getLineMapper(){
    	DefaultLineMapper<UserData> lineMapper=new DefaultLineMapper<>();
    	DelimitedLineTokenizer lineTokenizer=new DelimitedLineTokenizer();
    	lineTokenizer.setNames(new String [] {"name","address"});
    	//lineTokenizer.setIncludedFields(new int [] {1,2});
    	BeanWrapperFieldSetMapper<UserData> fieldSetMapper=new BeanWrapperFieldSetMapper<>();
    	fieldSetMapper.setTargetType(UserData.class);
    	//fieldSetMapper.mapFieldSet(UserData.class);
    	lineMapper.setFieldSetMapper(fieldSetMapper);
    	lineMapper.setLineTokenizer(lineTokenizer);
    	
    	return lineMapper;
    }
    
    //processer
    public UserDataBatchProcess  processor() {
    	return new  UserDataBatchProcess();
    }
    
    // Creates the Writer, configuring the repository and the method that will be used to save the data into the database
    @Bean
    public RepositoryItemWriter<UserData> writer() {
        RepositoryItemWriter<UserData> iwriter = new RepositoryItemWriter<>();
        iwriter.setRepository(batchProcessRepository);
        iwriter.setMethodName("save");
        return iwriter;
    }
    
    // Executes the job, saving the data from .csv file into the database.
    @Bean
    public Job userEntryJob(JobCompletionListener listener, Step step1)
    throws Exception {

        return this.jobBuilderFactory.get("USER-DATA-ENTRY").incrementer(new RunIdIncrementer())
        .listener(listener).start(step1).build();
    }
    
    // Batch jobs are built from steps. A step contains the reader, processor and the writer.
    @Bean
    public Step step1()
    throws Exception {

        return this.stepBuilderFactory.get("step1")
        .<UserData, UserData>chunk(3)
        .reader(reader())
        .processor(processor())
        .writer(writer())
        .build();
    }
}
