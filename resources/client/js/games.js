function updateGamesList(id) {

    $.ajax({
        url: '/game/list/' + id,
        type: 'GET',
        success: gameslist => {

            if (gameslist.hasOwnProperty('error')) {

                alert(gameslist.error);

            } else {

                let gamesHTML = `<div class="container">`
                                    + `<div class="row mb-2">`
                                    + `<div class="col-3 bg-light font-weight-bold">Game Name</div>`
                                    + `<div class="col-1 bg-light font-weight-bold">Year</div>`
                                    + `<div class="col-2 bg-light font-weight-bold">Sales</div>`
                                    + `<div class="col-2 bg-light font-weight-bold">Image</div>`
                                    + `<div class="col-3 text-right bg-light font-weight-bold">Options</div>`
                                  + `</div>`;

                for (let game of gameslist) {
                    gamesHTML += `<div class="row mb-2">`
                                    + `<div class="col-3">${game.name}</div>`
                                    + `<div class="col-1">${game.year}</div>`
                                    + `<div class="col-2">${game.sales}</div>`
                                    + `<div class="col-2 small"><a href="${game.imageURL}" target=”_blank”><img width="120" height="90" src="${game.imageURL}"></a></div>`
                                    + `<div class="col-3 text-right">`
                                        + `<a class="btn btn-sm btn-success"  href="/client/game.html?id=${game.id}">Edit</a>`
                                    +`</div>`
                                 + `</div>`;
                }
                gamesHTML += `</div>`;

                $('#games').html(gamesHTML);

            }

        }
    });

}


function pageLoad() {

    let params = getQueryStringParameters();

    $('#console').text(decodeURI(params['name']));

    updateGamesList(params['id']);

}