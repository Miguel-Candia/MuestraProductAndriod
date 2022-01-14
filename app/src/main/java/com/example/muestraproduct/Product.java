package com.example.muestraproduct;


public class Product {
    private String codigo;
    private String descripcion;
    private String stock;
    private String ubicacion;

    public Product() {
    }

    public Product(String codigo, String descripcion, String stock, String ubicacion) {
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.stock = stock;
        this.ubicacion = ubicacion;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    @Override
    public String toString(){
        return "Codigo: " + codigo + " | Descripción: " + descripcion;
    }
}