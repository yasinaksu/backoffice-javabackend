package com.omniteam.backofisbackend.batch;

import com.omniteam.backofisbackend.batch.step.OrderWriteSuccessfullNotifyStep;
import com.omniteam.backofisbackend.dto.jobrequest.JobRequestAddDto;
import com.omniteam.backofisbackend.entity.Order;
import com.omniteam.backofisbackend.entity.RequestStatus;
import com.omniteam.backofisbackend.service.implementation.JobRequestServiceImpl;
import com.omniteam.backofisbackend.shared.result.Result;
import org.hibernate.SessionFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.builder.SimpleStepBuilder;
import org.springframework.batch.item.database.HibernateCursorItemReader;
import org.springframework.batch.item.database.builder.HibernateCursorItemReaderBuilder;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;

@Service
public class OrderOperationsBatch {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private JobRequestServiceImpl jobRequestService;

    @Autowired
    OrderWriteSuccessfullNotifyStep writeDoneStep;

//    @Bean
//    private OrderWriteSuccessfullNotifyStep writeDoneStep() {
//        return new OrderWriteSuccessfullNotifyStep();
//    }

    @Autowired
    SessionFactory sessionFactory;

    @Bean
    public HibernateCursorItemReader orderItemReader(SessionFactory sessionFactory) {

        return new HibernateCursorItemReaderBuilder<Order>()
                .name("orderReader")
                .sessionFactory(sessionFactory)
                .queryString("from orders").build();
    }

    @Bean
    FlatFileItemWriter writer() {
        BeanWrapperFieldExtractor<Order> extractor = new BeanWrapperFieldExtractor<>();
        extractor.setNames(new String[]{
                "OrderId", "status",
                "customer.firstName", "customer.lastName",
                "User.firstName", "User.lastName",
                "modifiedDate", "createdDate"
        });
        extractor.afterPropertiesSet();

        DelimitedLineAggregator<Order> lineAggregator = new DelimitedLineAggregator<>();
        lineAggregator.setDelimiter(",");
        lineAggregator.setFieldExtractor(extractor);

        return new FlatFileItemWriterBuilder<Order>()
                .name("orderExportCSV")
                .resource(new FileSystemResource(
                        "target/test-outputs/orders.txt"))
                .lineAggregator(lineAggregator)
                .build();

    }


    @Bean
    public Step Write2FileStep() {
        StepExecutionListener listener = new StepExecutionListener() {
            @Override
            public void beforeStep(StepExecution stepExecution) {
            }

            @Override
            public ExitStatus afterStep(StepExecution stepExecution) {
                Result result = jobRequestService.add(new JobRequestAddDto(RequestStatus.STARTING, null));
                stepExecution.getJobExecution().getExecutionContext().put("JobRequestID", result.getId());
                return ExitStatus.COMPLETED;
            }
        };
        SimpleStepBuilder stepBuilder = this.stepBuilderFactory.get("writeFileStep-01")
                .chunk(0)
                .reader(orderItemReader(sessionFactory))
                .writer(writer());

        stepBuilder.listener(listener);
        return stepBuilder.build();
    }

    @Bean
    public Step fileReady() {
        return this.stepBuilderFactory.get("readyFileStep-02")
                .tasklet(writeDoneStep)
                .build();
    }


    @Qualifier("orderExporterJob")
    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public Job OrderExporterJob() {
        return this.jobBuilderFactory.get("order-export")
                .incrementer(new RunIdIncrementer())
                .preventRestart()
                .start(Write2FileStep())
                .next(fileReady())
                .build();

    }


}
