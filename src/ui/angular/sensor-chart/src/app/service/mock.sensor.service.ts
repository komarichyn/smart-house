import { Injectable } from '@angular/core';
import {IServiceService} from "./i-service.service";
import {Observable, of} from "rxjs";
import {Sensor, SensorData} from "../entity/sensor";
import {HttpClient} from "@angular/common/http";

import {Response} from "../entity/response";

import * as sensorsData from "../../mock/sensors/listData.json";
import * as sensors from "../../mock/sensors/list.json";
import * as sensor from "../../mock/sensors/get.json";


@Injectable({
  providedIn: 'root'
})
export class MockSensorService implements IServiceService{

  sensorDataJson : Response<SensorData[]> = sensorsData;
  sensorListJson : Response<Sensor[]> = sensors;
  sensorJson : Response<Sensor> = sensor;


  constructor(private httpClient: HttpClient) { }

  getSensor(id: number): Observable<Response<Sensor>> {
    return of(this.sensorJson);
  }

  getSensorData(sensorId: number): Observable<Response<SensorData[]>> {
    return of(this.sensorDataJson);
  }

  getSensors(): Observable<Response<Sensor[]>> {
    return of(this.sensorListJson);
  }
}
