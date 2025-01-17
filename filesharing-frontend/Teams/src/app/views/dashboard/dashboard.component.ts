import {Component, OnInit} from '@angular/core';
import {Team} from "../../models/Team";
import {TeamDialogComponent} from "../../dialog/team-dialog/team-dialog.component";
import {MatDialog} from "@angular/material";
import {BucketDialogComponent} from "../../dialog/bucket-dialog/bucket-dialog.component";
import {TeamService} from "../../services/team.service";
import {ActivatedRoute, Params, Router, UrlSegment} from "@angular/router";
import {BucketDTO, FolderDTO, TeamDTO} from "../../models/models";
import {BucketService} from "../../services/bucket.service";
import {FolderDialogComponent} from "../../dialog/folder-dialog/folder-dialog.component";
import {ResourceService} from "../../services/resource.service";
import {SYNC_TYPE, SyncService} from "../../services/sync.service";
import {PushNotificationsService} from "ng-push";
import {SwPush} from "@angular/service-worker";
import {NotificationService} from "../../services/notification.service";

import {KeycloakProfile} from "keycloak-js";
import {KeycloakService} from "keycloak-angular";





@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {

    public showMenu: boolean = false;
    sub: PushSubscription;
    public teams: Team[];

    public team: string;
    public bucket: string;

    public stato: String;// = Notification.permission;
    public supported: boolean = this._pushNotifications.isSupported();
    public active: boolean = false;

    profile: KeycloakProfile;





    readonly VAPID_PUBLIC_KEY = "BBYCxwATP2vVgw7mMPHJfT6bZrJP2iUV7OP_oxHzEcNFenrX66D8G34CdEmVULNg4WJXfjkeyT0AT9LwavpN8M4=";

    private urlparams: UrlSegment[];

    constructor(public dialog: MatDialog,
                private teamService: TeamService,
                private bucketService: BucketService,
                private resourceService: ResourceService,
                private syncService: SyncService,
                private router: ActivatedRoute,
                private route: Router,
                public _pushNotifications: PushNotificationsService,
                public swPush: SwPush,
                private notificationService: NotificationService,
                private keycloakService: KeycloakService) {
    }

    ngOnInit() {

        this.profile = this.keycloakService.getKeycloakInstance().profile;

        console.log("------ "+ this.profile.username);



        Notification.requestPermission().then(function(result) {
            if (result === 'denied') {
                console.log('Permission wasn\'t granted. Allow a retry.');
            }
            if (result === 'default') {
                console.log('The permission request was dismissed.');
            }
            if (result === 'granted') {
                console.log('The permission request accepted.');
            }

        });

        this.stato= Notification.permission;

        console.log('#### ' + Notification.permission);
        console.log('#### ' + this.active);

        if(localStorage.getItem(this.profile.username) === 'true'){
            this.active = true;
        } else {
            this.active = false;

        }



        // this.teams = this.teamService.getTeam();
        console.log(this.router.pathFromRoot);
        this.route.events.subscribe(data => {
            this.router.firstChild.paramMap.subscribe(params => {
                this.team = params.get('team');
                this.bucket = params.get('bucket');
            })
        });
        this.router.firstChild.paramMap.subscribe(params => {
            this.team = params.get('team');
            this.bucket = params.get('bucket');
            console.log(params)
        });
        this.router.url.subscribe((data) => {
            console.log('params ', data); //contains all the segments so put logic here of determining what to do according to nesting depth
            this.urlparams = data;
        });
        // this.router.children[0].params
        //     .subscribe((childParams: Params) =>{
        //         console.log("dashboard:",childParams);
        //     });
        if (this.router.children[0].children[0]) {
            this.router.children[0].children[0].url.subscribe(data => {
                this.urlparams = data;
            });
        }

    }

    setTest(): void {
        this.stato = Notification.permission;
    }

    localNotification(titolo, corpo) { //our function to be called on click
        let options = { //set options

            body: corpo,
            icon: "assets/images/logoNotifiche.png", //adding an icon
            vibrate: [100, 50, 100]
        }
        this._pushNotifications.create(titolo, options).subscribe( //creates a notification
            res => console.log(res),
            err => console.log(err)
        );
    }


    openDialogTeam(): void {
        const dialogRef = this.dialog.open(TeamDialogComponent, {
            width: '50vw',
            data: {}
        });
        this.showMenu = false;
        dialogRef.afterClosed().subscribe((result: TeamDTO) => {
            if (result) {
                result.buckets = [];
                result.buckets.push({name: 'Default', bucketType: "BUCKET", description: 'default'});
                // this.teamService.addTeam(result);
                this.teamService.save(result).subscribe(data => {
                    this.syncService.sendEvent(SYNC_TYPE.Team);
                });
            }
        });
    }

    openDialogBucket(): void {
        const dialogRef = this.dialog.open(BucketDialogComponent, {
            width: '50vw',
            data: {}
        });
        this.showMenu = false;
        dialogRef.afterClosed().subscribe((result: BucketDTO) => {
            if (result) {
                result.bucketType = "filesystem";
                console.log(result);
                this.bucketService.save(this.team, result).subscribe(data => {
                    this.syncService.sendEvent(SYNC_TYPE.Bucket);
                    this.syncService.sendEvent(SYNC_TYPE.Team);
                })
            }
        });
    }

    openDialogFolder(): void {
        const dialogRef = this.dialog.open(FolderDialogComponent, {
            width: '50vw',
            data: {}
        });
        this.showMenu = false;
        dialogRef.afterClosed().subscribe((result: FolderDTO) => {
            if (result) {
                result.parentUniqueId = this.urlparams.length > 0 ? this.urlparams[this.urlparams.length - 1].path : null;
                console.log(result);
                this.resourceService.addFolder(this.team, this.bucket, result).subscribe(data => {
                    this.syncService.sendEvent(SYNC_TYPE.Resource);
                });
            }
        });
    }

    openDialogUpload(): void {
        const dialogRef = this.dialog.open(FolderDialogComponent, {
            width: '50vw',
            data: {}
        });
        this.showMenu = false;
        dialogRef.afterClosed().subscribe((result: FolderDTO) => {
            if (result) {
                console.log(result);
                this.resourceService.addFolder(this.team, this.bucket, result).subscribe(data => {
                    this.syncService.sendEvent(SYNC_TYPE.Resource);
                });
            }
        });
    }


    subscribeToNotifications() {
        this.swPush.requestSubscription({
            serverPublicKey: this.VAPID_PUBLIC_KEY
        })
            .then(sub => {
                this.sub = sub;

                this.active = true;

                console.log("Notification Subscription: ", sub.toJSON());
                console.log("endpoint: " + sub.endpoint)

               // this.notify('Congratulazioni','Ha abilitato le notifiche push')

                this.notificationService.addPushSubscriber(sub).subscribe(
                    () => {
                        console.log('Sent push subscription object to server.')

                        localStorage.setItem(this.profile.username,'true');

                        this.localNotification('Congratulazioni','Hai abilitato le notifiche');
                    },
                    err => console.log('Could not send subscription object to server, reason: ', err)
                );


            })
            .catch(err => console.error("Could not subscribe to notifications", err));

    }

    unSubscribeToNotifications() {
        this.swPush.requestSubscription({
            serverPublicKey: this.VAPID_PUBLIC_KEY
        })
            .then(sub => {
                this.sub = sub;

                this.active = false;

                console.log("Notification Subscription: ", sub.toJSON());
                console.log("endpoint: " + sub.endpoint)


                this.notificationService.removePushSubscribe(sub).subscribe(
                    () => {
                        console.log('Sent push unsubscription object to server.');

                        localStorage.setItem(this.profile.username, 'false');

                        this.localNotification('Congratulazioni','Hai disabilitato le notifiche');
                    },
                    err => console.log('Could not send subscription object to server, reason: ', err)
                );


            })
            .catch(err => console.error("Could not subscribe to notifications", err));

    }

}
export declare class PushNotificationOptions {
    body: string;
    icon: string;
    sound: string;
    data: any;
    tag: string;
    dir: NotificationDirection;
    lang: string;
    renotify: boolean;
    sticky: boolean;
    vibrate: Array<number>;
    noscreen: boolean;
    silent: boolean;
}
