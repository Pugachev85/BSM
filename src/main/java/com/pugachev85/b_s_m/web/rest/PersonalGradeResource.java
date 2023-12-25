package com.pugachev85.b_s_m.web.rest;

import com.pugachev85.b_s_m.domain.PersonalGrade;
import com.pugachev85.b_s_m.repository.PersonalGradeRepository;
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
 * REST controller for managing {@link com.pugachev85.b_s_m.domain.PersonalGrade}.
 */
@RestController
@RequestMapping("/api/personal-grades")
@Transactional
public class PersonalGradeResource {

    private final Logger log = LoggerFactory.getLogger(PersonalGradeResource.class);

    private static final String ENTITY_NAME = "personalGrade";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PersonalGradeRepository personalGradeRepository;

    public PersonalGradeResource(PersonalGradeRepository personalGradeRepository) {
        this.personalGradeRepository = personalGradeRepository;
    }

    /**
     * {@code POST  /personal-grades} : Create a new personalGrade.
     *
     * @param personalGrade the personalGrade to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new personalGrade, or with status {@code 400 (Bad Request)} if the personalGrade has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PersonalGrade> createPersonalGrade(@Valid @RequestBody PersonalGrade personalGrade) throws URISyntaxException {
        log.debug("REST request to save PersonalGrade : {}", personalGrade);
        if (personalGrade.getId() != null) {
            throw new BadRequestAlertException("A new personalGrade cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PersonalGrade result = personalGradeRepository.save(personalGrade);
        return ResponseEntity
            .created(new URI("/api/personal-grades/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /personal-grades/:id} : Updates an existing personalGrade.
     *
     * @param id the id of the personalGrade to save.
     * @param personalGrade the personalGrade to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated personalGrade,
     * or with status {@code 400 (Bad Request)} if the personalGrade is not valid,
     * or with status {@code 500 (Internal Server Error)} if the personalGrade couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PersonalGrade> updatePersonalGrade(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PersonalGrade personalGrade
    ) throws URISyntaxException {
        log.debug("REST request to update PersonalGrade : {}, {}", id, personalGrade);
        if (personalGrade.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, personalGrade.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!personalGradeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PersonalGrade result = personalGradeRepository.save(personalGrade);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, personalGrade.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /personal-grades/:id} : Partial updates given fields of an existing personalGrade, field will ignore if it is null
     *
     * @param id the id of the personalGrade to save.
     * @param personalGrade the personalGrade to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated personalGrade,
     * or with status {@code 400 (Bad Request)} if the personalGrade is not valid,
     * or with status {@code 404 (Not Found)} if the personalGrade is not found,
     * or with status {@code 500 (Internal Server Error)} if the personalGrade couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PersonalGrade> partialUpdatePersonalGrade(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PersonalGrade personalGrade
    ) throws URISyntaxException {
        log.debug("REST request to partial update PersonalGrade partially : {}, {}", id, personalGrade);
        if (personalGrade.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, personalGrade.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!personalGradeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PersonalGrade> result = personalGradeRepository
            .findById(personalGrade.getId())
            .map(existingPersonalGrade -> {
                if (personalGrade.getGrade() != null) {
                    existingPersonalGrade.setGrade(personalGrade.getGrade());
                }

                return existingPersonalGrade;
            })
            .map(personalGradeRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, personalGrade.getId().toString())
        );
    }

    /**
     * {@code GET  /personal-grades} : get all the personalGrades.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of personalGrades in body.
     */
    @GetMapping("")
    public List<PersonalGrade> getAllPersonalGrades(
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get all PersonalGrades");
        if (eagerload) {
            return personalGradeRepository.findAllWithEagerRelationships();
        } else {
            return personalGradeRepository.findAll();
        }
    }

    /**
     * {@code GET  /personal-grades/:id} : get the "id" personalGrade.
     *
     * @param id the id of the personalGrade to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the personalGrade, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PersonalGrade> getPersonalGrade(@PathVariable("id") Long id) {
        log.debug("REST request to get PersonalGrade : {}", id);
        Optional<PersonalGrade> personalGrade = personalGradeRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(personalGrade);
    }

    /**
     * {@code DELETE  /personal-grades/:id} : delete the "id" personalGrade.
     *
     * @param id the id of the personalGrade to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePersonalGrade(@PathVariable("id") Long id) {
        log.debug("REST request to delete PersonalGrade : {}", id);
        personalGradeRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
