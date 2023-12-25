package com.pugachev85.b_s_m.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.pugachev85.b_s_m.IntegrationTest;
import com.pugachev85.b_s_m.domain.EducationalProgram;
import com.pugachev85.b_s_m.repository.EducationalProgramRepository;
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
 * Integration tests for the {@link EducationalProgramResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class EducationalProgramResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final Double DEFAULT_MONTH_LENGTH = 0.5D;
    private static final Double UPDATED_MONTH_LENGTH = 1D;

    private static final String ENTITY_API_URL = "/api/educational-programs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EducationalProgramRepository educationalProgramRepository;

    @Mock
    private EducationalProgramRepository educationalProgramRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEducationalProgramMockMvc;

    private EducationalProgram educationalProgram;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EducationalProgram createEntity(EntityManager em) {
        EducationalProgram educationalProgram = new EducationalProgram().title(DEFAULT_TITLE).monthLength(DEFAULT_MONTH_LENGTH);
        return educationalProgram;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EducationalProgram createUpdatedEntity(EntityManager em) {
        EducationalProgram educationalProgram = new EducationalProgram().title(UPDATED_TITLE).monthLength(UPDATED_MONTH_LENGTH);
        return educationalProgram;
    }

    @BeforeEach
    public void initTest() {
        educationalProgram = createEntity(em);
    }

    @Test
    @Transactional
    void createEducationalProgram() throws Exception {
        int databaseSizeBeforeCreate = educationalProgramRepository.findAll().size();
        // Create the EducationalProgram
        restEducationalProgramMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(educationalProgram))
            )
            .andExpect(status().isCreated());

        // Validate the EducationalProgram in the database
        List<EducationalProgram> educationalProgramList = educationalProgramRepository.findAll();
        assertThat(educationalProgramList).hasSize(databaseSizeBeforeCreate + 1);
        EducationalProgram testEducationalProgram = educationalProgramList.get(educationalProgramList.size() - 1);
        assertThat(testEducationalProgram.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testEducationalProgram.getMonthLength()).isEqualTo(DEFAULT_MONTH_LENGTH);
    }

    @Test
    @Transactional
    void createEducationalProgramWithExistingId() throws Exception {
        // Create the EducationalProgram with an existing ID
        educationalProgram.setId(1L);

        int databaseSizeBeforeCreate = educationalProgramRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEducationalProgramMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(educationalProgram))
            )
            .andExpect(status().isBadRequest());

        // Validate the EducationalProgram in the database
        List<EducationalProgram> educationalProgramList = educationalProgramRepository.findAll();
        assertThat(educationalProgramList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = educationalProgramRepository.findAll().size();
        // set the field null
        educationalProgram.setTitle(null);

        // Create the EducationalProgram, which fails.

        restEducationalProgramMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(educationalProgram))
            )
            .andExpect(status().isBadRequest());

        List<EducationalProgram> educationalProgramList = educationalProgramRepository.findAll();
        assertThat(educationalProgramList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMonthLengthIsRequired() throws Exception {
        int databaseSizeBeforeTest = educationalProgramRepository.findAll().size();
        // set the field null
        educationalProgram.setMonthLength(null);

        // Create the EducationalProgram, which fails.

        restEducationalProgramMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(educationalProgram))
            )
            .andExpect(status().isBadRequest());

        List<EducationalProgram> educationalProgramList = educationalProgramRepository.findAll();
        assertThat(educationalProgramList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEducationalPrograms() throws Exception {
        // Initialize the database
        educationalProgramRepository.saveAndFlush(educationalProgram);

        // Get all the educationalProgramList
        restEducationalProgramMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(educationalProgram.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].monthLength").value(hasItem(DEFAULT_MONTH_LENGTH.doubleValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllEducationalProgramsWithEagerRelationshipsIsEnabled() throws Exception {
        when(educationalProgramRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restEducationalProgramMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(educationalProgramRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllEducationalProgramsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(educationalProgramRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restEducationalProgramMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(educationalProgramRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getEducationalProgram() throws Exception {
        // Initialize the database
        educationalProgramRepository.saveAndFlush(educationalProgram);

        // Get the educationalProgram
        restEducationalProgramMockMvc
            .perform(get(ENTITY_API_URL_ID, educationalProgram.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(educationalProgram.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.monthLength").value(DEFAULT_MONTH_LENGTH.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingEducationalProgram() throws Exception {
        // Get the educationalProgram
        restEducationalProgramMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEducationalProgram() throws Exception {
        // Initialize the database
        educationalProgramRepository.saveAndFlush(educationalProgram);

        int databaseSizeBeforeUpdate = educationalProgramRepository.findAll().size();

        // Update the educationalProgram
        EducationalProgram updatedEducationalProgram = educationalProgramRepository.findById(educationalProgram.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEducationalProgram are not directly saved in db
        em.detach(updatedEducationalProgram);
        updatedEducationalProgram.title(UPDATED_TITLE).monthLength(UPDATED_MONTH_LENGTH);

        restEducationalProgramMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedEducationalProgram.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedEducationalProgram))
            )
            .andExpect(status().isOk());

        // Validate the EducationalProgram in the database
        List<EducationalProgram> educationalProgramList = educationalProgramRepository.findAll();
        assertThat(educationalProgramList).hasSize(databaseSizeBeforeUpdate);
        EducationalProgram testEducationalProgram = educationalProgramList.get(educationalProgramList.size() - 1);
        assertThat(testEducationalProgram.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testEducationalProgram.getMonthLength()).isEqualTo(UPDATED_MONTH_LENGTH);
    }

    @Test
    @Transactional
    void putNonExistingEducationalProgram() throws Exception {
        int databaseSizeBeforeUpdate = educationalProgramRepository.findAll().size();
        educationalProgram.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEducationalProgramMockMvc
            .perform(
                put(ENTITY_API_URL_ID, educationalProgram.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(educationalProgram))
            )
            .andExpect(status().isBadRequest());

        // Validate the EducationalProgram in the database
        List<EducationalProgram> educationalProgramList = educationalProgramRepository.findAll();
        assertThat(educationalProgramList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEducationalProgram() throws Exception {
        int databaseSizeBeforeUpdate = educationalProgramRepository.findAll().size();
        educationalProgram.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEducationalProgramMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(educationalProgram))
            )
            .andExpect(status().isBadRequest());

        // Validate the EducationalProgram in the database
        List<EducationalProgram> educationalProgramList = educationalProgramRepository.findAll();
        assertThat(educationalProgramList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEducationalProgram() throws Exception {
        int databaseSizeBeforeUpdate = educationalProgramRepository.findAll().size();
        educationalProgram.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEducationalProgramMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(educationalProgram))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EducationalProgram in the database
        List<EducationalProgram> educationalProgramList = educationalProgramRepository.findAll();
        assertThat(educationalProgramList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEducationalProgramWithPatch() throws Exception {
        // Initialize the database
        educationalProgramRepository.saveAndFlush(educationalProgram);

        int databaseSizeBeforeUpdate = educationalProgramRepository.findAll().size();

        // Update the educationalProgram using partial update
        EducationalProgram partialUpdatedEducationalProgram = new EducationalProgram();
        partialUpdatedEducationalProgram.setId(educationalProgram.getId());

        partialUpdatedEducationalProgram.title(UPDATED_TITLE);

        restEducationalProgramMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEducationalProgram.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEducationalProgram))
            )
            .andExpect(status().isOk());

        // Validate the EducationalProgram in the database
        List<EducationalProgram> educationalProgramList = educationalProgramRepository.findAll();
        assertThat(educationalProgramList).hasSize(databaseSizeBeforeUpdate);
        EducationalProgram testEducationalProgram = educationalProgramList.get(educationalProgramList.size() - 1);
        assertThat(testEducationalProgram.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testEducationalProgram.getMonthLength()).isEqualTo(DEFAULT_MONTH_LENGTH);
    }

    @Test
    @Transactional
    void fullUpdateEducationalProgramWithPatch() throws Exception {
        // Initialize the database
        educationalProgramRepository.saveAndFlush(educationalProgram);

        int databaseSizeBeforeUpdate = educationalProgramRepository.findAll().size();

        // Update the educationalProgram using partial update
        EducationalProgram partialUpdatedEducationalProgram = new EducationalProgram();
        partialUpdatedEducationalProgram.setId(educationalProgram.getId());

        partialUpdatedEducationalProgram.title(UPDATED_TITLE).monthLength(UPDATED_MONTH_LENGTH);

        restEducationalProgramMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEducationalProgram.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEducationalProgram))
            )
            .andExpect(status().isOk());

        // Validate the EducationalProgram in the database
        List<EducationalProgram> educationalProgramList = educationalProgramRepository.findAll();
        assertThat(educationalProgramList).hasSize(databaseSizeBeforeUpdate);
        EducationalProgram testEducationalProgram = educationalProgramList.get(educationalProgramList.size() - 1);
        assertThat(testEducationalProgram.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testEducationalProgram.getMonthLength()).isEqualTo(UPDATED_MONTH_LENGTH);
    }

    @Test
    @Transactional
    void patchNonExistingEducationalProgram() throws Exception {
        int databaseSizeBeforeUpdate = educationalProgramRepository.findAll().size();
        educationalProgram.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEducationalProgramMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, educationalProgram.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(educationalProgram))
            )
            .andExpect(status().isBadRequest());

        // Validate the EducationalProgram in the database
        List<EducationalProgram> educationalProgramList = educationalProgramRepository.findAll();
        assertThat(educationalProgramList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEducationalProgram() throws Exception {
        int databaseSizeBeforeUpdate = educationalProgramRepository.findAll().size();
        educationalProgram.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEducationalProgramMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(educationalProgram))
            )
            .andExpect(status().isBadRequest());

        // Validate the EducationalProgram in the database
        List<EducationalProgram> educationalProgramList = educationalProgramRepository.findAll();
        assertThat(educationalProgramList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEducationalProgram() throws Exception {
        int databaseSizeBeforeUpdate = educationalProgramRepository.findAll().size();
        educationalProgram.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEducationalProgramMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(educationalProgram))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EducationalProgram in the database
        List<EducationalProgram> educationalProgramList = educationalProgramRepository.findAll();
        assertThat(educationalProgramList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEducationalProgram() throws Exception {
        // Initialize the database
        educationalProgramRepository.saveAndFlush(educationalProgram);

        int databaseSizeBeforeDelete = educationalProgramRepository.findAll().size();

        // Delete the educationalProgram
        restEducationalProgramMockMvc
            .perform(delete(ENTITY_API_URL_ID, educationalProgram.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<EducationalProgram> educationalProgramList = educationalProgramRepository.findAll();
        assertThat(educationalProgramList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
