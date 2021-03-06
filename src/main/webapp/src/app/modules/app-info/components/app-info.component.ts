import { Component, OnInit } from '@angular/core';
import {InfoBasicDTO} from '../../../common/api/api-models';
import {AppInfoService} from '../app-info.service';

@Component({
  selector: 'pgk-app-info',
  templateUrl: './app-info.component.html',
  styleUrls: ['./app-info.component.scss']
})
export class AppInfoComponent implements OnInit {

  appInfo: InfoBasicDTO

  constructor(
    private httpService: AppInfoService
  ) { }

  ngOnInit(): void {
    this.httpService.getBasicInfo().subscribe((appInfo: InfoBasicDTO) => this.appInfo = appInfo);
  }

}
