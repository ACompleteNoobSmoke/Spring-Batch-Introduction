package com.acompletenoobsmoke.springbatch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

@SpringBootApplication
public class SpringBatchApplication {

    @Bean
    protected FlatFileItemReader<String> reader() {
        System.out.println("Using this reader -- SWAMP IZZO");
        return new FlatFileItemReaderBuilder<String>()
                .resource(new ClassPathResource("customers-100.csv"))
                .name("csv-reader")
                .lineMapper((line, lineNumber) -> line)
                .build();
    }

    @Bean
    protected FlatFileItemWriter<String> writer() {
        System.out.println("Using this writer -- SWAMP IZZO");
        String fileLocation = "src/main/resources/masked-customers-data.csv";
        return new FlatFileItemWriterBuilder<String>()
                .name("csv-writer")
                .resource(new FileSystemResource(fileLocation))
                .lineAggregator(item -> item)
                .build();
    }

    @Bean
    protected Step maskingStep(JobRepository jobRepo, PlatformTransactionManager manager,
                               FlatFileItemReader<String> reader1, TextItemProcessor processor,
                               FlatFileItemWriter<String> writer) {
        System.out.println("Using this step -- SWAMP IZZO");
        return new StepBuilder("masking-step", jobRepo)
                .<String, String> chunk(2, manager)
                .reader(reader1)
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Bean
    protected Job maskingJob(JobRepository jobRepository, Step maskingStep, BatchJobCompletedListener jobCompleted) {
        System.out.println("Using this job -- SWAMP IZZO");
        return new JobBuilder("masking-job", jobRepository)
                .start(maskingStep)
                .listener(jobCompleted)
                .build();
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringBatchApplication.class, args);
        System.out.println("This is a Spring Batch Application");
    }

}
