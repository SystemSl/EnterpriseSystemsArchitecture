package com.example.EnterpriseSystemsArchitecture.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "guilds")
public class Guild {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private int rating;

    @OneToMany(mappedBy = "guild")
    private List<Player> players;

    @OneToOne
    @JoinColumn(name = "leader_id")
    private Player leader;

    private String description;

    @Column(name = "created_at")
    private LocalDate createdAt = LocalDate.now();

    public Guild() {}

    public Guild(String name, int rating) {
        this.name = name;
        this.rating = rating;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

    public List<Player> getPlayers() { return players; }
    public void setPlayers(List<Player> players) { this.players = players; }

    public Player getLeader() { return leader; }
    public void setLeader(Player leader) { this.leader = leader; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDate getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDate createdAt) { this.createdAt = createdAt; }
}
