package nl.datavortex.winit.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    @NotBlank
    @Size(min = 3, max = 50)
    @Column(unique = true, nullable = false)
    private String username;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_groups",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "group_id")
    )
    private List<Group> groups = new ArrayList<>();

    // REMOVED: private Long elo;
    // ELO is now per-group in UserGroupStats

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserGroupStats> groupStats = new ArrayList<>();

    // Helper method to get ELO for a specific group
    public Long getEloForGroup(Group group) {
        return groupStats.stream()
                .filter(stats -> stats.getGroup().equals(group))
                .findFirst()
                .map(UserGroupStats::getElo)
                .orElse(1500L); // Default ELO if not found
    }

    // Helper method to get or create stats for a group
    public UserGroupStats getOrCreateStatsForGroup(Group group) {
        return groupStats.stream()
                .filter(stats -> stats.getGroup().equals(group))
                .findFirst()
                .orElseGet(() -> {
                    UserGroupStats newStats = new UserGroupStats();
                    newStats.setUser(this);
                    newStats.setGroup(group);
                    newStats.setElo(1500L);
                    groupStats.add(newStats);
                    return newStats;
                });
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return uuid != null && uuid.equals(user.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }

    @Override
    public String toString() {
        return "User{" +
                "uuid=" + uuid +
                ", username='" + username + '\'' +
                '}';
    }
}