import {Observable} from "rxjs";
import {Sensor, SensorData} from "../entity/sensor";
import {Response} from "../entity/response";


export interface IServiceService {
  /**
   * return list of available sensors
   */
  getSensors() : Observable<Response<Sensor[]>>;

  /**
   * return information about sensor
   * @param id
   */
  getSensor(id:number) : Observable<Response<Sensor>>;

  /**
   * return list records with data relates to sensor by id
   * @param sensorId
   */
  getSensorData(sensorId: number): Observable<Response<SensorData[]>>;
}
