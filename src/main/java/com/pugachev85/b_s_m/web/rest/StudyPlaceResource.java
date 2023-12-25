package com.pugachev85.b_s_m.web.rest;

import com.pugachev85.b_s_m.domain.StudyPlace;
import com.pugachev85.b_s_m.repository.StudyPlaceRepository;
import com.pugachev85.b_s_m.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.pugachev85.b_s_m.domain.StudyPlace}.
 */
@RestController
@RequestMapping("/api/study-places")
@Transactional
public class StudyPlaceResource {

    private final Logger log = LoggerFactory.getLogger(StudyPlaceResource.class);

    private static final String ENTITY_NAME = "studyPlace";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final StudyPlaceRepository studyPlaceRepository;

    public StudyPlaceResource(StudyPlaceRepository studyPlaceRepository) {
        this.studyPlaceRepository = studyPlaceRepository;
    }

    /**
     * {@code POST  /study-places} : Create a new studyPlace.
     *
     * @param studyPlace the studyPlace to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new studyPlace, or with status {@code 400 (Bad Request)} if the studyPlace has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<StudyPlace> createStudyPlace(@Valid @RequestBody StudyPlace studyPlace) throws URISyntaxException {
        log.debug("REST request to save StudyPlace : {}", studyPlace);
        if (studyPlace.getId() != null) {
            throw new BadRequestAlertException("A new studyPlace cannot already have an ID", ENTITY_NAME, "idexists");
        }
        StudyPlace result = studyPlaceRepository.save(studyPlace);
        return ResponseEntity
            .created(new URI("/api/study-places/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /study-places/:id} : Updates an existing studyPlace.
     *
     * @param id the id of the studyPlace to save.
     * @param studyPlace the studyPlace to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated studyPlace,
     * or with status {@code 400 (Bad Request)} if the studyPlace is not valid,
     * or with status {@code 500 (Internal Server Error)} if the studyPlace couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<StudyPlace> updateStudyPlace(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody StudyPlace studyPlace
    ) throws URISyntaxException {
        log.debug("REST request to update StudyPlace : {}, {}", id, studyPlace);
        if (studyPlace.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, studyPlace.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!studyPlaceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        StudyPlace result = studyPlaceRepository.save(studyPlace);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, studyPlace.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /study-places/:id} : Partial updates given fields of an existing studyPlace, field will ignore if it is null
     *
     * @param id the id of the studyPlace to save.
     * @param studyPlace the studyPlace to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated studyPlace,
     * or with status {@code 400 (Bad Request)} if the studyPlace is not valid,
     * or with status {@code 404 (Not Found)} if the studyPlace is not found,
     * or with status {@code 500 (Internal Server Error)} if the studyPlace couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<StudyPlace> partialUpdateStudyPlace(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody StudyPlace studyPlace
    ) throws URISyntaxException {
        log.debug("REST request to partial update StudyPlace partially : {}, {}", id, studyPlace);
        if (studyPlace.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, studyPlace.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!studyPlaceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<StudyPlace> result = studyPlaceRepository
            .findById(studyPlace.getId())
            .map(existingStudyPlace -> {
                if (studyPlace.getTitle() != null) {
                    existingStudyPlace.setTitle(studyPlace.getTitle());
                }

                return existingStudyPlace;
            })
            .map(studyPlaceRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, studyPlace.getId().toString())
        );
    }

    /**
     * {@code GET  /study-places} : get all the studyPlaces.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of studyPlaces in body.
     */
    @GetMapping("")
    public List<StudyPlace> getAllStudyPlaces() {
        log.debug("REST request to get all StudyPlaces");
        return studyPlaceRepository.findAll();
    }

    /**
     * {@code GET  /study-places/:id} : get the "id" studyPlace.
     *
     * @param id the id of the studyPlace to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the studyPlace, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<StudyPlace> getStudyPlace(@PathVariable("id") Long id) {
        log.debug("REST request to get StudyPlace : {}", id);
        Optional<StudyPlace> studyPlace = studyPlaceRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(studyPlace);
    }

    /**
     * {@code DELETE  /study-places/:id} : delete the "id" studyPlace.
     *
     * @param id the id of the studyPlace to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudyPlace(@PathVariable("id") Long id) {
        log.debug("REST request to delete StudyPlace : {}", id);
        studyPlaceRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
