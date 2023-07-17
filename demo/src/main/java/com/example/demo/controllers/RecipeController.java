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

import com.example.demo.entities.Recipe;
import com.example.demo.helpers.Helpers;
import com.example.demo.models.Response;
import com.example.demo.repositories.RecipeRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/recipe")
@CrossOrigin(origins = "*")
public class RecipeController {
    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private Helpers helpers;

    // Obtener todas las recetas
    @GetMapping
    public ResponseEntity<?> getRecipes() {
        Map<String, Object> response = new HashMap<>();

        try {
            List<Recipe> recipes = recipeRepository.findAllWithCategory();
            response = helpers.apiResponse(new Response<>(true, "Lista de recetas", recipes));
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (DataAccessException e) {
            return helpers.catchError(e, "Error al consultar las recetas");
        }
    }

    // Obtener una receta por el id
    @GetMapping("/{id}")
    public ResponseEntity<?> getRecipesById(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();

        try {
            Recipe recipeFound = recipeRepository.findById(id).orElse(null);
            if (recipeFound == null) {
                response = helpers.apiResponse(new Response<>(false, "Receta no encontrada", ""));
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            response = helpers.apiResponse(new Response<>(true, "Receta encontrada", recipeFound));
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (DataAccessException e) {
            return helpers.catchError(e, "Error al encontrar la receta");
        }
    }

    // Guardar receta
    @PostMapping
    public ResponseEntity<?> saveRecipe(@Valid @RequestBody Recipe recipe, BindingResult result) {
        Map<String, Object> response = new HashMap<>();
        if (result.hasErrors()) {
            return helpers.validarCampos(result);
        }

        try {
            recipeRepository.save(recipe);
            response = helpers.apiResponse(new Response<>(true, "Receta guardada", recipe));
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (DataAccessException e) {
            return helpers.catchError(e, "Error al guardar la receta");
        }
    }

    // Eliminar receta
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRecipe(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();

        try {
            Recipe recipeFound = recipeRepository.findById(id).orElse(null);
            if (recipeFound == null) {
                response = helpers.apiResponse(new Response<>(false, "Receta no encontrada", ""));
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            recipeRepository.delete(recipeFound);
            response = helpers.apiResponse(new Response<>(true, "Receta eliminada", ""));
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (DataAccessException e) {
            return helpers.catchError(e, "Error al encontrar la receta");
        }
    }
}
