package org.zeveon.drones.entity;

import jakarta.persistence.*;
import lombok.*;
import org.zeveon.drones.validation.annotations.BatteryLevelHigherThan;
import org.zeveon.drones.validation.annotations.WeightLimitNotExceeded;

/**
 * @author Stanislav Vafin
 */
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "medication", schema = "drones")
public class Medication {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "weight")
    private Integer weight;

    @Column(name = "code")
    private String code;

    @Column(name = "image_path")
    private String imagePath;

    @Column(name = "image_content_type")
    private String imageContentType;

    @WeightLimitNotExceeded
    @BatteryLevelHigherThan(25)
    @ManyToOne
    @JoinColumn(name = "drone_id")
    private Drone drone;
}
