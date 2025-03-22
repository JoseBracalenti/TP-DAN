package isi.dan.msclientes.servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import isi.dan.msclientes.dao.ClienteRepository;
import isi.dan.msclientes.dao.UserRepository;
import isi.dan.msclientes.dto.CreateUserDTO;
import isi.dan.msclientes.exception.ClienteNotFoundException;
import isi.dan.msclientes.exception.InvalidCredentialsException;
import isi.dan.msclientes.exception.UserNotFoundException;
import isi.dan.msclientes.model.Cliente;
import isi.dan.msclientes.model.User;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    public User getUserByEmail(String email) throws UserNotFoundException {
        return userRepository.findByCorreoElectronico(email)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));
    }

    public User authenticate(String email, String password) throws UserNotFoundException, InvalidCredentialsException {
        User user = userRepository.findByCorreoElectronico(email)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));
        if (user.getPassword().equals(password)) {
            throw new InvalidCredentialsException("La contraseña ingresada es incorrecta.");
        }
        ;
        return null;
    }

    public User save(CreateUserDTO dto) throws ClienteNotFoundException {
        User user = new User();
        user.setCorreoElectronico(dto.getEmail());
        user.setPassword(dto.getPassword());
        Cliente cliente = clienteRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new ClienteNotFoundException("Cliente no encontrado"));
        user.setCliente(cliente);
        return userRepository.save(user);
    }

}
