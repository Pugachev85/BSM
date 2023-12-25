import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './personal-grade.reducer';

export const PersonalGradeDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const personalGradeEntity = useAppSelector(state => state.personalGrade.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="personalGradeDetailsHeading">
          <Translate contentKey="basicStudentsManagmentApp.personalGrade.detail.title">PersonalGrade</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{personalGradeEntity.id}</dd>
          <dt>
            <span id="grade">
              <Translate contentKey="basicStudentsManagmentApp.personalGrade.grade">Grade</Translate>
            </span>
          </dt>
          <dd>{personalGradeEntity.grade}</dd>
          <dt>
            <Translate contentKey="basicStudentsManagmentApp.personalGrade.academicSubject">Academic Subject</Translate>
          </dt>
          <dd>{personalGradeEntity.academicSubject ? personalGradeEntity.academicSubject.title : ''}</dd>
          <dt>
            <Translate contentKey="basicStudentsManagmentApp.personalGrade.student">Student</Translate>
          </dt>
          <dd>
            {personalGradeEntity.students
              ? personalGradeEntity.students.map((val, i) => (
                  <span key={val.id}>
                    <a>
                      {val.secondName} {val.firstName} (№ {val.alphabetBookNumber})
                    </a>
                    {personalGradeEntity.students && i === personalGradeEntity.students.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/personal-grade" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/personal-grade/${personalGradeEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default PersonalGradeDetail;
