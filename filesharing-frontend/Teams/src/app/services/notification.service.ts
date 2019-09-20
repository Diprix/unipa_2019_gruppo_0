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
    return this.http.post('/api/notification/subscribe', sub);
  }

  remouvePushSubscribe(sub: any) {
    return this.http.post('/api/notification/unsubscribe', sub);
  }

  send() {
    return this.http.post('/api/notification/newsletter', null);
  }

}
