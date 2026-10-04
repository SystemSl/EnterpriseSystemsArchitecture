package com.example.EnterpriseSystemsArchitecture.rest;

import com.example.EnterpriseSystemsArchitecture.model.Guild;
import com.example.EnterpriseSystemsArchitecture.service.GuildService;
import com.example.EnterpriseSystemsArchitecture.service.PlayerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/guilds", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
public class GuildRestController {

    private final GuildService guildService;
    private final PlayerService playerService;

    public GuildRestController(GuildService guildService, PlayerService playerService) {
        this.guildService = guildService;
        this.playerService = playerService;
    }

    @GetMapping
    public List<Guild> getAllGuilds() {
        return guildService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Guild> getGuildById(@PathVariable Long id) {
        Guild guild = guildService.findById(id);
        return guild != null ? ResponseEntity.ok(guild) : ResponseEntity.notFound().build();
    }

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Guild> createGuild(@RequestBody Guild guild,
                                             @RequestParam(required = false) Long leaderId) {
        if (leaderId != null) {
            guild.setLeader(playerService.findById(leaderId));
        }
        guildService.save(guild);
        return ResponseEntity.status(HttpStatus.CREATED).body(guild);
    }

    @PutMapping(value = "/{id}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Guild> updateGuild(@PathVariable Long id,
                                             @RequestBody Guild guild,
                                             @RequestParam(required = false) Long leaderId) {
        if (guildService.findById(id) == null) {
            return ResponseEntity.notFound().build();
        }
        guild.setId(id);
        if (leaderId != null) {
            guild.setLeader(playerService.findById(leaderId));
        }
        guildService.save(guild);
        return ResponseEntity.ok(guild);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGuild(@PathVariable Long id) {
        if (guildService.findById(id) == null) {
            return ResponseEntity.notFound().build();
        }
        guildService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
