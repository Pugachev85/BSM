import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './group.reducer';

export const GroupDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const groupEntity = useAppSelector(state => state.group.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="groupDetailsHeading">
          <Translate contentKey="basicStudentsManagmentApp.group.detail.title">Group</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{groupEntity.id}</dd>
          <dt>
            <span id="title">
              <Translate contentKey="basicStudentsManagmentApp.group.title">Title</Translate>
            </span>
          </dt>
          <dd>{groupEntity.title}</dd>
          <dt>
            <span id="acceptanceDate">
              <Translate contentKey="basicStudentsManagmentApp.group.acceptanceDate">Acceptance Date</Translate>
            </span>
          </dt>
          <dd>
            {groupEntity.acceptanceDate ? (
              <TextFormat value={groupEntity.acceptanceDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <Translate contentKey="basicStudentsManagmentApp.group.studyPlace">Study Place</Translate>
          </dt>
          <dd>{groupEntity.studyPlace ? groupEntity.studyPlace.title : ''}</dd>
          <dt>
            <Translate contentKey="basicStudentsManagmentApp.group.educationalProgram">Educational Program</Translate>
          </dt>
          <dd>{groupEntity.educationalProgram ? groupEntity.educationalProgram.title : ''}</dd>
        </dl>
        <Button tag={Link} to="/group" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/group/${groupEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default GroupDetail;
