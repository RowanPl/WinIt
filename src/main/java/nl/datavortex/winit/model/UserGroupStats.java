package nl.datavortex.winit.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(
        name = "user_group_stats",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "group_id", "season_id"})
)
@Getter
@Setter
@NoArgsConstructor
public class UserGroupStats {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "season_id", nullable = false)
    private Season season;

    @Column(nullable = false)
    private Long elo = 1500L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "current_tier_id")
    private LeagueTier currentTier;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "peak_tier_id")
    private LeagueTier peakTier;

    @Column(nullable = false)
    private Integer wins = 0;

    @Column(nullable = false)
    private Integer losses = 0;

    @Column(nullable = false)
    private Integer totalMatches = 0;

    @Column(nullable = false)
    private Integer winStreak = 0;

    @Column(nullable = false)
    private Integer bestWinStreak = 0;

    private LocalDateTime lastMatchDate;

    private LocalDateTime lastPromotionDate;

    private Long startingSeasonElo;

    /**
     * Update tier based on current ELO
     * Returns true if tier changed
     */
    public boolean updateTier() {
        LeagueTier newTier = season.getLeagueTierByElo(elo);

        if (newTier == null) {
            return false;
        }

        if (!newTier.equals(currentTier)) {
            LeagueTier oldTier = currentTier;
            currentTier = newTier;

            if (peakTier == null || newTier.getTierLevel() > peakTier.getTierLevel()) {
                peakTier = newTier;
                lastPromotionDate = LocalDateTime.now();
            }

            return true;
        }

        return false;
    }

    /**
     * Record a win and update stats
     */
    public void recordWin(int eloGain) {
        this.elo += eloGain;
        this.wins++;
        this.totalMatches++;
        this.winStreak++;
        this.lastMatchDate = LocalDateTime.now();

        if (winStreak > bestWinStreak) {
            bestWinStreak = winStreak;
        }

        updateTier();
    }

    /**
     * Record a loss and update stats
     */
    public void recordLoss(int eloLoss) {
        this.elo -= eloLoss;
        this.losses++;
        this.totalMatches++;
        this.winStreak = 0;
        this.lastMatchDate = LocalDateTime.now();

        updateTier();
    }

    /**
     * Get win rate as percentage
     */
    public double getWinRate() {
        if (totalMatches == 0) {
            return 0.0;
        }
        return (double) wins / totalMatches * 100.0;
    }

    /**
     * Check if player is on a hot streak (3+ wins)
     */
    public boolean isOnHotStreak() {
        return winStreak >= 3;
    }

    /**
     * Get ELO change from season start
     */
    public Long getSeasonEloChange() {
        if (startingSeasonElo == null) {
            return 0L;
        }
        return elo - startingSeasonElo;
    }

    /**
     * Check if near promotion
     */
    public boolean isNearPromotion() {
        return currentTier != null && currentTier.isNearPromotion(elo, 50);
    }

    /**
     * Check if near demotion
     */
    public boolean isNearDemotion() {
        return currentTier != null && currentTier.isNearDemotion(elo, 50);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserGroupStats)) return false;
        UserGroupStats that = (UserGroupStats) o;
        return user != null && group != null && season != null &&
                user.equals(that.user) && group.equals(that.group) && season.equals(that.season);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, group, season);
    }

    @Override
    public String toString() {
        return String.format("UserGroupStats{user=%s, tier=%s, elo=%d, record=%d-%d, season=%s}",
                user.getUsername(),
                currentTier != null ? currentTier.getName() : "Unranked",
                elo, wins, losses,
                season.getName());
    }
}