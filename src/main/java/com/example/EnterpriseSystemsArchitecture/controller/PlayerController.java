package com.example.EnterpriseSystemsArchitecture.controller;

import com.example.EnterpriseSystemsArchitecture.model.Player;
import com.example.EnterpriseSystemsArchitecture.service.GuildService;
import com.example.EnterpriseSystemsArchitecture.service.PlayerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/players")
public class PlayerController {

    private final PlayerService playerService;
    private final GuildService guildService;

    public PlayerController(PlayerService playerService, GuildService guildService) {
        this.playerService = playerService;
        this.guildService = guildService;
    }

    @GetMapping
    public String listPlayers(Model model) {
        model.addAttribute("allPlayers", playerService.findAll());
        model.addAttribute("allGuilds", guildService.findAll());
        model.addAttribute("player", new Player());
        return "players";
    }

    @PostMapping("/save")
    public String savePlayer(@ModelAttribute Player player,
                             @RequestParam(value = "selectedGuildId", required = false) Long guildId) {
        if (guildId != null) {
            player.setGuild(guildService.findById(guildId));
        } else {
            player.setGuild(null);
        }
        playerService.save(player);
        return "redirect:/players";
    }

    @GetMapping("/edit/{id}")
    public String editPlayer(@PathVariable Long id, Model model) {
        model.addAttribute("allPlayers", playerService.findAll());
        model.addAttribute("allGuilds", guildService.findAll());
        model.addAttribute("player", playerService.findById(id));
        return "players";
    }

    @GetMapping("/delete/{id}")
    public String deletePlayer(@PathVariable Long id) {
        playerService.delete(id);
        return "redirect:/players";
    }
}

