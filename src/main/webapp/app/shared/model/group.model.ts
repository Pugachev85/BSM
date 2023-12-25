import dayjs from 'dayjs';
import { IStudyPlace } from 'app/shared/model/study-place.model';
import { IEducationalProgram } from 'app/shared/model/educational-program.model';

export interface IGroup {
  id?: number;
  title?: string;
  acceptanceDate?: dayjs.Dayjs | null;
  studyPlace?: IStudyPlace | null;
  educationalProgram?: IEducationalProgram | null;
}

export const defaultValue: Readonly<IGroup> = {};
