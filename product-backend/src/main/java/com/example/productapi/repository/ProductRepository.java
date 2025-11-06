package com.example.productapi.repository;

import com.example.productapi.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para operaciones de base de datos relacionadas con productos.
 * Extiende JpaRepository para obtener métodos CRUD básicos.
 * 
 * @author Enrique Caceres
 * @version 1.0.0
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * Busca productos por nombre (búsqueda parcial, case-insensitive).
     * 
     * @param nombre Nombre o parte del nombre a buscar
     * @return Lista de productos que coinciden con el nombre
     */
    List<Product> findByNombreContainingIgnoreCase(String nombre);

    /**
     * Busca productos por categoría.
     * 
     * @param categoria Categoría del producto
     * @return Lista de productos de la categoría especificada
     */
    List<Product> findByCategoria(String categoria);

    /**
     * Busca productos con stock menor o igual al valor especificado.
     * 
     * @param stock Cantidad máxima de stock
     * @return Lista de productos con stock bajo
     */
    List<Product> findByStockLessThanEqual(Integer stock);

    /**
     * Busca productos por rango de precios.
     * 
     * @param precioMin Precio mínimo
     * @param precioMax Precio máximo
     * @return Lista de productos dentro del rango de precios
     */
    @Query("SELECT p FROM Product p WHERE p.precio BETWEEN :precioMin AND :precioMax")
    List<Product> findByPrecioBetween(@Param("precioMin") java.math.BigDecimal precioMin, 
                                      @Param("precioMax") java.math.BigDecimal precioMax);

    /**
     * Verifica si existe un producto con el nombre especificado.
     * 
     * @param nombre Nombre del producto
     * @return true si existe, false en caso contrario
     */
    boolean existsByNombre(String nombre);
}

