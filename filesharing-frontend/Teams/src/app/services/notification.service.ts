import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class NotificationService {

  constructor(private http: HttpClient) {
  }

  addPushSubscriber(sub: any) {
    return this.http.post('/api/subscribe', sub);
  }

  send() {
    return this.http.post('/api/newsletter', null);
  }

}
