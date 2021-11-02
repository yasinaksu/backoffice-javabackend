package com.omniteam.backofisbackend.service.implementation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.omniteam.backofisbackend.dto.PagedDataWrapper;
import com.omniteam.backofisbackend.dto.jobrequest.JobRequestAddDto;
import com.omniteam.backofisbackend.dto.jobrequest.JobRequestUpdateDto;
import com.omniteam.backofisbackend.entity.JobRequest;
import com.omniteam.backofisbackend.entity.RequestStatus;
import com.omniteam.backofisbackend.repository.JobRequestRepository;
import com.omniteam.backofisbackend.shared.mapper.JobRequestMapper;
import com.omniteam.backofisbackend.shared.result.DataResult;
import com.omniteam.backofisbackend.shared.result.ErrorResult;
import com.omniteam.backofisbackend.shared.result.Result;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
public class JobRequestServiceImplTest {



    @InjectMocks
    private JobRequestServiceImpl jobRequestService;

    @Mock
    private JobRequestRepository jobRequestRepository;

    @Spy
    private JobRequestMapper jobRequestMapper= Mappers.getMapper(JobRequestMapper.class);

    @Spy
    private ObjectMapper objectMapper= JsonMapper.builder()
            .addModule(new JavaTimeModule())
            .addModule(new Jdk8Module())
            .build();

    @Test
    void getAllTest()
    {
        Page<JobRequest> jobRequestPage=new PageImpl<JobRequest>(new ArrayList<>(),Pageable.unpaged(),0);
        Mockito.when(jobRequestRepository.getAllBy(Mockito.any(Pageable.class)))
                .thenReturn(jobRequestPage);
        DataResult<PagedDataWrapper<JobRequest>> allServiceResult = jobRequestService.getAll(Pageable.unpaged());
        assertThat(allServiceResult).isNotNull();
        assertThat(allServiceResult.getData()).isNotNull();
        assertThat(allServiceResult.getData().getContent()).isNotNull();
        assertThat(allServiceResult.getMessage()).isNull();
        assertThat(allServiceResult.isSuccess()).isTrue();

    }


    @Test
    void addTest()
    {
        JobRequest jobRequestEntity = new JobRequest();
        jobRequestEntity.setJobRequestId(1);
        Mockito.when(jobRequestRepository.save(Mockito.any(JobRequest.class)))
                .thenReturn(jobRequestEntity);

        Result addResult = jobRequestService.add(new JobRequestAddDto());
        assertThat(addResult).isNotNull();
        assertThat(addResult.getId()).isNotNull();
        assertThat(addResult.getId()).isEqualTo(jobRequestEntity.getJobRequestId());
    }

    @Test
    void updateTest() throws JsonProcessingException {
        JobRequestUpdateDto updateDto = new JobRequestUpdateDto();
        updateDto.setJobRequestId(1);
        updateDto.setRequestStatus(RequestStatus.DONE);
        JobRequest savedJobRequestEntity = new JobRequest();
        savedJobRequestEntity.setJobRequestId(1);
        savedJobRequestEntity.setRequestStatus(RequestStatus.DONE);
        JobRequest getByJobRequest = new JobRequest(1,RequestStatus.STARTING,null);
        Mockito.when(jobRequestRepository.getById(1)).thenReturn(getByJobRequest);
        Mockito.when(jobRequestRepository.save(Mockito.any(JobRequest.class)))
                .thenReturn(savedJobRequestEntity);

        Result updateResult = jobRequestService.update(updateDto);
        assertThat(updateResult).isNotNull();
        assertThat(updateResult.getId()).isNotNull();
        assertThat(updateResult.getId()).isEqualTo(updateDto.getJobRequestId());
        assertThat(objectMapper.readValue(updateResult.getMessage(),JobRequest.class))
                .isNotNull();

    }

    @Test
    void updateFailTest() throws JsonProcessingException {
        JobRequestUpdateDto updateDto = new JobRequestUpdateDto();
        updateDto.setJobRequestId(1);
        updateDto.setRequestStatus(RequestStatus.DONE);
        JobRequest savedJobRequestEntity = new JobRequest();
        savedJobRequestEntity.setJobRequestId(1);
        savedJobRequestEntity.setRequestStatus(RequestStatus.DONE);
        JobRequest getByJobRequest = new JobRequest(1,RequestStatus.STARTING,null);
        Mockito.when(jobRequestRepository.getById(1)).thenReturn(null);

        Result updateResult = jobRequestService.update(updateDto);
        assertThat(updateResult).isNotNull().isInstanceOf(ErrorResult.class);
        assertThat(updateResult.getMessage()).isNotNull();

    }

    @Test
    void setStatusTest() throws JsonProcessingException {
        JobRequest getByJobRequest = new JobRequest(1,RequestStatus.STARTING,null);
        JobRequest savedRequest = new JobRequest(1,RequestStatus.DONE,null);
        Mockito.when(jobRequestRepository.getById(1)).thenReturn(getByJobRequest);
        Mockito.when(jobRequestRepository.save(Mockito.any(JobRequest.class)))
                .thenReturn(savedRequest);

        Result updateResult = jobRequestService.setStatus(1,RequestStatus.DONE);
        assertThat(updateResult).isNotNull();
        assertThat(updateResult.getId()).isNotNull();
        assertThat(updateResult.getId()).isEqualTo(getByJobRequest.getJobRequestId());
        assertThat(objectMapper.readValue(updateResult.getMessage(),JobRequest.class))
                .isNotNull();
        assertThat(objectMapper.readValue(updateResult.getMessage(),JobRequest.class).getRequestStatus())
                .isEqualTo(RequestStatus.DONE);

    }



}
