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

import {PushNotificationOptions, PushNotificationService } from 'ngx-push-notifications';


@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {

  public showMenu: boolean = false;

  public teams: Team[];

  public team: string;
  public bucket: string;

  private urlparams: UrlSegment[];


   // public pushButton = document.querySelector('.js-push-btn');
   // public isSubscribed: boolean = false;
   // public swRegistration: any = null;


  readonly applicationServerPublicKey = 'BBylNeBkzWwXI7VR7wnvbZw9PAt5ec5Lo86PzoOt49PN-ywaLEUk9KFA7qEC23hbp6mmp5EUfDTvKniY2MH1f-I';

  constructor(public dialog: MatDialog,
              private teamService: TeamService,
              private bucketService: BucketService,
              private resourceService: ResourceService,
              private syncService: SyncService,
              private router: ActivatedRoute,
              private route: Router,
              private _pushNotificationService: PushNotificationService) { }

  ngOnInit() {


      const isGranted = this._pushNotificationService.isPermissionGranted;
      if (!isGranted){
          this._pushNotificationService.requestPermission();
      }


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



      // - - - - - -
    /*
      navigator.serviceWorker.register('/sw.js')
          .then(function(swReg) {
              console.log('Service Worker is registered', swReg);

              this.swRegistration = swReg;
              initializeUI();
          })
    */
      // - - - - - - - -



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

    myFunction() {
        const title = 'Hello';
        const options = new PushNotificationOptions();
        options.body = 'Native Push Notification';

        this._pushNotificationService.create(title, options).subscribe((notif) => {
                if (notif.event.type === 'show') {
                    console.log('onshow');
                    setTimeout(() => {
                        notif.notification.close();
                    }, 3000);
                }
                if (notif.event.type === 'click') {
                    console.log('click');
                    notif.notification.close();
                }
                if (notif.event.type === 'close') {
                    console.log('close');
                }
            },
            (err) => {
                console.log(err);
            });
    }

    /*
    initializeUI():void {
        // Set the initial subscription value
        this.swRegistration.pushManager.getSubscription()
            .then(function(subscription) {
                this.isSubscribed = !(subscription === null);

                if (this.isSubscribed) {
                    console.log('User IS subscribed.');
                } else {
                    console.log('User is NOT subscribed.');
                }

                updateBtn();
            });
    }

    updateBtn(): void {
        if (this.isSubscribed) {
            this.pushButton.textContent = 'Disable Push Messaging';
        } else {
            this.pushButton.textContent = 'Enable Push Messaging';
        }

        this.pushButton.disabled = false;
    }

     */

}
