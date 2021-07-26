import {Injectable} from '@angular/core';
import {NativeDateAdapter} from '@angular/material/core';

@Injectable({
  providedIn: 'root'
})
export class PGKDateAdapter extends NativeDateAdapter {

  getFirstDayOfWeek(): number {
    return 1;
  }

}
