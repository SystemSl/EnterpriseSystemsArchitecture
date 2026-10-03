package org.example.service;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.example.model.Guild;
import org.example.model.Player;

import java.util.List;

@Stateless
public class GuildService {
    @PersistenceContext(unitName = "serverPU")
    private EntityManager em;

    public List<Guild> findAll() {
        return em.createQuery("SELECT g FROM Guild g", Guild.class).getResultList();
    }

    public Guild findById(Integer id) {
        return em.find(Guild.class, id);
    }

    public void create(Guild guild) {
        em.persist(guild);

        if (guild.getLeader() != null) {
            Player leader = em.find(Player.class, guild.getLeader().getId());
            if (leader != null) {
                leader.setGuild(guild);
            }
        }
    }

    public void update(Guild guild) {
        Guild mergedGuild = em.merge(guild);

        if (mergedGuild.getLeader() != null) {
            Player leader = em.find(Player.class, mergedGuild.getLeader().getId());
            if (leader != null && (leader.getGuild() == null || !leader.getGuild().getId().equals(mergedGuild.getId()))) {
                leader.setGuild(mergedGuild);
            }
        }
    }

    public void delete(Integer id) {
        Guild guild = findById(id);
        if (guild != null) {
            for (Player member : guild.getMembers()) {
                member.setGuild(null);
            }
            em.remove(guild);
        }
    }
}
