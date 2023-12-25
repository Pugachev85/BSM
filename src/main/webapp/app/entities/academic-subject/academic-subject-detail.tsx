import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './academic-subject.reducer';

export const AcademicSubjectDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const academicSubjectEntity = useAppSelector(state => state.academicSubject.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="academicSubjectDetailsHeading">
          <Translate contentKey="basicStudentsManagmentApp.academicSubject.detail.title">AcademicSubject</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{academicSubjectEntity.id}</dd>
          <dt>
            <span id="title">
              <Translate contentKey="basicStudentsManagmentApp.academicSubject.title">Title</Translate>
            </span>
          </dt>
          <dd>{academicSubjectEntity.title}</dd>
          <dt>
            <span id="hours">
              <Translate contentKey="basicStudentsManagmentApp.academicSubject.hours">Hours</Translate>
            </span>
          </dt>
          <dd>{academicSubjectEntity.hours}</dd>
        </dl>
        <Button tag={Link} to="/academic-subject" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/academic-subject/${academicSubjectEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default AcademicSubjectDetail;
