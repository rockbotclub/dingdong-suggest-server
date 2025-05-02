package cc.rockbot.dds.repository;

import cc.rockbot.dds.model.SuggestionDO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SuggestionRepository extends JpaRepository<SuggestionDO, Long> {
    Page<SuggestionDO> findByStatusAndOrgId(Integer status, String orgId, Pageable pageable);
    Page<SuggestionDO> findByStatus(Integer status, Pageable pageable);
    Page<SuggestionDO> findByOrgId(String orgId, Pageable pageable);
    List<SuggestionDO> findByUserWxid(String userWxid);
    
    @Query("SELECT s FROM SuggestionDO s WHERE s.userWxid = :userWxid AND s.orgId = :orgId AND FUNCTION('YEAR', s.gmtCreate) = :year")
    List<SuggestionDO> findByUserWxidAndOrgIdAndYear(
        @Param("userWxid") String userWxid,
        @Param("orgId") String orgId,
        @Param("year") Integer year
    );

    Page<SuggestionDO> findByUserWxidAndOrgIdAndYear(String userWxid, String orgId, int year, Pageable pageable);
} 