import {Injectable, isDevMode} from '@angular/core';
import {Observable} from "rxjs";
import {Sensor, SensorData} from "../entity/sensor";
import {HttpClient} from "@angular/common/http";
import {Response} from "../entity/response";
// import {APP_BASE_HREF} from '@angular/common';
import { environment } from '../../environments/environment';


import {IServiceService} from "./i-service.service";

@Injectable({
  providedIn: 'root'
})
export class SensorService implements IServiceService{

  contextPath:string = "/";

  constructor(private httpClient: HttpClient) {
    this.contextPath = isDevMode() ? "/" : environment.contextPath
  }

  getSensor(id: number): Observable<Response<Sensor>> {
    return this.httpClient.get<Response<Sensor>>(this.contextPath + 'sensors/list');
  }

  getSensorData(sensorId: number, pageNo: number = 0, pageSize:number = 50): Observable<Response<SensorData[]>> {
    let url = `${this.contextPath}sensors/listData/${sensorId}?pageNo=${pageNo}&pageSize=${pageSize}`;
    return this.httpClient.get<Response<SensorData[]>>(url);
  }

  getSensors(): Observable<Response<Sensor[]>> {
    return this.httpClient.get<Response<Sensor[]>>(this.contextPath + 'sensors/list');
  }
}
