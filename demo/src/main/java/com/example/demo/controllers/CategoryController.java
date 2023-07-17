package com.example.demo.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entities.Category;
import com.example.demo.helpers.Helpers;
import com.example.demo.models.Response;
import com.example.demo.repositories.CategoryRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/category")
@CrossOrigin(origins = "*")
public class CategoryController {
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private Helpers helpers;

    // Obtener todas las categorias
    @GetMapping
    public ResponseEntity<?> getCategories() {
        Map<String, Object> response = new HashMap<>();

        try {
            List<Category> categories = categoryRepository.findAll();
            response = helpers.apiResponse(new Response<>(true, "Lista de categorias", categories));
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (DataAccessException e) {
            return helpers.catchError(e, "Error al consultar las categorías");
        }
    }

    // Obtener una categoría por el id
    @GetMapping("/{id}")
    public ResponseEntity<?> getCategoriesById(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();

        try {
            Category categoryFound = categoryRepository.findById(id).orElse(null);
            if (categoryFound == null) {
                response = helpers.apiResponse(new Response<>(false, "Categoría no encontrada", ""));
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            response = helpers.apiResponse(new Response<>(true, "Categoría encontrada", categoryFound));
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (DataAccessException e) {
            return helpers.catchError(e, "Error al encontrar la categoría");
        }
    }

    // Guardar categoría
    @PostMapping
    public ResponseEntity<?> saveCategory(@Valid @RequestBody Category category, BindingResult result) {
        Map<String, Object> response = new HashMap<>();
        if (result.hasErrors()) {
            return helpers.validarCampos(result);
        }

        try {
            categoryRepository.save(category);
            response = helpers.apiResponse(new Response<>(true, "Categoría guardada", category));
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (DataAccessException e) {
            return helpers.catchError(e, "Error al guardar la categoría");
        }
    }

    // Eliminar categoría
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();

        try {
            Category categoryFound = categoryRepository.findById(id).orElse(null);
            if (categoryFound == null) {
                response = helpers.apiResponse(new Response<>(false, "Categoría no encontrada", ""));
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            categoryRepository.delete(categoryFound);
            response = helpers.apiResponse(new Response<>(true, "Categoría eliminada", ""));
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (DataAccessException e) {
            return helpers.catchError(e, "Error al encontrar la categoría");
        }
    }
}
