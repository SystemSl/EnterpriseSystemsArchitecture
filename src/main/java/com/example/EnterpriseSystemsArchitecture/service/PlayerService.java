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
public class PlayerService {

    private final PlayerRepository playerRepository;
    private final GuildRepository guildRepository; // Добавили для управления гильдиями

    public PlayerService(PlayerRepository playerRepository, GuildRepository guildRepository) {
        this.playerRepository = playerRepository;
        this.guildRepository = guildRepository;
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
        if (player.getId() != null) {
            Player currentDbPlayer = playerRepository.findById(player.getId()).orElse(null);
            if (currentDbPlayer != null) {
                Guild oldGuild = currentDbPlayer.getGuild();
                Guild newGuild = player.getGuild();
                boolean leftGuild = oldGuild != null && (newGuild == null || !oldGuild.getId().equals(newGuild.getId()));
                if (leftGuild) {
                    handleLeaderLeaving(oldGuild, player.getId());
                }
            }
        }
        playerRepository.save(player);
    }

    public void delete(Long id) {
        Player player = playerRepository.findById(id).orElse(null);
        if (player != null) {
            Guild guild = player.getGuild();
            if (guild != null) {
                handleLeaderLeaving(guild, player.getId());
            }
            playerRepository.delete(player);
        }
    }

    private void handleLeaderLeaving(Guild guild, Long leavingPlayerId) {
        if (guild.getLeader() != null && guild.getLeader().getId().equals(leavingPlayerId)) {
            List<Player> otherMembers = playerRepository.findByGuildId(guild.getId());
            otherMembers.removeIf(p -> p.getId().equals(leavingPlayerId));

            if (!otherMembers.isEmpty()) {
                guild.setLeader(otherMembers.get(0));
                guildRepository.save(guild);
            } else {
                guildRepository.delete(guild);
            }
        }
    }
}
