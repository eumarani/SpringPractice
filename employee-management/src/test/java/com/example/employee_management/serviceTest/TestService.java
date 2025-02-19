package com.example.employee_management.serviceTest;

import com.example.employee_management.entity.Employee;
import com.example.employee_management.exception.EmployeeNotFoundException;
import com.example.employee_management.repository.EmployeeRepository;
import com.example.employee_management.service.EmployeeServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TestService {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    @Test
    public void testSaveEmployee(){

        // Given (Arrange)
        Employee employee1=new Employee(1L,"jhon","23@gmail.com","IT");
        Employee employee2=new Employee(2L,"can","123@hot.com","Soft");

        when(employeeRepository.save(employee1))
               .thenReturn(employee1);
        when(employeeRepository.save(employee2))
                .thenReturn(employee2);

        // When (Act)
        Employee savedEmployee1=employeeService.saveEmployee(employee1);
        Employee savedEmployee2=employeeService.saveEmployee(employee2);

        // Then (Assert)
        assertNotNull(savedEmployee1);
        assertEquals("jhon",savedEmployee1.getName());
        assertEquals("23@gmail.com",savedEmployee1.getEmail());
        assertEquals("IT",savedEmployee1.getDepartment());

        assertNotNull(savedEmployee2);
        assertNotEquals(null,savedEmployee2.getName());

        // Verify that save() method was called once
        verify(employeeRepository,times(1)).save(employee1);
        verify(employeeRepository,times(1)).save(employee2);
    }

    @Test
    public void testGetAllEmployees(){
        List<Employee> employeeList= Arrays.asList(
                new Employee(1L,"uma","rani@gmail.com","HR"),
                new Employee(2L,"Shrey","ansh@gmail.com","Finance")
        );
        when(employeeRepository.findAll())
                .thenReturn(employeeList);

        List<Employee> result=employeeService.getAllEmployees();

        assertNotNull(result);
        assertEquals(2,result.size());
        assertEquals("uma",result.get(0).getName());
        assertNotEquals("Raj",result.get(1).getName());


    }

    @Test
    public void testGetEmployeeById_success(){
        Long employeeId=2L;
        Employee employee=new Employee(employeeId,"hey","345@yahoo.com","IT");
        when(employeeRepository.findById(employeeId))
                .thenReturn(Optional.of(employee));

        Optional<Employee> result=employeeService.getEmployeeById(employeeId);

        assertTrue(result.isPresent());
        assertEquals(employeeId,result.get().getId());

        verify(employeeRepository,times(1)).findById(employeeId);
    }
    @Test
    public void testGetEmployeeById_NotFound(){
        Long employeeId=1L;
        //Employee employee=new Employee(employeeId,"Ram","385@yahoo.com","Finance");
        when(employeeRepository.findById(employeeId))
                .thenReturn(Optional.empty());

        EmployeeNotFoundException thrownException=assertThrows(
                EmployeeNotFoundException.class,
        ()->employeeService.getEmployeeById(employeeId)
        );


        assertEquals("Employee not Found with id:" + employeeId, thrownException.getMessage());
       // assertEquals("Employee Not Found with id:" +employeeId,thrownException.getMessage());

        verify(employeeRepository,times(1)).findById(employeeId);

    }
    @Test
    public void testUpdateEmployee_Success() {
        // Given (Existing Employee)
        Long employeeId = 1L;
        Employee existingEmployee = new Employee(employeeId, "John Doe","john.doe@email.com","Finance");
        Employee updatedDetails = new Employee(employeeId, "Jane Smith", "jane.smith@email.com","HR" );

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(existingEmployee)); // ✅ Mock finding employee
        when(employeeRepository.save(any(Employee.class))).thenReturn(updatedDetails); // ✅ Mock saving updated employee

        // When
        Employee updatedEmployee = employeeService.updateEmployee(employeeId, updatedDetails);

        // Then
        assertNotNull(updatedEmployee);
        assertEquals("Jane Smith", updatedEmployee.getName());
        assertEquals("HR", updatedEmployee.getDepartment());
        assertEquals("jane.smith@email.com", updatedEmployee.getEmail());

        verify(employeeRepository, times(1)).findById(employeeId); // ✅ Verify findById() call
        verify(employeeRepository, times(1)).save(any(Employee.class)); // ✅ Verify save() call
    }

    @Test
    public void testUpdateEmployee_NotFound() {
        // Given (Employee does not exist)
        Long employeeId = 2L;
        Employee updatedDetails = new Employee(employeeId, "Alice Johnson", "alice.johnson@email.com","IT" );

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.empty()); // ✅ Mock repository to return empty

        // When & Then (Expecting Exception)
        EmployeeNotFoundException thrownException = assertThrows(
                EmployeeNotFoundException.class,
                () -> employeeService.updateEmployee(employeeId, updatedDetails)
        );

        assertEquals("Employee not Found with id:" + employeeId, thrownException.getMessage());

        verify(employeeRepository, times(1)).findById(employeeId); // ✅ Verify findById() call
        verify(employeeRepository, never()).save(any(Employee.class)); // ✅ Ensure save() is never called
    }
    @Test
    public void testDeleteEmployee_Success() {
        // Given (Existing Employee)
        Long employeeId = 1L;
        Employee employee = new Employee(employeeId, "John Doe", "john.doe@email.com","Finance" );

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee)); // ✅ Mock finding employee

        // When
        employeeService.deleteEmployee(employeeId);

        // Then
        verify(employeeRepository, times(1)).findById(employeeId); // ✅ Verify findById() call
        verify(employeeRepository, times(1)).delete(employee); // ✅ Verify delete() call
    }

    @Test
    public void testDeleteEmployee_NotFound() {
        // Given (Employee does not exist)
        Long employeeId = 2L;
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.empty()); // ✅ Mock repository to return empty

        // When & Then (Expecting Exception)
        EmployeeNotFoundException thrownException = assertThrows(
                EmployeeNotFoundException.class,
                () -> employeeService.deleteEmployee(employeeId)
        );

        assertEquals("Employee not Found with id:" + employeeId, thrownException.getMessage());

        verify(employeeRepository, times(1)).findById(employeeId); // ✅ Verify findById() call
        verify(employeeRepository, never()).delete(any(Employee.class)); // ✅ Ensure delete() is never called
    }
}



