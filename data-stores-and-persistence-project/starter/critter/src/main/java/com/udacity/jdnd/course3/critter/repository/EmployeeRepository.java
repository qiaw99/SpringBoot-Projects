package com.udacity.jdnd.course3.critter.repository;

import com.udacity.jdnd.course3.critter.entity.Employee;
import com.udacity.jdnd.course3.critter.user.EmployeeSkill;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Set;

@Repository
public interface EmployeeRepository extends CrudRepository<Employee, Long> {
//    @Query("select e from employee e where :dayOfWeek in e.daysAvailable and :skills member of a.skills")
//    List<Employee> findEmployByDateAndSkill(DayOfWeek dayOfWeek, Set<EmployeeSkill> skills);
}
