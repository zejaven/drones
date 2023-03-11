package org.zeveon.drones.entity;

import jakarta.persistence.*;
import lombok.*;
import org.zeveon.drones.model.Model;
import org.zeveon.drones.model.State;

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
@Table(name = "drone", schema = "drones")
public class Drone {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "serial_number", columnDefinition = "VARCHAR(100)")
    private String serialNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "model", columnDefinition = "VARCHAR(15)")
    private Model model;

    @Column(name = "weight_limit")
    private Integer weightLimit;

    @Column(name = "battery_capacity")
    private Integer batteryCapacity;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "state", columnDefinition = "VARCHAR(15) DEFAULT 'IDLE'")
    private State state = State.IDLE;

    @Builder.Default
    @OneToMany(mappedBy = "drone")
    private List<Medication> medications = new ArrayList<>();
}
