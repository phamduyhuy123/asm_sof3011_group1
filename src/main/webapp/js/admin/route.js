
var app = angular.module("myappAdmin", ["ngRoute"]);
app.config(function($routeProvider){
    $routeProvider
    .when("/admin",{
        templateUrl : "views/admin.jsp",
        controller: 'HomeAdminController'
    })
    .when("/video",{
        templateUrl : "views/videoAdmin.jsp"
    })
    .when("/user",{
        templateUrl : "views/userAdmin.jsp"
    })

    .otherwise({
        redirectTo: '/admin'
    });

});
app.run(function ($rootScope) {
    $rootScope.$on('$routeChangeStart', function () {

    });
    $rootScope.$on('$routeChangeSuccess', function () {
        alert("success")
    });
    $rootScope.$on('$routeChangeError', function () {

        alert("Lỗi");
    });
})