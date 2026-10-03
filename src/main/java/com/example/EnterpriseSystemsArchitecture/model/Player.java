package com.example.EnterpriseSystemsArchitecture.model;

import jakarta.persistence.*;

@Entity
@Table(name = "players")
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nickname;

    private int level;

    @Column(name = "character_class", nullable = false)
    private String characterClass;

    private String race;

    @ManyToOne
    @JoinColumn(name = "guild_id")
    private Guild guild;

    public Player() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }

    public int getLevel() { return level; }
    public void setLevel(int level) { this.level = level; }

    public String getCharacterClass() { return characterClass; }
    public void setCharacterClass(String characterClass) { this.characterClass = characterClass; }

    public String getRace() { return race; }
    public void setRace(String race) { this.race = race; }

    public Guild getGuild() { return guild; }
    public void setGuild(Guild guild) { this.guild = guild; }
}
