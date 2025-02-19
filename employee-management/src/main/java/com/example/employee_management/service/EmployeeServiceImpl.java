package com.example.employee_management.service;

import com.example.employee_management.entity.Employee;
import com.example.employee_management.exception.EmployeeNotFoundException;
import com.example.employee_management.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService{

    @Autowired
    public EmployeeRepository employeeRepository;

    @Override
    public Employee saveEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public Optional<Employee> getEmployeeById(Long id){
        return Optional.ofNullable(employeeRepository.findById(id)
                .orElseThrow(()->new EmployeeNotFoundException("Employee not Found with id:" +id)));

    }

    @Override
    public Employee updateEmployee(Long id,Employee employeeDetails) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not Found with id:" + id));

        employee.setName(employeeDetails.getName());
        employee.setDepartment(employeeDetails.getDepartment());
        employee.setEmail(employeeDetails.getEmail());

        return employeeRepository.save(employee);
    }

    @Override
    public void deleteEmployee(Long id){
      Employee employee=employeeRepository.findById(id)
              .orElseThrow(()->new EmployeeNotFoundException("Employee not Found with id:" +id));
      employeeRepository.delete(employee);
        System.out.println("deleted Successfully");

    }

}
