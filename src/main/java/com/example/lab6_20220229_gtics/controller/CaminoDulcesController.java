package com.example.lab6_20220229_gtics.controller;

import com.example.lab6_20220229_gtics.dto.JuegoDulcesSesion;
import com.example.lab6_20220229_gtics.entity.NumeroCasa;
import com.example.lab6_20220229_gtics.repository.NumeroCasaRepository;
import com.example.lab6_20220229_gtics.repository.UsuarioRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/juegos/dulces")
public class CaminoDulcesController {

    private final NumeroCasaRepository numeroCasaRepository;
    private final UsuarioRepository usuarioRepository;

    public CaminoDulcesController(NumeroCasaRepository numeroCasaRepository, UsuarioRepository usuarioRepository) {
        this.numeroCasaRepository = numeroCasaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping
    public String mostrarJuego(Authentication auth, HttpSession session, Model model) {
        var usuario = usuarioRepository.findByCorreo(auth.getName()).orElseThrow();
        JuegoDulcesSesion s = (JuegoDulcesSesion) session.getAttribute("juegoDulces");
        if (s == null || Boolean.TRUE.equals(s.getAdivinado())) {
            var opt = numeroCasaRepository.findFirstByUsuario_IdAndAdivinadoFalse(usuario.getId());
            if (opt.isEmpty()) {
                model.addAttribute("sinAsignacion", true);
                return "juegos/dulces/jugar";
            }
            NumeroCasa a = opt.get();
            s = new JuegoDulcesSesion();
            s.setAsignacionId(a.getId());
            s.setObjetivo(a.getNumeroObjetivo());
            s.setMinimoPasos((a.getNumeroObjetivo() + 5) / 6);
            session.setAttribute("juegoDulces", s);
        }
        model.addAttribute("estado", s);
        return "juegos/dulces/jugar";
    }

    @PostMapping("/intentar")
    public String intentar(@RequestParam("pasos") Integer pasos, HttpSession session, Model model) {
        JuegoDulcesSesion s = (JuegoDulcesSesion) session.getAttribute("juegoDulces");
        if (s == null || Boolean.TRUE.equals(s.getAdivinado())) {
            model.addAttribute("sinAsignacion", true);
            return "juegos/dulces/jugar";
        }
        s.setIntentos(s.getIntentos() + 1);
        if (pasos != null && pasos.equals(s.getMinimoPasos())) {
            s.setAdivinado(true);
            var asignacion = numeroCasaRepository.findById(s.getAsignacionId()).orElseThrow();
            asignacion.setIntentos(s.getIntentos());
            asignacion.setAdivinado(true);
            numeroCasaRepository.save(asignacion);
        }
        model.addAttribute("estado", s);
        return "juegos/dulces/jugar";
    }

    @GetMapping("/ranking")
    public String ranking(Model model) {
        model.addAttribute("top", numeroCasaRepository.findTop10ByAdivinadoTrueOrderByIntentosAsc());
        return "juegos/dulces/ranking";
    }

    @GetMapping("/admin/asignaciones")
    public String gestionarAsignaciones(Model model) {
        model.addAttribute("usuarios", usuarioRepository.findAll());
        model.addAttribute("asignaciones", numeroCasaRepository.findAll());
        return "juegos/dulces/admin_asignaciones";
    }

    @PostMapping("/admin/asignar")
    public String asignarNumero(@RequestParam("usuarioId") Long usuarioId,
                               @RequestParam("numero") Integer numero,
                               RedirectAttributes ra) {
        var usuario = usuarioRepository.findById(usuarioId).orElse(null);
        
        if (usuario == null || numero == null || numero <= 64) {
            ra.addFlashAttribute("error", "Usuario no encontrado o número inválido (debe ser > 64)");
            return "redirect:/juegos/dulces/admin/asignaciones";
        }
        
        // Verificar que no tenga asignación pendiente
        if (numeroCasaRepository.findFirstByUsuario_IdAndAdivinadoFalse(usuarioId).isPresent()) {
            ra.addFlashAttribute("error", "El usuario ya tiene un número asignado");
            return "redirect:/juegos/dulces/admin/asignaciones";
        }
        
        var asignacion = new NumeroCasa();
        asignacion.setUsuario(usuario);
        asignacion.setNumeroObjetivo(numero);
        asignacion.setIntentos(0);
        asignacion.setAdivinado(false);
        numeroCasaRepository.save(asignacion);
        
        ra.addFlashAttribute("msg", "Número asignado correctamente");
        return "redirect:/juegos/dulces/admin/asignaciones";
    }

    @PostMapping("/solicitar")
    public String solicitarAsignacion(Authentication auth, RedirectAttributes ra) {
        var usuario = usuarioRepository.findByCorreo(auth.getName()).orElseThrow();
        
        // Verificar que no tenga asignación pendiente
        if (numeroCasaRepository.findFirstByUsuario_IdAndAdivinadoFalse(usuario.getId()).isPresent()) {
            ra.addFlashAttribute("error", "Ya tienes un número asignado");
            return "redirect:/juegos/dulces";
        }
        
        ra.addFlashAttribute("msg", "Solicitud enviada al administrador");
        return "redirect:/juegos/dulces";
    }
}
