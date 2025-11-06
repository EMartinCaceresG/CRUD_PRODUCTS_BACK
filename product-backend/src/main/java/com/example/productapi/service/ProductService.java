package com.example.productapi.service;

import com.example.productapi.dto.ProductDTO;
import com.example.productapi.entity.Product;
import com.example.productapi.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio para gestión de productos
 * @author Enrique Caceres
 */
@Service
@Transactional
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ProductDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No se encontró el producto con ID: " + id));
        return convertToDTO(product);
    }

    public ProductDTO createProduct(ProductDTO productDTO) {
        // validar nombre duplicado
        if (productDTO.getNombre() != null && 
            productRepository.existsByNombre(productDTO.getNombre())) {
            throw new RuntimeException("El producto '" + productDTO.getNombre() + "' ya existe");
        }
        
        Product product = convertToEntity(productDTO);
        Product savedProduct = productRepository.save(product);
        return convertToDTO(savedProduct);
    }

    public ProductDTO updateProduct(Long id, ProductDTO productDTO) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + id));

        // chequear si el nombre ya está en uso por otro producto
        if (productDTO.getNombre() != null && 
            !existingProduct.getNombre().equals(productDTO.getNombre()) &&
            productRepository.existsByNombre(productDTO.getNombre())) {
            throw new RuntimeException("El nombre '" + productDTO.getNombre() + "' ya está en uso");
        }

        // actualizar solo los campos que vienen en el DTO
        if (productDTO.getNombre() != null) {
            existingProduct.setNombre(productDTO.getNombre());
        }
        if (productDTO.getDescripcion() != null) {
            existingProduct.setDescripcion(productDTO.getDescripcion());
        }
        if (productDTO.getPrecio() != null) {
            existingProduct.setPrecio(productDTO.getPrecio());
        }
        if (productDTO.getStock() != null) {
            existingProduct.setStock(productDTO.getStock());
        }
        if (productDTO.getCategoria() != null) {
            existingProduct.setCategoria(productDTO.getCategoria());
        }

        Product updatedProduct = productRepository.save(existingProduct);
        return convertToDTO(updatedProduct);
    }

    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("No se puede eliminar, producto no existe: " + id);
        }
        productRepository.deleteById(id);
    }

    // búsqueda simple por nombre (case-insensitive)
    public List<ProductDTO> searchProductsByName(String nombre) {
        return productRepository.findByNombreContainingIgnoreCase(nombre)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // helper para convertir entidad a DTO
    private ProductDTO convertToDTO(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setNombre(product.getNombre());
        dto.setDescripcion(product.getDescripcion());
        dto.setPrecio(product.getPrecio());
        dto.setStock(product.getStock());
        dto.setCategoria(product.getCategoria());
        dto.setFechaCreacion(product.getFechaCreacion());
        dto.setFechaActualizacion(product.getFechaActualizacion());
        return dto;
    }

    // helper para convertir DTO a entidad
    private Product convertToEntity(ProductDTO productDTO) {
        Product product = new Product();
        product.setId(productDTO.getId());
        product.setNombre(productDTO.getNombre());
        product.setDescripcion(productDTO.getDescripcion());
        product.setPrecio(productDTO.getPrecio());
        product.setStock(productDTO.getStock());
        product.setCategoria(productDTO.getCategoria());
        return product;
    }
}

