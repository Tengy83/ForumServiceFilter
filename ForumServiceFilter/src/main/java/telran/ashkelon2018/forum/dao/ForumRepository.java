package telran.ashkelon2018.forum.dao;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import telran.ashkelon2018.forum.domain.Post;

public interface ForumRepository extends MongoRepository<Post, String> {
//	@Query("{'tags': {'$in':?0}}")
	List<Post> findPostsByTagsIn(List<String> tags);
	
	List<Post> findPostsByAuthor(String author);
	
	List<Post> findPostsByDateCreatedBetween(LocalDateTime fromDate, LocalDateTime toDate);
}
