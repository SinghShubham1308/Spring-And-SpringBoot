package com.emailfollowup.email_followup_app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.emailfollowup.email_followup_app.entitty.EmailStatus;
import com.emailfollowup.email_followup_app.entitty.EmailTask;

/**
 * @author SinghShubham1308
 */
@Repository
public interface EmailTaskRepository extends JpaRepository<EmailTask, Long>{

	List<EmailTask> findByStatus(EmailStatus status);
	
}
