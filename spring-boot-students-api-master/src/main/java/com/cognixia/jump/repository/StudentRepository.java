package com.cognixia.jump.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cognixia.jump.model.Student;

// mark/label this as a repo
@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

	// one of the methods listed in jpa, retrieve all the records/entities from a table
	List<Student> findAll();
	
}
