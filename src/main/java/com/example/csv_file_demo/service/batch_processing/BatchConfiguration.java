package com.example.csv_file_demo.service.batch_processing;

import com.example.csv_file_demo.model.Author;
import com.example.csv_file_demo.model.Book;
import com.example.csv_file_demo.repository.BookRepository;
import com.example.csv_file_demo.service.batch_processing.partition.ColumnRangePartitioner;
import lombok.AllArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.partition.PartitionHandler;
import org.springframework.batch.core.partition.support.TaskExecutorPartitionHandler;
import org.springframework.batch.integration.async.AsyncItemProcessor;
import org.springframework.batch.integration.async.AsyncItemWriter;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableBatchProcessing
@AllArgsConstructor
public class BatchConfiguration {

    private JobBuilderFactory jobBuilderFactory;

    private StepBuilderFactory stepBuilderFactory;

    private BookWriter bookWriter;

    //Step1 saving the book info in a db
    @Bean
    public FlatFileItemReader<Book> reader(){
        FlatFileItemReader<Book> itemReader = new FlatFileItemReader<>();
        itemReader.setResource(new FileSystemResource("src/main/resources/BX-Books.csv"));
        itemReader.setName("csvReader");
        itemReader.setLinesToSkip(1);
        itemReader.setLineMapper(lineMapper());
        itemReader.setStrict(false);
        return itemReader;
    }

    private LineMapper<Book> lineMapper() {
        DefaultLineMapper<Book> lineMapper = new DefaultLineMapper<>();

        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(";");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames("ISBN","Book-Title","Book-Author","Year-Of-Publication","Publisher","Image-URL-S","Image-URL-M","Image-URL-L");

        BeanWrapperFieldSetMapper<Book> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(Book.class);

        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);
        return lineMapper;
    }

    @Bean
    public BookProcessor processor(){ return new BookProcessor(); }


    //Whatever data we get here, we just apply save method from the book repository


    @Bean
    public ColumnRangePartitioner partitioner(){
        return new ColumnRangePartitioner();
    }

    @Bean
    public PartitionHandler partitionHandler(){
        TaskExecutorPartitionHandler taskExecutorPartitionHandler = new TaskExecutorPartitionHandler();
        taskExecutorPartitionHandler.setGridSize(300);
        taskExecutorPartitionHandler.setTaskExecutor(taskExecutor());
        taskExecutorPartitionHandler.setStep(slaveStep());
        return taskExecutorPartitionHandler;
    }

    @Bean
    public Step slaveStep(){
        return stepBuilderFactory.get("slave-step").<Book, Book>chunk(100)
                .reader(reader())
                .processor(processor())
                .writer(bookWriter)
                .build();

    }

    @Bean
    public Step masterStep(){
        return stepBuilderFactory.get("master-step")
                .partitioner(slaveStep().getName(), partitioner())
                .partitionHandler(partitionHandler())
                .build();

    }

    //Step2 saving the author in db
//    @Bean
//    public FlatFileItemReader<Author> authorReader(){
//        FlatFileItemReader<Author> authorItemReader = new FlatFileItemReader<>();
//        authorItemReader.setResource(new FileSystemResource("src/main/resources/BX-Books.csv"));
//        authorItemReader.setName("csvAuthorReader");
//        authorItemReader.setLinesToSkip(1);
//        authorItemReader.setLineMapper(authorLineMapper());
//        authorItemReader.setStrict(false);
//        return authorItemReader;
//    }
//
//    private LineMapper<Author> authorLineMapper() {
//        DefaultLineMapper<Author> authorLineMapper = new DefaultLineMapper<>();
//
//        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
//        lineTokenizer.setDelimiter(";");
//        lineTokenizer.setStrict(false);
//        lineTokenizer.setNames("Book-Author");
//
//        BeanWrapperFieldSetMapper<Author> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
//        fieldSetMapper.setTargetType(Author.class);
//
//        authorLineMapper.setLineTokenizer(lineTokenizer);
//        authorLineMapper.setFieldSetMapper(fieldSetMapper);
//        return authorLineMapper;
//    }

    @Bean
    public Job job(){
        return jobBuilderFactory.get("importBooks")
                .flow(masterStep())
                .end().build();
    }

    @Bean
    public ThreadPoolTaskExecutor taskExecutor(){
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(10);
        threadPoolTaskExecutor.setMaxPoolSize(40);
        threadPoolTaskExecutor.afterPropertiesSet();
        return threadPoolTaskExecutor;
    }
}
