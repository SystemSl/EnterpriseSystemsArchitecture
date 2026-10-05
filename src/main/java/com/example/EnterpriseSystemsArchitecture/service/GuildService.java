package com.example.EnterpriseSystemsArchitecture.service;

import com.example.EnterpriseSystemsArchitecture.config.JmsConfig;
import com.example.EnterpriseSystemsArchitecture.dto.EntityChangeEvent;
import com.example.EnterpriseSystemsArchitecture.model.Guild;
import com.example.EnterpriseSystemsArchitecture.model.Player;
import com.example.EnterpriseSystemsArchitecture.repository.GuildRepository;
import com.example.EnterpriseSystemsArchitecture.repository.PlayerRepository;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class GuildService {

    private final GuildRepository guildRepository;
    private final PlayerRepository playerRepository;
    private final JmsTemplate jmsTemplate;

    public GuildService(GuildRepository guildRepository, PlayerRepository playerRepository, JmsTemplate jmsTemplate) {
        this.guildRepository = guildRepository;
        this.playerRepository = playerRepository;
        this.jmsTemplate = jmsTemplate;
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
        String action = (guild.getId() == null) ? "INSERT" : "UPDATE";

        Guild saved = guildRepository.save(guild);
        if (saved.getLeader() != null) {
            Player leader = playerRepository.findById(saved.getLeader().getId()).orElse(null);
            if (leader != null) {
                leader.setGuild(saved);
                playerRepository.save(leader);
            }
        }

        String details = "Name: " + saved.getName() + ", Rating: " + saved.getRating();
        jmsTemplate.convertAndSend(JmsConfig.ENTITY_EVENTS_TOPIC,
                new EntityChangeEvent(action, "Guild", saved.getId(), details));
    }

    public void delete(Long id) {
        Guild guild = findById(id);
        String guildName = (guild != null) ? guild.getName() : "ID " + id;

        List<Player> members = playerRepository.findByGuildId(id);
        for (Player member : members) {
            member.setGuild(null);
            playerRepository.save(member);
        }
        guildRepository.deleteById(id);

        jmsTemplate.convertAndSend(JmsConfig.ENTITY_EVENTS_TOPIC,
                new EntityChangeEvent("DELETE", "Guild", id, "Расформирована гильдия " + guildName));
    }
}