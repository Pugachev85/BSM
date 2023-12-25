import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import EducationalProgram from './educational-program';
import EducationalProgramDetail from './educational-program-detail';
import EducationalProgramUpdate from './educational-program-update';
import EducationalProgramDeleteDialog from './educational-program-delete-dialog';

const EducationalProgramRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<EducationalProgram />} />
    <Route path="new" element={<EducationalProgramUpdate />} />
    <Route path=":id">
      <Route index element={<EducationalProgramDetail />} />
      <Route path="edit" element={<EducationalProgramUpdate />} />
      <Route path="delete" element={<EducationalProgramDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default EducationalProgramRoutes;
