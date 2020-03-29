package com.sliit.springrest.payroll;

import com.sliit.springrest.ExceptionHandling.EmployeeNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class EmployeeController {

    @Autowired
    private EmployeeRepository employeeRepository;

    @GetMapping("api/employees")
    List<Employee> getAllEmployee(){
        return employeeRepository.findAll();
    }
    @PostMapping("api/employees")
    Employee saveEmployee(@RequestBody Employee newEmployee){
        return employeeRepository.save(newEmployee);
    }
    @GetMapping("api/employees/{id}")
    Employee getEmployeeById(@PathVariable Long id){
        return employeeRepository.findById(id)
                .orElseThrow( () -> new EmployeeNotFoundException(id));
    }

    @PutMapping("api/employees/{id}")
    Employee replaceEmployee (@RequestBody Employee newEmployee, @PathVariable Long id) {
        return employeeRepository.findById(id).
                map(employee -> {
                    employee.setName(newEmployee.getName());
                    employee.setRole(newEmployee.getRole());
                     return employeeRepository.save(employee);
                }).orElseGet(()->{
                    newEmployee.setId(id);
                    return employeeRepository.save(newEmployee);
        });

    }
    @DeleteMapping("api/employees/{id}")
    void deleteEmployee(@PathVariable Long id){
        employeeRepository.deleteById(id);
    }


}
