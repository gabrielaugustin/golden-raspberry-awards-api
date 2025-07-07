package com.augustin.gabriel.goldenraspberryawardsapi.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "films")
public class FilmEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private Integer nominationYear;

    @Column(nullable = false)
    private Boolean winner;

    @Builder.Default
    @OneToMany(mappedBy = "film", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FilmStudioEntity> filmStudios = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "film", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FilmProducerEntity> filmProducers = new ArrayList<>();

    public void setFilmStudios(List<StudioEntity> studios) {
        this.filmStudios = studios.stream()
                .map((s) -> FilmStudioEntity.builder().film(this).studio(s).build())
                .toList();
    }

    public void setFilmProducers(List<ProducerEntity> producers) {
        this.filmProducers = producers.stream()
                .map((p) -> FilmProducerEntity.builder().film(this).producer(p).build())
                .toList();
    }

}
