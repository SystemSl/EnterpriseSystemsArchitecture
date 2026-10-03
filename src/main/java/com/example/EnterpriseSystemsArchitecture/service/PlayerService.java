package com.example.EnterpriseSystemsArchitecture.service;

import com.example.EnterpriseSystemsArchitecture.model.Player;
import com.example.EnterpriseSystemsArchitecture.repository.PlayerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PlayerService {

    private final PlayerRepository playerRepository;

    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @Transactional(readOnly = true)
    public List<Player> findAll() {
        return playerRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Player findById(Long id) {
        return playerRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<Player> findFreePlayers() {
        return playerRepository.findByGuildIsNull();
    }

    @Transactional(readOnly = true)
    public List<Player> findPlayersByGuild(Long guildId) {
        return playerRepository.findByGuildId(guildId);
    }

    public void save(Player player) {
        playerRepository.save(player);
    }

    public void delete(Long id) {
        playerRepository.deleteById(id);
    }
}
