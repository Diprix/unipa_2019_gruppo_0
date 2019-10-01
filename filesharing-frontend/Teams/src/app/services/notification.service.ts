import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class NotificationService {

  constructor(private http: HttpClient) {
  }


  /// INSERITO NEL PATH notification/

  addPushSubscriber(sub: any) {
    return this.http.post('/api/subscribe', sub);
  }

  removePushSubscribe(sub: any) {
    return this.http.post('/api/unsubscribe', sub);
  }

}
