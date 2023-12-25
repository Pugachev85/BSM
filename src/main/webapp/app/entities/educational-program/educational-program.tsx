import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, getSortState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { ASC, DESC, SORT } from 'app/shared/util/pagination.constants';
import { overrideSortStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './educational-program.reducer';

export const EducationalProgram = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const educationalProgramList = useAppSelector(state => state.educationalProgram.entities);
  const loading = useAppSelector(state => state.educationalProgram.loading);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        sort: `${sortState.sort},${sortState.order}`,
      }),
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?sort=${sortState.sort},${sortState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [sortState.order, sortState.sort]);

  const sort = p => () => {
    setSortState({
      ...sortState,
      order: sortState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handleSyncList = () => {
    sortEntities();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = sortState.sort;
    const order = sortState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    } else {
      return order === ASC ? faSortUp : faSortDown;
    }
  };

  return (
    <div>
      <h2 id="educational-program-heading" data-cy="EducationalProgramHeading">
        <Translate contentKey="basicStudentsManagmentApp.educationalProgram.home.title">Educational Programs</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="basicStudentsManagmentApp.educationalProgram.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link
            to="/educational-program/new"
            className="btn btn-primary jh-create-entity"
            id="jh-create-entity"
            data-cy="entityCreateButton"
          >
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="basicStudentsManagmentApp.educationalProgram.home.createLabel">Create new Educational Program</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {educationalProgramList && educationalProgramList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="basicStudentsManagmentApp.educationalProgram.id">ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('title')}>
                  <Translate contentKey="basicStudentsManagmentApp.educationalProgram.title">Title</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('title')} />
                </th>
                <th className="hand" onClick={sort('monthLength')}>
                  <Translate contentKey="basicStudentsManagmentApp.educationalProgram.monthLength">Month Length</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('monthLength')} />
                </th>
                <th>
                  <Translate contentKey="basicStudentsManagmentApp.educationalProgram.academicYear">Academic Year</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="basicStudentsManagmentApp.educationalProgram.academicSubject">Academic Subject</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {educationalProgramList.map((educationalProgram, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/educational-program/${educationalProgram.id}`} color="link" size="sm">
                      {educationalProgram.id}
                    </Button>
                  </td>
                  <td>{educationalProgram.title}</td>
                  <td>{educationalProgram.monthLength}</td>
                  <td>
                    {educationalProgram.academicYear ? (
                      <Link to={`/academic-year/${educationalProgram.academicYear.id}`}>{educationalProgram.academicYear.title}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>
                    {educationalProgram.academicSubjects
                      ? educationalProgram.academicSubjects.map((val, j) => (
                          <span key={j}>
                            <Link to={`/academic-subject/${val.id}`}>{val.title}</Link>
                            {j === educationalProgram.academicSubjects.length - 1 ? '' : ', '}
                          </span>
                        ))
                      : null}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        tag={Link}
                        to={`/educational-program/${educationalProgram.id}`}
                        color="info"
                        size="sm"
                        data-cy="entityDetailsButton"
                      >
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/educational-program/${educationalProgram.id}/edit`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() => (window.location.href = `/educational-program/${educationalProgram.id}/delete`)}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="basicStudentsManagmentApp.educationalProgram.home.notFound">No Educational Programs found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default EducationalProgram;
