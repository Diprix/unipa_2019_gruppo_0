


const VERSION = 'v6';

function log(messages) {
    console.log(VERSION, messages);
}

log('Installing Service Worker');


//Implemento la cache storage PWA API

self.addEventListener('install', event => event.waitUntil(installServiceWorker()));

async function installServiceWorker() {

    log('Service Worker installation started');

    const request = new Request('offline.html');

    const response = await fetch(request);

    log('responce received after loading offline page', response);

    if (response.status !== 200) {
        throw new Error('Could not load offline page!');
    }

    const cache = await caches.open('app-cache');

    cache.put(request, response);

    log('Cache offile.html');

}



self.addEventListener('install', () => {

    log('version is installed');

});

self.addEventListener('active', () => {

    log('version is actived');

});

// - - - - - - - - - - - - - - - - - -
// Metodi Notifiche Push

self.addEventListener('push', function(event) {
    if (!(self.Notification && self.Notification.permission === 'granted')) {
        return;
    }

    var data = {};
    if (event.data) {
        data = event.data.json();
    }
    var title = data.title;
    var message = data.message;
    var icon = "img/FM_logo_2013.png";

    self.clickTarget = data.clickTarget;

    event.waitUntil(self.registration.showNotification(title, {
        body: message,
        tag: 'push-demo',
        icon: icon,
        badge: icon
    }));
});

self.addEventListener('notificationclick', function(event) {
    console.log('[Service Worker] Notification click Received.');

    event.notification.close();

    if(clients.openWindow){
        event.waitUntil(clients.openWindow(self.clickTarget));
    }
});