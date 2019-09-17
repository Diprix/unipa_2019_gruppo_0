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

  public stato:boolean = true;

    readonly VAPID_PUBLIC_KEY = "BLBx-hf2WrL2qEa0qKb-aCJbcxEvyn62GDTyyP9KTS5K7ZL0K7TfmOKSPqp8vQF0DaG8hpSBknz_x3qf5F4iEFo";

  private urlparams: UrlSegment[];

  constructor(public dialog: MatDialog,
              private teamService: TeamService,
              private bucketService: BucketService,
              private resourceService: ResourceService,
              private syncService: SyncService,
              private router: ActivatedRoute,
              private route: Router,
              private _pushNotifications: PushNotificationsService,
              private swPush: SwPush,
              private notificationService: NotificationService) { }

  ngOnInit() {
    // this.teams = this.teamService.getTeam();
    console.log(this.router.pathFromRoot);
    this.route.events.subscribe(data=>{
        this.router.firstChild.paramMap.subscribe(params=>{
          this.team = params.get('team');
          this.bucket = params.get('bucket');
             })
    });
    this.router.firstChild.paramMap.subscribe(params=>{
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
      if(this.router.children[0].children[0]) {
          this.router.children[0].children[0].url.subscribe(data => {
              this.urlparams = data;
          });
      }

  }


    notify(){ //our function to be called on click
        let options = { //set options
            body: "The truth is, I'am Iron Man!",
            icon: "assets/images/ironman.png" //adding an icon
        }
        this._pushNotifications.create('Iron Man', options).subscribe( //creates a notification
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
    dialogRef.afterClosed().subscribe((result:TeamDTO) => {
      if(result){
        result.buckets = [];
        result.buckets.push({name:'Default', bucketType: "BUCKET", description:'default'});
        // this.teamService.addTeam(result);
        this.teamService.save(result).subscribe(data=>{
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
    dialogRef.afterClosed().subscribe((result:BucketDTO) => {
      if(result){
        result.bucketType = "filesystem";
        console.log(result);
        this.bucketService.save(this.team, result).subscribe(data=>{
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
        dialogRef.afterClosed().subscribe((result:FolderDTO) => {
            if(result){
                result.parentUniqueId = this.urlparams.length>0?this.urlparams[this.urlparams.length-1].path:null;
                console.log(result);
                this.resourceService.addFolder(this.team, this.bucket, result).subscribe(data=>{
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
        dialogRef.afterClosed().subscribe((result:FolderDTO) => {
            if(result){
                console.log(result);
                this.resourceService.addFolder(this.team, this.bucket, result).subscribe(data=>{
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


                console.log("Notification Subscription: ", sub);


                this.notificationService.addPushSubscriber(sub).subscribe(
                    () => console.log('Sent push subscription object to server.'),
                    err =>  console.log('Could not send subscription object to server, reason: ', err)
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
