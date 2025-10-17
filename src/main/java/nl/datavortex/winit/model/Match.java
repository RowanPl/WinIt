package nl.datavortex.winit.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name="Matches")
public class Match {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.UUID)
    private UUID uuid;

    private String matchDetails;

    private String result;

    private String date;

    private UUID teamAId;

    private UUID teamBId;
}
