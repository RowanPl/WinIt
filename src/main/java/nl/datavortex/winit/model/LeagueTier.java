package nl.datavortex.winit.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "league_tiers")
@Getter
@Setter
@NoArgsConstructor
public class LeagueTier {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "season_id", nullable = false)
    private Season season;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @NotNull
    @Column(nullable = false)
    private Long minElo;

    @NotNull
    @Column(nullable = false)
    private Long maxElo;

    @NotNull
    @Column(nullable = false)
    private Integer tierLevel;

    @Column(length = 7)
    private String colorHex;

    private String iconUrl;

    private String description;

    // Premium feature: custom names per group
    @Column(nullable = false)
    private Boolean isCustomName = false;

    /**
     * Check if a player with given ELO is in this tier
     */
    public boolean containsElo(Long elo) {
        return elo >= minElo && elo <= maxElo;
    }

    /**
     * Get next tier (higher level)
     */
    public LeagueTier getNextTier() {
        return season.getLeagueTiers().stream()
                .filter(tier -> tier.getTierLevel() == this.tierLevel + 1)
                .findFirst()
                .orElse(null);
    }

    /**
     * Get previous tier (lower level)
     */
    public LeagueTier getPreviousTier() {
        return season.getLeagueTiers().stream()
                .filter(tier -> tier.getTierLevel() == this.tierLevel - 1)
                .findFirst()
                .orElse(null);
    }

    /**
     * Check if player is near promotion
     */
    public boolean isNearPromotion(Long currentElo, int threshold) {
        if (currentElo >= maxElo - threshold) {
            return getNextTier() != null;
        }
        return false;
    }

    /**
     * Check if player is near demotion
     */
    public boolean isNearDemotion(Long currentElo, int threshold) {
        if (currentElo <= minElo + threshold) {
            return getPreviousTier() != null;
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LeagueTier)) return false;
        LeagueTier that = (LeagueTier) o;
        return uuid != null && uuid.equals(that.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }

    @Override
    public String toString() {
        return String.format("LeagueTier{name='%s', level=%d, elo=%d-%d}",
                name, tierLevel, minElo, maxElo == Long.MAX_VALUE ? 9999 : maxElo);
    }
}