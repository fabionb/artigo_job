package br.com.fnbrandao.artigo_job;

import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import javax.persistence.EntityManager;

import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.jpa.QueryHints;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class OutboxRepository {

	@Autowired
	private EntityManager entityManager;

	public List<OutboxEntity> retrieveLimited(int quantity) {
		return entityManager.createQuery("SELECT o FROM OutboxEntity o WHERE o.processed = FALSE", OutboxEntity.class)
				.setMaxResults(quantity)
				.getResultList();
	}

	public List<OutboxEntity> retrievePaging(int pageNumber, int quantity, Date creationDate) {
		return entityManager.createQuery("SELECT o FROM OutboxEntity o WHERE o.retries < 3 AND o.creationDate <= :creationDate ORDER BY o.id", OutboxEntity.class)
				.setParameter("creationDate", creationDate)
				.setFirstResult((pageNumber - 1) * quantity)
				.setMaxResults(quantity)
				.getResultList();
	}

	public ScrollableResults retrieveScroll(int fetchSize, Date creationDate) {
		return entityManager.unwrap(Session.class).createQuery("SELECT o FROM OutboxEntity o WHERE o.retries < 3 AND o.creationDate <= :creationDate", OutboxEntity.class)
				.setParameter("creationDate", creationDate)
				.setFetchSize(fetchSize)
				.scroll();
	}

	public Stream<OutboxEntity> retrieveStream(int fetchSize, Date creationDate) {
		return entityManager.createQuery("SELECT o FROM OutboxEntity o WHERE o.retries < 3 AND o.creationDate <= :creationDate", OutboxEntity.class)
				.setParameter("creationDate", creationDate)
				.setHint(QueryHints.HINT_FETCH_SIZE, fetchSize)
				.getResultStream();
	}

}
