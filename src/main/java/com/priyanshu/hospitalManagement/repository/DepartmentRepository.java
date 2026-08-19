package com.priyanshu.hospitalManagement.repository;

import com.priyanshu.hospitalManagement.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
}