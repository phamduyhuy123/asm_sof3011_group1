
var app = angular.module("myapp", ['ngRoute']);
app.config(function ($routeProvider) {
    $routeProvider.when("/videoDetail", {
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
app.filter('viewNumber', function () {
    return function (num) {
        if (num >= 1000000000) {
            return (num / 1000000000).toFixed(1).replace(/\.0$/, '') + ' T';
        }
        if (num >= 1000000) {
            return (num / 1000000).toFixed(1).replace(/\.0$/, '') + ' Tr';
        }
        if (num >= 1000) {
            return (num / 1000).toFixed(1).replace(/\.0$/, '') + ' N';
        }
        return num;
    };
});
app.filter('formatDuration', function () {
    return function (duration) {
        var hours = Math.floor(duration / 3600);
        var minutes = Math.floor((duration % 3600) / 60);
        var seconds = duration % 60;
        if (hours > 0) {
            return hours + ':' + (minutes < 10 ? '0' : '') + minutes + ':' + (seconds < 10 ? '0' : '') + seconds;
        } else {
            return minutes + ':' + (seconds < 10 ? '0' : '') + seconds;
        }
    };
});
app.run(function ($rootScope) {
    $rootScope.$on('$routeChangeStart', function () {
        // alert("start")
    });
    $rootScope.$on('$routeChangeSuccess', function () {
        // alert("success")
    });
    $rootScope.$on('$routeChangeError', function () {

        alert("Lỗi");
    });
})
app.controller("myctrl", function ($scope, $http, $rootScope, $location, $routeParams, $route,$timeout,$window) {
    $rootScope.isRouteVideoDetail = function () {
        return $location.path().includes('videoDetail');
    };
    $rootScope.dateNow = new Date();
    console.log($rootScope.dateNow)
    $rootScope.user;
    // $http.get('api/findUser').then(function (response) {
    //     $rootScope.user = response.data;
    //     console.log($rootScope.user)
    // })
    $rootScope.relativeTimeFilter = function (dateUpload) {
        return moment(dateUpload).fromNow();
    }

    $scope.getSearchResults = function () {
        $timeout(function () {
            $http.get('/search', {
                params: { q: $scope.searchQuery }
            }).then(function (response) {
                $scope.searchResults = response.data;
            });
        }, 1000);
    };

    $scope.currentRoute = $route.current;
    $scope.checkRouteCurrent=function (){
        return  currentRoute.$$route.originalPath.includes('videoDetail') && $window.innerWidth <= 768;
    }
    $scope.$on('$routeChangeSuccess', function () {
        $scope.currentRoute = $route.current;
        
    });

});
app.controller("homectrl", function ($scope, $http, $rootScope, $location, $routeParams) {
    $scope.videos = [];
    $http.get('videos').then(function (response) {
        $scope.videos = response.data;
        $rootScope.videos = response.data;

        console.log(response.data)
    }, reason => {

    });


});
app.controller("videoDetailCtrl", function ($scope, $http, $rootScope, $routeParams, $location) {
    $scope.video;
    var searchObject=$location.search();

    var videoId=searchObject.videoId;
    $http.get('videoDetail?videoId=' + videoId).then(function (response) {
        $scope.video = response.data;

    }, reason => {

    });


});


