package com.example.lab6_20220229_gtics.controller;

import com.example.lab6_20220229_gtics.dto.JuegoCancionSesion;
import com.example.lab6_20220229_gtics.entity.AsignacionCancion;
import com.example.lab6_20220229_gtics.repository.AsignacionCancionRepository;
import com.example.lab6_20220229_gtics.repository.CancionRepository;
import com.example.lab6_20220229_gtics.repository.UsuarioRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/juegos/cancion")
public class JuegoCancionController {

    private final AsignacionCancionRepository asignacionCancionRepository;
    private final UsuarioRepository usuarioRepository;
    private final CancionRepository cancionRepository;

    public JuegoCancionController(AsignacionCancionRepository asignacionCancionRepository, 
                                  UsuarioRepository usuarioRepository,
                                  CancionRepository cancionRepository) {
        this.asignacionCancionRepository = asignacionCancionRepository;
        this.usuarioRepository = usuarioRepository;
        this.cancionRepository = cancionRepository;
    }

    @GetMapping
    public String mostrarJuego(Authentication auth, HttpSession session, Model model) {
        var usuario = usuarioRepository.findByCorreo(auth.getName()).orElseThrow();
        JuegoCancionSesion sesion = (JuegoCancionSesion) session.getAttribute("juegoCancion");
        if (sesion == null || Boolean.TRUE.equals(sesion.getAdivinada())) {
            var opt = asignacionCancionRepository.findFirstByUsuario_IdAndAdivinadaFalse(usuario.getId());
            if (opt.isEmpty()) {
                model.addAttribute("sinAsignacion", true);
                return "juegos/cancion/jugar";
            }
            AsignacionCancion a = opt.get();
            sesion = new JuegoCancionSesion();
            sesion.setAsignacionId(a.getId());
            sesion.setTitulo(a.getCancion().getTitulo());
            session.setAttribute("juegoCancion", sesion);
        }
        model.addAttribute("estado", sesion);
        return "juegos/cancion/jugar";
    }

    @PostMapping("/intentar")
    public String intentar(@RequestParam("intento") String intento, HttpSession session, Model model) {
        JuegoCancionSesion sesion = (JuegoCancionSesion) session.getAttribute("juegoCancion");
        if (sesion == null || Boolean.TRUE.equals(sesion.getAdivinada())) {
            model.addAttribute("sinAsignacion", true);
            return "juegos/cancion/jugar";
        }
        String a = intento == null ? "" : intento.trim().toLowerCase();
        String b = sesion.getTitulo().trim().toLowerCase();
        int n = Math.min(a.length(), b.length());
        int posiciones = 0;
        for (int i = 0; i < n; i++) if (a.charAt(i) == b.charAt(i)) posiciones++;
        int[] freq = new int[256];
        for (char c : b.toCharArray()) if (c < 256) freq[c]++;
        int letras = 0;
        for (char c : a.toCharArray()) if (c < 256 && freq[c] > 0) { letras++; freq[c]--; }
        sesion.setIntentos(sesion.getIntentos() + 1);
        sesion.setPosicionesCorrectas(posiciones);
        sesion.setLetrasCorrectas(letras);
        if (a.equals(b)) {
            sesion.setAdivinada(true);
            var asignacion = asignacionCancionRepository.findById(sesion.getAsignacionId()).orElseThrow();
            asignacion.setIntentos(sesion.getIntentos());
            asignacion.setAdivinada(true);
            asignacionCancionRepository.save(asignacion);
        }
        model.addAttribute("estado", sesion);
        return "juegos/cancion/jugar";
    }

    @GetMapping("/ranking")
    public String ranking(Model model) {
        model.addAttribute("top", asignacionCancionRepository.findTop10ByAdivinadaTrueOrderByIntentosAsc());
        return "juegos/cancion/ranking";
    }

    @GetMapping("/admin/asignaciones")
    public String gestionarAsignaciones(Model model) {
        model.addAttribute("usuarios", usuarioRepository.findAll());
        model.addAttribute("canciones", cancionRepository.findAll());
        model.addAttribute("asignaciones", asignacionCancionRepository.findAll());
        return "juegos/cancion/admin_asignaciones";
    }

    @PostMapping("/admin/asignar")
    public String asignarCancion(@RequestParam("usuarioId") Long usuarioId,
                                @RequestParam("cancionId") Long cancionId,
                                RedirectAttributes ra) {
        var usuario = usuarioRepository.findById(usuarioId).orElse(null);
        var cancion = cancionRepository.findById(cancionId).orElse(null);
        
        if (usuario == null || cancion == null) {
            ra.addFlashAttribute("error", "Usuario o canción no encontrados");
            return "redirect:/juegos/cancion/admin/asignaciones";
        }
        
        // Verificar que no tenga asignación pendiente
        if (asignacionCancionRepository.findFirstByUsuario_IdAndAdivinadaFalse(usuarioId).isPresent()) {
            ra.addFlashAttribute("error", "El usuario ya tiene una canción asignada");
            return "redirect:/juegos/cancion/admin/asignaciones";
        }
        
        var asignacion = new AsignacionCancion();
        asignacion.setUsuario(usuario);
        asignacion.setCancion(cancion);
        asignacion.setIntentos(0);
        asignacion.setAdivinada(false);
        asignacionCancionRepository.save(asignacion);
        
        ra.addFlashAttribute("msg", "Canción asignada correctamente");
        return "redirect:/juegos/cancion/admin/asignaciones";
    }

    @PostMapping("/solicitar")
    public String solicitarAsignacion(Authentication auth, RedirectAttributes ra) {
        var usuario = usuarioRepository.findByCorreo(auth.getName()).orElseThrow();
        
        // Verificar que no tenga asignación pendiente
        if (asignacionCancionRepository.findFirstByUsuario_IdAndAdivinadaFalse(usuario.getId()).isPresent()) {
            ra.addFlashAttribute("error", "Ya tienes una canción asignada");
            return "redirect:/juegos/cancion";
        }
        
        ra.addFlashAttribute("msg", "Solicitud enviada al administrador");
        return "redirect:/juegos/cancion";
    }
}
