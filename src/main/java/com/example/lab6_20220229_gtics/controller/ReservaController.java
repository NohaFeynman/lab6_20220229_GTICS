package com.example.lab6_20220229_gtics.controller;

import com.example.lab6_20220229_gtics.entity.Mesa;
import com.example.lab6_20220229_gtics.entity.Reserva;
import com.example.lab6_20220229_gtics.repository.MesaRepository;
import com.example.lab6_20220229_gtics.repository.ReservaRepository;
import com.example.lab6_20220229_gtics.repository.UsuarioRepository;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;

@Controller
public class ReservaController {

    private final ReservaRepository reservaRepository;
    private final MesaRepository mesaRepository;
    private final UsuarioRepository usuarioRepository;

    public ReservaController(ReservaRepository reservaRepository, MesaRepository mesaRepository, UsuarioRepository usuarioRepository) {
        this.reservaRepository = reservaRepository;
        this.mesaRepository = mesaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping("/reservas")
    public String listarDelUsuario(Authentication auth, Model model) {
        var usuario = usuarioRepository.findByCorreo(auth.getName()).orElseThrow();
        model.addAttribute("misReservas", reservaRepository.findByUsuario_Id(usuario.getId()));
        model.addAttribute("libres", mesaRepository.countByDisponibleTrue());
        model.addAttribute("ocupadas", mesaRepository.countByDisponibleFalse());
        return "reservas/lista";
    }

    @GetMapping("/reservas/nueva")
    public String nueva(Authentication auth, Model model, RedirectAttributes ra) {
        var usuario = usuarioRepository.findByCorreo(auth.getName()).orElseThrow();
        if (reservaRepository.findFirstByUsuario_Id(usuario.getId()).isPresent()) {
            ra.addFlashAttribute("msg", "Ya tienes una reserva registrada");
            return "redirect:/reservas";
        }
        model.addAttribute("mesas", mesaRepository.findByDisponibleTrue());
        model.addAttribute("reserva", new Reserva());
        return "reservas/form";
    }

    @PostMapping("/reservas/guardar")
    public String guardar(Authentication auth,
                          @RequestParam("mesaId") @NotNull Long mesaId,
                          RedirectAttributes ra) {
        var usuario = usuarioRepository.findByCorreo(auth.getName()).orElseThrow();
        if (reservaRepository.findFirstByUsuario_Id(usuario.getId()).isPresent()) {
            ra.addFlashAttribute("msg", "Ya tienes una reserva registrada");
            return "redirect:/reservas";
        }
        var mesa = mesaRepository.findById(mesaId).orElse(null);
        if (mesa == null || Boolean.FALSE.equals(mesa.getDisponible())) {
            ra.addFlashAttribute("msg", "Mesa no disponible");
            return "redirect:/reservas/nueva";
        }
        var r = new Reserva();
        r.setUsuario(usuario);
        r.setMesa(mesa);
        r.setFecha(LocalDateTime.now());
        reservaRepository.save(r);
        mesa.setDisponible(false);
        mesaRepository.save(mesa);
        ra.addFlashAttribute("msg", "Reserva creada");
        return "redirect:/reservas";
    }

    @PostMapping("/reservas/cancelar/{id}")
    public String cancelar(Authentication auth, @PathVariable Long id, RedirectAttributes ra) {
        var usuario = usuarioRepository.findByCorreo(auth.getName()).orElseThrow();
        var r = reservaRepository.findById(id).orElse(null);
        if (r == null || !r.getUsuario().getId().equals(usuario.getId())) {
            ra.addFlashAttribute("msg", "Operación inválida");
            return "redirect:/reservas";
        }
        Mesa m = r.getMesa();
        reservaRepository.deleteById(id);
        m.setDisponible(true);
        mesaRepository.save(m);
        ra.addFlashAttribute("msg", "Reserva cancelada");
        return "redirect:/reservas";
    }

    @GetMapping("/admin/reservas")
    public String listarTodas(Model model) {
        model.addAttribute("todas", reservaRepository.findAll());
        model.addAttribute("libres", mesaRepository.countByDisponibleTrue());
        model.addAttribute("ocupadas", mesaRepository.countByDisponibleFalse());
        return "reservas/admin_list";
    }

    @GetMapping("/admin/mesas")
    public String listarMesas(Model model) {
        model.addAttribute("mesas", mesaRepository.findAll());
        model.addAttribute("nuevaMesa", new Mesa());
        return "mesas/lista";
    }

    @PostMapping("/admin/mesas/guardar")
    public String guardarMesa(@ModelAttribute Mesa mesa, RedirectAttributes ra) {
        if (mesa.getDisponible() == null) mesa.setDisponible(true);
        if (mesa.getCapacidad() == null) mesa.setCapacidad(4);
        mesaRepository.save(mesa);
        ra.addFlashAttribute("msg", "Mesa guardada");
        return "redirect:/admin/mesas";
    }

    @PostMapping("/admin/mesas/toggle/{id}")
    public String alternarDisponibilidad(@PathVariable Long id, RedirectAttributes ra) {
        var m = mesaRepository.findById(id).orElse(null);
        if (m != null) {
            m.setDisponible(!Boolean.TRUE.equals(m.getDisponible()));
            mesaRepository.save(m);
            ra.addFlashAttribute("msg", "Disponibilidad actualizada");
        }
        return "redirect:/admin/mesas";
    }

    @PostMapping("/admin/reservas/liberar/{id}")
    public String liberar(@PathVariable Long id, RedirectAttributes ra) {
        var r = reservaRepository.findById(id).orElse(null);
        if (r != null) {
            Mesa m = r.getMesa();
            reservaRepository.deleteById(id);
            m.setDisponible(true);
            mesaRepository.save(m);
            ra.addFlashAttribute("msg", "Mesa liberada");
        }
        return "redirect:/admin/reservas";
    }
}
