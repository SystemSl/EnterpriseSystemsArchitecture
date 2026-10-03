package org.example.controller;

import jakarta.ejb.EJB;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import org.example.model.Guild;
import org.example.model.Player;
import org.example.service.GuildService;
import org.example.service.PlayerService;

import java.io.Serializable;
import java.util.List;

@Named
@RequestScoped
public class GuildController implements Serializable {

    @EJB
    private GuildService guildService;

    @EJB
    private PlayerService playerService;

    private Guild guild = new Guild();
    private Integer selectedLeaderId;

    public List<Guild> getAllGuilds() {
        return guildService.findAll();
    }

    public List<Player> getCandidateLeaders() {
        if (guild.getId() == null) {
            return playerService.findAvailablePlayers();
        } else {
            return playerService.findByGuildId(guild.getId());
        }
    }

    public String save() {
        if (selectedLeaderId != null) {
            Player leader = playerService.findById(selectedLeaderId);
            guild.setLeader(leader);
        }

        if (guild.getId() == null) {
            guildService.create(guild);
        } else {
            guildService.update(guild);
        }
        guild = new Guild();
        return "guilds?faces-redirect=true";
    }

    public void edit(Guild g) {
        this.guild = g;
        if (g.getLeader() != null) {
            this.selectedLeaderId = g.getLeader().getId();
        }
    }

    public String delete(Integer id) {
        guildService.delete(id);
        return "guilds?faces-redirect=true";
    }

    public Guild getGuild() {
        return guild;
    }

    public void setGuild(Guild guild) {
        this.guild = guild;
    }

    public Integer getSelectedLeaderId() {
        return selectedLeaderId;
    }

    public void setSelectedLeaderId(Integer selectedLeaderId) {
        this.selectedLeaderId = selectedLeaderId;
    }
}
