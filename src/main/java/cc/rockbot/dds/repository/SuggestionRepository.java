package cc.rockbot.dds.repository;

import cc.rockbot.dds.entity.Suggestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SuggestionRepository extends JpaRepository<Suggestion, Long> {
    List<Suggestion> findByUserWxid(String userWxid);
    List<Suggestion> findByOrgId(String orgId);
    List<Suggestion> findByStatus(Integer status);
} 