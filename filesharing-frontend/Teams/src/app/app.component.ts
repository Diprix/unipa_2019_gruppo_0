import {Component, OnInit, ViewChild} from '@angular/core';
import {KeycloakService} from "keycloak-angular";
import {KeycloakProfile} from "keycloak-js";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})

export class AppComponent implements OnInit{
  title = 'Teams';

  profile: KeycloakProfile;

  constructor(private keycloakService: KeycloakService){}

  ngOnInit(): void {
    this.profile = this.keycloakService.getKeycloakInstance().profile;
  }

  logout(){
    let redirectUrl= window.location.protocol + "//" + window.location.host;
    console.log(redirectUrl);

    this.keycloakService.logout(redirectUrl);
    console.log("logout effettuato")
  }


}
