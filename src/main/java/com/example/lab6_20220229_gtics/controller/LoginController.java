package com.example.lab6_20220229_gtics.controller;
import com.example.lab6_20220229_gtics.repository.UsuarioRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {
    private final UsuarioRepository usuarioRepository;
    public LoginController(UsuarioRepository usuarioRepository){ this.usuarioRepository = usuarioRepository; }

    @GetMapping("/loginForm")
    public String mostrarFormularioLogin(){ return "login/form"; }

    @GetMapping("/dashboard")
    public String mostrarDashboard(HttpSession session, Authentication auth, Model model){
        if (auth != null && session.getAttribute("usuario") == null) {
            usuarioRepository.findByCorreo(auth.getName()).ifPresent(u -> session.setAttribute("usuario", u));
        }
        model.addAttribute("active","Dashboard");
        return "home/dashboard";
    }
}
