package com.elize.stock.model;

import java.math.BigDecimal;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Product {

    @PrimaryKey(autoGenerate = true)
    private final long id;
    private final String nome;
    private final BigDecimal preco;
    private final int quantidade;

    public Product(long id, String nome, BigDecimal preco, int quantidade) {
        this.id = id;
        this.nome = nome;
        this.preco = preco;
        this.quantidade = quantidade;
    }

    public long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public BigDecimal getPreco() {
        if (preco != null) {
            return preco.setScale(2, BigDecimal.ROUND_HALF_EVEN);
        } else {
            return null;
        }

    }
    public int getQuantidade() {
        return quantidade;
    }

}
