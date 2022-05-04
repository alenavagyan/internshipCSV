package com.example.batch_dem0.service.configuration;

import com.example.batch_dem0.model.Author;
import com.example.batch_dem0.model.Image;
import com.example.batch_dem0.repository.AuthorRepository;
import com.example.batch_dem0.repository.ImageRepository;
import lombok.AllArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.support.SimpleFlow;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableBatchProcessing
@AllArgsConstructor
public class BatchConfiguration {

    private JobBuilderFactory jobBuilderFactory;

    private StepBuilderFactory stepBuilderFactory;

    private AuthorRepository bookRepository;

    private ImageRepository imageRepository;

    //Step1 saving the book info in a db
    @Bean
    public FlatFileItemReader<Author> reader(){
        FlatFileItemReader<Author> itemReader = new FlatFileItemReader<>();
        itemReader.setResource(new FileSystemResource("src/main/resources/BX-Books.csv"));
        itemReader.setName("csvReader");
        itemReader.setLinesToSkip(1);
        itemReader.setLineMapper(lineMapper());
        itemReader.setStrict(false);
        return itemReader;
    }

    private LineMapper<Author> lineMapper() {
        DefaultLineMapper<Author> lineMapper = new DefaultLineMapper<>();

        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(";");
        lineTokenizer.setStrict(false);
        lineTokenizer.setIncludedFields(2);
        lineTokenizer.setNames("Book-Author");

        BeanWrapperFieldSetMapper<Author> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(Author.class);

        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);
        return lineMapper;
    }

    @Bean
    public AuthorProcessor processor(){ return new AuthorProcessor(); }


    //Whatever data we get here, we just apply save method from the book repository
    @Bean
    public RepositoryItemWriter<Author> writer(){

        RepositoryItemWriter<Author> writer = new RepositoryItemWriter<>();
        writer.setRepository(bookRepository);

        return writer;
    }

    @Bean
    public Step step1(){
        return stepBuilderFactory.get("csv-step").<Author, Author>chunk(100)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();

    }

    //Step2 saving the imageURL in db
    @Bean
    public FlatFileItemReader<Image> imageReader(){
        FlatFileItemReader<Image> imageItemReader = new FlatFileItemReader<>();
        imageItemReader.setResource(new FileSystemResource("src/main/resources/BX-Books.csv"));
        imageItemReader.setName("csvImageReader");
        imageItemReader.setLinesToSkip(1);

        imageItemReader.setLineMapper(imageLineMapper());
        imageItemReader.setStrict(false);
        return imageItemReader;
    }

    private LineMapper<Image> imageLineMapper() {
        DefaultLineMapper<Image> imageLineMapper = new DefaultLineMapper<>();

        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(";");
        lineTokenizer.setStrict(false);
        lineTokenizer.setIncludedFields(5, 6, 7);
        lineTokenizer.setNames("Image-URL-S","Image-URL-M","Image-URL-L");

        BeanWrapperFieldSetMapper<Image> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(Image.class);

        imageLineMapper.setLineTokenizer(lineTokenizer);
        imageLineMapper.setFieldSetMapper(fieldSetMapper);
        return imageLineMapper;
    }

    @Bean
    public ImageProcessor imageProcessor() {return new ImageProcessor();}

    @Bean
    public RepositoryItemWriter<Image> imageWriter(){

        RepositoryItemWriter<Image> imageWriter = new RepositoryItemWriter<>();
        imageWriter.setRepository(imageRepository);

        return imageWriter;
    }

    @Bean
    public Step step2(){
        return stepBuilderFactory.get("csv-image-step").<Image, Image>chunk(100)
                .reader(imageReader())
                .processor(imageProcessor())
                .writer(imageWriter())
                .build();

    }

    //Setting steps to function simultaniously
    @Bean
    public Flow splitFlow(){
        return new FlowBuilder<SimpleFlow> ("splitFlow")
                .split(taskExecutor())
                .add(flow1(), flow2())
                .build();
    }

    @Bean
    public Flow flow1(){
        return new FlowBuilder<SimpleFlow> ("flow1")
                .start(step1())
                .build();
    }

    @Bean
    public Flow flow2(){
        return new FlowBuilder<SimpleFlow> ("flow2")
                .start(step2())
                .build();
    }

    @Bean
    public Job job(){
        return jobBuilderFactory.get("importBooks")
                .start(splitFlow())
                .end().build();
    }

    @Bean
    public ThreadPoolTaskExecutor taskExecutor(){
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(10);
        threadPoolTaskExecutor.setMaxPoolSize(30);
        threadPoolTaskExecutor.afterPropertiesSet();
        return threadPoolTaskExecutor;
    }
}