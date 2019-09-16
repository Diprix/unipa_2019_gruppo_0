
//per prima cosa controlliamo se il nostro browser supporta il service worker

if('serviceWorker' in navigator) {

    navigator.serviceWorker.register('/sw.js', {
        scope: '/' //intercetta tutte le richieste
    })
        .then(registration => {

            console.log('Service worker registration completed');

        });
} else {
    console.log('Service workers aren\'t supported in this browser.');
    //disableAndSetBtnMessage('Service workers unsupported');
}



//per supportare la diciture  => in js presente alla riga 9 è necessario
//aggionrare la Javascripo lenguage version in impostazioni