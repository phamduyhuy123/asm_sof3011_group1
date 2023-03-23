
var app = angular.module("myapp", ['ngRoute','infinite-scroll']);
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

    });
    $rootScope.$on('$routeChangeSuccess', function () {

    });
    $rootScope.$on('$routeChangeError', function () {

        alert("Lỗi");
    });
})
app.controller("myctrl", function ($scope, $http, $rootScope, $location, $routeParams, $route,$timeout,$window,$anchorScroll) {
    $anchorScroll();
    $rootScope.videos=[];
    $rootScope.isCollapsed=false;
    $rootScope.user;
    $rootScope.relativeTimeFilter = function (dateUpload) {
        return moment(dateUpload).fromNow();
    }

    $rootScope.getSearchResults = function () {
        $timeout(function () {
            $http.get('/search', {
                params: { q: $rootScope.searchQuery }
            }).then(function (response) {
                $rootScope.searchResults = response.data;
            });
        }, 1000);
    };
    $rootScope.$on('$routeChangeSuccess', function(event, current, previous) {
        $timeout(function() {
            var element = document.querySelector('.main-content');
         
            if (element) {
                element.scrollTop = 0;
                
            }
        });
        if ($location.path().includes('videoDetail')) {
            $rootScope.isCollapsed = true;
        } else {
            $rootScope.isCollapsed = false;
        }
    });



});
app.controller("homectrl", function ($scope, $http, $rootScope, $location, $routeParams) {
    $scope.videos = [];
    
    
    $scope.page = 1;
    $scope.pageSize = 8;
    $scope.limit=8
    $scope.loadMore = function() {
        console.log("loadmore called" +$scope.page)
        var offset = ($scope.page - 1) * $scope.pageSize;
        $http.get('videos?offset=' + offset + '&limit=' + $scope.pageSize).then(function(response) {
            var newItems = response.data;
            $scope.videos = $scope.videos.concat(newItems);
            $rootScope.videos=$scope.videos;
            $scope.limit=$scope.videos.length;
            $scope.page += 1;
        });
    };
    $scope.loadMore();

});
app.controller("videoDetailCtrl", function ($scope, $http, $rootScope, $routeParams, $location) {
    $scope.video;
    var searchObject=$location.search();
    $scope.videoUrl="";
    var videoId=searchObject.videoId;
    $http.get('videoDetail?videoId=' + videoId).then(function (response) {
        $scope.video = response.data;
        $scope.loadVideo($scope.video.id);
    }, reason => {

    });
    $http.get('comments?videoId='+videoId)
    $scope.countRepleParrentComment=function(id){

    }
    $scope.loadVideo = function(id) {
        $http.get('streamVideo?videoId=' + id).then(function(response) {
            $scope.videoUrl = response.data;
        });
    };


});
app.directive("directiveWhenScrolled", function() {
    return function(scope, elm, attr) {
      var raw = elm[0];
  
      elm.bind('scroll', function() {
        if (raw.scrollTop + raw.offsetHeight >= raw.scrollHeight) {
          scope.$apply(attr.directiveWhenScrolled);
        }
      });
    };
  });

