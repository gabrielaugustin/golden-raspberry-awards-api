package com.augustin.gabriel.goldenraspberryawardsapi.entities;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "film_producers")
public class FilmProducerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "film_id", nullable = false)
    private FilmEntity film;

    @ManyToOne
    @JoinColumn(name = "producer_id", nullable = false)
    private ProducerEntity producer;

}
