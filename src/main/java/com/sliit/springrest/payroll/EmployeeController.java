package com.sliit.springrest.payroll;

import com.sliit.springrest.ExceptionHandling.EmployeeNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class EmployeeController {

    @Autowired
    private EmployeeRepository employeeRepository;

    @GetMapping("api/employees")
    CollectionModel<EntityModel<Employee>> getAllEmployee(){

        List<EntityModel<Employee>> allemployees = employeeRepository.findAll().stream().map(employee ->
            new EntityModel<Employee>(employee, linkTo(methodOn(EmployeeController.class).getEmployeeById(employee.getId())).withSelfRel(),
                    linkTo(methodOn(EmployeeController.class).getAllEmployee()).withRel("employees")
            )).collect(Collectors.toList());

        return new CollectionModel<>(allemployees,linkTo(methodOn(EmployeeController.class).getAllEmployee()).withSelfRel());


    }
    @PostMapping("api/employees")
    Employee saveEmployee(@RequestBody Employee newEmployee){
        return employeeRepository.save(newEmployee);
    }
    @GetMapping("api/employees/{id}")
    EntityModel<Employee> getEmployeeById(@PathVariable Long id){
        Employee employee=employeeRepository.findById(id)
                .orElseThrow(()->new EmployeeNotFoundException(id));

        return new EntityModel<Employee>(employee,linkTo(methodOn(EmployeeController.class).getEmployeeById(id)).withSelfRel(),
                linkTo(methodOn(EmployeeController.class).getAllEmployee()).withRel("employees")
        );
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
