import dayjs from 'dayjs';
import { IStudent } from 'app/shared/model/student.model';

export interface IOrder {
  id?: number;
  title?: string;
  date?: dayjs.Dayjs;
  number?: number;
  students?: IStudent[] | null;
}

export const defaultValue: Readonly<IOrder> = {};
