package com.augustin.gabriel.goldenraspberryawardsapi.entities;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "film_studios")
public class FilmStudioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "film_id", nullable = false)
    private FilmEntity film;

    @ManyToOne
    @JoinColumn(name = "studio_id", nullable = false)
    private StudioEntity studio;

}
