package com.assessment2.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.assessment2.model.InfosysEmployees;


@Repository
public interface InfosysEmployeeRepository extends JpaRepository<InfosysEmployees, Long> {
		@Query(value = "SELECT * FROM INFOSYS_EMPLOYEES FETCH FIRST :limit ROWS ONLY", nativeQuery = true)
	    List<InfosysEmployees> findWithLimit(@Param("limit") int limit);
}
