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
public class PlayerController implements Serializable {
    @EJB
    private PlayerService playerService;

    @EJB
    private GuildService guildService;

    private Player player = new Player();

    private Integer selectedGuildId;

    public List<Player> getAllPlayers() {
        return playerService.findAll();
    }

    public List<Guild> getAllGuilds() {
        return guildService.findAll();
    }

    public String save() {
        if (selectedGuildId != null && selectedGuildId > 0) {
            Guild guild = guildService.findById(selectedGuildId);
            player.setGuild(guild);
        } else {
            player.setGuild(null);
        }

        if (player.getId() == null) {
            playerService.create(player);
        } else {
            playerService.update(player);
        }
        player = new Player();
        return "index?faces-redirect=true";
    }

    public void edit(Player p) {
        this.player = p;
        if (p.getGuild() != null) {
            this.selectedGuildId = p.getGuild().getId();
        }
    }

    public String delete(Integer id) {
        playerService.delete(id);
        return "index?faces-redirect=true";
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Integer getSelectedGuildId() {
        return selectedGuildId;
    }

    public void setSelectedGuildId(Integer selectedGuildId) {
        this.selectedGuildId = selectedGuildId;
    }
}
