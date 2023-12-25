import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import AcademicSubject from './academic-subject';
import AcademicSubjectDetail from './academic-subject-detail';
import AcademicSubjectUpdate from './academic-subject-update';
import AcademicSubjectDeleteDialog from './academic-subject-delete-dialog';

const AcademicSubjectRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<AcademicSubject />} />
    <Route path="new" element={<AcademicSubjectUpdate />} />
    <Route path=":id">
      <Route index element={<AcademicSubjectDetail />} />
      <Route path="edit" element={<AcademicSubjectUpdate />} />
      <Route path="delete" element={<AcademicSubjectDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default AcademicSubjectRoutes;
