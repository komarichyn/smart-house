import {Observable} from "rxjs";
import {Response} from "../entity/response";
import {Device} from "../entity/device";

export interface IDevice {

  getDevice(id:number) : Observable<Response<Device>>;

  getDevices(pageNo: number, pageSize:number ): Observable<Response<Device[]>>;

  updateDevice(device: Device) : Observable<Response<Device>>;
}
