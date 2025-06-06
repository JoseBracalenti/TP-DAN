package isi.dan.msclientes.dao;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import isi.dan.msclientes.model.Cliente;
import isi.dan.msclientes.model.EstadoDeObra;
import isi.dan.msclientes.model.Obra;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Testcontainers
@ActiveProfiles("db")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ObraRepositoryTest {

    Logger log = LoggerFactory.getLogger(ObraRepositoryTest.class);

    @Container
    public static MySQLContainer<?> mysqlContainer = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @Autowired
    private ObraRepository obraRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private EstadoDeObraRepository estadoDeObraRepository;

    private Cliente cliente;
    private EstadoDeObra estadoDeObra;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysqlContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mysqlContainer::getUsername);
        registry.add("spring.datasource.password", mysqlContainer::getPassword);
    }

    @BeforeEach
    void iniciarDatos(){
        cliente = new Cliente();
        cliente.setNombre("Test Cliente");
        cliente.setCorreoElectronico("test@cliente.com");
        cliente.setCuit("12998887776");
        clienteRepository.save(cliente);

        estadoDeObra = new EstadoDeObra();
        estadoDeObra.setEstado("HABILITADA");
        estadoDeObraRepository.save(estadoDeObra);
        
        Obra obra = new Obra();
        obra.setDireccion("Test Obra 999");
        obra.setLat(1);
        obra.setLng(1);
        obra.setPresupuesto(BigDecimal.valueOf(300));
        obra.setCliente(cliente);
        obra.setEstado(estadoDeObra);
        obraRepository.save(obra);
    }

    @BeforeEach
    void borrarDatos(){
        obraRepository.deleteAll();
    }

    @AfterAll
    static void stopContainer() {
        mysqlContainer.stop();
    }

    @Test
    void testSaveAndFindById() {
        Obra obra = new Obra();
        obra.setDireccion("Test Obra");
        obra.setLat(2);
        obra.setLng(2);
        obra.setPresupuesto(BigDecimal.valueOf(100));
        obra.setCliente(cliente);
        obra.setEstado(estadoDeObra);

        Optional<Obra> foundObra = obraRepository.findById(obra.getId());
        log.info("ENCONTRE: {} ",foundObra);
        assertThat(foundObra).isPresent();
        assertThat(foundObra.get().getDireccion()).isEqualTo("Test Obra");
    }

    @Test
    void testFindByPresupuesto() {
        List<Obra> resultado = obraRepository.findByPresupuestoGreaterThanEqual(BigDecimal.valueOf(200));
        log.info("ENCONTRE: {} ",resultado);
        assertThat(resultado.size()).isEqualTo(1);
        assertThat(resultado.get(0).getPresupuesto()).isGreaterThan(BigDecimal.valueOf(200));
        assertThat(resultado.get(1).getPresupuesto()).isGreaterThan(BigDecimal.valueOf(200));
    }

    @Test
    void testFindByClienteIdAndEstado_Estado() {
        List<Obra> obras = obraRepository.findByClienteIdAndEstado_Estado(1, "HABILITADA");
        log.info("ENCONTRE: {} ",obras);
        assertThat(obras.size()).isEqualTo(1);
        assertThat(obras.get(0).getEstado().getEstado()).isEqualTo("HABILITADA");
    }

}

