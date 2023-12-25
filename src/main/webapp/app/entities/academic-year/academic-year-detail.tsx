import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './academic-year.reducer';

export const AcademicYearDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const academicYearEntity = useAppSelector(state => state.academicYear.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="academicYearDetailsHeading">
          <Translate contentKey="basicStudentsManagmentApp.academicYear.detail.title">AcademicYear</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{academicYearEntity.id}</dd>
          <dt>
            <span id="title">
              <Translate contentKey="basicStudentsManagmentApp.academicYear.title">Title</Translate>
            </span>
          </dt>
          <dd>{academicYearEntity.title}</dd>
        </dl>
        <Button tag={Link} to="/academic-year" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/academic-year/${academicYearEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default AcademicYearDetail;
