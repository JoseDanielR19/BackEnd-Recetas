package com.example.demo.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "recipes")
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Este campo no puede quedar vacío; por favor, añade una imagen.")
    @Size(min = 4)
    @Column(name = "url_img", unique = true)
    private String urlImg;

    @NotNull(message = "Este campo no puede quedar vacío; por favor, añade un titulo.")
    @Size(min = 4, max = 35)
    @Column(name = "title")
    private String title;

    @NotNull(message = "Este campo no puede quedar vacío; por favor, añade las instrucciones.")
    @Size(min = 4, max = 10000)
    @Column(name = "instructions")
    private String instructions;

    @NotNull(message = "Este campo no puede quedar vacío; por favor, añade un país.")
    @Size(min = 2, max = 20)
    @Column(name = "country")
    private String country;

    @NotNull(message = "Este campo no puede quedar vacío; por favor, añade un video.")
    @Size(min = 4)
    @Column(name = "url_video", unique = true)
    private String urlVideo;

    // Asociación muchos a uno
    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull(message = "El campo category_id es obligatorio")
    @JoinColumn(name = "category_id")
    // @JsonProperty(access = Access.WRITE_ONLY)
    @JsonIgnoreProperties("recipes")
    private Category category;
}
