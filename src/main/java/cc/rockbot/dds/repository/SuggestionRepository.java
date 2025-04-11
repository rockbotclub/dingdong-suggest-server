package cc.rockbot.dds.repository;

import cc.rockbot.dds.entity.Suggestion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SuggestionRepository extends JpaRepository<Suggestion, Long> {
    Page<Suggestion> findByStatusAndOrgId(Integer status, String orgId, Pageable pageable);
    Page<Suggestion> findByStatus(Integer status, Pageable pageable);
    Page<Suggestion> findByOrgId(String orgId, Pageable pageable);
    List<Suggestion> findByUserWxid(String userWxid);
} 