package com.example.EnterpriseSystemsArchitecture.rest;

import com.example.EnterpriseSystemsArchitecture.model.Player;
import com.example.EnterpriseSystemsArchitecture.service.GuildService;
import com.example.EnterpriseSystemsArchitecture.service.PlayerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/players", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
public class PlayerRestController {

    private final PlayerService playerService;
    private final GuildService guildService;

    public PlayerRestController(PlayerService playerService, GuildService guildService) {
        this.playerService = playerService;
        this.guildService = guildService;
    }

    @GetMapping
    public List<Player> getAllPlayers() {
        return playerService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Player> getPlayerById(@PathVariable Long id) {
        Player player = playerService.findById(id);
        return player != null ? ResponseEntity.ok(player) : ResponseEntity.notFound().build();
    }

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Player> createPlayer(@RequestBody Player player,
                                               @RequestParam(required = false) Long guildId) {
        if (guildId != null) {
            player.setGuild(guildService.findById(guildId));
        }
        playerService.save(player);
        return ResponseEntity.status(HttpStatus.CREATED).body(player);
    }

    @PutMapping(value = "/{id}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Player> updatePlayer(@PathVariable Long id,
                                               @RequestBody Player player,
                                               @RequestParam(required = false) Long guildId) {
        if (playerService.findById(id) == null) {
            return ResponseEntity.notFound().build();
        }
        player.setId(id);
        if (guildId != null) {
            player.setGuild(guildService.findById(guildId));
        }
        playerService.save(player);
        return ResponseEntity.ok(player);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlayer(@PathVariable Long id) {
        if (playerService.findById(id) == null) {
            return ResponseEntity.notFound().build();
        }
        playerService.delete(id);
        return ResponseEntity.noContent().build();
    }
}