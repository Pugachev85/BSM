import academicYear from 'app/entities/academic-year/academic-year.reducer';
import studyPlace from 'app/entities/study-place/study-place.reducer';
import group from 'app/entities/group/group.reducer';
import educationalProgram from 'app/entities/educational-program/educational-program.reducer';
import academicSubject from 'app/entities/academic-subject/academic-subject.reducer';
import student from 'app/entities/student/student.reducer';
import employee from 'app/entities/employee/employee.reducer';
import order from 'app/entities/order/order.reducer';
import personalGrade from 'app/entities/personal-grade/personal-grade.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  academicYear,
  studyPlace,
  group,
  educationalProgram,
  academicSubject,
  student,
  employee,
  order,
  personalGrade,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
