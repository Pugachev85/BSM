import { IEducationalProgram } from 'app/shared/model/educational-program.model';
import { IEmployee } from 'app/shared/model/employee.model';

export interface IAcademicSubject {
  id?: number;
  title?: string;
  hours?: number;
  educationalPrograms?: IEducationalProgram[] | null;
  employees?: IEmployee[] | null;
}

export const defaultValue: Readonly<IAcademicSubject> = {};
