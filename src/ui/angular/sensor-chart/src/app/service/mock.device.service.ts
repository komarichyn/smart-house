import { Injectable } from '@angular/core';
import {IDevice} from "./i-device";
import {Device} from "../entity/device";
import {Observable, of} from "rxjs";
import {Response} from "../entity/response";

import * as device from "../../mock/devices/get.json";
import * as devices from "../../mock/devices/list.json";

@Injectable({
  providedIn: 'root'
})
export class MockDeviceService implements IDevice{

  deviceJson : Response<Device> = device;
  devicesJson : Response<Device[]> = devices;

  constructor() { }

  getDevice(id: number): Observable<Response<Device>> {
    return of(this.deviceJson);
  }

  getDevices(pageNo: number, pageSize: number): Observable<Response<Device[]>> {
    return of(this.devicesJson);
  }

  updateDevice(device: Device): Observable<Response<Device>> {
    return of(this.deviceJson);
  }


}
