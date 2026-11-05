package com.eryckavel.model;

import com.eryckavel.model.enums.CategoriaDespesa;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "despesa")
public class Despesa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "iddespesa")
    private Long id;
    @Column(name = "descricao", nullable = false, length = 150)
    private String descricao;
    @Column(name = "valor", nullable = false, columnDefinition = "NUMERIC(12,2)")
    private Double valor;
    @Column(name = "data", nullable = false)
    private LocalDate data;
    @Enumerated(EnumType.STRING)
    @Column(name = "categoria", nullable = false, length = 20)
    private CategoriaDespesa categoria;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idusuario", nullable = false)
    private Usuario usuario;

    public Despesa() {
    }

    public Despesa(Long id, String descricao, Double valor, LocalDate data, CategoriaDespesa categoria, Usuario usuario) {
        this.id = id;
        this.descricao = descricao;
        this.valor = valor;
        this.data = data;
        this.categoria = categoria;
        this.usuario = usuario;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public CategoriaDespesa getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaDespesa categoria) {
        this.categoria = categoria;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Despesa that = (Despesa) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}