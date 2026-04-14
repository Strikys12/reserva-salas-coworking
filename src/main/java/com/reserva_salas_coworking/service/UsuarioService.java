package com.reserva_salas_coworking.service;


import com.reserva_salas_coworking.entity.model.Usuario;
import com.reserva_salas_coworking.repository.ReservaRepository;
import com.reserva_salas_coworking.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ReservaRepository reservaRepository;

    //GetAll
    public List<Usuario> getAllUsuarios() {
        return usuarioRepository.findAll();
    }

    //GetByid

    public Usuario getUsuarioById(Long id) {
        return usuarioRepository.findById(id).orElse(null);
    }


    public Usuario saveUsuario(Usuario usuario){
        if(usuarioRepository.existsByEmail(usuario.getEmail())){
            throw new RuntimeException("El email ya está registrado!!");
        }
        return usuarioRepository.save(usuario);
    }

    //CreateUsuarios
    @Transactional
    public List<Usuario> saveUsuarios(List<Usuario> usuarios) {

        for(Usuario u : usuarios){
            if(usuarioRepository.existsByEmail(u.getEmail())) {
                throw new RuntimeException("El email ya está registrado!!!");
            }
        }

        return usuarioRepository.saveAll(usuarios);
    }

    @Transactional
    //DeleteUsuario
    public void deleteUsuario(Long id) {
        if (reservaRepository.existsByUsuarioId(id)){
            throw new RuntimeException("No se puede eliminar el usuario porque tiene reservas asociadas");
        }
        usuarioRepository.deleteById(id);
    }


    @Transactional
    //Actualizar Usuario
    public Usuario updateUsuario(Long id, Usuario usuario) {
        Usuario usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));

        // Validar email único (si se está actualizando el email)
        if (!usuarioExistente.getEmail().equals(usuario.getEmail()) && usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new RuntimeException("El email ya está registrado por otro usuario");
        }

        // Actualizar campos
        usuarioExistente.setEmail(usuario.getEmail());
        usuarioExistente.setEsPremium(usuario.isEsPremium());

        return usuarioRepository.save(usuarioExistente);
    }
}
