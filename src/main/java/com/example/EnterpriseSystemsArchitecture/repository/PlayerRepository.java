package com.example.EnterpriseSystemsArchitecture.repository;

import com.example.EnterpriseSystemsArchitecture.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {
    List<Player> findByGuildIsNull();
    List<Player> findByGuildId(Long guildId);
}
