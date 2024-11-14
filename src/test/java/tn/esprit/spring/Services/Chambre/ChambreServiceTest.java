package tn.esprit.spring.Services.Chambre;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.spring.DAO.Entities.Chambre;
import tn.esprit.spring.DAO.Entities.TypeChambre;
import tn.esprit.spring.DAO.Repositories.ChambreRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChambreServiceTest {

    @Mock
    private ChambreRepository chambreRepository;

    @InjectMocks
    private ChambreService chambreService;

    private Chambre chambre;

    @BeforeEach
    void setUp() {
        chambre = new Chambre();
        chambre.setIdChambre(1L);
        chambre.setNumeroChambre(101);
        chambre.setTypeC(TypeChambre.SIMPLE);
    }

    @Test
    void addOrUpdate_shouldSaveChambre() {
        when(chambreRepository.save(chambre)).thenReturn(chambre);
        Chambre savedChambre = chambreService.addOrUpdate(chambre);
        assertEquals(chambre, savedChambre);
        verify(chambreRepository, times(1)).save(chambre);
    }

    @Test
    void findAll_shouldReturnListOfChambres() {
        List<Chambre> chambres = Arrays.asList(chambre);
        when(chambreRepository.findAll()).thenReturn(chambres);
        List<Chambre> foundChambres = chambreService.findAll();
        assertEquals(1, foundChambres.size());
        assertEquals(chambre, foundChambres.get(0));
        verify(chambreRepository, times(1)).findAll();
    }

    @Test
    void findById_shouldReturnChambre_whenExists() {
        when(chambreRepository.findById(1L)).thenReturn(Optional.of(chambre));
        Chambre foundChambre = chambreService.findById(1L);
        assertEquals(chambre, foundChambre);
    }

    @Test
    void findById_shouldThrowException_whenNotFound() {
        when(chambreRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> chambreService.findById(1L));
    }

    @Test
    void deleteById_shouldDeleteChambre_whenExists() {
        doNothing().when(chambreRepository).deleteById(1L);
        chambreService.deleteById(1L);
        verify(chambreRepository, times(1)).deleteById(1L);
    }

    @Test
    void getChambresParNomBloc_shouldReturnChambres() {
        when(chambreRepository.findByBlocNomBloc("TestBloc")).thenReturn(Arrays.asList(chambre));
        List<Chambre> chambres = chambreService.getChambresParNomBloc("TestBloc");
        assertEquals(1, chambres.size());
        verify(chambreRepository, times(1)).findByBlocNomBloc("TestBloc");
    }
}
