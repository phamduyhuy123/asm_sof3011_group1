app.config(function ($routeProvider) {
    $routeProvider.when("/videoDetail", {
        templateUrl: "templates/videoDetail.html",
        controller: "videoDetailCtrl"
    }).when("/home", {
        templateUrl: "templates/home.html",
        controller: "homectrl"
    }).when("/login",{
        templateUrl: "templates/Login.html",
        controller: "loginctrl"
    }).when("/register",{
        templateUrl: "templates/Register.html",
        controller: "registerctrl"
    }).when("/editProfile",{
        templateUrl: "templates/UserDetail.html",
        controller: "editprofilectrl"
    }).when("/viewHistory",{
        templateUrl: "templates/viewHistory.html",
        controller: "viewhistoryctrl"
    }).when("/userChanel",{
        templateUrl:"templates/userChanel.html",
        controller: "userChanelctrl"
    }).when("/forgotpassword",{
        templateUrl:"templates/forgotpassword.html",
        controller: "forgotctrl"
    }).when("/forgotpassword2",{
        templateUrl:"templates/forgotpassword2.html",
        controller: "forgot2ctrl"
    }).when("/forgotpassword3",{
        templateUrl:"templates/forgotpassword3.html",
        controller: "forgot3ctrl"
    })
    
        .otherwise({
            redirectTo: "/home"

        });

});

app.run(function ($rootScope) {
    $rootScope.$on('$routeChangeStart', function () {

    });
    $rootScope.$on('$routeChangeSuccess', function () {

    });
    $rootScope.$on('$routeChangeError', function () {

        alert("Lỗi");
    });
})