package isi.dan.msclientes.servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import isi.dan.msclientes.dao.ClienteRepository;
import isi.dan.msclientes.dto.ClienteDTO;
import isi.dan.msclientes.dto.CreateClienteDTO;
import isi.dan.msclientes.exception.ClienteNotFoundException;
import isi.dan.msclientes.model.Cliente;
import isi.dan.msclientes.utils.ClienteMapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Value("${maximo.descubierto}")
    private BigDecimal maximoDescubiertoDefault;

    public List<ClienteDTO> findAll() {
        return clienteRepository.findAll().stream().map(ClienteMapper::toDTO).collect(Collectors.toList());
    }

    public ClienteDTO findById(Integer id) throws ClienteNotFoundException {
        return ClienteMapper.toDTO(clienteRepository.findById(id)
                .orElseThrow(() -> new ClienteNotFoundException("Cliente " + id + " no encontrado")));
    }

    public ClienteDTO save(CreateClienteDTO payload) {
        // Do a single mapping to avoid boilerplate on Mapping util
        Cliente cliente = new Cliente();
        cliente.setNombre(payload.getNombre());
        cliente.setCorreoElectronico(payload.getCorreoElectronico());
        cliente.setCuit(payload.getCuit());
        if (payload.getMaximoDescubierto() == null) {
            cliente.setMaximoDescubierto(maximoDescubiertoDefault);
        } else {
            cliente.setMaximoDescubierto(payload.getMaximoDescubierto());
        }
        return ClienteMapper.toDTO(clienteRepository.save(cliente));
    }

    public ClienteDTO update(Integer id, ClienteDTO cliente) throws ClienteNotFoundException {
        Cliente entity = clienteRepository.findById(id)
                .orElseThrow(() -> new ClienteNotFoundException("Cliente " + id + " no encontrado"));
        ClienteMapper.updateClienteFromDTO(cliente, entity);
        return ClienteMapper.toDTO(clienteRepository.save(entity));
    }

    public void deleteById(Integer id) throws ClienteNotFoundException {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ClienteNotFoundException("Cliente " + id + " no encontrado para borrar"));

        clienteRepository.delete(cliente);
    }
}
