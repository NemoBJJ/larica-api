package com.larica.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "donos_restaurante")
public class DonoRestaurante {
    
    // ATRIBUTOS
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String nome;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    @Column(nullable = false)
    private String senha;
    
    private String telefone;
    
    @Column(name = "data_cadastro", nullable = false)
    private LocalDate dataCadastro;

    // CONSTRUTORES
    public DonoRestaurante() {
        this.dataCadastro = LocalDate.now(); // Data atual como padrão
    }
    
    public DonoRestaurante(String nome, String email, String senha, String telefone) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.telefone = telefone;
        this.dataCadastro = LocalDate.now();
    }

    // GETTERS & SETTERS
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public LocalDate getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDate dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    // EQUALS & HASHCODE (Implementação robusta)
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        DonoRestaurante other = (DonoRestaurante) obj;
        return Objects.equals(id, other.id) && 
               Objects.equals(email, other.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email);
    }

    // TO STRING (Formatado para debug)
    @Override
    public String toString() {
        return "DonoRestaurante [" +
               "id=" + id + 
               ", nome=" + nome + 
               ", email=" + email + 
               ", telefone=" + telefone + 
               ", dataCadastro=" + dataCadastro + 
               "]";
    }
}