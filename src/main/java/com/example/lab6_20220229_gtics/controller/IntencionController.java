package com.example.lab6_20220229_gtics.controller;

import com.example.lab6_20220229_gtics.dto.IntencionDto;
import com.example.lab6_20220229_gtics.entity.Intencion;
import com.example.lab6_20220229_gtics.repository.IntencionRepository;
import com.example.lab6_20220229_gtics.repository.UsuarioRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;

@Controller
public class IntencionController {

    private final IntencionRepository intencionRepository;
    private final UsuarioRepository usuarioRepository;

    public IntencionController(IntencionRepository intencionRepository, UsuarioRepository usuarioRepository) {
        this.intencionRepository = intencionRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping("/intenciones/new")
    public String mostrarFormularioNuevaIntencion(Model model) {
        model.addAttribute("intencion", new IntencionDto());
        return "intenciones/form";
    }

    @PostMapping("/intenciones/guardar")
    public String guardarIntencion(@Valid IntencionDto intencion,
                                   BindingResult br,
                                   HttpSession session,
                                   Authentication auth,
                                   RedirectAttributes ra) {
        if (Boolean.TRUE.equals(session.getAttribute("intencionRegistrada"))) {
            br.reject("duplicada", "Ya registraste una intención en esta sesión");
        }
        String texto = intencion.getDescripcion() == null ? "" : intencion.getDescripcion().toLowerCase();
        if (texto.contains("odio") || texto.contains("pelea")) {
            br.rejectValue("descripcion", "prohibidas", "Palabras no permitidas");
        }
        if (br.hasErrors()) {
            return "intenciones/form";
        }
        var usuario = usuarioRepository.findByCorreo(auth.getName()).orElseThrow();
        var entidad = new Intencion();
        entidad.setUsuario(usuario);
        entidad.setDescripcion(intencion.getDescripcion().trim());
        entidad.setFecha(LocalDateTime.now());
        intencionRepository.save(entidad);
        session.setAttribute("intencionRegistrada", true);
        ra.addFlashAttribute("msg", "Intención registrada");
        return "redirect:/dashboard";
    }

    @GetMapping("/admin/intenciones")
    public String listarTodas(Model model) {
        model.addAttribute("lista", intencionRepository.findAll());
        return "intenciones/admin_list";
    }
}
