import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import AcademicYear from './academic-year';
import StudyPlace from './study-place';
import Group from './group';
import EducationalProgram from './educational-program';
import AcademicSubject from './academic-subject';
import Student from './student';
import Employee from './employee';
import Order from './order';
import PersonalGrade from './personal-grade';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="academic-year/*" element={<AcademicYear />} />
        <Route path="study-place/*" element={<StudyPlace />} />
        <Route path="group/*" element={<Group />} />
        <Route path="educational-program/*" element={<EducationalProgram />} />
        <Route path="academic-subject/*" element={<AcademicSubject />} />
        <Route path="student/*" element={<Student />} />
        <Route path="employee/*" element={<Employee />} />
        <Route path="order/*" element={<Order />} />
        <Route path="personal-grade/*" element={<PersonalGrade />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
