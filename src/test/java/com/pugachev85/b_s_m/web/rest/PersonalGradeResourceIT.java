package com.pugachev85.b_s_m.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.pugachev85.b_s_m.IntegrationTest;
import com.pugachev85.b_s_m.domain.PersonalGrade;
import com.pugachev85.b_s_m.repository.PersonalGradeRepository;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link PersonalGradeResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PersonalGradeResourceIT {

    private static final Integer DEFAULT_GRADE = 1;
    private static final Integer UPDATED_GRADE = 2;

    private static final String ENTITY_API_URL = "/api/personal-grades";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PersonalGradeRepository personalGradeRepository;

    @Mock
    private PersonalGradeRepository personalGradeRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPersonalGradeMockMvc;

    private PersonalGrade personalGrade;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PersonalGrade createEntity(EntityManager em) {
        PersonalGrade personalGrade = new PersonalGrade().grade(DEFAULT_GRADE);
        return personalGrade;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PersonalGrade createUpdatedEntity(EntityManager em) {
        PersonalGrade personalGrade = new PersonalGrade().grade(UPDATED_GRADE);
        return personalGrade;
    }

    @BeforeEach
    public void initTest() {
        personalGrade = createEntity(em);
    }

    @Test
    @Transactional
    void createPersonalGrade() throws Exception {
        int databaseSizeBeforeCreate = personalGradeRepository.findAll().size();
        // Create the PersonalGrade
        restPersonalGradeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(personalGrade)))
            .andExpect(status().isCreated());

        // Validate the PersonalGrade in the database
        List<PersonalGrade> personalGradeList = personalGradeRepository.findAll();
        assertThat(personalGradeList).hasSize(databaseSizeBeforeCreate + 1);
        PersonalGrade testPersonalGrade = personalGradeList.get(personalGradeList.size() - 1);
        assertThat(testPersonalGrade.getGrade()).isEqualTo(DEFAULT_GRADE);
    }

    @Test
    @Transactional
    void createPersonalGradeWithExistingId() throws Exception {
        // Create the PersonalGrade with an existing ID
        personalGrade.setId(1L);

        int databaseSizeBeforeCreate = personalGradeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPersonalGradeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(personalGrade)))
            .andExpect(status().isBadRequest());

        // Validate the PersonalGrade in the database
        List<PersonalGrade> personalGradeList = personalGradeRepository.findAll();
        assertThat(personalGradeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkGradeIsRequired() throws Exception {
        int databaseSizeBeforeTest = personalGradeRepository.findAll().size();
        // set the field null
        personalGrade.setGrade(null);

        // Create the PersonalGrade, which fails.

        restPersonalGradeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(personalGrade)))
            .andExpect(status().isBadRequest());

        List<PersonalGrade> personalGradeList = personalGradeRepository.findAll();
        assertThat(personalGradeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPersonalGrades() throws Exception {
        // Initialize the database
        personalGradeRepository.saveAndFlush(personalGrade);

        // Get all the personalGradeList
        restPersonalGradeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(personalGrade.getId().intValue())))
            .andExpect(jsonPath("$.[*].grade").value(hasItem(DEFAULT_GRADE)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPersonalGradesWithEagerRelationshipsIsEnabled() throws Exception {
        when(personalGradeRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPersonalGradeMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(personalGradeRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPersonalGradesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(personalGradeRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPersonalGradeMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(personalGradeRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getPersonalGrade() throws Exception {
        // Initialize the database
        personalGradeRepository.saveAndFlush(personalGrade);

        // Get the personalGrade
        restPersonalGradeMockMvc
            .perform(get(ENTITY_API_URL_ID, personalGrade.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(personalGrade.getId().intValue()))
            .andExpect(jsonPath("$.grade").value(DEFAULT_GRADE));
    }

    @Test
    @Transactional
    void getNonExistingPersonalGrade() throws Exception {
        // Get the personalGrade
        restPersonalGradeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPersonalGrade() throws Exception {
        // Initialize the database
        personalGradeRepository.saveAndFlush(personalGrade);

        int databaseSizeBeforeUpdate = personalGradeRepository.findAll().size();

        // Update the personalGrade
        PersonalGrade updatedPersonalGrade = personalGradeRepository.findById(personalGrade.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPersonalGrade are not directly saved in db
        em.detach(updatedPersonalGrade);
        updatedPersonalGrade.grade(UPDATED_GRADE);

        restPersonalGradeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPersonalGrade.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPersonalGrade))
            )
            .andExpect(status().isOk());

        // Validate the PersonalGrade in the database
        List<PersonalGrade> personalGradeList = personalGradeRepository.findAll();
        assertThat(personalGradeList).hasSize(databaseSizeBeforeUpdate);
        PersonalGrade testPersonalGrade = personalGradeList.get(personalGradeList.size() - 1);
        assertThat(testPersonalGrade.getGrade()).isEqualTo(UPDATED_GRADE);
    }

    @Test
    @Transactional
    void putNonExistingPersonalGrade() throws Exception {
        int databaseSizeBeforeUpdate = personalGradeRepository.findAll().size();
        personalGrade.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPersonalGradeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, personalGrade.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(personalGrade))
            )
            .andExpect(status().isBadRequest());

        // Validate the PersonalGrade in the database
        List<PersonalGrade> personalGradeList = personalGradeRepository.findAll();
        assertThat(personalGradeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPersonalGrade() throws Exception {
        int databaseSizeBeforeUpdate = personalGradeRepository.findAll().size();
        personalGrade.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonalGradeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(personalGrade))
            )
            .andExpect(status().isBadRequest());

        // Validate the PersonalGrade in the database
        List<PersonalGrade> personalGradeList = personalGradeRepository.findAll();
        assertThat(personalGradeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPersonalGrade() throws Exception {
        int databaseSizeBeforeUpdate = personalGradeRepository.findAll().size();
        personalGrade.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonalGradeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(personalGrade)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PersonalGrade in the database
        List<PersonalGrade> personalGradeList = personalGradeRepository.findAll();
        assertThat(personalGradeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePersonalGradeWithPatch() throws Exception {
        // Initialize the database
        personalGradeRepository.saveAndFlush(personalGrade);

        int databaseSizeBeforeUpdate = personalGradeRepository.findAll().size();

        // Update the personalGrade using partial update
        PersonalGrade partialUpdatedPersonalGrade = new PersonalGrade();
        partialUpdatedPersonalGrade.setId(personalGrade.getId());

        restPersonalGradeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPersonalGrade.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPersonalGrade))
            )
            .andExpect(status().isOk());

        // Validate the PersonalGrade in the database
        List<PersonalGrade> personalGradeList = personalGradeRepository.findAll();
        assertThat(personalGradeList).hasSize(databaseSizeBeforeUpdate);
        PersonalGrade testPersonalGrade = personalGradeList.get(personalGradeList.size() - 1);
        assertThat(testPersonalGrade.getGrade()).isEqualTo(DEFAULT_GRADE);
    }

    @Test
    @Transactional
    void fullUpdatePersonalGradeWithPatch() throws Exception {
        // Initialize the database
        personalGradeRepository.saveAndFlush(personalGrade);

        int databaseSizeBeforeUpdate = personalGradeRepository.findAll().size();

        // Update the personalGrade using partial update
        PersonalGrade partialUpdatedPersonalGrade = new PersonalGrade();
        partialUpdatedPersonalGrade.setId(personalGrade.getId());

        partialUpdatedPersonalGrade.grade(UPDATED_GRADE);

        restPersonalGradeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPersonalGrade.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPersonalGrade))
            )
            .andExpect(status().isOk());

        // Validate the PersonalGrade in the database
        List<PersonalGrade> personalGradeList = personalGradeRepository.findAll();
        assertThat(personalGradeList).hasSize(databaseSizeBeforeUpdate);
        PersonalGrade testPersonalGrade = personalGradeList.get(personalGradeList.size() - 1);
        assertThat(testPersonalGrade.getGrade()).isEqualTo(UPDATED_GRADE);
    }

    @Test
    @Transactional
    void patchNonExistingPersonalGrade() throws Exception {
        int databaseSizeBeforeUpdate = personalGradeRepository.findAll().size();
        personalGrade.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPersonalGradeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, personalGrade.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(personalGrade))
            )
            .andExpect(status().isBadRequest());

        // Validate the PersonalGrade in the database
        List<PersonalGrade> personalGradeList = personalGradeRepository.findAll();
        assertThat(personalGradeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPersonalGrade() throws Exception {
        int databaseSizeBeforeUpdate = personalGradeRepository.findAll().size();
        personalGrade.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonalGradeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(personalGrade))
            )
            .andExpect(status().isBadRequest());

        // Validate the PersonalGrade in the database
        List<PersonalGrade> personalGradeList = personalGradeRepository.findAll();
        assertThat(personalGradeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPersonalGrade() throws Exception {
        int databaseSizeBeforeUpdate = personalGradeRepository.findAll().size();
        personalGrade.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPersonalGradeMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(personalGrade))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PersonalGrade in the database
        List<PersonalGrade> personalGradeList = personalGradeRepository.findAll();
        assertThat(personalGradeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePersonalGrade() throws Exception {
        // Initialize the database
        personalGradeRepository.saveAndFlush(personalGrade);

        int databaseSizeBeforeDelete = personalGradeRepository.findAll().size();

        // Delete the personalGrade
        restPersonalGradeMockMvc
            .perform(delete(ENTITY_API_URL_ID, personalGrade.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PersonalGrade> personalGradeList = personalGradeRepository.findAll();
        assertThat(personalGradeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
