package com.example.EnterpriseSystemsArchitecture.service;

import com.example.EnterpriseSystemsArchitecture.model.Guild;
import com.example.EnterpriseSystemsArchitecture.model.Player;
import com.example.EnterpriseSystemsArchitecture.repository.GuildRepository;
import com.example.EnterpriseSystemsArchitecture.repository.PlayerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class GuildService {

    private final GuildRepository guildRepository;
    private final PlayerRepository playerRepository;

    public GuildService(GuildRepository guildRepository, PlayerRepository playerRepository) {
        this.guildRepository = guildRepository;
        this.playerRepository = playerRepository;
    }

    @Transactional(readOnly = true)
    public List<Guild> findAll() {
        return guildRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Guild findById(Long id) {
        return guildRepository.findById(id).orElse(null);
    }

    public void save(Guild guild) {
        guildRepository.save(guild);
    }

    public void delete(Long id) {
        List<Player> members = playerRepository.findByGuildId(id);
        for (Player member : members) {
            member.setGuild(null);
        }
        guildRepository.deleteById(id);
    }
}
