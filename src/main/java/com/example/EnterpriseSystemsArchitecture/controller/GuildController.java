package com.example.EnterpriseSystemsArchitecture.controller;

import com.example.EnterpriseSystemsArchitecture.model.Guild;
import com.example.EnterpriseSystemsArchitecture.service.GuildService;
import com.example.EnterpriseSystemsArchitecture.service.PlayerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/guilds")
public class GuildController {

    private final GuildService guildService;
    private final PlayerService playerService;

    public GuildController(GuildService guildService, PlayerService playerService) {
        this.guildService = guildService;
        this.playerService = playerService;
    }

    @GetMapping
    public String listGuilds(Model model) {
        Guild newGuild = new Guild();
        model.addAttribute("allGuilds", guildService.findAll());
        model.addAttribute("guild", newGuild);
        model.addAttribute("candidateLeaders", playerService.findFreePlayers());
        return "guilds";
    }

    @PostMapping("/save")
    public String saveGuild(@ModelAttribute Guild guild,
                            @RequestParam(value = "selectedLeaderId", required = false) Long leaderId) {
        if (leaderId != null) {
            guild.setLeader(playerService.findById(leaderId));
        }
        guildService.save(guild);
        return "redirect:/guilds";
    }

    @GetMapping("/edit/{id}")
    public String editGuild(@PathVariable Long id, Model model) {
        Guild guild = guildService.findById(id);
        model.addAttribute("allGuilds", guildService.findAll());
        model.addAttribute("guild", guild);
        model.addAttribute("candidateLeaders", playerService.findPlayersByGuild(id));
        return "guilds";
    }

    @GetMapping("/delete/{id}")
    public String deleteGuild(@PathVariable Long id) {
        guildService.delete(id);
        return "redirect:/guilds";
    }
}
