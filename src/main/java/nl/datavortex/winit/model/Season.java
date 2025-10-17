package nl.datavortex.winit.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "seasons")
@Getter
@Setter
@NoArgsConstructor
public class Season {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime startDate;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime endDate;

    @Column(nullable = false)
    private Boolean isActive = false;

    private String description;

    @OneToMany(mappedBy = "season", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LeagueTier> leagueTiers = new ArrayList<>();

    @OneToMany(mappedBy = "season", cascade = CascadeType.ALL)
    private List<Match> matches = new ArrayList<>();

    /**
     * Check if season is currently active based on dates
     */
    public boolean isCurrentlyActive() {
        LocalDateTime now = LocalDateTime.now();
        return now.isAfter(startDate) && now.isBefore(endDate);
    }

    /**
     * Check if season has ended
     */
    public boolean hasEnded() {
        return LocalDateTime.now().isAfter(endDate);
    }

    /**
     * Get league tier by ELO
     */
    public LeagueTier getLeagueTierByElo(Long elo) {
        return leagueTiers.stream()
                .filter(tier -> elo >= tier.getMinElo() && elo <= tier.getMaxElo())
                .findFirst()
                .orElse(null);
    }

    /**
     * Get sorted league tiers (ascending by minElo)
     */
    public List<LeagueTier> getSortedTiers() {
        return leagueTiers.stream()
                .sorted(Comparator.comparingLong(LeagueTier::getMinElo))
                .toList();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Season)) return false;
        Season season = (Season) o;
        return uuid != null && uuid.equals(season.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }

    @Override
    public String toString() {
        return "Season{" +
                "name='" + name + '\'' +
                ", active=" + isActive +
                ", " + startDate + " to " + endDate +
                '}';
    }
}