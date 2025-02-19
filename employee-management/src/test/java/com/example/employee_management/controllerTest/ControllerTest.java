package com.example.employee_management.controllerTest;

import com.example.employee_management.entity.Employee;



import com.example.employee_management.controller.EmployeeController;

import com.example.employee_management.service.EmployeeServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;  // ✅ Correct import
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;




import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;



@ExtendWith(MockitoExtension.class)
public class ControllerTest {


    private MockMvc mockMvc;

    @Mock
    private EmployeeServiceImpl employeeService;

    @InjectMocks
    private EmployeeController employeeController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(employeeController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void saveEmployeeTest() throws Exception{
        //to test this is the sample data
        Employee employee=new Employee(1L,"John Doe","john.doe@example.com","IT");

        // Mocking the service layer response
        when(employeeService.saveEmployee(any(Employee.class)))
                .thenReturn(employee);

        // Convert employee object to JSON
        String employeeJson=objectMapper.writeValueAsString(employee);

        // Perform POST request
        mockMvc.perform(post("/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(employeeJson))
                .andDo(print())
                .andExpect(status().isOk())  // Expecting HTTP 200 status
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect( jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"))
                .andExpect(jsonPath("$.department").value("IT"));


        // Verify that the service method was called once
        verify(employeeService,times(1))
                .saveEmployee(any(Employee.class));




    }


}
