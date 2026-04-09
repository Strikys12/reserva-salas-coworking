package com.reserva_salas_coworking.entity.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "Usuarios")
@Entity

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", nullable = false, length = 30)
    private String email;

    @Column(name = "esPremium", length = 30)
    private Boolean esPremium;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value = "reserva-usuario") // Permite serializar esta parte
    private List<Reserva> reservas = new ArrayList<>();

    public boolean esPremium() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'esPremium'");
    }

    public boolean isEsPremium() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
