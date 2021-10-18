package br.com.fnbrandao.artigo_job;

import java.util.List;

import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/limit")
public class LimitJobController {

	private static final Logger logger = LoggerFactory.getLogger(LimitJobController.class);

	@Autowired
	private EntityManager entityManager;

	@Autowired
	private OutboxRepository outboxRepository;

	@GetMapping("/job/{quantity}")
	@Transactional
	public int job(@PathVariable("quantity") int quantity) {
		int processed = 0;
		List<OutboxEntity> toProcess = outboxRepository.retrieveLimited(quantity);
		while (!toProcess.isEmpty()) {
			for (OutboxEntity item : toProcess) {
				// Do some processing
				logger.info("Processing item {}", item.getId());

				// Mark as processed
				item.setProcessed(true);

				processed++;
			}

			// Flush pending changes
			entityManager.flush();
			// Clear memory
			entityManager.clear();

			logger.info("Processed {}", processed);

			toProcess = outboxRepository.retrieveLimited(quantity);
		}
		return processed;
	}

}
