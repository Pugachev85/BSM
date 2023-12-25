import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './educational-program.reducer';

export const EducationalProgramDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const educationalProgramEntity = useAppSelector(state => state.educationalProgram.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="educationalProgramDetailsHeading">
          <Translate contentKey="basicStudentsManagmentApp.educationalProgram.detail.title">EducationalProgram</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{educationalProgramEntity.id}</dd>
          <dt>
            <span id="title">
              <Translate contentKey="basicStudentsManagmentApp.educationalProgram.title">Title</Translate>
            </span>
          </dt>
          <dd>{educationalProgramEntity.title}</dd>
          <dt>
            <span id="monthLength">
              <Translate contentKey="basicStudentsManagmentApp.educationalProgram.monthLength">Month Length</Translate>
            </span>
          </dt>
          <dd>{educationalProgramEntity.monthLength}</dd>
          <dt>
            <Translate contentKey="basicStudentsManagmentApp.educationalProgram.academicYear">Academic Year</Translate>
          </dt>
          <dd>{educationalProgramEntity.academicYear ? educationalProgramEntity.academicYear.title : ''}</dd>
          <dt>
            <Translate contentKey="basicStudentsManagmentApp.educationalProgram.academicSubject">Academic Subject</Translate>
          </dt>
          <dd>
            {educationalProgramEntity.academicSubjects
              ? educationalProgramEntity.academicSubjects.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.title}</a>
                    {educationalProgramEntity.academicSubjects && i === educationalProgramEntity.academicSubjects.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/educational-program" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/educational-program/${educationalProgramEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default EducationalProgramDetail;
