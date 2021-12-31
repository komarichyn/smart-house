import {Injectable, isDevMode} from '@angular/core';
import {IDevice} from "./i-device";
import {Observable, of} from "rxjs";
import {Device} from "../entity/device";
import {environment} from "../../environments/environment";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Response} from "../entity/response";
import {Sensor, SensorData} from "../entity/sensor";
import {catchError, retry, tap} from "rxjs/operators";

@Injectable({
  providedIn: 'root'
})
export class DeviceService implements IDevice{

  contextPath:string = "/";

  constructor(private httpClient: HttpClient) {
    this.contextPath = isDevMode() ? "/" : environment.contextPath
  }

  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };

  getDevice(id: number): Observable<Response<Device>> {
    return this.httpClient.get<Response<Device>>(this.contextPath + 'devices/get');
  }

  getDevices(pageNo: number = 0, pageSize: number = 10): Observable<Response<Device[]>> {
    let url = `${this.contextPath}devices/list/?pageNo=${pageNo}&pageSize=${pageSize}`;
    return this.httpClient.get<Response<Device[]>>(url);
  }

  updateDevice(device: Device): Observable<Response<Device>> {
    let url = `${this.contextPath}devices/update`;
    return this.httpClient.put<Device>(url, JSON.stringify(device), this.httpOptions)
    .pipe(
      tap(_ => this.log(`updated device by id=${device.id}`)),
      catchError(this.handleError<any>('device/update'))
    );
  }

  /**
   * Handle Http operation that failed.
   * Let the app continue.
   * @param operation - name of the operation that failed
   * @param result - optional value to return as the observable result
   */
  private handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {

      // TODO: send the error to remote logging infrastructure
      console.error(error); // log to console instead

      // TODO: better job of transforming error for user consumption
      this.log(`${operation} failed: ${error.message}`);

      // Let the app keep running by returning an empty result.
      return of(result as T);
    };
  }

  /** Log a HeroService message with the MessageService */
  private log(message: string) {
    console.log(`DeviceService: ${message}`);
  }

}
