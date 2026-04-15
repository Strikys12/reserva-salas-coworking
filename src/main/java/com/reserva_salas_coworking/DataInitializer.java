package com.reserva_salas_coworking;


import com.reserva_salas_coworking.entity.model.Usuario;
import com.reserva_salas_coworking.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {
    private final UsuarioRepository usuarioRepository;

    // Inyección por constructor (más limpio que @Autowired)
    public DataInitializer(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (usuarioRepository.count() == 0) {
            System.out.println("--- Cargando 100 usuarios de prueba en Local ---");
            List<Usuario> usuarios = new ArrayList<>();

            for (int i = 1; i <= 100; i++) {
                Usuario u = new Usuario();
                u.setEmail("usuario" + i + "@correo.com");

                // Unos premium y otros no para probar tus validaciones
                u.setEsPremium(i % 5 == 0);

                usuarios.add(u);
            }

            usuarioRepository.saveAll(usuarios);
            System.out.println("--- ¡100 usuarios insertados melos! ---");
        } else {
            System.out.println("--- La base de datos ya tiene info, no se insertó nada ---");
        }
    }
}




