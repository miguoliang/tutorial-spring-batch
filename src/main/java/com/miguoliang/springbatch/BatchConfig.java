package com.miguoliang.springbatch;

import javax.sql.DataSource;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

@Configuration
@EnableBatchProcessing
public class BatchConfig extends DefaultBatchConfigurer {
  
  @Autowired
  private JobBuilderFactory jobBuilderFactory;

  @Autowired
  private StepBuilderFactory stepBuilderFactory;
  
  @Bean
  public DataSource dataSource() {
      EmbeddedDatabaseBuilder embeddedDatabaseBuilder = new EmbeddedDatabaseBuilder();
      return embeddedDatabaseBuilder.addScript("classpath:org/springframework/batch/core/schema-drop-h2.sql")
              .addScript("classpath:org/springframework/batch/core/schema-h2.sql")
              .setType(EmbeddedDatabaseType.H2)
              .build();
  }
  
  @Bean
  public Job myJob() {
    return jobBuilderFactory.get("myJob")
      .start(step1())
      .next(step2())
      .build();
  }

  @Bean
  public Step step1() {
    return stepBuilderFactory.get("step1").tasklet((contribution, b) -> {
      System.out.println("Morning World!");
      return RepeatStatus.FINISHED;
    }).build();
  }

  @Bean
  public Step step2() {
    return stepBuilderFactory.get("step2").tasklet((contribution, b) -> {
      System.out.println("Goodnight World!");
      return RepeatStatus.FINISHED;
    }).build();
  }

}
