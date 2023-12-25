package com.pugachev85.b_s_m.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.pugachev85.b_s_m.IntegrationTest;
import com.pugachev85.b_s_m.domain.AcademicSubject;
import com.pugachev85.b_s_m.repository.AcademicSubjectRepository;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link AcademicSubjectResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AcademicSubjectResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final Integer DEFAULT_HOURS = 1;
    private static final Integer UPDATED_HOURS = 2;

    private static final String ENTITY_API_URL = "/api/academic-subjects";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AcademicSubjectRepository academicSubjectRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAcademicSubjectMockMvc;

    private AcademicSubject academicSubject;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AcademicSubject createEntity(EntityManager em) {
        AcademicSubject academicSubject = new AcademicSubject().title(DEFAULT_TITLE).hours(DEFAULT_HOURS);
        return academicSubject;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AcademicSubject createUpdatedEntity(EntityManager em) {
        AcademicSubject academicSubject = new AcademicSubject().title(UPDATED_TITLE).hours(UPDATED_HOURS);
        return academicSubject;
    }

    @BeforeEach
    public void initTest() {
        academicSubject = createEntity(em);
    }

    @Test
    @Transactional
    void createAcademicSubject() throws Exception {
        int databaseSizeBeforeCreate = academicSubjectRepository.findAll().size();
        // Create the AcademicSubject
        restAcademicSubjectMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(academicSubject))
            )
            .andExpect(status().isCreated());

        // Validate the AcademicSubject in the database
        List<AcademicSubject> academicSubjectList = academicSubjectRepository.findAll();
        assertThat(academicSubjectList).hasSize(databaseSizeBeforeCreate + 1);
        AcademicSubject testAcademicSubject = academicSubjectList.get(academicSubjectList.size() - 1);
        assertThat(testAcademicSubject.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testAcademicSubject.getHours()).isEqualTo(DEFAULT_HOURS);
    }

    @Test
    @Transactional
    void createAcademicSubjectWithExistingId() throws Exception {
        // Create the AcademicSubject with an existing ID
        academicSubject.setId(1L);

        int databaseSizeBeforeCreate = academicSubjectRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAcademicSubjectMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(academicSubject))
            )
            .andExpect(status().isBadRequest());

        // Validate the AcademicSubject in the database
        List<AcademicSubject> academicSubjectList = academicSubjectRepository.findAll();
        assertThat(academicSubjectList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = academicSubjectRepository.findAll().size();
        // set the field null
        academicSubject.setTitle(null);

        // Create the AcademicSubject, which fails.

        restAcademicSubjectMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(academicSubject))
            )
            .andExpect(status().isBadRequest());

        List<AcademicSubject> academicSubjectList = academicSubjectRepository.findAll();
        assertThat(academicSubjectList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkHoursIsRequired() throws Exception {
        int databaseSizeBeforeTest = academicSubjectRepository.findAll().size();
        // set the field null
        academicSubject.setHours(null);

        // Create the AcademicSubject, which fails.

        restAcademicSubjectMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(academicSubject))
            )
            .andExpect(status().isBadRequest());

        List<AcademicSubject> academicSubjectList = academicSubjectRepository.findAll();
        assertThat(academicSubjectList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAcademicSubjects() throws Exception {
        // Initialize the database
        academicSubjectRepository.saveAndFlush(academicSubject);

        // Get all the academicSubjectList
        restAcademicSubjectMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(academicSubject.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].hours").value(hasItem(DEFAULT_HOURS)));
    }

    @Test
    @Transactional
    void getAcademicSubject() throws Exception {
        // Initialize the database
        academicSubjectRepository.saveAndFlush(academicSubject);

        // Get the academicSubject
        restAcademicSubjectMockMvc
            .perform(get(ENTITY_API_URL_ID, academicSubject.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(academicSubject.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.hours").value(DEFAULT_HOURS));
    }

    @Test
    @Transactional
    void getNonExistingAcademicSubject() throws Exception {
        // Get the academicSubject
        restAcademicSubjectMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAcademicSubject() throws Exception {
        // Initialize the database
        academicSubjectRepository.saveAndFlush(academicSubject);

        int databaseSizeBeforeUpdate = academicSubjectRepository.findAll().size();

        // Update the academicSubject
        AcademicSubject updatedAcademicSubject = academicSubjectRepository.findById(academicSubject.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAcademicSubject are not directly saved in db
        em.detach(updatedAcademicSubject);
        updatedAcademicSubject.title(UPDATED_TITLE).hours(UPDATED_HOURS);

        restAcademicSubjectMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAcademicSubject.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAcademicSubject))
            )
            .andExpect(status().isOk());

        // Validate the AcademicSubject in the database
        List<AcademicSubject> academicSubjectList = academicSubjectRepository.findAll();
        assertThat(academicSubjectList).hasSize(databaseSizeBeforeUpdate);
        AcademicSubject testAcademicSubject = academicSubjectList.get(academicSubjectList.size() - 1);
        assertThat(testAcademicSubject.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testAcademicSubject.getHours()).isEqualTo(UPDATED_HOURS);
    }

    @Test
    @Transactional
    void putNonExistingAcademicSubject() throws Exception {
        int databaseSizeBeforeUpdate = academicSubjectRepository.findAll().size();
        academicSubject.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAcademicSubjectMockMvc
            .perform(
                put(ENTITY_API_URL_ID, academicSubject.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(academicSubject))
            )
            .andExpect(status().isBadRequest());

        // Validate the AcademicSubject in the database
        List<AcademicSubject> academicSubjectList = academicSubjectRepository.findAll();
        assertThat(academicSubjectList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAcademicSubject() throws Exception {
        int databaseSizeBeforeUpdate = academicSubjectRepository.findAll().size();
        academicSubject.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAcademicSubjectMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(academicSubject))
            )
            .andExpect(status().isBadRequest());

        // Validate the AcademicSubject in the database
        List<AcademicSubject> academicSubjectList = academicSubjectRepository.findAll();
        assertThat(academicSubjectList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAcademicSubject() throws Exception {
        int databaseSizeBeforeUpdate = academicSubjectRepository.findAll().size();
        academicSubject.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAcademicSubjectMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(academicSubject))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AcademicSubject in the database
        List<AcademicSubject> academicSubjectList = academicSubjectRepository.findAll();
        assertThat(academicSubjectList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAcademicSubjectWithPatch() throws Exception {
        // Initialize the database
        academicSubjectRepository.saveAndFlush(academicSubject);

        int databaseSizeBeforeUpdate = academicSubjectRepository.findAll().size();

        // Update the academicSubject using partial update
        AcademicSubject partialUpdatedAcademicSubject = new AcademicSubject();
        partialUpdatedAcademicSubject.setId(academicSubject.getId());

        partialUpdatedAcademicSubject.hours(UPDATED_HOURS);

        restAcademicSubjectMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAcademicSubject.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAcademicSubject))
            )
            .andExpect(status().isOk());

        // Validate the AcademicSubject in the database
        List<AcademicSubject> academicSubjectList = academicSubjectRepository.findAll();
        assertThat(academicSubjectList).hasSize(databaseSizeBeforeUpdate);
        AcademicSubject testAcademicSubject = academicSubjectList.get(academicSubjectList.size() - 1);
        assertThat(testAcademicSubject.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testAcademicSubject.getHours()).isEqualTo(UPDATED_HOURS);
    }

    @Test
    @Transactional
    void fullUpdateAcademicSubjectWithPatch() throws Exception {
        // Initialize the database
        academicSubjectRepository.saveAndFlush(academicSubject);

        int databaseSizeBeforeUpdate = academicSubjectRepository.findAll().size();

        // Update the academicSubject using partial update
        AcademicSubject partialUpdatedAcademicSubject = new AcademicSubject();
        partialUpdatedAcademicSubject.setId(academicSubject.getId());

        partialUpdatedAcademicSubject.title(UPDATED_TITLE).hours(UPDATED_HOURS);

        restAcademicSubjectMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAcademicSubject.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAcademicSubject))
            )
            .andExpect(status().isOk());

        // Validate the AcademicSubject in the database
        List<AcademicSubject> academicSubjectList = academicSubjectRepository.findAll();
        assertThat(academicSubjectList).hasSize(databaseSizeBeforeUpdate);
        AcademicSubject testAcademicSubject = academicSubjectList.get(academicSubjectList.size() - 1);
        assertThat(testAcademicSubject.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testAcademicSubject.getHours()).isEqualTo(UPDATED_HOURS);
    }

    @Test
    @Transactional
    void patchNonExistingAcademicSubject() throws Exception {
        int databaseSizeBeforeUpdate = academicSubjectRepository.findAll().size();
        academicSubject.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAcademicSubjectMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, academicSubject.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(academicSubject))
            )
            .andExpect(status().isBadRequest());

        // Validate the AcademicSubject in the database
        List<AcademicSubject> academicSubjectList = academicSubjectRepository.findAll();
        assertThat(academicSubjectList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAcademicSubject() throws Exception {
        int databaseSizeBeforeUpdate = academicSubjectRepository.findAll().size();
        academicSubject.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAcademicSubjectMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(academicSubject))
            )
            .andExpect(status().isBadRequest());

        // Validate the AcademicSubject in the database
        List<AcademicSubject> academicSubjectList = academicSubjectRepository.findAll();
        assertThat(academicSubjectList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAcademicSubject() throws Exception {
        int databaseSizeBeforeUpdate = academicSubjectRepository.findAll().size();
        academicSubject.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAcademicSubjectMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(academicSubject))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AcademicSubject in the database
        List<AcademicSubject> academicSubjectList = academicSubjectRepository.findAll();
        assertThat(academicSubjectList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAcademicSubject() throws Exception {
        // Initialize the database
        academicSubjectRepository.saveAndFlush(academicSubject);

        int databaseSizeBeforeDelete = academicSubjectRepository.findAll().size();

        // Delete the academicSubject
        restAcademicSubjectMockMvc
            .perform(delete(ENTITY_API_URL_ID, academicSubject.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AcademicSubject> academicSubjectList = academicSubjectRepository.findAll();
        assertThat(academicSubjectList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
