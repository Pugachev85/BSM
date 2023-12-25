package com.pugachev85.b_s_m.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.pugachev85.b_s_m.IntegrationTest;
import com.pugachev85.b_s_m.domain.StudyPlace;
import com.pugachev85.b_s_m.repository.StudyPlaceRepository;
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
 * Integration tests for the {@link StudyPlaceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class StudyPlaceResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/study-places";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private StudyPlaceRepository studyPlaceRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restStudyPlaceMockMvc;

    private StudyPlace studyPlace;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StudyPlace createEntity(EntityManager em) {
        StudyPlace studyPlace = new StudyPlace().title(DEFAULT_TITLE);
        return studyPlace;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StudyPlace createUpdatedEntity(EntityManager em) {
        StudyPlace studyPlace = new StudyPlace().title(UPDATED_TITLE);
        return studyPlace;
    }

    @BeforeEach
    public void initTest() {
        studyPlace = createEntity(em);
    }

    @Test
    @Transactional
    void createStudyPlace() throws Exception {
        int databaseSizeBeforeCreate = studyPlaceRepository.findAll().size();
        // Create the StudyPlace
        restStudyPlaceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(studyPlace)))
            .andExpect(status().isCreated());

        // Validate the StudyPlace in the database
        List<StudyPlace> studyPlaceList = studyPlaceRepository.findAll();
        assertThat(studyPlaceList).hasSize(databaseSizeBeforeCreate + 1);
        StudyPlace testStudyPlace = studyPlaceList.get(studyPlaceList.size() - 1);
        assertThat(testStudyPlace.getTitle()).isEqualTo(DEFAULT_TITLE);
    }

    @Test
    @Transactional
    void createStudyPlaceWithExistingId() throws Exception {
        // Create the StudyPlace with an existing ID
        studyPlace.setId(1L);

        int databaseSizeBeforeCreate = studyPlaceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restStudyPlaceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(studyPlace)))
            .andExpect(status().isBadRequest());

        // Validate the StudyPlace in the database
        List<StudyPlace> studyPlaceList = studyPlaceRepository.findAll();
        assertThat(studyPlaceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = studyPlaceRepository.findAll().size();
        // set the field null
        studyPlace.setTitle(null);

        // Create the StudyPlace, which fails.

        restStudyPlaceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(studyPlace)))
            .andExpect(status().isBadRequest());

        List<StudyPlace> studyPlaceList = studyPlaceRepository.findAll();
        assertThat(studyPlaceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllStudyPlaces() throws Exception {
        // Initialize the database
        studyPlaceRepository.saveAndFlush(studyPlace);

        // Get all the studyPlaceList
        restStudyPlaceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(studyPlace.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)));
    }

    @Test
    @Transactional
    void getStudyPlace() throws Exception {
        // Initialize the database
        studyPlaceRepository.saveAndFlush(studyPlace);

        // Get the studyPlace
        restStudyPlaceMockMvc
            .perform(get(ENTITY_API_URL_ID, studyPlace.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(studyPlace.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE));
    }

    @Test
    @Transactional
    void getNonExistingStudyPlace() throws Exception {
        // Get the studyPlace
        restStudyPlaceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingStudyPlace() throws Exception {
        // Initialize the database
        studyPlaceRepository.saveAndFlush(studyPlace);

        int databaseSizeBeforeUpdate = studyPlaceRepository.findAll().size();

        // Update the studyPlace
        StudyPlace updatedStudyPlace = studyPlaceRepository.findById(studyPlace.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedStudyPlace are not directly saved in db
        em.detach(updatedStudyPlace);
        updatedStudyPlace.title(UPDATED_TITLE);

        restStudyPlaceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedStudyPlace.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedStudyPlace))
            )
            .andExpect(status().isOk());

        // Validate the StudyPlace in the database
        List<StudyPlace> studyPlaceList = studyPlaceRepository.findAll();
        assertThat(studyPlaceList).hasSize(databaseSizeBeforeUpdate);
        StudyPlace testStudyPlace = studyPlaceList.get(studyPlaceList.size() - 1);
        assertThat(testStudyPlace.getTitle()).isEqualTo(UPDATED_TITLE);
    }

    @Test
    @Transactional
    void putNonExistingStudyPlace() throws Exception {
        int databaseSizeBeforeUpdate = studyPlaceRepository.findAll().size();
        studyPlace.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStudyPlaceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, studyPlace.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(studyPlace))
            )
            .andExpect(status().isBadRequest());

        // Validate the StudyPlace in the database
        List<StudyPlace> studyPlaceList = studyPlaceRepository.findAll();
        assertThat(studyPlaceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchStudyPlace() throws Exception {
        int databaseSizeBeforeUpdate = studyPlaceRepository.findAll().size();
        studyPlace.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStudyPlaceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(studyPlace))
            )
            .andExpect(status().isBadRequest());

        // Validate the StudyPlace in the database
        List<StudyPlace> studyPlaceList = studyPlaceRepository.findAll();
        assertThat(studyPlaceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamStudyPlace() throws Exception {
        int databaseSizeBeforeUpdate = studyPlaceRepository.findAll().size();
        studyPlace.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStudyPlaceMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(studyPlace)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the StudyPlace in the database
        List<StudyPlace> studyPlaceList = studyPlaceRepository.findAll();
        assertThat(studyPlaceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateStudyPlaceWithPatch() throws Exception {
        // Initialize the database
        studyPlaceRepository.saveAndFlush(studyPlace);

        int databaseSizeBeforeUpdate = studyPlaceRepository.findAll().size();

        // Update the studyPlace using partial update
        StudyPlace partialUpdatedStudyPlace = new StudyPlace();
        partialUpdatedStudyPlace.setId(studyPlace.getId());

        restStudyPlaceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStudyPlace.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStudyPlace))
            )
            .andExpect(status().isOk());

        // Validate the StudyPlace in the database
        List<StudyPlace> studyPlaceList = studyPlaceRepository.findAll();
        assertThat(studyPlaceList).hasSize(databaseSizeBeforeUpdate);
        StudyPlace testStudyPlace = studyPlaceList.get(studyPlaceList.size() - 1);
        assertThat(testStudyPlace.getTitle()).isEqualTo(DEFAULT_TITLE);
    }

    @Test
    @Transactional
    void fullUpdateStudyPlaceWithPatch() throws Exception {
        // Initialize the database
        studyPlaceRepository.saveAndFlush(studyPlace);

        int databaseSizeBeforeUpdate = studyPlaceRepository.findAll().size();

        // Update the studyPlace using partial update
        StudyPlace partialUpdatedStudyPlace = new StudyPlace();
        partialUpdatedStudyPlace.setId(studyPlace.getId());

        partialUpdatedStudyPlace.title(UPDATED_TITLE);

        restStudyPlaceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStudyPlace.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStudyPlace))
            )
            .andExpect(status().isOk());

        // Validate the StudyPlace in the database
        List<StudyPlace> studyPlaceList = studyPlaceRepository.findAll();
        assertThat(studyPlaceList).hasSize(databaseSizeBeforeUpdate);
        StudyPlace testStudyPlace = studyPlaceList.get(studyPlaceList.size() - 1);
        assertThat(testStudyPlace.getTitle()).isEqualTo(UPDATED_TITLE);
    }

    @Test
    @Transactional
    void patchNonExistingStudyPlace() throws Exception {
        int databaseSizeBeforeUpdate = studyPlaceRepository.findAll().size();
        studyPlace.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStudyPlaceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, studyPlace.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(studyPlace))
            )
            .andExpect(status().isBadRequest());

        // Validate the StudyPlace in the database
        List<StudyPlace> studyPlaceList = studyPlaceRepository.findAll();
        assertThat(studyPlaceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchStudyPlace() throws Exception {
        int databaseSizeBeforeUpdate = studyPlaceRepository.findAll().size();
        studyPlace.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStudyPlaceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(studyPlace))
            )
            .andExpect(status().isBadRequest());

        // Validate the StudyPlace in the database
        List<StudyPlace> studyPlaceList = studyPlaceRepository.findAll();
        assertThat(studyPlaceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamStudyPlace() throws Exception {
        int databaseSizeBeforeUpdate = studyPlaceRepository.findAll().size();
        studyPlace.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStudyPlaceMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(studyPlace))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the StudyPlace in the database
        List<StudyPlace> studyPlaceList = studyPlaceRepository.findAll();
        assertThat(studyPlaceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteStudyPlace() throws Exception {
        // Initialize the database
        studyPlaceRepository.saveAndFlush(studyPlace);

        int databaseSizeBeforeDelete = studyPlaceRepository.findAll().size();

        // Delete the studyPlace
        restStudyPlaceMockMvc
            .perform(delete(ENTITY_API_URL_ID, studyPlace.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<StudyPlace> studyPlaceList = studyPlaceRepository.findAll();
        assertThat(studyPlaceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
