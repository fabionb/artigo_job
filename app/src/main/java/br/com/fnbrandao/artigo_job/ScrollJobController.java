package br.com.fnbrandao.artigo_job;

import java.util.Date;

import javax.persistence.EntityManager;

import org.hibernate.ScrollableResults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/scroll")
public class ScrollJobController {

	private static final Logger logger = LoggerFactory.getLogger(ScrollJobController.class);

	@Autowired
	private EntityManager entityManager;

	@Autowired
	private OutboxRepository outboxRepository;

	@GetMapping("/job/{quantity}/{fetchSize}")
	@Transactional
	public int job(@PathVariable("quantity") int quantity, @PathVariable("fetchSize") int fetchSize) {
		int processed = 0;

		Date creationDate = new Date();
		ScrollableResults toProcess = outboxRepository.retrieveScroll(fetchSize, creationDate);
		while (toProcess.next()) {
			OutboxEntity item = (OutboxEntity) toProcess.get(0);
			// Do some processing
			logger.info("Processing item {}", item.getId());

			// Mark as processed
			item.setRetries(item.getRetries() + 1);
			item.setProcessed(true);

			processed++;

			if (processed % quantity == 0) {
				// Flush pending changes
				entityManager.flush();
				// Clear memory
				entityManager.clear();

				logger.info("Processed {}", processed);
			}
		}
		logger.info("Processed {}", processed);
		return processed;
	}

}
