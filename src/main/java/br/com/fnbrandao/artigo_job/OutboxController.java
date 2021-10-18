package br.com.fnbrandao.artigo_job;

import java.util.Date;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OutboxController {

	@Autowired
	private EntityManager entityManager;

	@GetMapping("/create/{quantity}")
	@Transactional
	public void create(@PathVariable("quantity") int quantity) {
		for (int i = 0; i < quantity; i++) {
			entityManager.persist(new OutboxEntity());
		}
	}
	
	@GetMapping("/createMillion")
	@Transactional
	public void createMillion() {
		entityManager.createNativeQuery("insert into outbox_entity (id, processed, retries, creation_date) " //
				+ "select OUTBOX_SEQ.nextval, 0, 0, :date from " //
				+ "(select level as l from dual connect by level <= 1000) a, " //
				+ "(select level as l from dual connect by level <= 1000) b") //
		.setParameter("date", new Date())
		.executeUpdate();
	}
	
}
