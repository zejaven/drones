package org.zeveon.drones.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.zeveon.drones.model.Model;
import org.zeveon.drones.model.State;
import org.zeveon.drones.validation.annotations.BatteryLevelHigherThan;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Stanislav Vafin
 */
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@BatteryLevelHigherThan(25)
@Table(name = "drone", schema = "drones")
public class Drone {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 100)
    @Column(name = "serial_number", columnDefinition = "VARCHAR(100)")
    private String serialNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "model", columnDefinition = "VARCHAR(15)")
    private Model model;

    @Max(500)
    @Column(name = "weight_limit")
    private Integer weightLimit;

    @Max(100)
    @Column(name = "battery_capacity", nullable = false, columnDefinition = "INTEGER DEFAULT 0")
    private Integer batteryCapacity;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "state", columnDefinition = "VARCHAR(15) DEFAULT 'IDLE'")
    private State state = State.IDLE;

    @Builder.Default
    @OneToMany(mappedBy = "drone")
    private List<Medication> medications = new ArrayList<>();
}
