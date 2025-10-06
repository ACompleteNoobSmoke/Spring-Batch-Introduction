package com.acompletenoobsmoke.springbatch;

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

@SpringBootApplication
public class SpringBatchApplication {

    @Bean
    protected FlatFileItemReader<String> reader() {
        return new FlatFileItemReaderBuilder<String>()
                .resource(new ClassPathResource("customers-100.csv"))
                .name("csv-reader")
                .lineMapper((line, lineNumber) -> line)
                .build();
    }

    @Bean
    protected FlatFileItemWriter<String> writer() {
        String fileLocation = "src/main/resources/masked-customers-data.csv";
        return new FlatFileItemWriterBuilder<String>()
                .name("csv-writer")
                .resource(new FileSystemResource(fileLocation))
                .lineAggregator(item -> item)
                .build();
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringBatchApplication.class, args);
        System.out.println("This is a Spring Batch Application");
    }

}
