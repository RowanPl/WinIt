package nl.datavortex.winit.model;

import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name="Teams")
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;
    private String name;
    private String description;
    @ManyToMany(mappedBy = "teams")
    private List<User> playerList;

    @OneToMany
    private List<Match> matches;
}
