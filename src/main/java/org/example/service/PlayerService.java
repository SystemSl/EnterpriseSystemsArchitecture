package org.example.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.example.model.Guild;
import org.example.model.Player;

import java.util.List;

@Stateless
public class PlayerService {

    @PersistenceContext(unitName = "serverPU")
    private EntityManager em;

    public List<Player> findAll() {
        return em.createQuery("SELECT p FROM Player p LEFT JOIN FETCH p.guild", Player.class).getResultList();
    }

    public Player findById(Integer id) {
        return em.find(Player.class, id);
    }

    public List<Player> findByGuildId(Integer guildId) {
        return em.createQuery("SELECT p FROM Player p WHERE p.guild.id = :guildId", Player.class)
                .setParameter("guildId", guildId)
                .getResultList();
    }

    public void create(Player player) {
        em.persist(player);
    }

    public void update(Player updatedPlayer) {
        Player currentDbPlayer = em.find(Player.class, updatedPlayer.getId());
        Guild oldGuild = currentDbPlayer.getGuild();
        Guild newGuild = updatedPlayer.getGuild();

        boolean leftGuild = oldGuild != null &&
                (newGuild == null || !oldGuild.getId().equals(newGuild.getId()));

        if (leftGuild) {
            handleLeaderLeaving(oldGuild, updatedPlayer.getId());
        }

        em.merge(updatedPlayer);
    }

    public void delete(Integer id) {
        Player player = findById(id);
        if (player != null) {
            Guild guild = player.getGuild();
            if (guild != null) {
                handleLeaderLeaving(guild, player.getId());
            }
            em.remove(player);
        }
    }

    private void handleLeaderLeaving(Guild guild, Integer leavingPlayerId) {
        if (guild.getLeader() != null && guild.getLeader().getId().equals(leavingPlayerId)) {
            List<Player> otherMembers = em.createQuery("SELECT p FROM Player p WHERE p.guild.id = :guildId AND p.id != :playerId", Player.class)
                    .setParameter("guildId", guild.getId())
                    .setParameter("playerId", leavingPlayerId)
                    .getResultList();

            if (!otherMembers.isEmpty()) {
                Player newLeader = otherMembers.get(0);
                guild.setLeader(newLeader);
                em.merge(guild);
            } else {
                em.remove(em.contains(guild) ? guild : em.merge(guild));
            }
        }
    }

    public List<Player> findAvailablePlayers() {
        return em.createQuery("SELECT p FROM Player p WHERE p.guild IS NULL", Player.class)
                .getResultList();
    }
}
