package com.example.lab6_20220229_gtics.controller;

import com.example.lab6_20220229_gtics.entity.Heroe;
import com.example.lab6_20220229_gtics.repository.HeroeRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
public class HeroeController {

    private final HeroeRepository heroeRepository;

    public HeroeController(HeroeRepository heroeRepository) {
        this.heroeRepository = heroeRepository;
    }

    @GetMapping("/admin/heroes")
    public String listarHeroesAdmin(Model model) {
        model.addAttribute("lista", heroeRepository.findAll());
        List<Heroe> heroes = heroeRepository.findAll();
        Heroe heroe = heroes.get(0);

        System.out.println("heroes: " + heroe.getNombre());
        return "heroes/lista";
    }
    @GetMapping("/heroes")
    public String listarHeroesVisitante(Model model) {
        model.addAttribute("lista", heroeRepository.findAll());
        List<Heroe> heroes = heroeRepository.findAll();
        Heroe heroe = heroes.get(0);
        System.out.println("heroes: " + heroe.getNombre());
        return "heroes/lista";
    }

    @GetMapping("/admin/heroes/nuevo")
    public String mostrarFormularioNuevoHeroe(Model model) {
        model.addAttribute("heroe", new Heroe());
        return "heroes/form";
    }

    @PostMapping("/admin/heroes/guardar")
    public String guardarHeroe(@ModelAttribute("heroe") @Valid Heroe heroe) {
        heroeRepository.save(heroe);
        return "redirect:/heroes";
    }

    @GetMapping("/admin/heroes/editar/{id}")
    public String mostrarFormularioEditarHeroe(@PathVariable Long id, Model model) {
        Optional<Heroe> opt = heroeRepository.findById(id);
        if (opt.isPresent()) {
            model.addAttribute("heroe", opt.get());
            return "heroes/form";
        }
        return "redirect:/heroes";
    }

    @PostMapping("/admin/heroes/eliminar/{id}")
    public String eliminarHeroe(@PathVariable Long id) {
        heroeRepository.deleteById(id);
        return "redirect:/heroes";
    }
}
