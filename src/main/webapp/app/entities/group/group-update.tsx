import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IStudyPlace } from 'app/shared/model/study-place.model';
import { getEntities as getStudyPlaces } from 'app/entities/study-place/study-place.reducer';
import { IEducationalProgram } from 'app/shared/model/educational-program.model';
import { getEntities as getEducationalPrograms } from 'app/entities/educational-program/educational-program.reducer';
import { IGroup } from 'app/shared/model/group.model';
import { getEntity, updateEntity, createEntity, reset } from './group.reducer';

export const GroupUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const studyPlaces = useAppSelector(state => state.studyPlace.entities);
  const educationalPrograms = useAppSelector(state => state.educationalProgram.entities);
  const groupEntity = useAppSelector(state => state.group.entity);
  const loading = useAppSelector(state => state.group.loading);
  const updating = useAppSelector(state => state.group.updating);
  const updateSuccess = useAppSelector(state => state.group.updateSuccess);

  const handleClose = () => {
    navigate('/group');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getStudyPlaces({}));
    dispatch(getEducationalPrograms({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  // eslint-disable-next-line complexity
  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }

    const entity = {
      ...groupEntity,
      ...values,
      studyPlace: studyPlaces.find(it => it.id.toString() === values.studyPlace.toString()),
      educationalProgram: educationalPrograms.find(it => it.id.toString() === values.educationalProgram.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...groupEntity,
          studyPlace: groupEntity?.studyPlace?.id,
          educationalProgram: groupEntity?.educationalProgram?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="basicStudentsManagmentApp.group.home.createOrEditLabel" data-cy="GroupCreateUpdateHeading">
            <Translate contentKey="basicStudentsManagmentApp.group.home.createOrEditLabel">Create or edit a Group</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="group-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('basicStudentsManagmentApp.group.title')}
                id="group-title"
                name="title"
                data-cy="title"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('basicStudentsManagmentApp.group.acceptanceDate')}
                id="group-acceptanceDate"
                name="acceptanceDate"
                data-cy="acceptanceDate"
                type="date"
              />
              <ValidatedField
                id="group-studyPlace"
                name="studyPlace"
                data-cy="studyPlace"
                label={translate('basicStudentsManagmentApp.group.studyPlace')}
                type="select"
              >
                <option value="" key="0" />
                {studyPlaces
                  ? studyPlaces.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.title}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="group-educationalProgram"
                name="educationalProgram"
                data-cy="educationalProgram"
                label={translate('basicStudentsManagmentApp.group.educationalProgram')}
                type="select"
              >
                <option value="" key="0" />
                {educationalPrograms
                  ? educationalPrograms.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.title}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/group" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default GroupUpdate;
