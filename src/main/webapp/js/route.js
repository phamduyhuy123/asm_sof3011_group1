app.config(function ($routeProvider) {
    $routeProvider.when("/videoDetail/:id", {
        templateUrl: "templates/videoDetail.html",
        controller: "videoDetailCtrl"
    }).when("/home", {
        templateUrl: "templates/home.html",
        controller: "homectrl"
    })
        .otherwise({
            redirectTo: "/home"

        });

});