
var app = angular.module("myapp", ['ngRoute']);

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
app.controller("videoDetailCtrl", function ($scope, $http, $rootScope, $routeParams, $location,$q) {
    $scope.video=[];
    var searchObject=$location.search();
    $scope.videoUrl="";
    $scope.comments=[];
    var videoId=searchObject.videoId;
    $http.get('videoDetail?videoId=' + videoId).then(function (response) {
        $scope.video = response.data;
        $scope.loadVideo($scope.video.id);

    }, reason => {

    });
    $http.get('api/comments?videoId='+videoId).then(function (response) {
        $scope.comments=response.data;
        console.log($scope.comments)
    });
    $scope.commentsData = {};
    $scope.loadChildrenComments = function(parentId) {
        if ($scope.commentsData[parentId]) {
            return $scope.commentsData[parentId];
        }
        else {
            var promise = $http.get('api/comment/get/childrenComment?parentId=' + parentId).then(function(response) {
                $scope.commentsData[parentId] = response.data;
                return response.data;
            });
            $scope.commentsData[parentId] = promise;
            console.log(promise)
            return promise;
        }
    };
    $scope.player = null;
    $scope.playCount=0;
    $scope.initPlayer = function() {
        $scope.player= new Plyr("#plyr-video",{
            speed: { selected: 1, options: [0.5, 0.75, 1, 1.25, 1.5, 1.75, 2] }
        });
        $scope.player.on('play', (event) => {
            $scope.playCount++;
            console.log($scope.playCount)
            if($scope.playCount==1){
               
            }
        });
    };
    $scope.loadVideo = function(id) {
        $http.get('streamVideo?videoId=' + id).then(function(response) {
            $scope.videoUrl = response.data;
            $scope.initPlayer();
        });
    };

    $scope.sendCommentToVideo=function (value,videoId,userId) {
        $http.post('api/comment/post/commentVideo?videoId='+videoId+'&userId='+userId, value).then(function (response){
           $scope.comments.unshift(response.data);

        });

    };
    $scope.sendChildrenCommentToVideo=function (value,videoId,userId,commentId) {
        $http.post('api/comment/post/commentVideo?videoId='+videoId+'&userId='+userId+'&parentId='+commentId, value).then(function (response){
            console.log($scope.commentsData[commentId])
            $scope.commentsData[commentId].push(response.data);
            console.log($scope.commentsData[commentId])

        });
    };
    $scope.onCheckboxClick = function(checkboxNumber) {
        if (checkboxNumber === 1 && $scope.checkbox2) {
          $scope.checkbox2 = false;
        } else if (checkboxNumber === 2 && $scope.checkbox1) {
          $scope.checkbox1 = false;
        }
      };
});


