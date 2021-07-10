import {NativeDateAdapter} from '@angular/material/core';

export class PGKDateAdapter extends NativeDateAdapter {

  getFirstDayOfWeek(): number {
    return 1;
  }

}
