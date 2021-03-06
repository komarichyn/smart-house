// This file can be replaced during build by using the `fileReplacements` array.
// `ng build` replaces `environment.ts` with `environment.prod.ts`.
// The list of file replacements can be found in `angular.json`.

import {SensorService} from "../app/service/sensor.service";
import {MockSensorService} from "../app/service/mock.sensor.service";
import {DeviceService} from "../app/service/device.service";
import {MockDeviceService} from "../app/service/mock.device.service";


export const environment = {
  production: false,
  providers: [
    {provide: SensorService, useClass: MockSensorService},
    {provide: DeviceService, useClass: MockDeviceService}
  ],
  contextPath: "/"
};

/*
 * For easier debugging in development mode, you can import the following file
 * to ignore zone related error stack frames such as `zone.run`, `zoneDelegate.invokeTask`.
 *
 * This import should be commented out in production mode because it will have a negative impact
 * on performance if an error is thrown.
 */
// import 'zone.js/plugins/zone-error';  // Included with Angular CLI.
